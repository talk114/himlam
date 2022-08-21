package gateway.core.channel.stb_ecom;

import gateway.core.channel.PaymentGate;
import gateway.core.channel.stb_ecom.dto.STBConstants;
import gateway.core.channel.stb_ecom.dto.req.*;
import gateway.core.channel.stb_ecom.dto.res.BaseDataRes;
import gateway.core.channel.stb_ecom.dto.res.BaseResponse;
import gateway.core.channel.stb_ecom.dto.res.CheckOutRes;
import gateway.core.dto.PGResponse;
import gateway.core.dto.request.DataRequest;
import gateway.core.util.PGSecurity;
import gateway.core.util.PGUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import vn.nganluong.naba.channel.vib.dto.PaymentDTO;
import vn.nganluong.naba.dto.PaymentConst;
import vn.nganluong.naba.entities.PaymentAccount;
import vn.nganluong.naba.service.CommonLogService;
import vn.nganluong.naba.service.PaymentService;
import vn.nganluong.naba.utils.RequestUtil;

import javax.net.ssl.*;
import java.io.*;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Date;
import java.util.Map;

/**
 * 
 * @author vinhnt
 *
 */
public class STBEcom extends PaymentGate {

	@Autowired
	private PaymentService paymentService;

	@Autowired
	private CommonLogService commonLogService;
	private static final String SERVICE_NAME = "STB_ECOM";

	private static final Logger logger = LogManager.getLogger(STBEcom.class);
	/**
	 * Giao dịch request OTP
	 */
	public PGResponse RequestOTP(PaymentAccount paymentAccount, String inputStr) throws Exception{
		//TODO
		WriteInfoLog("STB REQUEST OTP");
		BaseWithdrawReq req = objectMapper.readValue(inputStr, BaseWithdrawReq.class);

		BaseRequest baseRequest = new BaseRequest();
		String requestDatetime = Utils.getDateTime(new Date());
		baseRequest.setRequestDateTime(requestDatetime);
		baseRequest.setRequestID(Utils.genReqUID());
		baseRequest.setFunctionName(STBConstants.FUNC_REQUEST_OTP);

		String data =  objectMapper.writeValueAsString(req);
		baseRequest.setData(STBSecurity.Encrypt3DES(data, paymentAccount.getEncryptKey()));
		STBSecurity.initParam(paymentAccount);

		String body = objectMapper.writeValueAsString(baseRequest);
		String bodySigned = STBSecurity.sign(STBSecurity.md5(body));
		String test = STBSecurity.sign("A95F65C420007E3D2B9A7B6224FD97A5");
		// TODO
		ClientRequest.setUser(paymentAccount.getUsername());
		ClientRequest.setPassword(paymentAccount.getPassword());

		String[] paramsLog = new String[] { objectMapper.writeValueAsString(req) };

		// TODO
		logger.info(commonLogService.createContentLog(STBConstants.CHANNEL_CODE, SERVICE_NAME,
				STBConstants.FUNCTION_CODE_REQUEST_OTP, true, true, false, paramsLog));

		// TODO: Call API STB
		String res = ClientRequest.callApi(body, bodySigned);
		// TODO
		paramsLog = new String[] { objectMapper.writeValueAsString(res) };

		logger.info("STB RESPONSE  OTP" + paramsLog);
		BaseResponse response = objectMapper.readValue(res, BaseResponse.class);
		response.setData(STBSecurity.Decrypt3DES(response.getData(), paymentAccount.getEncryptKey()));

		PGResponse pGResponse = new PGResponse();
		pGResponse.setStatus(true);
		pGResponse.setErrorCode(response.getRespCode());
		pGResponse.setMessage(STBConstants.getErrorMessage(response.getRespCode()));
		pGResponse.setData(response.getData());
		return pGResponse;
	}



	/**
	 * Giao dịch request OTP ACC
	 */
	public PGResponse RequestOTPAcc(PaymentAccount paymentAccount, String inputStr) throws Exception{
		WriteInfoLog("STB REQUEST OTPAcc");
		BaseWithdrawReq req = objectMapper.readValue(inputStr, BaseWithdrawReq.class);
		BaseRequest baseRequest = new BaseRequest();

		String requestDatetime = Utils.getDateTime(new Date());
		baseRequest.setRequestDateTime(requestDatetime);
		baseRequest.setRequestID(Utils.genReqUID());
		baseRequest.setFunctionName(STBConstants.FUNC_REQUEST_OTP_ACC);

		String data =  objectMapper.writeValueAsString(req);
		baseRequest.setData(STBSecurity.Encrypt3DES(data, paymentAccount.getEncryptKey()));
		STBSecurity.initParam(paymentAccount);

		String body = objectMapper.writeValueAsString(baseRequest);
		String bodySigned = STBSecurity.sign(STBSecurity.md5(body));
		// TODO
		ClientRequest.setUser(paymentAccount.getUsername());
		ClientRequest.setPassword(paymentAccount.getPassword());

		String[] paramsLog = new String[] { objectMapper.writeValueAsString(req) };

		// TODO
		logger.info(commonLogService.createContentLog(STBConstants.CHANNEL_CODE, SERVICE_NAME,
				STBConstants.FUNCTION_CODE_REQUEST_OTP_ACC, true, true, false, paramsLog));

		// TODO: Call API STB
		String res = ClientRequest.callApi(body, bodySigned);
		// TODO
		paramsLog = new String[] { objectMapper.writeValueAsString(res) };

		logger.info("STB RESPONSE OTPAcc" + paramsLog);
		BaseResponse response = objectMapper.readValue(res, BaseResponse.class);
		response.setData(STBSecurity.Decrypt3DES(response.getData(), paymentAccount.getEncryptKey()));

		PGResponse pGResponse = new PGResponse();
		pGResponse.setStatus(true);
		pGResponse.setErrorCode(response.getRespCode());
		pGResponse.setMessage(STBConstants.getErrorMessage(response.getRespCode()));
		pGResponse.setData(response.getData());
		return pGResponse;
	}

	/**
	 * Giao dịch nạp tiền vào Ví với thẻ liên kết
	 */
	public PGResponse TopUpByCard(PaymentAccount paymentAccount, String inputStr) throws Exception{
		WriteInfoLog("STB REQUEST TopUpByCard");
		BaseTransactionReq req = objectMapper.readValue(inputStr, BaseTransactionReq.class);
		BaseRequest baseRequest = new BaseRequest();
		String requestDatetime = Utils.getDateTime(new Date());
		baseRequest.setRequestDateTime(requestDatetime);
		baseRequest.setRequestID(Utils.genReqUID());
		baseRequest.setFunctionName(STBConstants.FUNC_TOP_UP_BY_CARD);

		String data =  objectMapper.writeValueAsString(req);
		baseRequest.setData(STBSecurity.Encrypt3DES(data, paymentAccount.getEncryptKey()));
		STBSecurity.initParam(paymentAccount);

		String body = objectMapper.writeValueAsString(baseRequest);
		String bodySigned = STBSecurity.sign(STBSecurity.md5(body));
		// TODO
		ClientRequest.setUser(paymentAccount.getUsername());
		ClientRequest.setPassword(paymentAccount.getPassword());

		String[] paramsLog = new String[] { objectMapper.writeValueAsString(req) };

		// TODO
		logger.info(commonLogService.createContentLog(STBConstants.CHANNEL_CODE, SERVICE_NAME,
				STBConstants.FUNCTION_CODE_TOP_UP_BY_CARD, true, true, false, paramsLog));

		// TODO: Call API STB
		String res = ClientRequest.callApi(body, bodySigned);
		// TODO
		paramsLog = new String[] { objectMapper.writeValueAsString(res) };

		logger.info("STB RESPONSE TopUpByCard" + paramsLog);
		BaseResponse response = objectMapper.readValue(res, BaseResponse.class);
		response.setData(STBSecurity.Decrypt3DES(response.getData(), paymentAccount.getEncryptKey()));

		PGResponse pGResponse = new PGResponse();
		pGResponse.setStatus(true);
		pGResponse.setErrorCode(response.getRespCode());
		pGResponse.setMessage(STBConstants.getErrorMessage(response.getRespCode()));
		pGResponse.setData(response.getData());
		return pGResponse;
	}

	/**
	 * Giao dịch nạp tiền vào ví với tài khoản đã liên kết
	 */
	public PGResponse TopUpByAccount(PaymentAccount paymentAccount, String inputStr) throws Exception{
		WriteInfoLog("STB REQUEST TopUpByAccount");
		BaseTransactionReq req = objectMapper.readValue(inputStr, BaseTransactionReq.class);
		BaseRequest baseRequest = new BaseRequest();
		String requestDatetime = Utils.getDateTime(new Date());
		baseRequest.setRequestDateTime(requestDatetime);
		baseRequest.setRequestID(Utils.genReqUID());
		baseRequest.setFunctionName(STBConstants.FUNC_TOP_UP_BY_ACCOUNT);

		String data =  objectMapper.writeValueAsString(req);
		baseRequest.setData(STBSecurity.Encrypt3DES(data, paymentAccount.getEncryptKey()));
		STBSecurity.initParam(paymentAccount);

		String body = objectMapper.writeValueAsString(baseRequest);
		String bodySigned = STBSecurity.sign(STBSecurity.md5(body));
		// TODO
		ClientRequest.setUser(paymentAccount.getUsername());
		ClientRequest.setPassword(paymentAccount.getPassword());

		String[] paramsLog = new String[] { objectMapper.writeValueAsString(req) };

		// TODO
		logger.info(commonLogService.createContentLog(STBConstants.CHANNEL_CODE, SERVICE_NAME,
				STBConstants.FUNCTION_CODE_TOP_UP_BY_ACCOUNT, true, true, false, paramsLog));

		// TODO: Call API STB
		String res = ClientRequest.callApi(body, bodySigned);
		// TODO
		paramsLog = new String[] { objectMapper.writeValueAsString(res) };

		logger.info("STB RESPONSE TopUpByAccount" + paramsLog);
		BaseResponse response = objectMapper.readValue(res, BaseResponse.class);
		response.setData(STBSecurity.Decrypt3DES(response.getData(), paymentAccount.getEncryptKey()));

		PGResponse pGResponse = new PGResponse();
		pGResponse.setStatus(true);
		pGResponse.setErrorCode(response.getRespCode());
		pGResponse.setMessage(STBConstants.getErrorMessage(response.getRespCode()));
		pGResponse.setData(response.getData());
		return pGResponse;
	}

	/**
	 * Giao dịch thanh toán mua hàng bằng thẻ đã liên kết
	 */
	public PGResponse PurchaseByCard(PaymentAccount paymentAccount, String inputStr) throws Exception{
		WriteInfoLog("STB REQUEST PurchaseByCard");
		BaseTransactionReq req = objectMapper.readValue(inputStr, BaseTransactionReq.class);
		BaseRequest baseRequest = new BaseRequest();
		String requestDatetime = Utils.getDateTime(new Date());
		baseRequest.setRequestDateTime(requestDatetime);
		baseRequest.setRequestID(Utils.genReqUID());
		baseRequest.setFunctionName(STBConstants.FUNC_PURCHASE_BY_CARD);

		String data =  objectMapper.writeValueAsString(req);
		baseRequest.setData(STBSecurity.Encrypt3DES(data, paymentAccount.getEncryptKey()));
		STBSecurity.initParam(paymentAccount);

		String body = objectMapper.writeValueAsString(baseRequest);
		String bodySigned = STBSecurity.sign(STBSecurity.md5(body));
		// TODO
		ClientRequest.setUser(paymentAccount.getUsername());
		ClientRequest.setPassword(paymentAccount.getPassword());

		String[] paramsLog = new String[] { objectMapper.writeValueAsString(req) };

		// TODO
		logger.info(commonLogService.createContentLog(STBConstants.CHANNEL_CODE, SERVICE_NAME,
				STBConstants.FUNCTION_CODE_PURCHASE_BY_CARD, true, true, false, paramsLog));

		// TODO: Call API STB
		String res = ClientRequest.callApi(body, bodySigned);
		// TODO
		paramsLog = new String[] { objectMapper.writeValueAsString(res) };

		logger.info("STB RESPONSE PurchaseByCard" + paramsLog);
		BaseResponse response = objectMapper.readValue(res, BaseResponse.class);
		response.setData(STBSecurity.Decrypt3DES(response.getData(), paymentAccount.getEncryptKey()));

		PGResponse pGResponse = new PGResponse();
		pGResponse.setStatus(true);
		pGResponse.setErrorCode(response.getRespCode());
		pGResponse.setMessage(STBConstants.getErrorMessage(response.getRespCode()));
		pGResponse.setData(response.getData());
		return pGResponse;

	}

	/**
	 * Giao dịch thanh toán mua hàng bằng tài khoản đã liên kết
	 */
	public PGResponse PurchaseByAccount(PaymentAccount paymentAccount, String inputStr) throws Exception{
		//TODO
		WriteInfoLog("STB REQUEST PurchaseByAccount");
		BaseTransactionReq req = objectMapper.readValue(inputStr, BaseTransactionReq.class);
		BaseRequest baseRequest = new BaseRequest();
		String requestDatetime = Utils.getDateTime(new Date());
		baseRequest.setRequestDateTime(requestDatetime);
		baseRequest.setRequestID(Utils.genReqUID());
		baseRequest.setFunctionName(STBConstants.FUNC_PURCHASE_BY_ACCOUNT);

		String data =  objectMapper.writeValueAsString(req);
		baseRequest.setData(STBSecurity.Encrypt3DES(data, paymentAccount.getEncryptKey()));
		STBSecurity.initParam(paymentAccount);

		String body = objectMapper.writeValueAsString(baseRequest);
		String bodySigned = STBSecurity.sign(STBSecurity.md5(body));
		// TODO
		ClientRequest.setUser(paymentAccount.getUsername());
		ClientRequest.setPassword(paymentAccount.getPassword());

		String[] paramsLog = new String[] { objectMapper.writeValueAsString(req) };

		// TODO
		logger.info(commonLogService.createContentLog(STBConstants.CHANNEL_CODE, SERVICE_NAME,
				STBConstants.FUNCTION_CODE_PURCHASE_BY_ACCOUNT, true, true, false, paramsLog));

		// TODO: Call API STB
		String res = ClientRequest.callApi(body, bodySigned);
		// TODO
		paramsLog = new String[] { objectMapper.writeValueAsString(res) };

		logger.info("STB RESPONSE PurchaseByAccount" + paramsLog);
		BaseResponse response = objectMapper.readValue(res, BaseResponse.class);
		response.setData(STBSecurity.Decrypt3DES(response.getData(), paymentAccount.getEncryptKey()));

		PGResponse pGResponse = new PGResponse();
		pGResponse.setStatus(true);
		pGResponse.setErrorCode(response.getRespCode());
		pGResponse.setMessage(STBConstants.getErrorMessage(response.getRespCode()));
		pGResponse.setData(response.getData());
		return pGResponse;

	}

	/**
	 * Step 1 Giao dịch rút tiền từ ví về thẻ/tài khoản liên kết
	 */
	public PGResponse SubscriptionInquiry(PaymentAccount paymentAccount,String inputStr) throws Exception{
		//TODO
		WriteInfoLog("STB REQUEST SubscriptionInquiry");
		BaseWithdrawReq req = objectMapper.readValue(inputStr, BaseWithdrawReq.class);

		String requestDatetime = Utils.getDateTime(new Date());
		BaseRequest baseRequest = new BaseRequest();
		baseRequest.setRequestDateTime(requestDatetime);
		baseRequest.setRequestID(Utils.genReqUID());
		baseRequest.setFunctionName(STBConstants.FUNC_SUBSCRIPTION_INQUIRY);

		String data =  objectMapper.writeValueAsString(req);
		baseRequest.setData(STBSecurity.Encrypt3DES(data, paymentAccount.getEncryptKey()));
		STBSecurity.initParam(paymentAccount);

		String body = objectMapper.writeValueAsString(baseRequest);
		String bodySigned = STBSecurity.sign(STBSecurity.md5(body));
		// TODO
		ClientRequest.setUser(paymentAccount.getUsername());
		ClientRequest.setPassword(paymentAccount.getPassword());

		String[] paramsLog = new String[] { objectMapper.writeValueAsString(req) };

		// TODO
		logger.info(commonLogService.createContentLog(STBConstants.CHANNEL_CODE, SERVICE_NAME,
				STBConstants.FUNCTION_CODE_SUBSCRIPTION_INQUIRY, true, true, false, paramsLog));

		// TODO: Call API STB
		String res = ClientRequest.callApi(body, bodySigned);
		// TODO
		paramsLog = new String[] { objectMapper.writeValueAsString(res) };

		logger.info("STB RESPONSE  SubscriptionInquiry" + paramsLog);
		BaseResponse response = objectMapper.readValue(res, BaseResponse.class);
		response.setData(STBSecurity.Decrypt3DES(response.getData(), paymentAccount.getEncryptKey()));

		PGResponse pGResponse = new PGResponse();
		pGResponse.setStatus(true);
		pGResponse.setErrorCode(response.getRespCode());
		pGResponse.setMessage(STBConstants.getErrorMessage(response.getRespCode()));
		pGResponse.setData(response.getData());
		return pGResponse;

	}

	/**
	 * Step 2 Giao dịch rút tiền từ ví về thẻ/tài khoản liên kết
	 */
	public PGResponse CashOutSubscription(PaymentAccount paymentAccount, String inputStr) throws Exception {
		//TODO
		WriteInfoLog("STB REQUEST CashOutSubscription");
		CashOutReq req = objectMapper.readValue(inputStr,CashOutReq.class);
		BaseRequest baseRequest = new BaseRequest();
		String requestDatetime = Utils.getDateTime(new Date());
		baseRequest.setRequestDateTime(requestDatetime);
		baseRequest.setRequestID(Utils.genReqUID());
		baseRequest.setFunctionName(STBConstants.FUNC_CASH_OUT_SUBSCRIPTION);

		String data =  objectMapper.writeValueAsString(req);
		baseRequest.setData(STBSecurity.Encrypt3DES(data, paymentAccount.getEncryptKey()));
		STBSecurity.initParam(paymentAccount);

		String body = objectMapper.writeValueAsString(baseRequest);
		String bodySigned = STBSecurity.sign(STBSecurity.md5(body));
		// TODO
		ClientRequest.setUser(paymentAccount.getUsername());
		ClientRequest.setPassword(paymentAccount.getPassword());

		String[] paramsLog = new String[] { objectMapper.writeValueAsString(req) };

		// TODO
		logger.info(commonLogService.createContentLog(STBConstants.CHANNEL_CODE, SERVICE_NAME,
				STBConstants.FUNCTION_CODE_CASH_OUT_SUBSCRIPTION, true, true, false, paramsLog));

		// TODO: Call API STB
		String res = ClientRequest.callApi(body, bodySigned);
		// TODO
		paramsLog = new String[] { objectMapper.writeValueAsString(res) };

		logger.info("STB RESPONSE CashOutSubscription" + paramsLog);
		BaseResponse response = objectMapper.readValue(res, BaseResponse.class);
		response.setData(STBSecurity.Decrypt3DES(response.getData(), paymentAccount.getEncryptKey()));

		PGResponse pGResponse = new PGResponse();
		pGResponse.setStatus(true);
		pGResponse.setErrorCode(response.getRespCode());
		pGResponse.setMessage(STBConstants.getErrorMessage(response.getRespCode()));
		pGResponse.setData(response.getData());
		return pGResponse;

	}

	/**
	 * Step 1 Giao dịch thực hiện chuyển tiền từ Ví đếm thẻ sacombank
	 */
	public PGResponse CardInquiry(PaymentAccount paymentAccount, String inputStr) throws Exception {
		//TODO
		WriteInfoLog("STB REQUEST CardInquiry");
		CardInquiryReq req = objectMapper.readValue(inputStr,CardInquiryReq.class);
		BaseRequest baseRequest = new BaseRequest();
		String requestDatetime = Utils.getDateTime(new Date());
		baseRequest.setRequestDateTime(requestDatetime);
		baseRequest.setRequestID(Utils.genReqUID());
		baseRequest.setFunctionName(STBConstants.FUNC_CARD_INQUIRY);

		String data =  objectMapper.writeValueAsString(req);
		baseRequest.setData(STBSecurity.Encrypt3DES(data, paymentAccount.getEncryptKey()));
		STBSecurity.initParam(paymentAccount);

		String body = objectMapper.writeValueAsString(baseRequest);
		String bodySigned = STBSecurity.sign(STBSecurity.md5(body));
		// TODO
		ClientRequest.setUser(paymentAccount.getUsername());
		ClientRequest.setPassword(paymentAccount.getPassword());

		String[] paramsLog = new String[] { objectMapper.writeValueAsString(req) };

		// TODO
		logger.info(commonLogService.createContentLog(STBConstants.CHANNEL_CODE, SERVICE_NAME,
				STBConstants.FUNCTION_CODE_CARD_INQUIRY, true, true, false, paramsLog));

		// TODO: Call API STB
		String res = ClientRequest.callApi(body, bodySigned);
		// TODO
		paramsLog = new String[] { objectMapper.writeValueAsString(res) };

		logger.info("STB RESPONSE CardInquiry" + paramsLog);
		BaseResponse response = objectMapper.readValue(res, BaseResponse.class);
		response.setData(STBSecurity.Decrypt3DES(response.getData(), paymentAccount.getEncryptKey()));

		PGResponse pGResponse = new PGResponse();
		pGResponse.setStatus(true);
		pGResponse.setErrorCode(response.getRespCode());
		pGResponse.setMessage(STBConstants.getErrorMessage(response.getRespCode()));
		pGResponse.setData(response.getData());
		return pGResponse;
	}

	/**
	 * Step 2 Giao dịch thực hiện chuyển tiền từ Ví đếm thẻ sacombank
	 */
	public PGResponse FundTransferToSTBCard(PaymentAccount paymentAccount, String inputStr) throws Exception {
		//TODO
		WriteInfoLog("STB REQUEST FundTransferToSTBCard");
		TransferToSTBCardReq req = objectMapper.readValue(inputStr,TransferToSTBCardReq.class);
		BaseRequest baseRequest = new BaseRequest();

		String requestDatetime = Utils.getDateTime(new Date());
		baseRequest.setRequestDateTime(requestDatetime);
		baseRequest.setRequestID(Utils.genReqUID());
		baseRequest.setFunctionName(STBConstants.FUNC_TRANSFER_TO_STB_CARD);

		String data =  objectMapper.writeValueAsString(req);
		baseRequest.setData(STBSecurity.Encrypt3DES(data, paymentAccount.getEncryptKey()));
		STBSecurity.initParam(paymentAccount);

		String body = objectMapper.writeValueAsString(baseRequest);
		String bodySigned = STBSecurity.sign(STBSecurity.md5(body));
		// TODO
		ClientRequest.setUser(paymentAccount.getUsername());
		ClientRequest.setPassword(paymentAccount.getPassword());

		String[] paramsLog = new String[] { objectMapper.writeValueAsString(req) };

		// TODO
		logger.info(commonLogService.createContentLog(STBConstants.CHANNEL_CODE, SERVICE_NAME,
				STBConstants.FUNCTION_CODE_TRANSFER_TO_STB_CARD, true, true, false, paramsLog));

		// TODO: Call API STB
		String res = ClientRequest.callApi(body, bodySigned);
		// TODO
		paramsLog = new String[] { objectMapper.writeValueAsString(res) };

		logger.info("STB RESPONSE FundTransferToSTBCard" + paramsLog);
		BaseResponse response = objectMapper.readValue(res, BaseResponse.class);
		response.setData(STBSecurity.Decrypt3DES(response.getData(), paymentAccount.getEncryptKey()));

		PGResponse pGResponse = new PGResponse();
		pGResponse.setStatus(true);
		pGResponse.setErrorCode(response.getRespCode());
		pGResponse.setMessage(STBConstants.getErrorMessage(response.getRespCode()));
		pGResponse.setData(response.getData());
		return pGResponse;

	}

	/**
	 * Step 1 Giao dịch thực hiện chuyển tiền từ ví đến tài khoản sacombank
	 */
	public PGResponse AccountInquiry(PaymentAccount paymentAccount, String inputStr) throws Exception {
		//TODO
		WriteInfoLog("STB REQUEST AccountInquiry");
		AccountInquiryReq req = objectMapper.readValue(inputStr,AccountInquiryReq.class);
		BaseRequest baseRequest = new BaseRequest();
		String requestDatetime = Utils.getDateTime(new Date());
		baseRequest.setRequestDateTime(requestDatetime);
		baseRequest.setRequestID(Utils.genReqUID());
		baseRequest.setFunctionName(STBConstants.FUNC_ACCOUNT_INQUIRY);

		String data =  objectMapper.writeValueAsString(req);
		baseRequest.setData(STBSecurity.Encrypt3DES(data, paymentAccount.getEncryptKey()));
		STBSecurity.initParam(paymentAccount);

		String body = objectMapper.writeValueAsString(baseRequest);
		String bodySigned = STBSecurity.sign(STBSecurity.md5(body));
		// TODO
		ClientRequest.setUser(paymentAccount.getUsername());
		ClientRequest.setPassword(paymentAccount.getPassword());

		String[] paramsLog = new String[] { objectMapper.writeValueAsString(req) };

		// TODO
		logger.info(commonLogService.createContentLog(STBConstants.CHANNEL_CODE, SERVICE_NAME,
				STBConstants.FUNCTION_CODE_ACCOUNT_INQUIRY, true, true, false, paramsLog));

		// TODO: Call API STB
		String res = ClientRequest.callApi(body, bodySigned);
		// TODO
		paramsLog = new String[] { objectMapper.writeValueAsString(res) };

		logger.info("STB RESPONSE AccountInquiry" + paramsLog);
		BaseResponse response = objectMapper.readValue(res, BaseResponse.class);
		response.setData(STBSecurity.Decrypt3DES(response.getData(), paymentAccount.getEncryptKey()));

		PGResponse pGResponse = new PGResponse();
		pGResponse.setStatus(true);
		pGResponse.setErrorCode(response.getRespCode());
		pGResponse.setMessage(STBConstants.getErrorMessage(response.getRespCode()));
		pGResponse.setData(response.getData());
		return pGResponse;
	}

	/**
	 * Step 2 Giao dịch thực hiện chuyển tiền từ ví đến tài khoản sacombank
	 */
	public PGResponse FundTransferToSTBAccount(PaymentAccount paymentAccount, String inputStr) throws Exception {
		//TODO
		WriteInfoLog("STB REQUEST FundTransferToSTBAccount");
		TransferToSTBAccountReq req = objectMapper.readValue(inputStr,TransferToSTBAccountReq.class);
		BaseRequest baseRequest = new BaseRequest();
		String requestDatetime = Utils.getDateTime(new Date());
		baseRequest.setRequestDateTime(requestDatetime);
		baseRequest.setRequestID(Utils.genReqUID());
		baseRequest.setFunctionName(STBConstants.FUNC_TRANSFER_TO_STB_ACCOUNT);

		String data =  objectMapper.writeValueAsString(req);
		baseRequest.setData(STBSecurity.Encrypt3DES(data, paymentAccount.getEncryptKey()));
		STBSecurity.initParam(paymentAccount);

		String body = objectMapper.writeValueAsString(baseRequest);
		String bodySigned = STBSecurity.sign(STBSecurity.md5(body));
		// TODO
		ClientRequest.setUser(paymentAccount.getUsername());
		ClientRequest.setPassword(paymentAccount.getPassword());

		String[] paramsLog = new String[] { objectMapper.writeValueAsString(req) };

		// TODO
		logger.info(commonLogService.createContentLog(STBConstants.CHANNEL_CODE, SERVICE_NAME,
				STBConstants.FUNCTION_CODE_TRANSFER_TO_STB_ACCOUNT, true, true, false, paramsLog));

		// TODO: Call API STB
		String res = ClientRequest.callApi(body, bodySigned);
		// TODO
		paramsLog = new String[] { objectMapper.writeValueAsString(res) };

		logger.info("STB RESPONSE FundTransferToSTBAccount" + paramsLog);
		BaseResponse response = objectMapper.readValue(res, BaseResponse.class);
		response.setData(STBSecurity.Decrypt3DES(response.getData(), paymentAccount.getEncryptKey()));

		PGResponse pGResponse = new PGResponse();
		pGResponse.setStatus(true);
		pGResponse.setErrorCode(response.getRespCode());
		pGResponse.setMessage(STBConstants.getErrorMessage(response.getRespCode()));
		pGResponse.setData(response.getData());
		return pGResponse;

	}

	/**
	 * Giao dịch hủy liên kết thẻ/tài khoản
	 */
	public PGResponse CancelSubscription(PaymentAccount paymentAccount, String inputStr) throws Exception {
		//TODO
		WriteInfoLog("STB REQUEST  CancelSubscription ");
		BaseWithdrawReq req = objectMapper.readValue(inputStr, BaseWithdrawReq.class);

		BaseRequest baseRequest = new BaseRequest();
		String requestDatetime = Utils.getDateTime(new Date());
		baseRequest.setRequestDateTime(requestDatetime);
		baseRequest.setRequestID(Utils.genReqUID());
		baseRequest.setFunctionName(STBConstants.FUNC_CANCEL_SUBSCRIPTION);

		String data =  objectMapper.writeValueAsString(req);
		baseRequest.setData(STBSecurity.Encrypt3DES(data, paymentAccount.getEncryptKey()));
		STBSecurity.initParam(paymentAccount);

		String body = objectMapper.writeValueAsString(baseRequest);
		String bodySigned = STBSecurity.sign(STBSecurity.md5(body));
		// TODO
		ClientRequest.setUser(paymentAccount.getUsername());
		ClientRequest.setPassword(paymentAccount.getPassword());

		String[] paramsLog = new String[] { objectMapper.writeValueAsString(req) };

		// TODO
		logger.info(commonLogService.createContentLog(STBConstants.CHANNEL_CODE, SERVICE_NAME,
				STBConstants.FUNCTION_CODE_CANCEL_SUBSCRIPTION, true, true, false, paramsLog));

		// TODO: Call API STB
		String res = ClientRequest.callApi(body, bodySigned);
		// TODO
		paramsLog = new String[] { objectMapper.writeValueAsString(res) };

		logger.info("STB RESPONSE  OTP" + paramsLog);
		BaseResponse response = objectMapper.readValue(res, BaseResponse.class);
		response.setData(STBSecurity.Decrypt3DES(response.getData(), paymentAccount.getEncryptKey()));

		PGResponse pGResponse = new PGResponse();
		pGResponse.setStatus(true);
		pGResponse.setErrorCode(response.getRespCode());
		pGResponse.setMessage(STBConstants.getErrorMessage(response.getRespCode()));
		pGResponse.setData(response.getData());
		return pGResponse;
	}

	/**
	 * Step 1 Giao dịch chuyển tiền đến thẻ nội địa Napas
	 */
	public PGResponse DomesticInquiry(PaymentAccount paymentAccount, String inputStr) throws Exception {
		//TODO
		WriteInfoLog("STB REQUEST DomesticInquiry");
		DomesticInquiryReq req = objectMapper.readValue(inputStr,DomesticInquiryReq.class);
		PaymentDTO paymentDTO =	BIFT_Card(req);
		BaseRequest baseRequest = new BaseRequest();
		String requestDatetime = Utils.getDateTime(new Date());
		baseRequest.setRequestDateTime(requestDatetime);
		baseRequest.setRequestID(Utils.genReqUID());
		baseRequest.setFunctionName(STBConstants.FUNC_DOMESTIC_INQUIRY);

		String data =  objectMapper.writeValueAsString(req);
		baseRequest.setData(STBSecurity.Encrypt3DES(data, paymentAccount.getEncryptKey()));
		STBSecurity.initParam(paymentAccount);

		String body = objectMapper.writeValueAsString(baseRequest);
		String bodySigned = STBSecurity.sign(STBSecurity.md5(body));
		// TODO

		String[] paramsLog = new String[] { objectMapper.writeValueAsString(req) };

		// TODO
		logger.info(commonLogService.createContentLog(STBConstants.CHANNEL_CODE, SERVICE_NAME,
				STBConstants.FUNCTION_CODE_DOMESTIC_INQUIRY, true, true, false, paramsLog));

		// TODO: Call API STB
		String res = ClientRequest.callApi(body, bodySigned);
		// TODO
		paramsLog = new String[] { objectMapper.writeValueAsString(res) };

		logger.info("STB RESPONSE  OTP" + paramsLog);
		BaseResponse response = objectMapper.readValue(res, BaseResponse.class);
		response.setData(STBSecurity.Decrypt3DES(response.getData(), paymentAccount.getEncryptKey()));
		paymentDTO.setRawResponse(objectMapper.writeValueAsString(response));
		if (response.getRespCode().equals("00")){
			paymentDTO.setPgTransactionStatus(PaymentConst.EnumBankStatus.SUCCEEDED.code());
		}else {
			paymentDTO.setPgTransactionStatus(PaymentConst.EnumBankStatus.FAILED.code());
			paymentDTO.setChannelTransactionStatus(PaymentConst.EnumBankStatus.FAILED.code());
		}
		paymentService.updateTransactionStatusAfterCreatedPayment(paymentDTO);

		PGResponse pGResponse = new PGResponse();
		pGResponse.setStatus(true);
		pGResponse.setErrorCode(response.getRespCode());
		pGResponse.setMessage(STBConstants.getErrorMessage(response.getRespCode()));
		pGResponse.setData(response.getData());
		return pGResponse;
	}

	/**
	 * Step 2 Giao dịch chuyển tiền đến thẻ nội địa Napas
	 */
	public PGResponse DomesticFundTransfer(PaymentAccount paymentAccount, String inputStr) throws Exception {
		//TODO
		WriteInfoLog("STB REQUEST DomesticFundTransfer");
		DomesticFundTransfer req = objectMapper.readValue(inputStr,DomesticFundTransfer.class);
		PaymentDTO paymentDTO = new PaymentDTO();
		paymentDTO.setChannelId(23);
		paymentDTO.setMerchantTransactionId(req.getRefNumber());
		paymentDTO.setRawRequest(inputStr);
		paymentService.createPayment(paymentDTO);
		BaseRequest baseRequest = new BaseRequest();
		String requestDatetime = Utils.getDateTime(new Date());
		baseRequest.setRequestDateTime(requestDatetime);
		baseRequest.setRequestID(Utils.genReqUID());
		baseRequest.setFunctionName(STBConstants.FUNC_DOMESTIC_FUND_TRANSFER);

		String data =  objectMapper.writeValueAsString(req);
		baseRequest.setData(STBSecurity.Encrypt3DES(data, paymentAccount.getEncryptKey()));
		STBSecurity.initParam(paymentAccount);

		String body = objectMapper.writeValueAsString(baseRequest);
		String bodySigned = STBSecurity.sign(STBSecurity.md5(body));
		// TODO

		String[] paramsLog = new String[] { objectMapper.writeValueAsString(req) };

		// TODO
		logger.info(commonLogService.createContentLog(STBConstants.CHANNEL_CODE, SERVICE_NAME,
				STBConstants.FUNCTION_CODE_DOMESTIC_FUND_TRANSFER, true, true, false, paramsLog));

		// TODO: Call API STB
		String res = ClientRequest.callApi(body, bodySigned);
		// TODO
		paramsLog = new String[] { objectMapper.writeValueAsString(res) };

		logger.info("STB RESPONSE  OTP" + paramsLog);
		BaseResponse response = objectMapper.readValue(res, BaseResponse.class);
		response.setData(STBSecurity.Decrypt3DES(response.getData(), paymentAccount.getEncryptKey()));

		paymentDTO.setRawResponse(objectMapper.writeValueAsString(response));
		if (response.getRespCode().equals("00")){
			paymentDTO.setPgTransactionStatus(PaymentConst.EnumBankStatus.SUCCEEDED.code());
		}else {
			paymentDTO.setPgTransactionStatus(PaymentConst.EnumBankStatus.FAILED.code());
			paymentDTO.setChannelTransactionStatus(PaymentConst.EnumBankStatus.FAILED.code());
		}
		paymentService.updateTransactionStatusAfterCreatedPayment(paymentDTO);

		PGResponse pGResponse = new PGResponse();
		pGResponse.setStatus(true);
		pGResponse.setErrorCode(response.getRespCode());
		pGResponse.setMessage(STBConstants.getErrorMessage(response.getRespCode()));
		pGResponse.setData(response.getData());
		return pGResponse;



	}

	/**
	 * Step 1 Giao dịch chuyển tiền đến taì khoản napas
	 */
	public PGResponse DomesticAccountInquiry(PaymentAccount paymentAccount, String inputStr) throws Exception {
		WriteInfoLog("STB REQUEST DomesticAccountInquiry" +inputStr);
		DomesticAccountInquiry req = objectMapper.readValue(inputStr,DomesticAccountInquiry.class);

		PaymentDTO paymentDTO = new PaymentDTO();
		paymentDTO.setChannelId(23);
		paymentDTO.setMerchantTransactionId(req.getRefNumber());
		paymentDTO.setRawRequest(objectMapper.writeValueAsString(req));
		paymentService.createPayment(paymentDTO);

		BaseRequest baseRequest = new BaseRequest();
		String requestDatetime = Utils.getDateTime(new Date());
		baseRequest.setRequestDateTime(requestDatetime);
		baseRequest.setRequestID(Utils.genReqUID());
		baseRequest.setFunctionName(STBConstants.FUNC_DOMESTIC_ACCOUNT_INQUIRY);

		String data =  objectMapper.writeValueAsString(req);
		baseRequest.setData(STBSecurity.Encrypt3DES(data, paymentAccount.getEncryptKey()));
		STBSecurity.initParam(paymentAccount);

		String body = objectMapper.writeValueAsString(baseRequest);
		String bodySigned = STBSecurity.sign(STBSecurity.md5(body));
		// TODO

		// TODO
		logger.info("Request DomesticAccountInquiry " +body);

		// TODO: Call API STB
		String res = ClientRequest.callApi(body, bodySigned);
		// TODO

		logger.info("STB RESPONSE  DomesticAccountInquiry" +res);
		BaseResponse response = objectMapper.readValue(res, BaseResponse.class);
		response.setData(STBSecurity.Decrypt3DES(response.getData(), paymentAccount.getEncryptKey()));

		paymentDTO.setRawResponse(objectMapper.writeValueAsString(response));
		if (response.getRespCode().equals("00")){
			paymentDTO.setPgTransactionStatus(PaymentConst.EnumBankStatus.SUCCEEDED.code());
		}else {
			paymentDTO.setPgTransactionStatus(PaymentConst.EnumBankStatus.FAILED.code());
			paymentDTO.setChannelTransactionStatus(PaymentConst.EnumBankStatus.FAILED.code());
		}
		paymentService.updateTransactionStatusAfterCreatedPayment(paymentDTO);

		PGResponse pGResponse = new PGResponse();
		pGResponse.setStatus(true);
		pGResponse.setErrorCode(response.getRespCode());
		pGResponse.setMessage(STBConstants.getErrorMessage(response.getRespCode()));
		pGResponse.setData(response.getData());
		return pGResponse;


	}

	/**
	 * Step 2 Giao dịch chuyển tiền đến taì khoản napas
	 */
	public PGResponse DomesticAccountFundTransfer(PaymentAccount paymentAccount, String inputStr) throws Exception {
		WriteInfoLog("STB REQUEST DomesticAccountFundTransfer " +inputStr);
		DomesticAccountFundTransfer req = objectMapper.readValue(inputStr,DomesticAccountFundTransfer.class);

		PaymentDTO paymentDTO = new PaymentDTO();
		paymentDTO.setChannelId(23);
		paymentDTO.setMerchantTransactionId(req.getRefNumber());
		paymentDTO.setRawRequest(objectMapper.writeValueAsString(req));
		paymentService.createPayment(paymentDTO);

		BaseRequest baseRequest = new BaseRequest();
		String requestDatetime = Utils.getDateTime(new Date());
		baseRequest.setRequestDateTime(requestDatetime);
		baseRequest.setRequestID(Utils.genReqUID());
		baseRequest.setFunctionName(STBConstants.FUNC_DOMESTIC_ACCOUNT_FUND_TRANSFER);

		String data =  objectMapper.writeValueAsString(req);
		baseRequest.setData(STBSecurity.Encrypt3DES(data, paymentAccount.getEncryptKey()));
		STBSecurity.initParam(paymentAccount);

		String body = objectMapper.writeValueAsString(baseRequest);
		String bodySigned = STBSecurity.sign(STBSecurity.md5(body));
		// TODO

		// TODO
		logger.info("Request DomesticAccountFundTransfer " +body);

		// TODO: Call API STB
		String res = ClientRequest.callApi(body, bodySigned);
		// TODO

		logger.info("STB RESPONSE   DomesticAccountInquiry" + res);
		BaseResponse response = objectMapper.readValue(res, BaseResponse.class);
		response.setData(STBSecurity.Decrypt3DES(response.getData(), paymentAccount.getEncryptKey()));

		paymentDTO.setRawResponse(objectMapper.writeValueAsString(response));
		if (response.getRespCode().equals("00")){
			paymentDTO.setPgTransactionStatus(PaymentConst.EnumBankStatus.SUCCEEDED.code());
		}else {
			paymentDTO.setPgTransactionStatus(PaymentConst.EnumBankStatus.FAILED.code());
			paymentDTO.setChannelTransactionStatus(PaymentConst.EnumBankStatus.FAILED.code());
		}
		paymentService.updateTransactionStatusAfterCreatedPayment(paymentDTO);

		PGResponse pGResponse = new PGResponse();
		pGResponse.setStatus(true);
		pGResponse.setErrorCode(response.getRespCode());
		pGResponse.setMessage(STBConstants.getErrorMessage(response.getRespCode()));
		pGResponse.setData(response.getData());
		return pGResponse;

	}
	private PaymentDTO BIFT_Card(DomesticInquiryReq req) throws Exception{
		PaymentDTO paymentDTO = new PaymentDTO();
		paymentDTO.setChannelId(23);
		paymentDTO.setMerchantTransactionId(req.getRefNumber());
		paymentDTO.setRawRequest(objectMapper.writeValueAsString(req));
		paymentService.createPayment(paymentDTO);
		return paymentDTO;
	}


	/**
	 * Step 1 Giao dịch chuyển tiền đến thẻ Visa/Master
	 */
	public PGResponse VisaInquiry(PaymentAccount paymentAccount, String inputStr) throws Exception {
		WriteInfoLog("STB REQUEST VisaInquiry");
		VisaInquiry req = objectMapper.readValue(inputStr,VisaInquiry.class);
		BaseRequest baseRequest = new BaseRequest();
		String requestDatetime = Utils.getDateTime(new Date());
		baseRequest.setRequestDateTime(requestDatetime);
		baseRequest.setRequestID(Utils.genReqUID());
		baseRequest.setFunctionName(STBConstants.FUNC_VISA_INQUIRY);

		String data =  objectMapper.writeValueAsString(req);
		baseRequest.setData(STBSecurity.Encrypt3DES(data, paymentAccount.getEncryptKey()));
		STBSecurity.initParam(paymentAccount);

		String body = objectMapper.writeValueAsString(baseRequest);
		String bodySigned = STBSecurity.sign(STBSecurity.md5(body));
		// TODO
		ClientRequest.setUser(paymentAccount.getUsername());
		ClientRequest.setPassword(paymentAccount.getPassword());

		String[] paramsLog = new String[] { objectMapper.writeValueAsString(req) };

		// TODO
		logger.info(commonLogService.createContentLog(STBConstants.CHANNEL_CODE, SERVICE_NAME,
				STBConstants.FUNCTION_CODE_VISA_INQUIRY, true, true, false, paramsLog));

		// TODO: Call API STB
		String res = ClientRequest.callApi(body, bodySigned);
		// TODO
		paramsLog = new String[] { objectMapper.writeValueAsString(res) };

		logger.info("STB RESPONSE  OTP" + paramsLog);
		BaseResponse response = objectMapper.readValue(res, BaseResponse.class);
		response.setData(STBSecurity.Decrypt3DES(response.getData(), paymentAccount.getEncryptKey()));


		PGResponse pGResponse = new PGResponse();
		pGResponse.setStatus(true);
		pGResponse.setErrorCode(response.getRespCode());
		pGResponse.setMessage(STBConstants.getErrorMessage(response.getRespCode()));
		pGResponse.setData(response.getData());
		return pGResponse;
	}

	/**
	 * Step 2 Giao dịch chuyển tiền đến thẻ Visa/Master
	 */
	public PGResponse VisaTransfer(PaymentAccount paymentAccount, String inputStr) throws Exception {
		WriteInfoLog("STB REQUEST VisaTransfer");
		VisaTransfer req = objectMapper.readValue(inputStr,VisaTransfer.class);

		BaseRequest baseRequest = new BaseRequest();
		String requestDatetime = Utils.getDateTime(new Date());
		baseRequest.setRequestDateTime(requestDatetime);
		baseRequest.setRequestID(Utils.genReqUID());
		baseRequest.setFunctionName(STBConstants.FUNC_VISA_TRANSFER);

		String data =  objectMapper.writeValueAsString(req);
		baseRequest.setData(STBSecurity.Encrypt3DES(data, paymentAccount.getEncryptKey()));
		STBSecurity.initParam(paymentAccount);

		String body = objectMapper.writeValueAsString(baseRequest);
		String bodySigned = STBSecurity.sign(STBSecurity.md5(body));
		// TODO
		ClientRequest.setUser(paymentAccount.getUsername());
		ClientRequest.setPassword(paymentAccount.getPassword());

		String[] paramsLog = new String[] { objectMapper.writeValueAsString(req) };

		// TODO
		logger.info(commonLogService.createContentLog(STBConstants.CHANNEL_CODE, SERVICE_NAME,
				STBConstants.FUNCTION_CODE_VISA_TRANSFER, true, true, false, paramsLog));

		// TODO: Call API STB
		String res = ClientRequest.callApi(body, bodySigned);
		// TODO
		paramsLog = new String[] { objectMapper.writeValueAsString(res) };

		logger.info("STB RESPONSE  OTP" + paramsLog);
		BaseResponse response = objectMapper.readValue(res, BaseResponse.class);
		response.setData(STBSecurity.Decrypt3DES(response.getData(), paymentAccount.getEncryptKey()));

		PGResponse pGResponse = new PGResponse();
		pGResponse.setStatus(true);
		pGResponse.setErrorCode(response.getRespCode());
		pGResponse.setMessage(STBConstants.getErrorMessage(response.getRespCode()));
		pGResponse.setData(response.getData());
		return pGResponse;

	}

	/**
	 * <b>Liên kết thẻ STB</b>
	 * 
	 */
	public PGResponse LinkCard(PaymentAccount paymentAccount, String inputStr) throws Exception {
		WriteInfoLog("STB REQUEST LinkCard" +inputStr);
		PGResponse pGResponse = new PGResponse();
		LinkCardReq dataReq = objectMapper.readValue(inputStr, LinkCardReq.class);
		String[] paramsLog = new String[] { objectMapper.writeValueAsString(dataReq) };

		// TODO
		logger.info(commonLogService.createContentLog(STBConstants.CHANNEL_CODE, SERVICE_NAME,
				STBConstants.FUNCTION_CODE_LINK_CARD, true, true, false, paramsLog));


		if (StringUtils.isNotEmpty(STBValidator.validateLinkCard(dataReq))) {
			return createErrorResponse(STBValidator.validateLinkCard(dataReq));
		}

	   PaymentDTO paymentDTO =	createOrder(dataReq);

		dataReq.setAccessKey(STBConstants.AccessKey);
		dataReq.setProfileID(STBConstants.ProfileID);
		dataReq.setSubscribeWithMin("False");
		dataReq.setIsTokenRequest("False");;
		dataReq.setTransactionDateTime(Utils.getDateTime(new Date()));

		Map<String, Object> data = PGUtil.objToMapParameters(dataReq);
		String sign = STBSecurity.signatureByMD5withRSA(PGUtil.sortMap(data));

		dataReq.setSignature(sign);
		Map<String, Object> params =  PGUtil.objToMapParameters(dataReq);

		pGResponse = callAPI(params,STBConstants.Url_Checkout,pGResponse,paymentDTO);

		WriteInfoLog("ResponseSTB SendOder" +objectMapper.writeValueAsString(pGResponse));
		return pGResponse;

	}





	private PGResponse callAPI(Map<String, Object> params, String url, PGResponse pGResponse, PaymentDTO paymentDTO) throws IOException {
		RestTemplate restTemplate = null;
		try {
			restTemplate = new RestTemplate(RequestUtil.createRequestFactory());
		} catch (KeyManagementException | NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED);
		MultiValueMap<String, String> mapBody = new LinkedMultiValueMap<String, String>();
		for (Map.Entry<String, Object> entry : params.entrySet()) {
			mapBody.add(entry.getKey(), (String) entry.getValue());
		}
		HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(mapBody, headers);
		ResponseEntity<Void> responseEntity = restTemplate.postForEntity(url, entity, Void.class);
		String location =  responseEntity.getHeaders().getFirst("Location");

		String urlCheckout = STBConstants.Url_location + location;
		paymentDTO.setRawResponse(urlCheckout);
		if (responseEntity.getStatusCodeValue() == 500) {
			pGResponse.setStatus(false);
			pGResponse.setErrorCode(String.valueOf(responseEntity.getStatusCodeValue()));
			paymentDTO.setPgTransactionStatus(PaymentConst.EnumBankStatus.FAILED.code());
			paymentDTO.setChannelTransactionStatus(PaymentConst.EnumBankStatus.FAILED.code());
			return pGResponse;
		}
		paymentDTO.setPgTransactionStatus(PaymentConst.EnumBankStatus.SUCCEEDED.code());
		paymentService.updateTransactionStatusAfterCreatedPayment(paymentDTO);

		CheckOutRes checkOutRes = new CheckOutRes(urlCheckout);
		String responseCheckout = objectMapper.writeValueAsString(checkOutRes);


		pGResponse.setStatus(true);
		pGResponse.setData(responseCheckout);

		return pGResponse;
	}




	private PaymentDTO createOrder(LinkCardReq linkCardReq) throws Exception{
		PaymentDTO paymentDTO = new PaymentDTO();
		paymentDTO.setChannelId(23);
		paymentDTO.setAmount(linkCardReq.getTotalAmount().toString());
		paymentDTO.setMerchantTransactionId(linkCardReq.getTransactionID());
		paymentDTO.setRawRequest(objectMapper.writeValueAsString(linkCardReq));
		paymentService.createPayment(paymentDTO);
		return paymentDTO;
	}


	/**
	 * <b>TransactionQuery</b>
	 *
	 */
	public PGResponse TransactionQuery(PaymentAccount paymentAccount, String inputStr) throws Exception {
		  WriteInfoLog("STB REQUEST TransactionQuery" +inputStr);
		  PGResponse pGResponse = new PGResponse();
		  TransactionQuery transactionQuery = objectMapper.readValue(inputStr,TransactionQuery.class);
		  transactionQuery.setTransactionDateTime(Utils.getDateTime(new Date()));
		  String dataRequest =  objectMapper.writeValueAsString(transactionQuery);
		  BaseTransactionQuery baseTransactionQuery = new BaseTransactionQuery();
		  baseTransactionQuery.setAccessKey(STBConstants.AccessKey);
		  baseTransactionQuery.setProfileID(STBConstants.ProfileID);
		  baseTransactionQuery.setRequestID(Utils.genReqUID().replace("-",""));
		  String data =  PGSecurity.encryptTripleDESECB(STBConstants.ShareKey,dataRequest);
		  baseTransactionQuery.setData(data);

		  Map<String, Object> parameters = PGUtil.objToMapParameters(baseTransactionQuery);
		  String sign = STBSecurity.signatureRSAWithSecretKey(PGUtil.sortMap(parameters));

		  baseTransactionQuery.setSignature(sign);
		  String body = objectMapper.writeValueAsString(baseTransactionQuery);

		  URL url = new URL(STBConstants.Url_Query);
		  PaymentDTO paymentDTO = new PaymentDTO();
		  paymentDTO.setChannelId(23);
		  paymentDTO.setMerchantTransactionId(transactionQuery.getTransactionID());
		  paymentDTO.setRawRequest(body);
		  pGResponse = callAPI_QUERY(body,url,pGResponse,paymentDTO);

		  WriteInfoLog("STB Response TransactionQuery" + objectMapper.writeValueAsString(pGResponse));

		  return pGResponse;
	}

	private PGResponse callAPI_QUERY(String body,URL url,PGResponse pGResponse,PaymentDTO paymentDTO) throws Exception{
		SSLContext ssl_ctx = SSLContext.getInstance("TLS");
		TrustManager[] trust_mgr = ClientRequest.get_trust_mgr();
		ssl_ctx.init(null, trust_mgr, new SecureRandom());
		HttpsURLConnection.setDefaultSSLSocketFactory(ssl_ctx.getSocketFactory());
		HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
			@Override
			public boolean verify(String string, SSLSession ssls) {
				return true;
			}
		});

		HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
		con.setRequestMethod("POST");
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("Accept", "application/json");
		con.setDoOutput(true);

		try (DataOutputStream wr = new DataOutputStream((con.getOutputStream()))) {
			wr.writeBytes(body);
			wr.flush();
		}catch (Exception e){
			e.printStackTrace();
		}

		StringBuilder response = new StringBuilder();
		InputStream is = con.getInputStream();
		try (BufferedReader in = new BufferedReader(new InputStreamReader(is))) {
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		JSONObject object = new JSONObject(response.toString());
		paymentDTO.setRawResponse(response.toString());
		if(object.getBoolean("Success") == true){
			pGResponse.setChannelErrorCode(object.getString("ResponseCode"));
		}
        if(pGResponse.getChannelErrorCode().equals("00")){
			paymentDTO.setChannelTransactionStatus(PaymentConst.EnumBankStatus.SUCCEEDED.code());
		}else {
        	paymentDTO.setChannelTransactionStatus(PaymentConst.EnumBankStatus.PENDING.code());
		}
        paymentService.updateChannelTransactionStatusPayment(paymentDTO);
		pGResponse.setStatus(true);
		pGResponse.setData(response);

		return pGResponse;
	}







	/**
	 * <b>Notify thanh toán checkout</b>
	 *
	 */
	public String NotifySTB_ECOM( String inputStr) throws Exception {
		STBNotifyEcomReq stbNotifyEcomReq = objectMapper.readValue(inputStr,STBNotifyEcomReq.class);
		StringBuilder builder =  new  StringBuilder();
		builder.append(stbNotifyEcomReq.getAccountNo());
		builder.append(stbNotifyEcomReq.getCardNumber());
		builder.append(stbNotifyEcomReq.getCardType());
		builder.append(stbNotifyEcomReq.getDescription());
		builder.append(stbNotifyEcomReq.getExpiryDate());
		builder.append(stbNotifyEcomReq.getResponseCode());
		builder.append(stbNotifyEcomReq.getSubscriptionSource());
		builder.append(stbNotifyEcomReq.getSubscriptionType());
		builder.append(stbNotifyEcomReq.getToken());
		builder.append(stbNotifyEcomReq.getTransactionID());
		boolean verify = false;
		try {
			verify = STBSecurity.verifySign(builder.toString(),stbNotifyEcomReq.getSignature());
		} catch (IOException | GeneralSecurityException e) {
			// TODO Write log:
			WriteErrorLog("Verify signature DAB error :: " + e.getMessage());
		}

		if (verify) {
			// TODO Write log:
			WriteInfoLog("VERIFY SIGNATURE TRUE");
		} else {
			// TODO Write log:
			WriteErrorLog("VERIFY SIGNATURE FALSE");
		}
		return null;





	}





	/**
	 * <b>Xác thực OTP liên kết thẻ</b>
	 */
//	public PGResponse VerifyOtpLinkCard(PaymentAccount paymentAccount, String inputStr) throws Exception {
//		WriteInfoLog("STB VERIFY OTP LINK CARD");
//
//		DataRequest input = objectMapper.readValue(inputStr, DataRequest.class);
//		// TODO: Validate param
//
//		BaseRequest req = new BaseRequest();
//		buildCommonParam(req, input, STBConstants.FUNC_LINK_CARD);
//
//		LinkCardReq dataReq = new LinkCardReq();
//		dataReq.setRefNumber(input.getTransId());
//
//		dataReq.setAuthentication(true);
//		dataReq.setEnrollment(false);
//		dataReq.setValidation(true);
//
//		dataReq.setOtp(input.getOtp());
//		dataReq.setCustomerID(input.getUserId());
//		dataReq.setSsn(input.getCustIDNo()); // so CMT
//		dataReq.setFullName(input.getCardHolerName()); // tên KH, option
//		dataReq.setCardNumber(input.getCardNumber());
//		dataReq.setDescription(input.getDescription());
//
//		// Set Data request
//		String data = objectMapper.writeValueAsString(dataReq);
//		req.setData(STBSecurity.Encrypt3DES(data, paymentAccount.getEncryptKey()));
//
//		return process(paymentAccount, req);
//	}

	/**
	 * <b>Hủy liên kết thẻ STB</b>
	 * 
	 * @throws Exception
	 * @throws NoSuchAlgorithmException
	 */
	public PGResponse UnLinkCard(PaymentAccount paymentAccount, String inputStr) throws NoSuchAlgorithmException, Exception {
		WriteInfoLog("STB UNLINK CARD");

		DataRequest input = objectMapper.readValue(inputStr, DataRequest.class);
		// TODO: Validate param

		BaseRequest req = new BaseRequest();
		buildCommonParam(req, input, STBConstants.FUNC_UNLINK_CARD);

		// Set Data request
		UnLinkCardReq dataReq = new UnLinkCardReq();
		dataReq.setRefNumber(input.getTransId());

		dataReq.setCustomerID(input.getUserId());
		dataReq.setSubscriptionID(input.getToken());
		dataReq.setDescription(input.getDescription());

		String data = objectMapper.writeValueAsString(dataReq);
		req.setData(STBSecurity.Encrypt3DES(data, paymentAccount.getEncryptKey()));

		return process(paymentAccount, req);
	}

	/**
	 * <b>Rút tiền/Thanh toán bằng thẻ STB đã liên kết</b>
	 */
	public PGResponse PaymentWithToken(PaymentAccount paymentAccount, String inputStr) throws Exception {
		WriteInfoLog("STB PaymentWithToken");

		DataRequest input = objectMapper.readValue(inputStr, DataRequest.class);
		// TODO: Validate param

		BaseRequest req = new BaseRequest();
		buildCommonParam(req, input, STBConstants.FUNC_PAY_WITH_TOKEN);

		// Set Data request
		PaymentWithProfileIdReq dataReq = new PaymentWithProfileIdReq();
		dataReq.setRefNumber(input.getTransId());

		dataReq.setAuthentication(true);
		dataReq.setEnrollment(true);
		dataReq.setValidation(false);

		dataReq.setOtp("");
		dataReq.setCustomerID(input.getUserId());
		dataReq.setSubscriptionID(input.getToken());
		dataReq.setAmount(df.format(input.getAmount()));
		dataReq.setDescription(input.getDescription());

		String data = objectMapper.writeValueAsString(dataReq);
		req.setData(STBSecurity.Encrypt3DES(data, paymentAccount.getEncryptKey()));

		return process(paymentAccount, req);
	}
	
	public PGResponse VerifyOtpPaymentWithToken(PaymentAccount paymentAccount, String inputStr) throws Exception {
		WriteInfoLog("STB VerifyOtpPaymentWithToken");
		
		DataRequest input = objectMapper.readValue(inputStr, DataRequest.class);
		//TODO: Validate param
		
		BaseRequest req = new BaseRequest();
		buildCommonParam(req, input, STBConstants.FUNC_PAY_WITH_TOKEN);
		
		// Set Data request
		PaymentWithProfileIdReq dataReq = new PaymentWithProfileIdReq();
		dataReq.setRefNumber(input.getTransId());
		
		dataReq.setAuthentication(true);
		dataReq.setEnrollment(false);
		dataReq.setValidation(true);
		
		dataReq.setOtp(input.getOtp());
		dataReq.setCustomerID(input.getUserId());
		dataReq.setSubscriptionID(input.getToken());
		dataReq.setAmount(df.format(input.getAmount()));
		dataReq.setDescription(input.getDescription());
		
		String data = objectMapper.writeValueAsString(dataReq);
		req.setData(STBSecurity.Encrypt3DES(data, paymentAccount.getEncryptKey()));
		
		return process(paymentAccount, req);
	}

	/**
	 * <b>Đảo giao dịch Rút tiền/Thanh toán bằng thẻ STB đã liên kết</b>
	 *
	 */
	public PGResponse ReversalPaymentWithToken(PaymentAccount paymentAccount, String inputStr) throws NoSuchAlgorithmException, Exception {
		WriteInfoLog("STB Reversal PaymentWithToken");

		DataRequest input = objectMapper.readValue(inputStr, DataRequest.class);
		// TODO: Validate param

		BaseRequest req = new BaseRequest();
		buildCommonParam(req, input, STBConstants.FUNC_REVERSAL_PAY_WITH_TOKEN);

		// Set Data request
		ReversalPaymentWithProfileIdReq dataReq = new ReversalPaymentWithProfileIdReq();
		dataReq.setRefNumber(input.getReversalTransId()); // Mã giao dịch
															// Payment

		dataReq.setCustomerID(input.getUserId());
		dataReq.setSubscriptionID(input.getToken());
		dataReq.setDescription(input.getDescription());

		String data = objectMapper.writeValueAsString(dataReq);
		req.setData(STBSecurity.Encrypt3DES(data, paymentAccount.getEncryptKey()));

		return process(paymentAccount, req);
	}

	/**
	 * <b>Step 1 Rút tiền/Thanh toán bằng thẻ STB nội địa</b>
	 */
	public PGResponse Payment(PaymentAccount paymentAccount, String inputStr) throws Exception {
		WriteInfoLog("STB PaymentWithCard");

		DataRequest input = objectMapper.readValue(inputStr, DataRequest.class);
		// TODO: Validate param

		BaseRequest req = new BaseRequest();
		buildCommonParam(req, input, STBConstants.FUNC_PAY_WITH_CARD);

		// Set Data request
		PaymentReq dataReq = new PaymentReq();
		dataReq.setRefNumber(input.getTransId());

		dataReq.setAuthentication(true);
		dataReq.setEnrollment(true);
		dataReq.setValidation(false);

		dataReq.setOtp("");
		dataReq.setSsn(input.getCustIDNo());
		dataReq.setFullName(input.getCardHolerName());
		dataReq.setCardNumber(input.getCardNumber());
		dataReq.setAmount(df.format(input.getAmount()));
		dataReq.setDescription(input.getDescription());

		String data = objectMapper.writeValueAsString(dataReq);
		req.setData(STBSecurity.Encrypt3DES(data, paymentAccount.getEncryptKey()));

		return process(paymentAccount, req);
	}

	/**
	 * <b>Step 2 Rút tiền/Thanh toán bằng thẻ STB nội địa</b>
	 */
	public PGResponse VerifyOtpPayment(PaymentAccount paymentAccount, String inputStr) throws Exception {
		WriteInfoLog("STB Verify Otp PaymentWithCard");

		DataRequest input = objectMapper.readValue(inputStr, DataRequest.class);
		// TODO: Validate param

		BaseRequest req = new BaseRequest();
		buildCommonParam(req, input, STBConstants.FUNC_PAY_WITH_CARD);

		// Set Data request
		PaymentReq dataReq = new PaymentReq();
		dataReq.setRefNumber(input.getTransId());

		dataReq.setAuthentication(true);
		dataReq.setEnrollment(false);
		dataReq.setValidation(true);

		dataReq.setOtp(input.getOtp());
		dataReq.setSsn(input.getCustIDNo());
		dataReq.setFullName(input.getCardHolerName());
		dataReq.setCardNumber(input.getCardNumber());
		dataReq.setAmount(df.format(input.getAmount()));
		dataReq.setDescription(input.getDescription());

		String data = objectMapper.writeValueAsString(dataReq);
		req.setData(STBSecurity.Encrypt3DES(data, paymentAccount.getEncryptKey()));

		return process(paymentAccount, req);
	}

	/**
	 * <b>Đảo giao dịch Rút tiền/Thanh toán bằng thẻ STB nội địa</b>
	 */
	public PGResponse ReversalPayment(PaymentAccount paymentAccount, String inputStr) throws Exception {
		WriteInfoLog("STB Reversal PaymentWithCard");

		DataRequest input = objectMapper.readValue(inputStr, DataRequest.class);
		// TODO: Validate param

		BaseRequest req = new BaseRequest();
		buildCommonParam(req, input, STBConstants.FUNC_REVERSAL_PAY_WITH_CARD);

		// Set Data request
		ReversalPaymentReq dataReq = new ReversalPaymentReq();
		dataReq.setRefNumber(input.getReversalTransId()); // Mã giao dịch
															// Payment

		dataReq.setSsn(input.getCustIDNo());
		dataReq.setFullName(input.getCardHolerName());
		dataReq.setCardNumber(input.getCardNumber());
		dataReq.setAmount(df.format(input.getAmount()));
		dataReq.setDescription(input.getDescription());

		String data = objectMapper.writeValueAsString(dataReq);
		req.setData(STBSecurity.Encrypt3DES(data, paymentAccount.getEncryptKey()));

		return process(paymentAccount, req);
	}

	/**
	 * <b>Tra cứu thông tin thẻ nội địa, napas trước khi thực hiển Rút Ví ->
	 * Thẻ</b>
	 */
	public PGResponse CheckCard(PaymentAccount paymentAccount, String inputStr) throws Exception {
		// TODO
		WriteInfoLog("STB CheckCard");
		return callCheckCard(paymentAccount, inputStr, false);
	}

	/**
	 * <b>Tra cứu thông tin thẻ nội địa đã liên kết trước khi thực hiển Rút Ví
	 * -> Thẻ LK</b>
	 */
//	public String CheckLinkCard(String inputStr) throws Exception {
//		WriteInfoLog("STB CheckLinkCard");
//		return callCheckCard(inputStr, true);
//	}

	private PGResponse callCheckCard(PaymentAccount paymentAccount, String inputStr, boolean isLinked) throws Exception {
		DataRequest input = objectMapper.readValue(inputStr, DataRequest.class);

		// TODO: Validate param
		if (StringUtils.isNotEmpty(STBValidator.validateCheckCardParam(input))) {
			return createErrorResponse(STBValidator.validateCheckCardParam(input));
		}

		BaseRequest req = new BaseRequest();
		buildCommonParam(req, input, STBConstants.FUNC_CHECK_CARD);

		// Set Data request
		BaseTransferCardReq dataReq = new BaseTransferCardReq();
		dataReq.setRefNumber(input.getTransId());

		dataReq.setCardNumber(isLinked ? input.getToken() : input.getCardNumber());
		dataReq.setAmount(df.format(input.getAmount()));
		dataReq.setDescription(input.getDescription());

		String data = objectMapper.writeValueAsString(dataReq);
		req.setData(STBSecurity.Encrypt3DES(data, paymentAccount.getEncryptKey()));

		return process(paymentAccount, req);
	}

	/**
	 * <b>Chuyển tiền vào thẻ STB nội địa (ko phải thẻ liên kết)</b>
	 * 
	 * @throws Exception
	 */
	public PGResponse TransferToCard(PaymentAccount paymentAccount, String inputStr) throws Exception {
		// TODO
		WriteInfoLog("STB TransferToCard");
		return callTransferToCard(paymentAccount, inputStr, false);
	}

	public PGResponse TransferToLinkCard(PaymentAccount paymentAccount, String inputStr) throws Exception {
		// TODO
		WriteInfoLog("STB TransferToLinkCard");
		return callTransferToCard(paymentAccount, inputStr, true);
	}

	private PGResponse callTransferToCard(PaymentAccount paymentAccount, String inputStr, boolean isLinked) throws Exception {
		DataRequest input = objectMapper.readValue(inputStr, DataRequest.class);
		// TODO: Validate param
		if (StringUtils.isNotEmpty(STBValidator.validateTransferToCardParam(input))) {
			return createErrorResponse(STBValidator.validateTransferToCardParam(input));
		}

		BaseRequest req = new BaseRequest();
		buildCommonParam(req, input, STBConstants.FUNC_TRANSFER_TO_CARD);

		// Set Data request
		TransferCardReq dataReq = new TransferCardReq();
		dataReq.setRefNumber(input.getTransId());
		dataReq.setInqRefNumber(input.getInquiryTransId());

		dataReq.setCardNumber(isLinked ? input.getToken() : input.getCardNumber());
		dataReq.setAmount(df.format(input.getAmount()));
		dataReq.setDescription(input.getDescription());

		String data = objectMapper.writeValueAsString(dataReq);
		req.setData(STBSecurity.Encrypt3DES(data, paymentAccount.getEncryptKey()));

		return process(paymentAccount, req);
	}

	/**
	 * <b>Đảo giao dịch Chuyển tiền vào thẻ STB nội địa (ko phải thẻ liên
	 * kết)</b>
	 * 
	 * @throws Exception
	 */
	public PGResponse ReversalTransferToCard(PaymentAccount paymentAccount, String inputStr) throws Exception {
		// TODO
		WriteInfoLog("STB ReversalTransferToCard");

		DataRequest input = objectMapper.readValue(inputStr, DataRequest.class);
		// TODO: Validate param

		BaseRequest req = new BaseRequest();
		buildCommonParam(req, input, STBConstants.FUNC_REVERSAL_TRANSFER_TO_CARD);

		// Set Data request
		BaseTransferCardReq dataReq = new BaseTransferCardReq();
		dataReq.setRefNumber(input.getReversalTransId()); // Mã giao dịch
															// Transfer To Card

		dataReq.setCardNumber(input.getCardNumber());
		dataReq.setAmount(df.format(input.getAmount()));
		dataReq.setDescription(input.getDescription());

		String data = objectMapper.writeValueAsString(dataReq);
		req.setData(STBSecurity.Encrypt3DES(data, paymentAccount.getEncryptKey()));

		return process(paymentAccount, req);
	}

	/**
	 * <b>Tra cứu thông tin thẻ iBFT</b>
	 */
	public PGResponse CheckCardIBFT(PaymentAccount paymentAccount, String inputStr) throws Exception {
		// TODO
		WriteInfoLog("STB CheckCardiBFT");

		DataRequest input = objectMapper.readValue(inputStr, DataRequest.class);
		// TODO: Validate param
		if (StringUtils.isNotEmpty(STBValidator.validateCheckCardParam(input))) {
			return createErrorResponse(STBValidator.validateCheckCardParam(input));
		}

		BaseRequest req = new BaseRequest();
		buildCommonParam(req, input, STBConstants.FUNC_CHECK_CARD_NAPAS);

		// Set Data request
		BaseTransferCardReq dataReq = new BaseTransferCardReq();
		dataReq.setRefNumber(input.getTransId());

		dataReq.setCardNumber(input.getCardNumber());
		dataReq.setAmount(df.format(input.getAmount()));
		dataReq.setDescription(input.getDescription());

		String data = objectMapper.writeValueAsString(dataReq);
		req.setData(STBSecurity.Encrypt3DES(data, paymentAccount.getEncryptKey()));

		return process(paymentAccount, req);
	}

	/**
	 * <b>Chuyển tiền tới thẻ IBFT</b>
	 */
	public PGResponse TransferToCardIBFT(PaymentAccount paymentAccount, String inputStr) throws Exception {
		// TODO
		WriteInfoLog("STB TransferToCardiBFT");

		DataRequest input = objectMapper.readValue(inputStr, DataRequest.class);
		// TODO: Validate param
		if (StringUtils.isNotEmpty(STBValidator.validateTransferToCardParam(input))) {
			return createErrorResponse(STBValidator.validateTransferToCardParam(input));
		}

		BaseRequest req = new BaseRequest();
		buildCommonParam(req, input, STBConstants.FUNC_TRANSFER_TO_CARD_NAPAS);

		// Set Data request
		TransferCardReq dataReq = new TransferCardReq();
		dataReq.setRefNumber(input.getTransId());
		dataReq.setInqRefNumber(input.getInquiryTransId());

		dataReq.setCardNumber(input.getCardNumber());
		dataReq.setAmount(df.format(input.getAmount()));
		dataReq.setDescription(input.getDescription());

		String data = objectMapper.writeValueAsString(dataReq);

		req.setData(STBSecurity.Encrypt3DES(data, paymentAccount.getEncryptKey()));

		return process(paymentAccount, req);
	}

	/**
	 * <b>Tra cứu thẻ Visa Master</b>
	 * 
	 * @throws Exception
	 */
	public PGResponse CheckVisaMasterCard(PaymentAccount paymentAccount, String inputStr) throws Exception {
		WriteInfoLog("STB CheckVisaCard");

		DataRequest input = objectMapper.readValue(inputStr, DataRequest.class);
		// TODO: Validate param

		BaseRequest req = new BaseRequest();
		buildCommonParam(req, input, STBConstants.FUNC_CHECK_CARD_VISA);

		// Set Data request
		BaseTransferVisaReq dataReq = new BaseTransferVisaReq();
		dataReq.setRefNumber(input.getTransId());

		dataReq.setCardNumber(input.getCardNumber());
		dataReq.setAmount(df.format(input.getAmount()));
		dataReq.setDescription(input.getDescription());

		String data = objectMapper.writeValueAsString(dataReq);
		req.setData(STBSecurity.Encrypt3DES(data, paymentAccount.getEncryptKey()));

		return process(paymentAccount, req);
	}

	/**
	 * <b>Chuyển tiền vào thẻ Visa Master</b>
	 * 
	 * @throws Exception
	 */
	public PGResponse TransferToVisaMasterCard(PaymentAccount paymentAccount, String inputStr) throws Exception {
		// TODO
		WriteInfoLog("STB TransferVisaCard");

		DataRequest input = objectMapper.readValue(inputStr, DataRequest.class);
		// TODO: Validate param

		BaseRequest req = new BaseRequest();
		buildCommonParam(req, input, STBConstants.FUNC_TRANSFER_TO_CARD_VISA);

		// Set Data request
		TransferVisaReq dataReq = new TransferVisaReq();
		dataReq.setRefNumber(input.getTransId());
		dataReq.setInqRefNumber(input.getInquiryTransId()); // mã giao dịch
															// check card

		dataReq.setCardNumber(input.getCardNumber());
		dataReq.setCardToken(input.getCardToken()); // token thẻ visa/master
		dataReq.setAmount(df.format(input.getAmount()));
		dataReq.setDescription(input.getDescription());

		String data = objectMapper.writeValueAsString(dataReq);
		req.setData(STBSecurity.Encrypt3DES(data, paymentAccount.getEncryptKey()));

		return process(paymentAccount, req);
	}
	
	/**
	 * <b>Tra cứu TKNH Sacombank</b>
	 * 
	 */
	public PGResponse CheckBankAccSTB(PaymentAccount paymentAccount, String inputStr) throws Exception{
		// TODO
		WriteInfoLog("STB Check BankAcc");
		
		DataRequest input = objectMapper.readValue(inputStr, DataRequest.class);
		//TODO: Validate param
		
		BaseRequest req = new BaseRequest();
		buildCommonParam(req, input, STBConstants.FUNC_CHECK_BANK_ACC_STB);
		
		// Set Data request
		BaseTransferBankAccReq dataReq = new BaseTransferBankAccReq();
		dataReq.setRefNumber(input.getTransId());
		
		dataReq.setAccountNumber(input.getBenAcctNo());
		dataReq.setAmount(df.format(input.getAmount()));
		dataReq.setDescription(input.getDescription());
		
		String data = objectMapper.writeValueAsString(dataReq);
		req.setData(STBSecurity.Encrypt3DES(data, paymentAccount.getEncryptKey()));
		
		return process(paymentAccount, req);
	}
	
	/**
	 * <b>Chuyển tiền vào TKNH Sacombank</b>
	 * 
	 */
	public PGResponse TransferToBankAccSTB(PaymentAccount paymentAccount, String inputStr) throws Exception{
		// TODO
		WriteInfoLog("STB Transfer BankAcc");
		
		DataRequest input = objectMapper.readValue(inputStr, DataRequest.class);
		//TODO: Validate param
		
		BaseRequest req = new BaseRequest();
		buildCommonParam(req, input, STBConstants.FUNC_TRANSFER_BANK_ACC_STB);
		
		// Set Data request
		BaseTransferBankAccReq dataReq = new BaseTransferBankAccReq();
		dataReq.setRefNumber(input.getTransId());
		dataReq.setInqRefNumber(input.getInquiryTransId());			// mã giao dịch check card
		
		dataReq.setAccountNumber(input.getBenAcctNo());
		dataReq.setAmount(df.format(input.getAmount()));
		dataReq.setDescription(input.getDescription());
		
		String data = objectMapper.writeValueAsString(dataReq);
		req.setData(STBSecurity.Encrypt3DES(data, paymentAccount.getEncryptKey()));
		
		return process(paymentAccount, req);
	}
	
	/**
	 * <b>Tra cứu TKNH IBFT</b>
	 * 
	 */
	public PGResponse CheckBankAccIBFT(PaymentAccount paymentAccount, String inputStr) throws Exception{
		// TODO
		WriteInfoLog("STB Check BankAcc IBFT");
		
		DataRequest input = objectMapper.readValue(inputStr, DataRequest.class);
		//TODO: Validate param
		
		BaseRequest req = new BaseRequest();
		buildCommonParam(req, input, STBConstants.FUNC_CHECK_BANK_ACC_IBFT);
		
		// Set Data request
		BaseTransferBankAccReq dataReq = new BaseTransferBankAccReq();
		dataReq.setRefNumber(input.getTransId());
		
		dataReq.setAccountNumber(input.getBenAcctNo());
		dataReq.setBankCode(input.getBenBankId());
		
		dataReq.setAmount(df.format(input.getAmount()));
		dataReq.setDescription(input.getDescription());
		
		String data = objectMapper.writeValueAsString(dataReq);
		req.setData(STBSecurity.Encrypt3DES(data, paymentAccount.getEncryptKey()));
		
		return process(paymentAccount, req);
	}
	
	/**
	 * <b>Chuyển tiền vào TKNH IBFT</b>
	 * 
	 */
	public PGResponse TransferToBankAccIBFT(PaymentAccount paymentAccount, String inputStr) throws Exception{
		// TODO
		WriteInfoLog("STB Transfer BankAcc");
		
		DataRequest input = objectMapper.readValue(inputStr, DataRequest.class);
		//TODO: Validate param
		
		BaseRequest req = new BaseRequest();
		buildCommonParam(req, input, STBConstants.FUNC_TRANSFER_BANK_ACC_IBFT);
		
		// Set Data request
		BaseTransferBankAccReq dataReq = new BaseTransferBankAccReq();
		dataReq.setRefNumber(input.getTransId());
		dataReq.setInqRefNumber(input.getInquiryTransId());			// mã giao dịch check card
		
		dataReq.setAccountNumber(input.getBenAcctNo());
		dataReq.setBankCode(input.getBenBankId());
		
		dataReq.setAmount(df.format(input.getAmount()));
		dataReq.setDescription(input.getDescription());
		
		String data = objectMapper.writeValueAsString(dataReq);
		req.setData(STBSecurity.Encrypt3DES(data, paymentAccount.getEncryptKey()));
		
		return process(paymentAccount, req);
	}

	/**
	 * <b>Rút tiền về ATM</b>
	 */
	public PGResponse Cardless(PaymentAccount paymentAccount, String inputStr) throws Exception {
		// TODO
		WriteInfoLog("STB Cardless");

		DataRequest input = objectMapper.readValue(inputStr, DataRequest.class);
		// TODO: Validate param

		BaseRequest req = new BaseRequest();
		buildCommonParam(req, input, STBConstants.FUNC_CARDLESS);

		// Set Data request
		BaseCardlessReq dataReq = new BaseCardlessReq();
		dataReq.setRefNumber(input.getTransId());

		dataReq.setSenderName(input.getUserName()); // Họ tên người gửi
		dataReq.setRecipientMobile(input.getBenMobileNum()); // Sđt người nhận
		dataReq.setAmount(df.format(input.getAmount()));
		dataReq.setDescription(input.getDescription());

		String data = objectMapper.writeValueAsString(dataReq);
		req.setData(STBSecurity.Encrypt3DES(data, paymentAccount.getEncryptKey()));

		return process(paymentAccount, req);
	}

	/**
	 * <b>Đảo giao dịch Rút tiền về ATM</b>
	 */
	public PGResponse ReversalCardless(PaymentAccount paymentAccount, String inputStr) throws Exception {
		// TODO
		WriteInfoLog("STB ReversalCardless");

		DataRequest input = objectMapper.readValue(inputStr, DataRequest.class);
		// TODO: Validate param

		BaseRequest req = new BaseRequest();
		buildCommonParam(req, input, STBConstants.FUNC_REVERSAL_CARDLESS);

		// Set Data request
		BaseCardlessReq dataReq = new BaseCardlessReq();
		dataReq.setSenderName(input.getUserName()); // Họ tên người gửi
		dataReq.setRecipientMobile(input.getBenMobileNum()); // Sđt người nhận
		dataReq.setAmount(df.format(input.getAmount()));
		dataReq.setDescription(input.getDescription());
		dataReq.setRefNumber(input.getReversalTransId()); // mã giao dịch
															// cardless

		String data = objectMapper.writeValueAsString(dataReq);
		req.setData(STBSecurity.Encrypt3DES(data, paymentAccount.getEncryptKey()));

		return process(paymentAccount, req);
	}

	/**
	 * <b>Hủy giao dịch Rút tiền về ATM</b>
	 */
	public PGResponse CancelCardless(PaymentAccount paymentAccount, String inputStr) throws Exception {
		// TODO
		WriteInfoLog("STB CancelCardless");

		DataRequest input = objectMapper.readValue(inputStr, DataRequest.class);
		// TODO: Validate param

		BaseRequest req = new BaseRequest();
		buildCommonParam(req, input, STBConstants.FUNC_REVERSAL_CARDLESS);

		// Set Data request
		CancelCardlessReq dataReq = new CancelCardlessReq();
		dataReq.setRefNumber(input.getTransId());
		dataReq.setOrgRefNumber(input.getCancelTransId()); // Mã giao dịch
															// cardless

		dataReq.setSenderName(input.getUserName()); // Họ tên người gửi
		dataReq.setRecipientMobile(input.getBenMobileNum()); // Sđt người nhận
		dataReq.setAmount(df.format(input.getAmount()));
		dataReq.setDescription(input.getDescription());

		String data = objectMapper.writeValueAsString(dataReq);
		req.setData(STBSecurity.Encrypt3DES(data, paymentAccount.getEncryptKey()));

		return process(paymentAccount, req);
	}

	/**
	 * <b>Tra cứu trạng thái giao dịch</b>
	 * 
	 * @throws Exception
	 */
	public PGResponse Query(PaymentAccount paymentAccount, String inputStr) throws Exception {
		// TODO
		WriteInfoLog("STB QueryTrans");

		DataRequest input = objectMapper.readValue(inputStr, DataRequest.class);
		// TODO: Validate param
		if (StringUtils.isNotEmpty(STBValidator.validateQueryParam(input))) {
			return createErrorResponse(STBValidator.validateQueryParam(input));
		}

		BaseRequest req = new BaseRequest();
		buildCommonParam(req, input, STBConstants.FUNC_QUERY_TRANSACTION);

		// Set Data request
		QueryTransactionReq dataReq = new QueryTransactionReq();
		dataReq.setType(input.getQueryType());
		dataReq.setDate(input.getQueryTransDate());
		dataReq.setRefNumber(input.getQueryTransId());

		String data = objectMapper.writeValueAsString(dataReq);
		req.setData(data);
		// TODO
		WriteInfoLog("2. STBiBFT REQUEST PARAM before signature", objectMapper.writeValueAsString(req));
		req.setData(STBSecurity.Encrypt3DES(data, paymentAccount.getEncryptKey()));

		return process(paymentAccount, req);
	}

	/**
	 * <b>Tra cứu số dư tk NCC</b>
	 * 
	 * @throws Exception
	 */
	public PGResponse QueryBalance(PaymentAccount paymentAccount, String inputStr) throws Exception {
		// TODO
		WriteInfoLog("STB QueryBalance");

		DataRequest input = objectMapper.readValue(inputStr, DataRequest.class);
		// TODO: Validate param

		BaseRequest req = new BaseRequest();
		buildCommonParam(req, input, STBConstants.FUNC_QUERY_BALANCE);

		// Set Data request
		QueryBalanceReq dataReq = new QueryBalanceReq();
		dataReq.setRefNumber(input.getTransId());

		String data = objectMapper.writeValueAsString(dataReq);
		req.setData(STBSecurity.Encrypt3DES(data, paymentAccount.getEncryptKey()));

		return process(paymentAccount, req);
	}

	private PGResponse process(PaymentAccount paymentAccount, BaseRequest req) throws Exception {
		STBSecurity.initParam(paymentAccount);

		String body = objectMapper.writeValueAsString(req);
		String bodySigned = STBSecurity.sign(STBSecurity.md5(body));

		// TODO
		//ClientRequest.setStbUrl(getPaymentChannel().getUrl());
		ClientRequest.setUser(paymentAccount.getUsername());
		ClientRequest.setPassword(paymentAccount.getPassword());

		// TODO
		WriteInfoLog("2. STB REQUEST PARAM", body);

		// TODO: Call API STB
		String res = ClientRequest.callApi(body, bodySigned);
		// TODO
		WriteInfoLog("3. STB RESPONSE", res);

		BaseResponse response = objectMapper.readValue(res, BaseResponse.class);
		response.setData(STBSecurity.Decrypt3DES(response.getData(), paymentAccount.getEncryptKey()));

		if(StringUtils.isBlank(response.getData())){
			BaseDataRes dataRes = new BaseDataRes();
			dataRes.setTransId(req.getTransId());
			dataRes.setRequestId(req.getRequestID());
			response.setData(objectMapper.writeValueAsString(dataRes));
		}

		PGResponse pGResponse = new PGResponse();
		pGResponse.setStatus(true);
		pGResponse.setErrorCode(response.getRespCode());
		pGResponse.setMessage(STBConstants.getErrorMessage(response.getRespCode()));
		pGResponse.setData(response.getData());
		//return objectMapper.writeValueAsString(pGResponse);
		return pGResponse;
	}

	private void buildCommonParam(BaseRequest req, DataRequest input, String funcName) throws Exception {
		req.setFunctionName(funcName);
		try {
		//	req.setRequestDateTime(Utils.getDateTime(input.getTransTime()));// 20170606085942
			req.setRequestDateTime(Utils.getDateTime(new Date()));
		} catch (Exception e) {
			logger.error("STB iBFT date format incorrect: " + input.getTransTime());
			throw new Exception("STB trans_date format incorrect");
		}
		req.setRequestID(Utils.genReqUID());
		req.setTransId(input.getTransId());
	}

	public static PGResponse createErrorResponse(String message){
		PGResponse resp = new PGResponse();
		resp.setStatus(false);
		resp.setErrorCode("01");
		resp.setDescription(message);
		try {
			WriteInfoLog("STB SendOrder CheckValidate" +resp.getDescription());
			return resp;
		} catch (Exception ex) {
			logger.info(ex.getMessage());
		}
		return null;
	}


}
