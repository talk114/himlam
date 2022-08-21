package clientTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import gateway.core.dto.PGRequest;
import gateway.core.dto.request.DataRequest;
import gateway.core.util.HttpUtil;
import gateway.core.util.PGSecurity;
import gateway.core.util.RandomUtil;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class ClientTestSTB extends BaseTest {

	static {
		 CHANNEL_NAME = "STB";
		 USER_CODE = "NL";
		 MD5 = "04392bb24a98c047dce89046f08ad188";
		 USER_AUTH = "NL@2017";
		 PASS_AUTH = "0290f14cf8c4fb4c38deef8479cab69b";

//		CHANNEL_NAME = "STB_IBFT";
//		USER_CODE = "NL";
//		MD5 = "04392bb24a98c047dce89046f08ad188";
//		USER_AUTH = "NL@2017";
//		PASS_AUTH = "0290f14cf8c4fb4c38deef8479cab69b";

//		CHANNEL_NAME = "STB_VIMO";
//		USER_CODE = "VIMO_STB";
//		MD5 = "6c65701d250a23cc5d47ec986a621e07";
//		USER_AUTH = "VMSTB@#$2018";
//		PASS_AUTH = "f3f839489a6b5519a7f51e05eade6e83";
	}

	public static void main(String args[]) throws IOException, NoSuchAlgorithmException {

		String func = "";
		DataRequest req = null;

		func = "CheckCard";//
		req = buildParamCheckCard();//

		// func = "CheckBankAccSTB";
		// req = buildParamCheckBankAcc();

		// func = "CheckCardIBFT";
		// req = buildCheckCardiBFT();

		// func = "TransferToCardIBFT";
		// req = buildParamTransferToCardiBFT();

		// func = "LinkCard";
		// req = buildParamLinkcard();

		// func = "VerifyOtpLinkCard";
		// req = buildParamLinkcardStep2();

		// func = "UnLinkCard";
		// req = buildParamUnLinkcard();

		// func = "PaymentWithProfileId";
		// req = buildParamPayWithProfile();

//		 func = "CheckCard";
//		 req = buildParamCheckCard();

//		 func = "TransferToCard";//
//		 req = buildParamTransferToCard();//

		// func = "CheckLinkCard";
		// req = buildParamCheckLinkCard();

		// func = "TransferToLinkCard";
		// req = buildParamTransferToLinkCard();

		// func = "QueryBalance";
		// req = buildParamQueryBalance();

		// func = "Query";
		// req = buildParamQuery();

		// func = "Payment";
		// req = buildParamPayment();

		// func = "VerifyOtpPayment";
		// req = buildParamVerifyOtpPayment();

		// func = "CheckVisaMasterCard";
		// req = buildParamCheckVisaMasterCard();

		// func = "TransferToVisaMasterCard";
		// req = buildParamTransferVisaMaster();

		// func = "Cardless";
		// req = buildParamCardless();

		// func = "CancelCardless";
		// req = buildParamCancelCardless();

		// func = "TransferToBankAccSTB";
		// req = buildParamTransferBankAcc();

		// func = "TransferToBankAccIBFT";
		// req = buildParamTransferBankAccIBFT();

		// resetChannel("STB_VIMO", 0);
		// resetChannel("STB", 2);

		callApi(mapper.writeValueAsString(req), func, 1);

	}

	public static void callApiEcom(DataRequest param, String func, int isLocal)
			throws IOException, NoSuchAlgorithmException {
		String url = "";
		if (isLocal == 0)
			url = "http://localhost:8080/PaymentGateway/restful/api/request";
		else if (isLocal == 1)
			url = "http://10.0.0.70:8081/PaymentGateway/restful/api/request";

		ObjectMapper mapper = new ObjectMapper();

		PGRequest request = new PGRequest();
		request.setChannelName("STB");
		request.setFnc(func);
		request.setPgUserCode("VIMO");
		request.setData(mapper.writeValueAsString(param));
		request.setChecksum(PGSecurity.md5(request.getData() + "04392bb24a98c047dce89046f08ad188"));

		String userPass = "Vimo@2017" + ":" + "0290f14cf8c4fb4c38deef8479cab69b";
		String responseFromService = HttpUtil.sendRequest(url, mapper.writeValueAsString(request), userPass);
		System.out.println("Response String: " + responseFromService);
	}

	static DataRequest buildCheckCardiBFT() {
		DataRequest req = new DataRequest();
		req.setTransId(RandomUtil.randomDigitString(12, "VM"));
		req.setTransTime(getTrxTime());
		req.setDescription("IBFT CRD                                ");
		req.setCardNumber("9704050729437553");
		// 9704366806423244017
		req.setAmount(111110d);

		return req;
	}

	static DataRequest buildParamTransferToCardiBFT() {
		DataRequest req = new DataRequest();
		req.setTransId(RandomUtil.randomDigitString(10, "VM"));
		req.setInquiryTransId("VM6838407045");
		req.setAmount(100000d);
		req.setTransTime(getTrxTime());
		req.setDescription("TRANSFER TO CARD");
		req.setCardNumber("9704030006998907");

		return req;
	}

	static DataRequest buildParamQuery() {
		DataRequest req = new DataRequest();

		req.setQueryTransId("VM0001113842");
		req.setQueryType("DomesticAccountFundTransfer");
		req.setTransTime(getTrxTime());
		req.setQueryTransDate("20190118");
		return req;
	}

	static DataRequest buildParamLinkcard() {
		DataRequest req = new DataRequest();
		req.setTransId(RandomUtil.randomDigitString(10, "VM"));
		req.setTransTime(getTrxTime());

		req.setCardNumber("9704030003772081");
		req.setCardHolerName("NGUYEN SY NGUYEN");

		// req.setUserId("0902033612");
		req.setCustIDNo("040371666");
		return req;
	}

	static DataRequest buildParamLinkcardStep2() {
		DataRequest req = new DataRequest();
		req.setTransId("VM3571915938");
		req.setTransTime(getTrxTime());

		req.setCardNumber("9704030003772081");
		req.setCardHolerName("NGUYEN SY NGUYEN");

		req.setUserId("0902033612");
		req.setCustIDNo("040371666");
		req.setOtp("123456");
		return req;
	}

	static DataRequest buildParamUnLinkcard() {
		DataRequest req = new DataRequest();
		req.setTransId(RandomUtil.randomDigitString(10, "VM"));
		req.setTransTime(getTrxTime());

		req.setUserId("0902033612");
		req.setToken("2962448698");
		req.setDescription("Huy lien ket the");
		return req;
	}

	static DataRequest buildParamQueryBalance() {
		DataRequest req = new DataRequest();
		req.setTransId(RandomUtil.randomDigitString(10, "VM"));
		req.setTransTime(getTrxTime());
		return req;
	}

	static DataRequest buildParamPayWithProfile() {
		DataRequest req = new DataRequest();
		req.setTransId(RandomUtil.randomDigitString(10, "VM"));
		req.setTransTime(getTrxTime());

		req.setUserId("0902033612");
		req.setToken("2962448698");
		req.setAmount(100000d);
		req.setDescription("Thanh toan bang the Lien ket");
		return req;
	}

	static DataRequest buildParamPayment() {
		DataRequest req = new DataRequest();
		req.setTransId(RandomUtil.randomDigitString(10, "VM"));
		req.setTransTime(getTrxTime());
		// req.setCustIDNo("04111111111");

		req.setCardNumber("9704035968469292324324");
		req.setCardHolerName("NGUYEN HONG HANH");

		req.setAmount(2000d);
		req.setDescription("Payment with card");

		return req;
	}

	static DataRequest buildParamVerifyOtpPayment() {
		DataRequest req = new DataRequest();
		req.setTransId("VM6533929839");
		req.setTransTime(getTrxTime());
		// req.setCustIDNo("04111111111");

		req.setCardNumber("9704035968469292");
		req.setCardHolerName("NGUYEN HONG HANH");

		req.setOtp("532503");
		req.setAmount(2000d);
		req.setDescription("Verify OTP Payment with card");

		return req;
	}

	static DataRequest buildParamCheckCard() {
		DataRequest req = new DataRequest();
		req.setTransId(RandomUtil.randomDigitString(10, "NL"));
		req.setTransTime(getTrxTime());

//		req.setAmount(0d);
		req.setCardNumber("9704032720804098");
		req.setBenAcctNo("060129997500");
//		req.setDescription("CHECK CARD");
		return req;
	}

	static DataRequest buildParamTransferToCard() {
		DataRequest req = new DataRequest();
		req.setAmount(10000d);
		req.setTransId(RandomUtil.randomDigitString(10, "NL"));
		req.setInquiryTransId("NL7207067019");
		req.setTransTime(getTrxTime());

		req.setBenAcctNo("2235684251");
		req.setDescription("TRANSFER TO CARD");
		req.setUserName("NGO VAN A");

		return req;
	}

	static DataRequest buildParamCheckLinkCard() {
		DataRequest req = new DataRequest();
		req.setTransId(RandomUtil.randomDigitString(10, "VM"));
		req.setTransTime(getTrxTime());

		req.setAmount(100000d);
		req.setToken("2962448698");
		req.setDescription("CHECK CARD");
		return req;
	}

	static DataRequest buildParamTransferToLinkCard() {
		DataRequest req = new DataRequest();
		req.setTransId(RandomUtil.randomDigitString(10, "VM"));
		req.setTransTime(getTrxTime());

		req.setAmount(100000d);
		req.setToken("2962448698");
		req.setDescription("TRANSFER TO LINK CARD");
		req.setInquiryTransId("VM6905702210");

		return req;
	}

	static DataRequest buildParamCheckVisaMasterCard() {
		DataRequest req = new DataRequest();
		req.setTransId(RandomUtil.randomDigitString(10, "VM"));
		req.setTransTime(getTrxTime());

		req.setCardNumber("402704060178915"); // 5463070010000000
												// 4111111111111111
		req.setAmount(0d);
		req.setDescription("CHECK VISA MASTER CARD");

		return req;
	}

	static DataRequest buildParamTransferVisaMaster() {
		DataRequest req = new DataRequest();
		req.setTransId(RandomUtil.randomDigitString(10, "VM"));
		req.setTransTime(getTrxTime());

		req.setInquiryTransId("VM0510981012");
		req.setCardNumber("5463070010000000"); // 5463070010000000
												// 4111111111111111
		req.setCardToken("3314048318400763"); // 2989499606402043
												// 3314048318400763
		req.setAmount(120000d);
		req.setDescription("TRANSFER VISA MASTER CARD");

		return req;
	}

	static DataRequest buildParamCardless() {
		DataRequest req = new DataRequest();
		req.setTransId(RandomUtil.randomDigitString(10, "VM"));
		req.setTransTime(getTrxTime());

		req.setUserName("NGUYEN TUAN VINH");
		req.setBenMobileNum("0902033612");
		req.setAmount(100000d);
		req.setDescription("TEST CARDLESS");
		return req;
	}

	static DataRequest buildParamReversalCardless() {
		DataRequest req = new DataRequest();
		req.setTransId(RandomUtil.randomDigitString(10, "VM"));
		req.setTransTime(getTrxTime());

		req.setUserName("NGUYEN TUAN VINH");
		req.setBenMobileNum("0902033612");
		req.setAmount(100000d);
		req.setDescription("TEST CARDLESS");
		req.setReversalTransId("VM7271311321");
		return req;
	}

	static DataRequest buildParamCancelCardless() {
		DataRequest req = new DataRequest();
		req.setTransId(RandomUtil.randomDigitString(10, "VM"));
		req.setTransTime(getTrxTime());

		req.setUserName("NGUYEN TUAN VINH");
		req.setBenMobileNum("0902033612");
		req.setAmount(100000d);
		req.setDescription("TEST CARDLESS");
		req.setCancelTransId("VM7271311321");
		return req;
	}

	static DataRequest buildParamCheckBankAcc() {
		DataRequest req = new DataRequest();
		req.setTransId(RandomUtil.randomDigitString(10, "VM"));
		req.setTransTime(getTrxTime());

		req.setAmount(0d);
		req.setBenAcctNo("");
		req.setDescription("CHECK BANK ACC");
		return req;
	}

	static DataRequest buildParamTransferBankAcc() {
		DataRequest req = new DataRequest();
		req.setTransId(RandomUtil.randomDigitString(10, "VM"));
		req.setTransTime(getTrxTime());

		req.setAmount(100000d);
		req.setBenAcctNo("3828686679");
		req.setInquiryTransId("NGL957967643");

		return req;
	}

	static DataRequest buildParamCheckBankIBFT() {
		DataRequest req = new DataRequest();
		req.setTransId(RandomUtil.randomDigitString(9, "VMM"));
		req.setTransTime(getTrxTime());

		req.setAmount(0d);
		req.setBenAcctNo("141995537"); // 1004796319
		req.setBenBankId("970432");
		req.setDescription("IBFT ACC  BANKACCOUNT");
		return req;
	}

	static DataRequest buildParamTransferBankAccIBFT() {
		DataRequest req = new DataRequest();
		req.setTransId(RandomUtil.randomDigitString(10, "VM"));
		req.setTransTime(getTrxTime());

		req.setAmount(100000d);
		req.setBenAcctNo("3828686679");
		req.setBenBankId("970419");
		req.setInquiryTransId("NGL957967643");

		return req;
	}

	// static String getTrxTime() {
	// Calendar c = Calendar.getInstance();
	// SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
	// return df.format(c.getTime());
	// }
}
