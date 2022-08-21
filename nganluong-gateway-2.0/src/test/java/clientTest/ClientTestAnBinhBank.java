package clientTest;

import gateway.core.dto.request.DataRequest;
import gateway.core.util.RandomUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ClientTestAnBinhBank extends BaseTest {

	static {
		CHANNEL_NAME = "AN_BINH_BANK_NGL";
		USER_CODE = "NL";
		MD5 = "04392bb24a98c047dce89046f08ad188";
		USER_AUTH = "NL@2017";
		PASS_AUTH = "0290f14cf8c4fb4c38deef8479cab69b";
	}

	public static void main(String args[]) throws Exception {

		String func = "";
		DataRequest param = null;

//		func = "PaymentWithBankAcc";
//		param = paymentWithBankAcc();

		func = "DoiSoat";
		param = DoiSoat();

//		func = "GetFileLog";
//		param = GetFileLog();


//		 func = "VerifyOtp";
//		 param = verifyOtp();

//		 func = "GetBalance";
//		 param = getBalance();

//		 func = "CheckTransStatus";
//		 param = checkTransStatus();

		callApi(mapper.writeValueAsString(param), func, 1);
//		 resetChannel(1);
	}

	private static DataRequest GetFileLog() {
		DataRequest req = new DataRequest();
		req.setTransTime(getTrxTime());
		return req;
	}

	private static DataRequest DoiSoat() {
		DataRequest req = new DataRequest();
		req.setTransTime(getTrxTime());
		return req;
	}

	private static DataRequest paymentWithBankAcc() {
		DataRequest req = new DataRequest();
		req.setTransId(RandomUtil.randomDigitString(10));
		req.setBillNo(RandomUtil.randomDigitString(10, "NL"));

		req.setBankAccountNumber("0011002589099");
		req.setBankAccountHolderName("HOANG NHU LAM");

		req.setCustIDNo("145049243");
		req.setCustPhoneNo("0365750826");

		req.setAmount(10000d);
		req.setTransTime(getTrxTime());
		req.setCurrencyCode("VND");

		req.setDescription("Test pay with bank acc");
		req.setClientIp("123.30.51.37");
		return req;
	}

	private static DataRequest verifyOtp() {
		DataRequest req = new DataRequest();
		req.setTransId(RandomUtil.randomDigitString(10));
		req.setVerifyType("payment"); // link bank-wallet payment
		req.setChannel("PAYMENTS"); // WALLETS PAYMENTS
		req.setOtp("abb12345");
		req.setBankTransId("ABB19047008091460301");
		req.setTransTime(getTrxTime());
		return req;
	}

	private static DataRequest getBalance() {
		DataRequest req = new DataRequest();
		req.setTransId(RandomUtil.randomDigitString(10));
		req.setBankAccountNumber("0521039492008");
		return req;
	}

	private static DataRequest checkTransStatus() {
		DataRequest req = new DataRequest();
		req.setTransId(RandomUtil.randomDigitString(10));
		req.setQueryTransId("7072838130");

		// 1: check trạng thái link
		// 2: check trạng thái bank2wallet
		// 3: check trạng thái wallet2bank
		return req;
	}

	protected static String getTrxTime() {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		return df.format(new Date());
	}

}
