package com.bounter.pay.service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.stereotype.Service;

import com.bounter.pay.entity.WxPayOrder;

/**
 * 微信支付模拟Service实现
 * @author simon
 *
 */
@Service
public class WxPayService {

	//模拟数据库
	private static ConcurrentMap<String,WxPayOrder> wxpayMap = new ConcurrentHashMap<>();
	
	/**
	 * 新增订单
	 * @param order
	 */
	public void createOrder(WxPayOrder order) {
		wxpayMap.put(order.getOut_trade_no(), order);
	}
	
	/**
	 * 查询订单
	 * @param orderTradeNo
	 * @return
	 */
	public WxPayOrder getOrder(String orderTradeNo) {
		return wxpayMap.get(orderTradeNo);
	}
}
