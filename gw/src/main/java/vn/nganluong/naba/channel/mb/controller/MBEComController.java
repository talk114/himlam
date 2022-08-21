/*
package vn.nganluong.naba.channel.mb.controller;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import gateway.core.dto.PGRequest;
import gateway.core.dto.PGResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

import gateway.core.channel.mb.dto.MBConst;
import gateway.core.channel.mb.dto.request.MBConfirmTransactionMBRequets;
import gateway.core.channel.mb.dto.request.MBCreateTransactionMBRequets;
import gateway.core.channel.mb.dto.request.MBRevertTransactionMBRequets;
import gateway.core.channel.mb.dto.request.MBStatusTransactionMBRequets;
import gateway.core.channel.mb.dto.response.MBGetTokenResponse;
import gateway.core.channel.mb.dto.response.MBPaymentEcomResponse;
import vn.nganluong.naba.channel.vib.dto.PaymentDTO;
import vn.nganluong.naba.dto.AddTransactionGWResponse;
import vn.nganluong.naba.dto.DataAddTransactionGWResponse;
import vn.nganluong.naba.dto.LogConst;
import vn.nganluong.naba.dto.PaymentConst;
import vn.nganluong.naba.dto.PaymentConst.EnumPaymentRevertStatus;
import vn.nganluong.naba.dto.ResponseJson;
import vn.nganluong.naba.entities.ChannelFunction;
import vn.nganluong.naba.entities.Payment;
import vn.nganluong.naba.service.ChannelFunctionService;
import vn.nganluong.naba.service.CommonLogService;
import vn.nganluong.naba.service.CommonPGResponseService;
import vn.nganluong.naba.service.CommonResponseService;
import vn.nganluong.naba.service.PaymentService;
import vn.nganluong.naba.service.PgLogChannelFunctionService;
import vn.nganluong.naba.utils.RequestUtil;

@RestController
@RequestMapping(value = "/mb/ecom")
public class MBEComController {

	private static final Logger logger = LogManager.getLogger(MBEComController.class);
	private static final String SERVICE_NAME = "ECOM";

	@Autowired
	private PgLogChannelFunctionService pgLogChannelFunctionService;

	@Autowired
	private ChannelFunctionService channelFunctionService;

	@Autowired
	private PaymentService paymentService;

	@Autowired
	private CommonLogService commonLogService;

	@Autowired
	private CommonResponseService commonResponseService;

	@Autowired
	private CommonPGResponseService commonPGResponseService;

	@GetMapping("/token")
	public String getAccessToken() {
		logger.info(commonLogService.createContentLogStartEndFunction(MBConst.CHANNEL_CODE, SERVICE_NAME,
				MBConst.FUNCTION_CODE_ACCESS_TOKEN, true));

		RestTemplate restTemplate = null;
		try {
			restTemplate = new RestTemplate(RequestUtil.createRequestFactory());
		} catch (KeyManagementException | NoSuchAlgorithmException e) {

			pgLogChannelFunctionService.writeLogChannelFunction(MBConst.CHANNEL_CODE,
					MBConst.CHANNEL_FUNCTION_ACCESS_TOKEN_CODE, false);

			String[] paramsLog = new String[] { e.getMessage() };
			logger.info(commonLogService.createContentLog(MBConst.CHANNEL_CODE, SERVICE_NAME,
					MBConst.FUNCTION_CODE_ACCESS_TOKEN, false, false, true, paramsLog));
		}

		ChannelFunction channelFunction = channelFunctionService.findChannelFunctionByCodeAndChannelCode(
				MBConst.CHANNEL_FUNCTION_ACCESS_TOKEN_CODE, MBConst.CHANNEL_CODE);

		if (channelFunction == null) {
			pgLogChannelFunctionService.writeLogChannelFunction(MBConst.CHANNEL_CODE,
					MBConst.CHANNEL_FUNCTION_ACCESS_TOKEN_CODE, false);

			String[] paramsLog = new String[] { MBConst.CHANNEL_NOT_CONFIG_MSG };
			logger.info(commonLogService.createContentLog(MBConst.CHANNEL_CODE, SERVICE_NAME,
					MBConst.FUNCTION_CODE_ACCESS_TOKEN, false, false, true, paramsLog));
			return null;
		}

		restTemplate.getInterceptors()
				.add(new BasicAuthorizationInterceptor(channelFunction.getUser(), channelFunction.getPassword()));

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.add("Accept", "application/json");

		MultiValueMap<String, String> mapBody = new LinkedMultiValueMap<String, String>();
		mapBody.add("grant_type", "client_credentials");

		HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<MultiValueMap<String, String>>(mapBody,
				headers);

		// post request
		ResponseEntity<MBGetTokenResponse> response = restTemplate
				.exchange(RequestUtil.createUrlChannelFunction(channelFunction.getHost(), channelFunction.getPort(),
						channelFunction.getUrl()), HttpMethod.POST, entity, MBGetTokenResponse.class);

		MBGetTokenResponse getTokenResponse = response.getBody();

		if (response.getStatusCode().equals(HttpStatus.OK)) {
			pgLogChannelFunctionService.writeLogChannelFunction(MBConst.CHANNEL_CODE,
					MBConst.CHANNEL_FUNCTION_ACCESS_TOKEN_CODE, true);

			String[] paramsLog = new String[] { getTokenResponse.getAccess_token() };
			logger.info(commonLogService.createContentLog(MBConst.CHANNEL_CODE, SERVICE_NAME,
					MBConst.FUNCTION_CODE_ACCESS_TOKEN, true, false, true, paramsLog));

		} else {
			pgLogChannelFunctionService.writeLogChannelFunction(MBConst.CHANNEL_CODE,
					MBConst.CHANNEL_FUNCTION_ACCESS_TOKEN_CODE, false);

			String[] paramsLog = new String[] { "Resonse httpstatus: " + response.getStatusCode() };
			logger.info(commonLogService.createContentLog(MBConst.CHANNEL_CODE, SERVICE_NAME,
					MBConst.FUNCTION_CODE_ACCESS_TOKEN, false, false, true, paramsLog));
		}

		logger.info(commonLogService.createContentLogStartEndFunction(MBConst.CHANNEL_CODE, SERVICE_NAME,
				MBConst.FUNCTION_CODE_ACCESS_TOKEN, false));
		return getTokenResponse.getAccess_token();
	}

	@PostMapping(path = "/add_transaction", consumes = { MediaType.APPLICATION_FORM_URLENCODED_VALUE }, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> addTransaction(MBCreateTransactionMBRequets transactionInfo) {
		String channelFucntionId = MBConst.CHANNEL_FUNCTION_ADD_TRANSACTION;
		String[] paramsLog = null;
		PaymentDTO paymentDTO = new PaymentDTO();
		try {
			logger.info(commonLogService.createContentLogStartEndFunction(MBConst.CHANNEL_CODE, SERVICE_NAME,
					MBConst.FUNCTION_CODE_ADD_TRANSACTION, true));

			if (StringUtils.isBlank(transactionInfo.getClient_transaction_id())) {
				paramsLog = new String[] { "Merchant transaction is empty" };
				logger.info(commonLogService.createContentLog(MBConst.CHANNEL_CODE, SERVICE_NAME,
						MBConst.FUNCTION_CODE_ADD_TRANSACTION, false, false, true, paramsLog));

				logger.info(commonLogService.createContentLogStartEndFunction(MBConst.CHANNEL_CODE, SERVICE_NAME,
						MBConst.FUNCTION_CODE_ADD_TRANSACTION, false));
				return commonResponseService.returnBadRequets_TransactionEmpty();
			} else {
				Payment paymentTocheckExist = paymentService
						.findByMerchantTransactionId(transactionInfo.getClient_transaction_id());
				if (paymentTocheckExist != null) {

					paramsLog = new String[] { "Merchant transaction id (trace id) already exist ("
							+ transactionInfo.getClient_transaction_id() + ")" };
					logger.info(commonLogService.createContentLog(MBConst.CHANNEL_CODE, SERVICE_NAME,
							MBConst.FUNCTION_CODE_ADD_TRANSACTION, false, false, true, paramsLog));

					logger.info(commonLogService.createContentLogStartEndFunction(MBConst.CHANNEL_CODE, SERVICE_NAME,
							MBConst.FUNCTION_CODE_ADD_TRANSACTION, false));
					return commonResponseService.returnBadRequets_TransactionExist();
				}
			}

			String accessToken = getAccessToken();

			if (StringUtils.isBlank(accessToken)) {
				logger.info(commonLogService.createContentLogStartEndFunction(MBConst.CHANNEL_CODE, SERVICE_NAME,
						MBConst.FUNCTION_CODE_ADD_TRANSACTION, false));
				return commonResponseService.returnBadGateway();
			}

			ChannelFunction channelFunction = channelFunctionService
					.findChannelFunctionByCodeAndChannelCode(channelFucntionId, MBConst.CHANNEL_CODE);

			if (channelFunction == null) {
				pgLogChannelFunctionService.writeLogChannelFunction(MBConst.CHANNEL_CODE, channelFucntionId, false);

				paramsLog = new String[] { MBConst.CHANNEL_NOT_CONFIG_MSG };

				logger.info(commonLogService.createContentLog(MBConst.CHANNEL_CODE, SERVICE_NAME,
						MBConst.FUNCTION_CODE_ADD_TRANSACTION, false, false, true, paramsLog));
				return commonResponseService.returnBadRequets_WithCause(paramsLog[0]);
			}

			RestTemplate restTemplate = new RestTemplate(RequestUtil.createRequestFactory());
			
//			HttpComponentsClientHttpRequestFactory rf =
//				    (HttpComponentsClientHttpRequestFactory) restTemplate.getRequestFactory();
//				rf.setReadTimeout(1 * 20000);
//				rf.setConnectTimeout(1 * 20000);

			String clientMessageId = RandomStringUtils.randomAlphanumeric(24);
			String transactionId = createTransactionIdFromClientTransactionId(transactionInfo.getClient_transaction_id());

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.add("Authorization", "Bearer " + accessToken);
			headers.add("Accept", "application/json");
			headers.add("clientMessageId", clientMessageId);
			headers.add("transactionId", transactionId);

			JSONObject bodyRequest = new JSONObject();
			bodyRequest.put("accountName", transactionInfo.getCard_name());
			bodyRequest.put("accountNumber", transactionInfo.getCard_no());
			bodyRequest.put("amount", transactionInfo.getAmount());
			bodyRequest.put("currency", MBConst.CCY);
			bodyRequest.put("dateOpen", transactionInfo.getDate_open());
			bodyRequest.put("fee", transactionInfo.getFee());
			bodyRequest.put("merchant", transactionInfo.getMerchant());
			bodyRequest.put("mobile", transactionInfo.getMobile());
			bodyRequest.put("paymentDetails", transactionInfo.getPayment_description());
			bodyRequest.put("serviceType", transactionInfo.getService_type());

			HttpEntity<String> entity = new HttpEntity<String>(bodyRequest.toString(), headers);

			String resourceUrl = RequestUtil.createUrlChannelFunction(channelFunction.getHost(),
					channelFunction.getPort(), channelFunction.getUrl());

			// https://api-sandbox.mbbank.com.vn/ms/ewallet/v1.0/paymentgw/request
			UriComponentsBuilder builderUri = UriComponentsBuilder.fromHttpUrl(resourceUrl);

			// add infor to log
			bodyRequest.put("client_transaction_id", transactionInfo.getClient_transaction_id());

			ObjectMapper mapperObj = new ObjectMapper();
			paramsLog = new String[] { "URL: " + builderUri.toUriString() + ", body: [" + bodyRequest.toString() + "]" };
			logger.info(commonLogService.createContentLog(MBConst.CHANNEL_CODE, SERVICE_NAME,
					MBConst.FUNCTION_CODE_ADD_TRANSACTION, true, true, false, paramsLog));

			// Create payment before call api into channel
			// Add payment to database:
			
			paymentDTO.setChannelId(channelFunction.getChannel().getId());
			paymentDTO.setAmount(transactionInfo.getAmount());
			paymentDTO.setMerchantTransactionId(transactionInfo.getClient_transaction_id());
			paymentDTO.setAccountNo(transactionInfo.getCard_no());
			// paymentDTO.setCardNo(transactionInfo.getCard_no());
			paymentDTO.setRawRequest(LogConst.LOG_CONTENT_PREFIX_ADD_TRANSACTION + StringUtils.join(paramsLog));
			paymentDTO.setDescription(transactionInfo.getPayment_description());
			paymentDTO.setChannelTransactionId(transactionId);
			paymentDTO.setMerchantCode(transactionInfo.getMerchant());

			paymentService.createPayment(paymentDTO);

			MBPaymentEcomResponse responseBody = new MBPaymentEcomResponse();
			try {
				ResponseEntity<MBPaymentEcomResponse> response = restTemplate.exchange(builderUri.toUriString(),
						HttpMethod.POST, entity, MBPaymentEcomResponse.class);
				responseBody =  response.getBody();
			} catch (HttpClientErrorException | HttpServerErrorException exx) {
				responseBody = mapperObj.readValue(exx.getResponseBodyAsString(), MBPaymentEcomResponse.class);
			}

			paymentDTO.setRawResponse(LogConst.LOG_CONTENT_PREFIX_ADD_TRANSACTION + mapperObj.writeValueAsString(responseBody));

			AddTransactionGWResponse jsonObject = new AddTransactionGWResponse();
			jsonObject.setBank_message(StringUtils.join(responseBody.getError_desc()));
			jsonObject.setBank_error_code(responseBody.getError_code());
			jsonObject.setTransaction_id(transactionId);

			if (StringUtils.equals(responseBody.getError_code(), MBConst.MB_RESPONSE_ERROR_CODE_SUCCESS)) {
				ResponseJson prefixResult = commonResponseService.returnGatewayRequestSuccessPrefix();
				jsonObject.setError_code(prefixResult.getError_code());
				jsonObject.setMessage(prefixResult.getMessage());
				jsonObject.setRequest_id(responseBody.getData().getRequestId());
				pgLogChannelFunctionService.writeLogChannelFunction(MBConst.CHANNEL_CODE,
						MBConst.CHANNEL_FUNCTION_ADD_TRANSACTION, true);
				
				paymentDTO.setPgTransactionStatus(PaymentConst.EnumBankStatus.SUCCEEDED.code());
				paymentDTO.setClientRequestId(responseBody.getData().getRequestId());
				
				paramsLog = new String[] { paymentDTO.getRawResponse() };
				logger.info(commonLogService.createContentLog(MBConst.CHANNEL_CODE, SERVICE_NAME,
						MBConst.FUNCTION_CODE_ADD_TRANSACTION, true, false, true, paramsLog));
			} else {
				paymentDTO.setPgTransactionStatus(PaymentConst.EnumBankStatus.FAILED.code());
				paymentDTO.setChannelTransactionStatus(PaymentConst.EnumBankStatus.FAILED.code());
				ResponseJson prefixResult = commonResponseService.returnChannelBadRequestPrefix();
				jsonObject.setError_code(prefixResult.getError_code());
				jsonObject.setMessage(prefixResult.getMessage());

				pgLogChannelFunctionService.writeLogChannelFunction(MBConst.CHANNEL_CODE,
						MBConst.CHANNEL_FUNCTION_ADD_TRANSACTION, false);

				paramsLog = new String[] { paymentDTO.getRawResponse() };
				logger.info(commonLogService.createContentLog(MBConst.CHANNEL_CODE, SERVICE_NAME,
						MBConst.FUNCTION_CODE_ADD_TRANSACTION, false, false, true, paramsLog));
			}
			paymentService.updateTransactionStatusAfterCreatedPayment(paymentDTO);
			logger.info(commonLogService.createContentLogStartEndFunction(MBConst.CHANNEL_CODE, SERVICE_NAME,
					MBConst.FUNCTION_CODE_ADD_TRANSACTION, false));
			return new ResponseEntity<AddTransactionGWResponse>(jsonObject, HttpStatus.OK);

		} catch (RestClientException e) {

			pgLogChannelFunctionService.writeLogChannelFunction(MBConst.CHANNEL_CODE, channelFucntionId, false);
			
			paymentDTO.setPgTransactionStatus(PaymentConst.EnumBankStatus.FAILED.code());
			paymentDTO.setChannelTransactionStatus(PaymentConst.EnumBankStatus.FAILED.code());
			paymentDTO.setRawResponse(e.getMessage());
			paymentService.updateTransactionStatusAfterCreatedPayment(paymentDTO);

			paramsLog = new String[] { e.getMessage() };
			logger.info(commonLogService.createContentLog(MBConst.CHANNEL_CODE, SERVICE_NAME,
					MBConst.FUNCTION_CODE_ADD_TRANSACTION, false, false, true, paramsLog));
			return commonResponseService.returnChannelBadRequest(e.getMessage());
			
			
		} catch (Exception e) {

			paramsLog = new String[] { e.getMessage() };
			logger.info(commonLogService.createContentLog(MBConst.CHANNEL_CODE, SERVICE_NAME,
					MBConst.FUNCTION_CODE_ADD_TRANSACTION, false, false, true, paramsLog));
			logger.info(commonLogService.createContentLogStartEndFunction(MBConst.CHANNEL_CODE, SERVICE_NAME,
					MBConst.FUNCTION_CODE_ADD_TRANSACTION, false));
			return commonResponseService.returnBadGateway();
		}

	}

	public ResponseEntity<?> addTransaction(PGRequest pgRequest) {

		ObjectMapper mapperObj = new ObjectMapper();

		String channelFucntionId = MBConst.CHANNEL_FUNCTION_ADD_TRANSACTION;
		String[] paramsLog = null;
		PaymentDTO paymentDTO = new PaymentDTO();
		try {

			logger.info(commonLogService.createContentLogStartEndFunction(MBConst.CHANNEL_CODE, SERVICE_NAME,
					MBConst.FUNCTION_CODE_ADD_TRANSACTION, true));

			MBCreateTransactionMBRequets transactionInfo = mapperObj.readValue(pgRequest.getData(), MBCreateTransactionMBRequets.class);
			if (StringUtils.isBlank(transactionInfo.getClient_transaction_id())) {
				paramsLog = new String[] { "Merchant transaction is empty" };
				logger.info(commonLogService.createContentLog(MBConst.CHANNEL_CODE, SERVICE_NAME,
						MBConst.FUNCTION_CODE_ADD_TRANSACTION, false, false, true, paramsLog));

				logger.info(commonLogService.createContentLogStartEndFunction(MBConst.CHANNEL_CODE, SERVICE_NAME,
						MBConst.FUNCTION_CODE_ADD_TRANSACTION, false));
				return commonPGResponseService.returnBadRequets_TransactionEmpty();
			} else {
				Payment paymentToCheckExist = paymentService
						.findByMerchantTransactionId(transactionInfo.getClient_transaction_id());
				if (paymentToCheckExist != null) {

					paramsLog = new String[] { "Merchant transaction id (trace id) already exist ("
							+ transactionInfo.getClient_transaction_id() + ")" };
					logger.info(commonLogService.createContentLog(MBConst.CHANNEL_CODE, SERVICE_NAME,
							MBConst.FUNCTION_CODE_ADD_TRANSACTION, false, false, true, paramsLog));

					logger.info(commonLogService.createContentLogStartEndFunction(MBConst.CHANNEL_CODE, SERVICE_NAME,
							MBConst.FUNCTION_CODE_ADD_TRANSACTION, false));
					return commonPGResponseService.returnBadRequets_TransactionExist();
				}
			}

			String accessToken = getAccessToken();

			if (StringUtils.isBlank(accessToken)) {
				logger.info(commonLogService.createContentLogStartEndFunction(MBConst.CHANNEL_CODE, SERVICE_NAME,
						MBConst.FUNCTION_CODE_ADD_TRANSACTION, false));
				return commonPGResponseService.returnBadGateway();
			}

			ChannelFunction channelFunction = channelFunctionService
					.findChannelFunctionByCodeAndChannelCode(channelFucntionId, MBConst.CHANNEL_CODE);

			if (channelFunction == null) {
				pgLogChannelFunctionService.writeLogChannelFunction(MBConst.CHANNEL_CODE, channelFucntionId, false);

				paramsLog = new String[] { MBConst.CHANNEL_NOT_CONFIG_MSG };

				logger.info(commonLogService.createContentLog(MBConst.CHANNEL_CODE, SERVICE_NAME,
						MBConst.FUNCTION_CODE_ADD_TRANSACTION, false, false, true, paramsLog));
				return commonPGResponseService.returnBadRequets_WithCause(paramsLog[0]);
			}

			RestTemplate restTemplate = new RestTemplate(RequestUtil.createRequestFactory());

			String clientMessageId = RandomStringUtils.randomAlphanumeric(24);
			String transactionId = createTransactionIdFromClientTransactionId(transactionInfo.getClient_transaction_id());

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.add("Authorization", "Bearer " + accessToken);
			headers.add("Accept", "application/json");
			headers.add("clientMessageId", clientMessageId);
			headers.add("transactionId", transactionId);

			JSONObject bodyRequest = new JSONObject();
			bodyRequest.put("accountName", transactionInfo.getCard_name());
			bodyRequest.put("accountNumber", transactionInfo.getCard_no());
			bodyRequest.put("amount", transactionInfo.getAmount());
			bodyRequest.put("currency", MBConst.CCY);
			bodyRequest.put("dateOpen", transactionInfo.getDate_open());
			bodyRequest.put("fee", transactionInfo.getFee());
			bodyRequest.put("merchant", transactionInfo.getMerchant());
			bodyRequest.put("mobile", transactionInfo.getMobile());
			bodyRequest.put("paymentDetails", transactionInfo.getPayment_description());
			bodyRequest.put("serviceType", transactionInfo.getService_type());

			HttpEntity<String> entity = new HttpEntity<String>(bodyRequest.toString(), headers);

			String resourceUrl = RequestUtil.createUrlChannelFunction(channelFunction.getHost(),
					channelFunction.getPort(), channelFunction.getUrl());

			// https://api-sandbox.mbbank.com.vn/ms/ewallet/v1.0/paymentgw/request
			UriComponentsBuilder builderUri = UriComponentsBuilder.fromHttpUrl(resourceUrl);

			//
			bodyRequest.put("client_transaction_id", transactionInfo.getClient_transaction_id());
			paramsLog = new String[] { "URL: " + builderUri.toUriString() + ", body: [" + bodyRequest.toString() + "]" };
			logger.info(commonLogService.createContentLog(MBConst.CHANNEL_CODE, SERVICE_NAME,
					MBConst.FUNCTION_CODE_ADD_TRANSACTION, true, true, false, paramsLog));

			// Create payment before call api into channel
			// Add payment to database:

			paymentDTO.setChannelId(channelFunction.getChannel().getId());
			paymentDTO.setAmount(transactionInfo.getAmount());
			paymentDTO.setMerchantTransactionId(transactionInfo.getClient_transaction_id());
			paymentDTO.setAccountNo(transactionInfo.getCard_no());
			// TODO
			// paymentDTO.setCardNo(transactionInfo.getCard_no());
			paymentDTO.setRawRequest(LogConst.LOG_CONTENT_PREFIX_ADD_TRANSACTION + StringUtils.join(paramsLog));
			paymentDTO.setDescription(transactionInfo.getPayment_description());
			paymentDTO.setChannelTransactionId(transactionId);
			paymentDTO.setMerchantCode(transactionInfo.getMerchant());

			paymentService.createPayment(paymentDTO);

			MBPaymentEcomResponse responseBody = new MBPaymentEcomResponse();

			try {
				ResponseEntity<MBPaymentEcomResponse> response = restTemplate.exchange(builderUri.toUriString(),
						HttpMethod.POST, entity, MBPaymentEcomResponse.class);

				responseBody =  response.getBody();
			} catch (HttpClientErrorException | HttpServerErrorException exx) {
				responseBody = mapperObj.readValue(exx.getResponseBodyAsString(), MBPaymentEcomResponse.class);
			}

			paymentDTO.setRawResponse(LogConst.LOG_CONTENT_PREFIX_ADD_TRANSACTION + mapperObj.writeValueAsString(responseBody));

			AddTransactionGWResponse jsonObject = new AddTransactionGWResponse();
			PGResponse pgResponse = new PGResponse();
			pgResponse.setStatus(true);
			DataAddTransactionGWResponse dataObject = new DataAddTransactionGWResponse();

			jsonObject.setBank_message(StringUtils.join(responseBody.getError_desc()));
			jsonObject.setBank_error_code(responseBody.getError_code());
			jsonObject.setTransaction_id(transactionId);
			dataObject.setTransaction_id(transactionId);
			pgResponse.setChannelErrorCode(responseBody.getError_code());
			pgResponse.setChannelMessage(StringUtils.join(responseBody.getError_desc()));


			if (StringUtils.equals(responseBody.getError_code(), MBConst.MB_RESPONSE_ERROR_CODE_SUCCESS)) {
				PGResponse prefixResult = commonPGResponseService.returnGatewayRequestSuccessPrefix();
				jsonObject.setError_code(prefixResult.getErrorCode());
				jsonObject.setMessage(prefixResult.getMessage());
				jsonObject.setRequest_id(responseBody.getData().getRequestId());
				dataObject.setRequest_id(responseBody.getData().getRequestId());

				pgResponse.setErrorCode(prefixResult.getErrorCode());
				pgResponse.setMessage(prefixResult.getMessage());

				pgLogChannelFunctionService.writeLogChannelFunction(MBConst.CHANNEL_CODE,
						MBConst.CHANNEL_FUNCTION_ADD_TRANSACTION, true);

				paymentDTO.setPgTransactionStatus(PaymentConst.EnumBankStatus.SUCCEEDED.code());
				paymentDTO.setClientRequestId(responseBody.getData().getRequestId());

				paramsLog = new String[] { paymentDTO.getRawResponse() };
				logger.info(commonLogService.createContentLog(MBConst.CHANNEL_CODE, SERVICE_NAME,
						MBConst.FUNCTION_CODE_ADD_TRANSACTION, true, false, true, paramsLog));

			} else {
				paymentDTO.setPgTransactionStatus(PaymentConst.EnumBankStatus.FAILED.code());
				paymentDTO.setChannelTransactionStatus(PaymentConst.EnumBankStatus.FAILED.code());
				PGResponse prefixResult = commonPGResponseService.returnChannelBadRequestPrefix();

				pgResponse.setErrorCode(prefixResult.getErrorCode());
				pgResponse.setMessage(prefixResult.getMessage());

				jsonObject.setError_code(prefixResult.getErrorCode());
				jsonObject.setMessage(prefixResult.getMessage());

				pgLogChannelFunctionService.writeLogChannelFunction(MBConst.CHANNEL_CODE,
						MBConst.CHANNEL_FUNCTION_ADD_TRANSACTION, false);

				paramsLog = new String[] { paymentDTO.getRawResponse() };
				logger.info(commonLogService.createContentLog(MBConst.CHANNEL_CODE, SERVICE_NAME,
						MBConst.FUNCTION_CODE_ADD_TRANSACTION, false, false, true, paramsLog));
			}
			pgResponse.setData(dataObject);
			paymentService.updateTransactionStatusAfterCreatedPayment(paymentDTO);
			logger.info(commonLogService.createContentLogStartEndFunction(MBConst.CHANNEL_CODE, SERVICE_NAME,
					MBConst.FUNCTION_CODE_ADD_TRANSACTION, false));
			return new ResponseEntity<PGResponse>(pgResponse, HttpStatus.OK);

		} catch (RestClientException e) {

			pgLogChannelFunctionService.writeLogChannelFunction(MBConst.CHANNEL_CODE, channelFucntionId, false);

			paymentDTO.setPgTransactionStatus(PaymentConst.EnumBankStatus.FAILED.code());
			paymentDTO.setChannelTransactionStatus(PaymentConst.EnumBankStatus.FAILED.code());
			paymentDTO.setRawResponse(e.getMessage());
			paymentService.updateTransactionStatusAfterCreatedPayment(paymentDTO);

			paramsLog = new String[] { e.getMessage() };
			logger.info(commonLogService.createContentLog(MBConst.CHANNEL_CODE, SERVICE_NAME,
					MBConst.FUNCTION_CODE_ADD_TRANSACTION, false, false, true, paramsLog));
			return commonPGResponseService.returnChannelBadRequest(e.getMessage());


		} catch (Exception e) {

			paramsLog = new String[] { e.getMessage() };
			logger.info(commonLogService.createContentLog(MBConst.CHANNEL_CODE, SERVICE_NAME,
					MBConst.FUNCTION_CODE_ADD_TRANSACTION, false, false, true, paramsLog));
			logger.info(commonLogService.createContentLogStartEndFunction(MBConst.CHANNEL_CODE, SERVICE_NAME,
					MBConst.FUNCTION_CODE_ADD_TRANSACTION, false));
			return commonPGResponseService.returnBadGateway();
		}

	}
	
	
	@PostMapping(path = "/confirm_transaction", consumes = { MediaType.APPLICATION_FORM_URLENCODED_VALUE }, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	ResponseEntity<?> confirmTransaction(MBConfirmTransactionMBRequets transactionInfo) {
		String channelFucntionId = MBConst.CHANNEL_FUNCTION_CONFIRM_TRANSACTION;
		String[] paramsLog = null;
		PaymentDTO paymentDTO = new PaymentDTO();
		paymentDTO.setMerchantTransactionId(transactionInfo.getClient_transaction_id());
		try {
			logger.info(commonLogService.createContentLogStartEndFunction(MBConst.CHANNEL_CODE, SERVICE_NAME,
					MBConst.FUNCTION_CODE_CONFIRM_TRANSACTION, true));

			if (StringUtils.isBlank(transactionInfo.getClient_transaction_id())) {
				paramsLog = new String[] { "Merchant transaction is empty" };
				logger.info(commonLogService.createContentLog(MBConst.CHANNEL_CODE, SERVICE_NAME,
						MBConst.FUNCTION_CODE_CONFIRM_TRANSACTION, false, false, true, paramsLog));

				logger.info(commonLogService.createContentLogStartEndFunction(MBConst.CHANNEL_CODE, SERVICE_NAME,
						MBConst.FUNCTION_CODE_CONFIRM_TRANSACTION, false));
				return commonResponseService.returnBadRequets_TransactionEmpty();
			}
			
			Payment paymentTocheckExist = paymentService
					.findByMerchantTransactionId(transactionInfo.getClient_transaction_id());
			if (paymentTocheckExist == null) {

				paramsLog = new String[] { "Merchant transaction id (trace id) not exist ("
						+ transactionInfo.getClient_transaction_id() + ")" };
				logger.info(commonLogService.createContentLog(MBConst.CHANNEL_CODE, SERVICE_NAME,
						MBConst.FUNCTION_CODE_CONFIRM_TRANSACTION, false, false, true, paramsLog));

				logger.info(commonLogService.createContentLogStartEndFunction(MBConst.CHANNEL_CODE, SERVICE_NAME,
						MBConst.FUNCTION_CODE_CONFIRM_TRANSACTION, false));
				return commonResponseService.returnBadRequets_TransactionEmpty();
			}
			

			String accessToken = getAccessToken();

			if (StringUtils.isBlank(accessToken)) {
				logger.info(commonLogService.createContentLogStartEndFunction(MBConst.CHANNEL_CODE, SERVICE_NAME,
						MBConst.FUNCTION_CODE_CONFIRM_TRANSACTION, false));
				return commonResponseService.returnBadGateway();
			}

			ChannelFunction channelFunction = channelFunctionService
					.findChannelFunctionByCodeAndChannelCode(channelFucntionId, MBConst.CHANNEL_CODE);

			if (channelFunction == null) {
				pgLogChannelFunctionService.writeLogChannelFunction(MBConst.CHANNEL_CODE, channelFucntionId, false);

				paramsLog = new String[] { MBConst.CHANNEL_NOT_CONFIG_MSG };

				logger.info(commonLogService.createContentLog(MBConst.CHANNEL_CODE, SERVICE_NAME,
						MBConst.FUNCTION_CODE_CONFIRM_TRANSACTION, false, false, true, paramsLog));
				return commonResponseService.returnBadRequets_WithCause(paramsLog[0]);
			}

			RestTemplate restTemplate = new RestTemplate(RequestUtil.createRequestFactory());
			
//			HttpComponentsClientHttpRequestFactory rf =
//				    (HttpComponentsClientHttpRequestFactory) restTemplate.getRequestFactory();
//				rf.setReadTimeout(1 * 20000);
//				rf.setConnectTimeout(1 * 20000);

			String clientMessageId = RandomStringUtils.randomAlphanumeric(24);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.add("Authorization", "Bearer " + accessToken);
			headers.add("Accept", "application/json");
			headers.add("clientMessageId", clientMessageId);
			headers.add("transactionId", paymentTocheckExist.getChannelTransactionId());

			JSONObject bodyRequest = new JSONObject();
			bodyRequest.put("otp", transactionInfo.getOtp());
			bodyRequest.put("requestId", paymentTocheckExist.getClientRequestId());

			HttpEntity<String> entity = new HttpEntity<String>(bodyRequest.toString(), headers);

			String resourceUrl = RequestUtil.createUrlChannelFunction(channelFunction.getHost(),
					channelFunction.getPort(), channelFunction.getUrl());

			// https://api-sandbox.mbbank.com.vn/ms/ewallet/v1.0/paymentgw/request
			UriComponentsBuilder builderUri = UriComponentsBuilder.fromHttpUrl(resourceUrl);

			ObjectMapper mapperObj = new ObjectMapper();
			paramsLog = new String[] { "URL: " + builderUri.toUriString() + ", body: [" + bodyRequest.toString() + "]" };
			logger.info(commonLogService.createContentLog(MBConst.CHANNEL_CODE, SERVICE_NAME,
					MBConst.FUNCTION_CODE_CONFIRM_TRANSACTION, true, true, false, paramsLog));
			
			paymentDTO.setRawRequest(LogConst.LOG_CONTENT_PREFIX_CONFIRM_TRANSACTION + StringUtils.join(paramsLog));

			MBPaymentEcomResponse responseBody = new MBPaymentEcomResponse();
			
			try {
				ResponseEntity<MBPaymentEcomResponse> response = restTemplate.exchange(builderUri.toUriString(),
						HttpMethod.POST, entity, MBPaymentEcomResponse.class);
				responseBody = response.getBody();
			} catch (HttpClientErrorException | HttpServerErrorException exx) {
				responseBody = mapperObj.readValue(exx.getResponseBodyAsString(), MBPaymentEcomResponse.class);
			}
			
			
			paymentDTO.setRawResponse(LogConst.LOG_CONTENT_PREFIX_CONFIRM_TRANSACTION + mapperObj.writeValueAsString(responseBody));

			AddTransactionGWResponse jsonObject = new AddTransactionGWResponse();
			jsonObject.setBank_message(StringUtils.join(responseBody.getError_desc()));
			jsonObject.setBank_error_code(responseBody.getError_code());
			// jsonObject.setTransaction_id(transactionId);

			if (StringUtils.equals(responseBody.getError_code(), MBConst.MB_RESPONSE_ERROR_CODE_SUCCESS)) {
				ResponseJson prefixResult = commonResponseService.returnGatewayRequestSuccessPrefix();
				jsonObject.setError_code(prefixResult.getError_code());
				jsonObject.setMessage(prefixResult.getMessage());
				jsonObject.setRequest_id(responseBody.getData().getRequestId());
				jsonObject.setFt(responseBody.getData().getFt());
				pgLogChannelFunctionService.writeLogChannelFunction(MBConst.CHANNEL_CODE,
						MBConst.CHANNEL_FUNCTION_CONFIRM_TRANSACTION, true);
				
				if (StringUtils.isNotBlank(jsonObject.getFt()) && StringUtils.contains(jsonObject.getFt(), "FT")) {
					paymentDTO.setChannelTransactionStatus(PaymentConst.EnumBankStatus.SUCCEEDED.code());
					paymentDTO.setChannelTransactionSeq(jsonObject.getFt());
				}
				else {
					paymentDTO.setChannelTransactionStatus(PaymentConst.EnumBankStatus.PENDING.code());
				}
				
				// paymentDTO.setChannelTransactionStatus(PaymentConst.EnumBankStatus.SUCCEEDED.code());
				paymentDTO.setClientRequestId(responseBody.getData().getRequestId());
				
				paramsLog = new String[] { paymentDTO.getRawResponse() };
				logger.info(commonLogService.createContentLog(MBConst.CHANNEL_CODE, SERVICE_NAME,
						MBConst.FUNCTION_CODE_CONFIRM_TRANSACTION, true, false, true, paramsLog));
			} else {
				paymentDTO.setChannelTransactionStatus(PaymentConst.EnumBankStatus.PENDING.code());
				ResponseJson prefixResult = commonResponseService.returnChannelBadRequestPrefix();
				jsonObject.setError_code(prefixResult.getError_code());
				jsonObject.setMessage(prefixResult.getMessage());

				pgLogChannelFunctionService.writeLogChannelFunction(MBConst.CHANNEL_CODE,
						MBConst.CHANNEL_FUNCTION_CONFIRM_TRANSACTION, false);

				paramsLog = new String[] { paymentDTO.getRawResponse() };
				logger.info(commonLogService.createContentLog(MBConst.CHANNEL_CODE, SERVICE_NAME,
						MBConst.FUNCTION_CODE_CONFIRM_TRANSACTION, false, false, true, paramsLog));
			}
			
			paymentService.updateChannelTransactionStatusPayment(paymentDTO);
			
			logger.info(commonLogService.createContentLogStartEndFunction(MBConst.CHANNEL_CODE, SERVICE_NAME,
					MBConst.FUNCTION_CODE_CONFIRM_TRANSACTION, false));
			return new ResponseEntity<AddTransactionGWResponse>(jsonObject, HttpStatus.OK);

		} catch (RestClientException e) {

			pgLogChannelFunctionService.writeLogChannelFunction(MBConst.CHANNEL_CODE, channelFucntionId, false);
			
			paymentDTO.setChannelTransactionStatus(PaymentConst.EnumBankStatus.PENDING.code());
			paymentDTO.setRawResponse(e.getMessage());
			paymentService.updateChannelTransactionStatusPayment(paymentDTO);

			paramsLog = new String[] { e.getMessage() };
			logger.info(commonLogService.createContentLog(MBConst.CHANNEL_CODE, SERVICE_NAME,
					MBConst.FUNCTION_CODE_CONFIRM_TRANSACTION, false, false, true, paramsLog));
			return commonResponseService.returnChannelBadRequest(e.getMessage());
			
			
		} catch (Exception e) {

			paramsLog = new String[] { e.getMessage() };
			logger.info(commonLogService.createContentLog(MBConst.CHANNEL_CODE, SERVICE_NAME,
					MBConst.FUNCTION_CODE_CONFIRM_TRANSACTION, false, false, true, paramsLog));
			logger.info(commonLogService.createContentLogStartEndFunction(MBConst.CHANNEL_CODE, SERVICE_NAME,
					MBConst.FUNCTION_CODE_CONFIRM_TRANSACTION, false));
			return commonResponseService.returnBadGateway();
		}

	}
	
	@PostMapping(path = "/revert_transaction", consumes = { MediaType.APPLICATION_FORM_URLENCODED_VALUE }, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	ResponseEntity<?> revertTransaction(MBRevertTransactionMBRequets transactionInfo) {
		String channelFucntionId = MBConst.CHANNEL_FUNCTION_REVERT_TRANSACTION;
		String[] paramsLog = null;
		PaymentDTO paymentDTO = new PaymentDTO();
		paymentDTO.setMerchantTransactionId(transactionInfo.getClient_transaction_id());
		try {
			logger.info(commonLogService.createContentLogStartEndFunction(MBConst.CHANNEL_CODE, SERVICE_NAME,
					MBConst.FUNCTION_CODE_REVERT_TRANSACTION, true));

			if (StringUtils.isBlank(transactionInfo.getClient_transaction_id())) {
				paramsLog = new String[] { "Merchant transaction is empty" };
				logger.info(commonLogService.createContentLog(MBConst.CHANNEL_CODE, SERVICE_NAME,
						MBConst.FUNCTION_CODE_REVERT_TRANSACTION, false, false, true, paramsLog));

				logger.info(commonLogService.createContentLogStartEndFunction(MBConst.CHANNEL_CODE, SERVICE_NAME,
						MBConst.FUNCTION_CODE_REVERT_TRANSACTION, false));
				return commonResponseService.returnBadRequets_TransactionEmpty();
			}
			
			Payment paymentTocheckExist = paymentService
					.findByMerchantTransactionId(transactionInfo.getClient_transaction_id());
			if (paymentTocheckExist == null) {

				paramsLog = new String[] { "Merchant transaction id (trace id) not exist ("
						+ transactionInfo.getClient_transaction_id() + ")" };
				logger.info(commonLogService.createContentLog(MBConst.CHANNEL_CODE, SERVICE_NAME,
						MBConst.FUNCTION_CODE_REVERT_TRANSACTION, false, false, true, paramsLog));

				logger.info(commonLogService.createContentLogStartEndFunction(MBConst.CHANNEL_CODE, SERVICE_NAME,
						MBConst.FUNCTION_CODE_REVERT_TRANSACTION, false));
				return commonResponseService.returnBadRequets_TransactionEmpty();
			}
			

			String accessToken = getAccessToken();

			if (StringUtils.isBlank(accessToken)) {
				logger.info(commonLogService.createContentLogStartEndFunction(MBConst.CHANNEL_CODE, SERVICE_NAME,
						MBConst.FUNCTION_CODE_REVERT_TRANSACTION, false));
				return commonResponseService.returnBadGateway();
			}

			ChannelFunction channelFunction = channelFunctionService
					.findChannelFunctionByCodeAndChannelCode(channelFucntionId, MBConst.CHANNEL_CODE);

			if (channelFunction == null) {
				pgLogChannelFunctionService.writeLogChannelFunction(MBConst.CHANNEL_CODE, channelFucntionId, false);

				paramsLog = new String[] { MBConst.CHANNEL_NOT_CONFIG_MSG };

				logger.info(commonLogService.createContentLog(MBConst.CHANNEL_CODE, SERVICE_NAME,
						MBConst.FUNCTION_CODE_REVERT_TRANSACTION, false, false, true, paramsLog));
				return commonResponseService.returnBadRequets_WithCause(paramsLog[0]);
			}

			RestTemplate restTemplate = new RestTemplate(RequestUtil.createRequestFactory());
			
//			HttpComponentsClientHttpRequestFactory rf =
//				    (HttpComponentsClientHttpRequestFactory) restTemplate.getRequestFactory();
//				rf.setReadTimeout(1 * 10000);
//				rf.setConnectTimeout(1 * 10000);

			String clientMessageId = RandomStringUtils.randomAlphanumeric(24);
			String transactionId = RandomStringUtils.randomAlphanumeric(12);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.add("Authorization", "Bearer " + accessToken);
			headers.add("Accept", "application/json");
			headers.add("clientMessageId", clientMessageId);
			headers.add("transactionId", transactionId);

			JSONObject bodyRequest = new JSONObject();
			bodyRequest.put("amount", transactionInfo.getAmount());
			bodyRequest.put("originRequestTransactionId", paymentTocheckExist.getChannelTransactionId());

			HttpEntity<String> entity = new HttpEntity<String>(bodyRequest.toString(), headers);

			String resourceUrl = RequestUtil.createUrlChannelFunction(channelFunction.getHost(),
					channelFunction.getPort(), channelFunction.getUrl());

			// https://api-sandbox.mbbank.com.vn/ms/ewallet/v1.0/paymentgw/request
			UriComponentsBuilder builderUri = UriComponentsBuilder.fromHttpUrl(resourceUrl);

			ObjectMapper mapperObj = new ObjectMapper();
			paramsLog = new String[] { "URL: " + builderUri.toUriString() + ", body: [" + bodyRequest.toString() + "]" };
			logger.info(commonLogService.createContentLog(MBConst.CHANNEL_CODE, SERVICE_NAME,
					MBConst.FUNCTION_CODE_REVERT_TRANSACTION, true, true, false, paramsLog));
			paymentDTO.setRawRequest(LogConst.LOG_CONTENT_PREFIX_REVERT_TRANSACTION + StringUtils.join(paramsLog));
			MBPaymentEcomResponse responseBody = new MBPaymentEcomResponse();
			
			try {
				ResponseEntity<MBPaymentEcomResponse> response = restTemplate.exchange(builderUri.toUriString(),
						HttpMethod.POST, entity, MBPaymentEcomResponse.class);
				responseBody = response.getBody();
			} catch (HttpClientErrorException | HttpServerErrorException exx) {
				responseBody = mapperObj.readValue(exx.getResponseBodyAsString(), MBPaymentEcomResponse.class);
			}
			
			paymentDTO.setRawResponse(LogConst.LOG_CONTENT_PREFIX_REVERT_TRANSACTION + mapperObj.writeValueAsString(responseBody));

			AddTransactionGWResponse jsonObject = new AddTransactionGWResponse();
			jsonObject.setBank_message(StringUtils.join(responseBody.getError_desc()));
			jsonObject.setBank_error_code(responseBody.getError_code());
			// jsonObject.setTransaction_id(transactionId);

			if (StringUtils.equals(responseBody.getError_code(), MBConst.MB_RESPONSE_ERROR_CODE_SUCCESS)) {
				ResponseJson prefixResult = commonResponseService.returnGatewayRequestSuccessPrefix();
				jsonObject.setError_code(prefixResult.getError_code());
				jsonObject.setMessage(prefixResult.getMessage());
				jsonObject.setRequest_id(responseBody.getData().getRequestId());
				
				if (StringUtils.isEmpty(jsonObject.getBank_message())) {
					jsonObject.setBank_message("Revert success");
				}
				
				pgLogChannelFunctionService.writeLogChannelFunction(MBConst.CHANNEL_CODE,
						MBConst.CHANNEL_FUNCTION_REVERT_TRANSACTION, true);
				
				paymentDTO.setChannelRevertStatus(EnumPaymentRevertStatus.REVERTED.code()); // TODO
				paymentDTO.setClientRequestId(responseBody.getData().getRequestId());
				
				paramsLog = new String[] { paymentDTO.getRawResponse() };
				logger.info(commonLogService.createContentLog(MBConst.CHANNEL_CODE, SERVICE_NAME,
						MBConst.FUNCTION_CODE_REVERT_TRANSACTION, true, false, true, paramsLog));
			} else {
				ResponseJson prefixResult = commonResponseService.returnChannelBadRequestPrefix();
				jsonObject.setError_code(prefixResult.getError_code());
				jsonObject.setMessage(prefixResult.getMessage());

				pgLogChannelFunctionService.writeLogChannelFunction(MBConst.CHANNEL_CODE,
						MBConst.CHANNEL_FUNCTION_REVERT_TRANSACTION, false);

				paramsLog = new String[] { paymentDTO.getRawResponse() };
				logger.info(commonLogService.createContentLog(MBConst.CHANNEL_CODE, SERVICE_NAME,
						MBConst.FUNCTION_CODE_REVERT_TRANSACTION, false, false, true, paramsLog));
			}
			
			paymentService.updateChannelTransactionStatusPayment(paymentDTO);
			
			logger.info(commonLogService.createContentLogStartEndFunction(MBConst.CHANNEL_CODE, SERVICE_NAME,
					MBConst.FUNCTION_CODE_REVERT_TRANSACTION, false));
			return new ResponseEntity<AddTransactionGWResponse>(jsonObject, HttpStatus.OK);

		} catch (RestClientException e) {

			pgLogChannelFunctionService.writeLogChannelFunction(MBConst.CHANNEL_CODE, channelFucntionId, false);
			
			paramsLog = new String[] { e.getMessage() };
			logger.info(commonLogService.createContentLog(MBConst.CHANNEL_CODE, SERVICE_NAME,
					MBConst.FUNCTION_CODE_REVERT_TRANSACTION, false, false, true, paramsLog));
			return commonResponseService.returnChannelBadRequest(e.getMessage());
			
			
		} catch (Exception e) {

			paramsLog = new String[] { e.getMessage() };
			logger.info(commonLogService.createContentLog(MBConst.CHANNEL_CODE, SERVICE_NAME,
					MBConst.FUNCTION_CODE_REVERT_TRANSACTION, false, false, true, paramsLog));
			logger.info(commonLogService.createContentLogStartEndFunction(MBConst.CHANNEL_CODE, SERVICE_NAME,
					MBConst.FUNCTION_CODE_REVERT_TRANSACTION, false));
			return commonResponseService.returnBadGateway();
		}

	}
	
	@PostMapping(path = "/status_transaction", consumes = { MediaType.APPLICATION_FORM_URLENCODED_VALUE }, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	ResponseEntity<?> statusTransaction(MBStatusTransactionMBRequets transactionInfo) {
		String channelFucntionId = MBConst.CHANNEL_FUNCTION_STATUS_TRANSACTION;
		String[] paramsLog = null;
		PaymentDTO paymentDTO = new PaymentDTO();
		paymentDTO.setMerchantTransactionId(transactionInfo.getClient_transaction_id());
		try {
			logger.info(commonLogService.createContentLogStartEndFunction(MBConst.CHANNEL_CODE, SERVICE_NAME,
					MBConst.FUNCTION_CODE_STATUS_TRANSACTION, true));

			if (StringUtils.isBlank(transactionInfo.getClient_transaction_id())) {
				paramsLog = new String[] { "Merchant transaction is empty" };
				logger.info(commonLogService.createContentLog(MBConst.CHANNEL_CODE, SERVICE_NAME,
						MBConst.FUNCTION_CODE_STATUS_TRANSACTION, false, false, true, paramsLog));

				logger.info(commonLogService.createContentLogStartEndFunction(MBConst.CHANNEL_CODE, SERVICE_NAME,
						MBConst.FUNCTION_CODE_STATUS_TRANSACTION, false));
				return commonResponseService.returnBadRequets_TransactionEmpty();
			}
			
			Payment paymentTocheckExist = paymentService
					.findByMerchantTransactionId(transactionInfo.getClient_transaction_id());
			if (paymentTocheckExist == null) {

				paramsLog = new String[] { "Merchant transaction id (trace id) not exist ("
						+ transactionInfo.getClient_transaction_id() + ")" };
				logger.info(commonLogService.createContentLog(MBConst.CHANNEL_CODE, SERVICE_NAME,
						MBConst.FUNCTION_CODE_STATUS_TRANSACTION, false, false, true, paramsLog));

				logger.info(commonLogService.createContentLogStartEndFunction(MBConst.CHANNEL_CODE, SERVICE_NAME,
						MBConst.FUNCTION_CODE_STATUS_TRANSACTION, false));
				return commonResponseService.returnBadRequets_TransactionEmpty();
			}

			String accessToken = getAccessToken();

			if (StringUtils.isBlank(accessToken)) {
				logger.info(commonLogService.createContentLogStartEndFunction(MBConst.CHANNEL_CODE, SERVICE_NAME,
						MBConst.FUNCTION_CODE_STATUS_TRANSACTION, false));
				return commonResponseService.returnBadGateway();
			}

			ChannelFunction channelFunction = channelFunctionService
					.findChannelFunctionByCodeAndChannelCode(channelFucntionId, MBConst.CHANNEL_CODE);

			if (channelFunction == null) {
				pgLogChannelFunctionService.writeLogChannelFunction(MBConst.CHANNEL_CODE, channelFucntionId, false);

				paramsLog = new String[] { MBConst.CHANNEL_NOT_CONFIG_MSG };

				logger.info(commonLogService.createContentLog(MBConst.CHANNEL_CODE, SERVICE_NAME,
						MBConst.FUNCTION_CODE_STATUS_TRANSACTION, false, false, true, paramsLog));
				return commonResponseService.returnBadRequets_WithCause(paramsLog[0]);
			}

			RestTemplate restTemplate = new RestTemplate(RequestUtil.createRequestFactory());
			
			String clientMessageId = RandomStringUtils.randomAlphanumeric(24);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.add("Authorization", "Bearer " + accessToken);
			headers.add("Accept", "application/json");
			headers.add("clientMessageId", clientMessageId);
			headers.add("transactionId", "P3-" + paymentTocheckExist.getChannelTransactionId());
			
			MultiValueMap<String, String> mapParams = new LinkedMultiValueMap<String, String>();
			mapParams.add("transactionId", "P3-" + paymentTocheckExist.getChannelTransactionId());

			HttpEntity<?> entity = new HttpEntity<>(headers);

			String resourceUrl = RequestUtil.createUrlChannelFunction(channelFunction.getHost(),
					channelFunction.getPort(), channelFunction.getUrl());

			// https://api-sandbox.mbbank.com.vn/ms/ewallet/v1.0/transaction/status
			UriComponentsBuilder builderUri = UriComponentsBuilder.fromHttpUrl(resourceUrl).queryParams(mapParams);

			ObjectMapper mapperObj = new ObjectMapper();
			paramsLog = new String[] { "URL: " + builderUri.toUriString() + ", data: Header: ["
					+ mapperObj.writeValueAsString(headers) + "], body: []" };
			logger.info(commonLogService.createContentLog(MBConst.CHANNEL_CODE, SERVICE_NAME,
					MBConst.FUNCTION_CODE_STATUS_TRANSACTION, true, true, false, paramsLog));

			MBPaymentEcomResponse responseBody;
			try {
				ResponseEntity<MBPaymentEcomResponse> response = restTemplate.exchange(builderUri.toUriString(),
						HttpMethod.GET, entity, MBPaymentEcomResponse.class);
				responseBody = response.getBody();
			} catch (HttpClientErrorException | HttpServerErrorException exx) {
				responseBody = mapperObj.readValue(exx.getResponseBodyAsString(), MBPaymentEcomResponse.class);
			}
			
			paymentDTO.setRawResponse(LogConst.LOG_CONTENT_PREFIX_STATUS_TRANSACTION + mapperObj.writeValueAsString(responseBody));

			AddTransactionGWResponse jsonObject = new AddTransactionGWResponse();
			jsonObject.setBank_message(StringUtils.join(responseBody.getError_desc()));
			jsonObject.setBank_error_code(responseBody.getError_code());
			// jsonObject.setTransaction_id(transactionId);

			if (StringUtils.equals(responseBody.getError_code(), MBConst.MB_RESPONSE_ERROR_CODE_SUCCESS)) {
				ResponseJson prefixResult = commonResponseService.returnGatewayRequestSuccessPrefix();
				jsonObject.setError_code(prefixResult.getError_code());
				jsonObject.setMessage(prefixResult.getMessage());
				jsonObject.setRequest_id(responseBody.getData().getRequestId());
				jsonObject.setFt(StringUtils.join(responseBody.getData().getRoot_id(), ','));
				pgLogChannelFunctionService.writeLogChannelFunction(MBConst.CHANNEL_CODE,
						MBConst.CHANNEL_FUNCTION_STATUS_TRANSACTION, true);
				
				// TODO
				if (StringUtils.isNotBlank(jsonObject.getFt()) && StringUtils.contains(jsonObject.getFt(), "FT")) {
					paymentDTO.setChannelTransactionStatus(PaymentConst.EnumBankStatus.SUCCEEDED.code());
					paymentDTO.setChannelTransactionSeq(jsonObject.getFt());
				}
				else {
					paymentDTO.setChannelTransactionStatus(PaymentConst.EnumBankStatus.FAILED.code());
				}
				
				paymentDTO.setClientRequestId(responseBody.getData().getRequestId());
				
				paramsLog = new String[] { paymentDTO.getRawResponse() };
				logger.info(commonLogService.createContentLog(MBConst.CHANNEL_CODE, SERVICE_NAME,
						MBConst.FUNCTION_CODE_STATUS_TRANSACTION, true, false, true, paramsLog));
			} else {
				// TODO
				paymentDTO.setChannelTransactionStatus(PaymentConst.EnumBankStatus.FAILED.code());
				ResponseJson prefixResult = commonResponseService.returnChannelBadRequestPrefix();
				jsonObject.setError_code(prefixResult.getError_code());
				jsonObject.setMessage(prefixResult.getMessage());

				pgLogChannelFunctionService.writeLogChannelFunction(MBConst.CHANNEL_CODE,
						MBConst.CHANNEL_FUNCTION_STATUS_TRANSACTION, false);

				paramsLog = new String[] { paymentDTO.getRawResponse() };
				logger.info(commonLogService.createContentLog(MBConst.CHANNEL_CODE, SERVICE_NAME,
						MBConst.FUNCTION_CODE_STATUS_TRANSACTION, false, false, true, paramsLog));
			}
			
			paymentService.updateChannelTransactionStatusPayment(paymentDTO);
			
			logger.info(commonLogService.createContentLogStartEndFunction(MBConst.CHANNEL_CODE, SERVICE_NAME,
					MBConst.FUNCTION_CODE_STATUS_TRANSACTION, false));
			return new ResponseEntity<AddTransactionGWResponse>(jsonObject, HttpStatus.OK);

		} catch (RestClientException e) {

			pgLogChannelFunctionService.writeLogChannelFunction(MBConst.CHANNEL_CODE, channelFucntionId, false);
			
			paramsLog = new String[] { e.getMessage() };
			logger.info(commonLogService.createContentLog(MBConst.CHANNEL_CODE, SERVICE_NAME,
					MBConst.FUNCTION_CODE_STATUS_TRANSACTION, false, false, true, paramsLog));
			return commonResponseService.returnChannelBadRequest(e.getMessage());
			
			
		} catch (Exception e) {

			paramsLog = new String[] { e.getMessage() };
			logger.info(commonLogService.createContentLog(MBConst.CHANNEL_CODE, SERVICE_NAME,
					MBConst.FUNCTION_CODE_STATUS_TRANSACTION, false, false, true, paramsLog));
			logger.info(commonLogService.createContentLogStartEndFunction(MBConst.CHANNEL_CODE, SERVICE_NAME,
					MBConst.FUNCTION_CODE_STATUS_TRANSACTION, false));
			return commonResponseService.returnBadGateway();
		}

	}

	private String createTransactionIdFromClientTransactionId(String clientTransactionId) {

		if (StringUtils.length(clientTransactionId) > 12) {
			return StringUtils.upperCase(RandomStringUtils.randomAlphanumeric(12));
		} else {
			return clientTransactionId + StringUtils
					.upperCase(RandomStringUtils.randomAlphanumeric(12 - StringUtils.length(clientTransactionId)));
		}
	}
}
*/