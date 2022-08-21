package clientTest;

import gateway.core.channel.ChannelCommonConfig;
import gateway.core.channel.vnpost.VnPostSecurity;
import gateway.core.channel.vnpost.dto.req.BaseVnpostApiReq;
import gateway.core.util.PGSecurity;
import gateway.core.util.RandomUtil;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ClientTestVNPOST extends BaseTest {

	// https://sandbox.nganluong.vn:8088/nl35/vnpost.api.post.php
	// https://uat.nganluong.vn/vnpost.api.post.php
//	static final String key = "NEXTTECH11Hlx36rcZGIQE20042018"; // key live
	static final String key = "NL@!#@$#%#^^#@^"; // key test
	static {
		CHANNEL_NAME = "VNPOST";
		USER_CODE = "NL_VNPOST";
		MD5 = "17a441e8e5e9e8f5a81aa97f7c355b31";
		USER_AUTH = "NLVNPOST@456";
		PASS_AUTH = "1a758ab1c6efd7eb0b817b23697ee63e";
	}

	public static void main(String args[]) throws Exception {
		String funcPartner = "vnpostApi";

		BaseVnpostApiReq input = null;
		//CashIn
//		input = paramInqueryCashIn();
//		 input = paramWithdrawCashIn();

		 //CashOut
//		input = paramInqueryCashOut();
		input = paramWithdrawCashOut();

		String requestPartner = decryptReq(input);
		callApiPartner(requestPartner, funcPartner, 0);


		// resetChannel("VNPOST",2);

	}

	//TEST CASE ---CASHIN---

	static BaseVnpostApiReq paramInqueryCashIn() {
		BaseVnpostApiReq req = new BaseVnpostApiReq();
		req.setFunc("inquery");
		req.setAccessCode("NL");
		req.setVnpostServiceCode("NLCASHIN");
		req.setCashinId("NL19707047");
		req.setRequestId(RandomUtil.randomDigitString(6, "VNPOST"));
		req.setDateTime(getTrxTime());
		return req;
	}

	static BaseVnpostApiReq paramWithdrawCashIn() {
		BaseVnpostApiReq req = new BaseVnpostApiReq();
		req.setFunc("payment"); // withdrawconfirm
		req.setAccessCode("NL");
		req.setVnpostServiceCode("NLCASHIN");
		req.setCashinId("NL19707047");
		req.setVerifyCode("4e6bf82a8c43369d1642b244a026d156");

		req.setRequestId(RandomUtil.randomDigitString(6, "VNPOST"));
		req.setDateTime(getTrxTime());
		return req;
	}

	//TEST CASE ---CASHOUT---
	static BaseVnpostApiReq paramInqueryCashOut() {
		BaseVnpostApiReq req = new BaseVnpostApiReq();
		req.setFunc("inquery");
		req.setAccessCode("NL");
		req.setVnpostServiceCode("NLCASHOUT");
		req.setCashoutId("NL322");
		req.setRequestId(RandomUtil.randomDigitString(6, "VNPOST"));
		req.setDateTime(getTrxTime());
		return req;
	}

	static BaseVnpostApiReq paramWithdrawCashOut() {
		BaseVnpostApiReq req = new BaseVnpostApiReq();
		req.setFunc("withdrawconfirm"); // withdrawconfirm
		req.setAccessCode("NL");
		req.setVnpostServiceCode("NLCASHOUT");
		req.setCashoutId("NL322");// NL322, NL323, NL324, NL325, NL326
		req.setVerifyCode("5737c6ec2e0716f3d8a7a5c4e0de0d9a");

		req.setRequestId(RandomUtil.randomDigitString(6, "VNPOST"));
		req.setDateTime(getTrxTime());
		return req;
	}

	protected static String getTrxTime() {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		return df.format(new Date());
	}

	static String decryptReq(BaseVnpostApiReq input) throws Exception {
		String cash = "";
		if (!StringUtils.isEmpty(input.getCashinId())) {
			cash = input.getCashinId();
		} else if (!StringUtils.isEmpty(input.getCashoutId())) {
			cash = input.getCashoutId();
		}
		String md5 = PGSecurity.md5(input.getFunc(), input.getAccessCode(), ChannelCommonConfig.VNPOST_ENCRYPT_KEY, input.getVnpostServiceCode(), cash,
				input.getRequestId(), input.getDateTime());
		input.setChecksum(md5);

		System.out.println("Request: " + mapper.writeValueAsString(input));
		String requestPartner = VnPostSecurity.openssl_encrypt(mapper.writeValueAsString(input), ChannelCommonConfig.VNPOST_ENCRYPT_KEY);
		return requestPartner;
	}
}
