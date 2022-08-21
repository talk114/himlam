package clientTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import gateway.core.dto.request.DataRequest;
import gateway.core.util.RandomUtil;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class ClientTestVCCB extends BaseTest {

	static {
		USER_CODE = "NL";
		USER_AUTH = "NL@2017";
		PASS_AUTH = "0290f14cf8c4fb4c38deef8479cab69b";
		MD5 = "04392bb24a98c047dce89046f08ad188";
		CHANNEL_NAME = "VCCB";

	}

	public static void main(String args[]) throws Exception {

		// Search so du Nha cung cap
		String function = "";
		String param = null;

//		function = "CheckBalance";
//		param = CheckBalance();

//		function = "CheckCardVCCB";
//		param = CheckCardVCCB();

//		function = "CheckBankAccVCCB";
//		param = CheckBankAccVCCB();

//		function = "CheckCardIBFT";
//		param = CheckCardIBFT();

//		function = "CheckBankAccIBFT";
//		param = CheckBankAccIBFT();
//		
//		function = "QueryInfoAccount";
//		param = checkbalance();
		
//		function = "TransferCardVCCB";
//		param = TransferCardVCCB();

//		function = "TransferBankAccVCCB";
//		param = TransferBankAccVCCB();

//		function = "TransferCardIBFT";
//		param = TransferCardIBFT();

//		function = "TransferBankAccIBFT";
//		param = TransferBankAccIBFT();

		function = "UploadReconciliationNGLA";
		param = UploadReconciliationNGLA();

		//resetChannel(1);
		callApi(param, function, 1);
	}
	
	static String TransferBankAccIBFT() throws JsonProcessingException {
		DataRequest param = new DataRequest();
		param.setTransId("NGLA190823" + RandomUtil.randomDigitString(6));
		param.setTransTime(buildTransTime() + "");
		param.setMerchantId("NGLA");
//		param.setUserId("0123456789");
		
		param.setAmount((double)2000000);
		param.setBankAccountNumber("0129837294");
		param.setBankCode("970406");
		return mapper.writeValueAsString(param);
	}
	
	static String CheckBalanceBankAccIBFT() throws JsonProcessingException {
		DataRequest param = new DataRequest();
		param.setTransId("NGLA190823" + RandomUtil.randomDigitString(6));
		param.setTransTime(buildTransTime() + "");
		param.setMerchantId("NGLA");
		
		param.setBankAccountNumber("0047041028926");
		return mapper.writeValueAsString(param);
	}
	
	static String TransferBankAccVCCB() throws JsonProcessingException {
		DataRequest param = new DataRequest();
		param.setTransId("NGLA190823" + RandomUtil.randomDigitString(6));
		param.setTransTime(buildTransTime() + "");
		param.setMerchantId("NGLA");
//		param.setUserId("0123456789");
		param.setAmount((double)200000000);

		param.setBankAccountNumber("0027041000906");
		param.setBankCode("970454");
		return mapper.writeValueAsString(param);
	}
//
	
	public static String UploadReconciliationNGLA() throws JsonProcessingException {
		DataRequest param = new DataRequest();
		param.setTransId("NGLA190823" + RandomUtil.randomDigitString(6));
		param.setTransTime(buildTransTime() + "");
		param.setMerchantId("NGLA");
		param.setProcessDate("20210110");

		// param.setData("[{\\\"merchant_id\\\":\\\"NGLA\\\",\\\"trans_id\\\":\\\"NGLA20190905\\\",\\\"trans_time\\\":\\\"20190905104239\\\",\\\"card_number\\\":\\\"1425374127612\\\",\\\"card_ind\\\":\\\"Y\\\",\\\"business_type\\\":\\\"B2B\\\",\\\"amount\\\":1000000,\\\"currency_code\\\":\\\"VND\\\",\\\"description\\\":\\\"test\\\",\\\"bank_trans_id\\\":\\\"NGLA20190905\\\",\\\"reversal_ind\\\":\\\"N\\\",\\\"trans_status\\\":\\\"00\\\",\\\"trans_type\\\":\\\"2101\\\"},{\\\"merchant_id\\\":\\\"NGLA\\\",\\\"trans_id\\\":\\\"NGLA20190905\\\",\\\"trans_time\\\":\\\"20190905104239\\\",\\\"card_number\\\":\\\"1425374127612\\\",\\\"card_ind\\\":\\\"Y\\\",\\\"business_type\\\":\\\"B2B\\\",\\\"amount\\\":1000000,\\\"currency_code\\\":\\\"VND\\\",\\\"description\\\":\\\"test\\\",\\\"bank_trans_id\\\":\\\"NGLA20190905\\\",\\\"reversal_ind\\\":\\\"N\\\",\\\"trans_status\\\":\\\"00\\\",\\\"trans_type\\\":\\\"2101\\\"}]");
		param.setData("[{\"merchant_id\":\"NGLA\",\"trans_id\":\"NGLA20190905\",\"trans_time\":\"20190905104239\",\"card_number\":\"1425374127612\",\"card_ind\":\"Y\",\"business_type\":\"B2B\",\"amount\":1000000,\"currency_code\":\"VND\",\"description\":\"test\",\"bank_trans_id\":\"NGLA20190905\",\"reversal_ind\":\"N\",\"trans_status\":\"00\",\"trans_type\":\"2101\"},{\"merchant_id\":\"NGLA\",\"trans_id\":\"NGLA20190905\",\"trans_time\":\"20190905104239\",\"card_number\":\"1425374127612\",\"card_ind\":\"Y\",\"business_type\":\"B2B\",\"amount\":1000000,\"currency_code\":\"VND\",\"description\":\"test\",\"bank_trans_id\":\"NGLA20190905\",\"reversal_ind\":\"N\",\"trans_status\":\"00\",\"trans_type\":\"2101\"}]");


		return mapper.writeValueAsString(param);
	}
	
	
	
	private static String buildTransTime() {
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
		return now.format(formatter);
	}
	
	static String CheckBankAccVCCB() throws JsonProcessingException {
		DataRequest param = new DataRequest();
		param.setTransId("NGLA190823" + RandomUtil.randomDigitString(6));
		param.setTransTime(buildTransTime());
		param.setMerchantId("NGLA");
		
		param.setBankAccountNumber("0687041000560");
		param.setBankCode("970406");
		return mapper.writeValueAsString(param);
	}

	static String CheckBankAccIBFT() throws JsonProcessingException {
		DataRequest param = new DataRequest();
		param.setTransId("NGLA190823" + RandomUtil.randomDigitString(6));
		param.setTransTime(buildTransTime());
		param.setMerchantId("NGLB");
		param.setDescription("Rut tien ve NH TCB");
		param.setBankAccountNumber("19036653838011");
		param.setBankCode("970407");
		return mapper.writeValueAsString(param);
	}
	
	static String CheckCardVCCB() throws JsonProcessingException {
		DataRequest param = new DataRequest();
		param.setTransId("NGLA190823" + RandomUtil.randomDigitString(6));
		param.setTransTime(buildTransTime());
		param.setMerchantId("NGLA");
		
		param.setBankAccountNumber("9704542000167339");
		param.setBankCode("970406");
		return mapper.writeValueAsString(param);
	}
	
	static String TransferCardIBFT() throws JsonProcessingException {
		DataRequest param = new DataRequest();
		param.setTransId("NGLA190823" + RandomUtil.randomDigitString(6));
		param.setTransTime(buildTransTime());
		param.setMerchantId("NGLA");
		
		param.setCardNumber("9704060129837294");
		param.setBankCode("970406");
		param.setAmount((double)1000000);
		return mapper.writeValueAsString(param);
	}
	
	static String TransferCardVCCB() throws JsonProcessingException {
		DataRequest param = new DataRequest();
		param.setTransId("NGLA190823" + RandomUtil.randomDigitString(6));
		param.setTransTime(buildTransTime());
		param.setMerchantId("NGLA");
		//param.setUserId("0123456789");
		
		param.setCardNumber("9704542000167339");
		param.setBankCode("970406");
		param.setAmount((double)400000);
		return mapper.writeValueAsString(param);
	}
	
//	static String CheckBalance() throws JsonProcessingException {
//		DataRequest param = new DataRequest();
//		param.setTransId("NGLA190823" + RandomUtil.randomDigitString(6));
//		param.setTransTime(buildTransTime() + "");
//		param.setMerchantId("NGLA");
//		param.setUserId("0123456789");
//		
//		param.setBankAccountNumber("0027041000906");
//		return mapper.writeValueAsString(param);
//	}
//	
	static String CheckCardIBFT() throws JsonProcessingException {
		DataRequest param = new DataRequest();
		param.setTransId("NGLA190823" + RandomUtil.randomDigitString(6));
		param.setTransTime(buildTransTime() + "");
		param.setMerchantId("NGLB");
		param.setUserId("0123456789");
		
		param.setCardNumber("9704222081999757");
		param.setBankCode("970422");
		param.setDescription("NGUYEN HUNG DUY");
		return mapper.writeValueAsString(param);
		
	}
//	

	static String CheckBalance() throws JsonProcessingException {

		DataRequest param = new DataRequest();
		param.setTransId("NGLA190823" + RandomUtil.randomDigitString(6));
		param.setTransTime(buildTransTime() + "");
		param.setMerchantId("NGLB");
		//param.setUserId("0123456789");

		param.setBankAccountNumber("0047041028926");
		param.setDescription("");
		return mapper.writeValueAsString(param);
	}


	protected static String getTrxTime() {
		SimpleDateFormat df = new SimpleDateFormat("MMddHHmmss");
		return df.format(new Date());
	}
}
