package com.bounter.pay.config;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import com.github.wxpay.sdk.WXPayConfig;

public class WxPayConfig implements WXPayConfig{

	private byte[] certData;
	
	//初始化退款、撤销时的商户证书
    public WxPayConfig() throws Exception {
    	String certPath = "D://第三方开放平台/wx_apiclient_cert.p12";
        File file = new File(certPath);
        InputStream certStream = new FileInputStream(file);
        this.certData = new byte[(int) file.length()];
        certStream.read(this.certData);
        certStream.close();
    }

    public String getAppID() {
        return "";
    }

    /** 微信支付商户号 */
    public String getMchID() {
        return "";
    }

    public String getKey() {
        return "";
    }

    public int getHttpConnectTimeoutMs() {
        return 8000;
    }

    public int getHttpReadTimeoutMs() {
        return 10000;
    }

	@Override
	public InputStream getCertStream() {
		ByteArrayInputStream certBis;
        certBis = new ByteArrayInputStream(this.certData);
        return certBis;
	}
}
