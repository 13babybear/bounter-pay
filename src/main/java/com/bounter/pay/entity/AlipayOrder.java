package com.bounter.pay.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AlipayOrder {

	// 商户订单号，商户网站订单系统中唯一订单号，必填
	private String out_trade_no;

	// 付款金额，必填
	private Double total_amount;

	// 订单名称，必填
	private String subject;
	
	// 支付宝交易号
	private String trade_no;
	
	// 退款金额，退款时必填
	private Double refund_amount;
	
	// 退款的原因说明, 可空
	private String refund_reason;
	
	// 标识一次退款请求，同一笔交易多次退款需要保证唯一，如需部分退款，则此参数必传
	private String out_request_no;

	// 商品描述，可空
	private String body;

	public String getOut_trade_no() {
		return out_trade_no;
	}

	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}

	public Double getTotal_amount() {
		return total_amount;
	}

	public void setTotal_amount(Double total_amount) {
		this.total_amount = total_amount;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getTrade_no() {
		return trade_no;
	}

	public void setTrade_no(String trade_no) {
		this.trade_no = trade_no;
	}

	public Double getRefund_amount() {
		return refund_amount;
	}

	public void setRefund_amount(Double refund_amount) {
		this.refund_amount = refund_amount;
	}

	public String getRefund_reason() {
		return refund_reason;
	}

	public void setRefund_reason(String refund_reason) {
		this.refund_reason = refund_reason;
	}

	public String getOut_request_no() {
		return out_request_no;
	}

	public void setOut_request_no(String out_request_no) {
		this.out_request_no = out_request_no;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

}
