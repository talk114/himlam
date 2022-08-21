package clientTest;


import gateway.core.channel.viettelpost.dto.req.ViettelPostReq;

import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ClientTestVTTPost extends BaseTest {

	static {
		CHANNEL_NAME = "VIETTEL_POST";
		USER_CODE = "NGL_VTTPOST";
		MD5 = "04392bb24a98c047dce89046f08ad188";
		USER_AUTH = "NL@2017";
		PASS_AUTH = "0290f14cf8c4fb4c38deef8479cab69b";
	}

	public static void main(String args[]) throws Exception {
		String md5Key = "NganluongViettelPost@23d20c827aef88c1";
		String cypherKey = "PeaceSoftViettelPost@2018";

		ViettelPostReq req = null;

		req = buildInquery();
//		req = buildPayment();
//		req = buildCheckQuery();

		System.out.println("Request: " + mapper.writeValueAsString(req));
//		String requestPartner = ViettelPostSecurity.encryptAES(mapper.writeValueAsString(req), ChannelCommonConfig.VTT_ENCRYPT_KEY);

		String funcPartner = "viettelPostApi";

//		System.out.println("ssss" + requestPartner);
//		callApiPartner(requestPartner, funcPartner, 0);

		// resetChannel(1);
	}

	private static ViettelPostReq buildPayment() throws NoSuchAlgorithmException {
		SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
		ViettelPostReq req = new ViettelPostReq();
		req.setFunction("payment");
		req.setAccessCode("PS");
		req.setServiceCode("PSPAY"); // VMPAY, NLPAY

		req.setPaymentId("PS2-19707699");
		req.setRequestId("VTT-TRANS-ID28");
		req.setAmount("21200");
		req.setDateTime(df.format(new Date()));
//		req.setChecksum(PGSecurity.md5(req.dataBeforeChecksum(ChannelCommonConfig.VTT_AUTH_KEY, req.getFunction())));

		return req;
	}

	private static ViettelPostReq buildCheckQuery() throws NoSuchAlgorithmException {
		SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
		ViettelPostReq req = new ViettelPostReq();
		req.setFunction("query");
		req.setAccessCode("PS");
		req.setServiceCode("PSPAY"); // VMPAY, NLPAY

		req.setPaymentId("PS2-5042731");
//		req.setRequestId("VTT-TRANS-ID32");
//		req.setAmount("21200");
		req.setDateTime(df.format(new Date()));
//		req.setChecksum(PGSecurity.md5(req.dataBeforeChecksum(ChannelCommonConfig.VTT_AUTH_KEY, req.getFunction())));

		return req;
	}

	private static ViettelPostReq buildInquery() throws NoSuchAlgorithmException {
		SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
		ViettelPostReq req = new ViettelPostReq();
		req.setFunction("inquery");
		req.setAccessCode("PS");
		req.setServiceCode("PSPAY"); // VMPAY, NLPAY

		req.setPaymentId("PS2-19707699");
		req.setRequestId("VTT-TRANS-ID");
		req.setDateTime(df.format(new Date()));
//		req.setChecksum(PGSecurity.md5(req.dataBeforeChecksum(ChannelCommonConfig.VTT_AUTH_KEY, req.getFunction())));

		return req;
	}

	protected static String getTrxTime() {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		return df.format(new Date());
	}
}
