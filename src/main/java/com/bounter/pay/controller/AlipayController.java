package com.bounter.pay.controller;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.bounter.pay.config.AlipayConfig;
import com.bounter.pay.entity.AlipayOrder;
import com.bounter.pay.service.AlipayService;

@Controller
public class AlipayController {
	
	//支付宝支付客户端
	private AlipayClient alipayClient;
	
	@Autowired
	private AlipayService alipayService;
	
	public AlipayController() {
		//初始化支付客户端
		alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl, AlipayConfig.app_id, AlipayConfig.merchant_private_key, "json", AlipayConfig.charset, AlipayConfig.alipay_public_key, AlipayConfig.sign_type);
	}
	
	@RequestMapping("/")
	public String home() {
		return "index";
	}
	
	/**
	 * 支付接口
	 * @param order
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/alipay/pay")
	public String pay(AlipayOrder order) throws Exception {
		//TODO:这里执行商户系统创建新的订单操作
		alipayService.createOrder(order);
		
		//跳转到支付页面
		return "alipay.payPage";
	}
	
	/**
	 * 付款成功后同步跳转到商家页面
	 * @return
	 */
	@RequestMapping("/return_url")
	public String returnPage() {
		return "alipay.paySuccess";
	}
	
	/**
	 * 支付结果异步通知,POST方式，支付结果以异步通知内容为准
	 * @return
	 * @throws Exception 
	 */
	@PostMapping("/notify_url")
	public void notifyUrl(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//获取支付宝POST过来反馈信息
		Map<String,String> params = new HashMap<String,String>();
		Map<String,String[]> requestParams = request.getParameterMap();
		for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i]
						: valueStr + values[i] + ",";
			}
			params.put(name, valueStr);
		}
		
		//验签
		boolean signVerified = AlipaySignature.rsaCheckV1(params, AlipayConfig.alipay_public_key, AlipayConfig.charset, AlipayConfig.sign_type); //调用SDK验证签名

		/* 实际验证过程建议商户务必添加以下校验：
		1、需要验证该通知数据中的out_trade_no是否为商户系统中创建的订单号，
		2、判断total_amount是否确实为该订单的实际金额（即商户订单创建时的金额），
		3、校验通知中的seller_id（或者seller_email) 是否为out_trade_no这笔单据的对应的操作方（有的时候，一个商户可能有多个seller_id/seller_email）
		4、验证app_id是否为该商户本身。
		*/
		if(signVerified) {//验证成功
			//商户订单号
			String out_trade_no = new String(request.getParameter("out_trade_no"));
			//交易状态
			String trade_status = new String(request.getParameter("trade_status"));
			
			//验证该通知数据中的out_trade_no是否为商户系统中创建的订单号
			AlipayOrder order =  alipayService.getOrder(out_trade_no);
			if(order != null) {
				//TODO:更新订单
				if(trade_status.equals("TRADE_FINISHED")){
					//判断该笔订单是否在商户网站中已经做过处理
					//如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
					//如果有做过处理，不执行商户的业务程序
						
					//注意：
					//退款日期超过可退款期限后（如三个月可退款），支付宝系统发送该交易状态通知
				}else if (trade_status.equals("TRADE_SUCCESS")){
					//判断该笔订单是否在商户网站中已经做过处理
					//如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
					//如果有做过处理，不执行商户的业务程序
					
					//注意：
					//付款完成后，支付宝系统发送该交易状态通知
				}
				System.out.println("success");
				//通知支付宝交易成功，否则支付宝会一直尝试异步通知
				response.getWriter().println("success");
			}
		} else {
			System.out.println("fail");
			response.getWriter().println("fail");
		}
	}
	
	/**
	 * 统一收单交易退款接口 
	 * @param order
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/alipay/refund")
	public void refund(AlipayOrder order, HttpServletResponse response) throws Exception {
		//创建退款请求，设置请求参数
		AlipayTradeRefundRequest alipayRequest = new AlipayTradeRefundRequest();
		
		alipayRequest.setBizContent("{\"out_trade_no\":\""+ order.getOut_trade_no() +"\"," 
				+ "\"trade_no\":\""+ order.getTrade_no() +"\"," 
				+ "\"refund_amount\":\""+ order.getRefund_amount() +"\"," 
				+ "\"refund_reason\":\""+ order.getRefund_reason() +"\"," 
				+ "\"out_request_no\":\""+ order.getOut_request_no() +"\"}");
		
		//调用SDK发起请求
		String result = alipayClient.execute(alipayRequest).getBody();
		
		//TODO:解析返回对象，更新系统订单状态
		
		//输出
		response.getWriter().println(result);
	}
}
