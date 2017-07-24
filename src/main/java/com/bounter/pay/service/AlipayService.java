package com.bounter.pay.service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.stereotype.Service;

import com.bounter.pay.entity.AlipayOrder;

/**
 * 阿里云支付模拟Service实现
 * @author simon
 *
 */
@Service
public class AlipayService {

	//模拟数据库
	private static ConcurrentMap<String,AlipayOrder> alipayMap = new ConcurrentHashMap<>();
	
	/**
	 * 新增订单
	 * @param order
	 */
	public void createOrder(AlipayOrder order) {
		alipayMap.put(order.getOut_trade_no(), order);
	}
	
	/**
	 * 查询订单
	 * @param orderTradeNo
	 * @return
	 */
	public AlipayOrder getOrder(String orderTradeNo) {
		return alipayMap.get(orderTradeNo);
	}
}
