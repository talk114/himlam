package vn.nganluong.naba.channel.vib.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.commons.codec.Charsets;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.w3c.dom.Node;

import com.fasterxml.jackson.databind.ObjectMapper;

import vn.nganluong.naba.channel.vib.dto.PaymentDTO;
import vn.nganluong.naba.channel.vib.dto.RqAccountBalanceObject;
import vn.nganluong.naba.channel.vib.dto.RqERPLinkGetListTransObject;
import vn.nganluong.naba.channel.vib.dto.RqInputTransactionExtObject;
import vn.nganluong.naba.channel.vib.dto.RqTransactionHistoryInqObject;
import vn.nganluong.naba.channel.vib.dto.TransactionExtObj;
import vn.nganluong.naba.channel.vib.dto.VIBConst;
import vn.nganluong.naba.channel.vib.request.AddTransactionVIBRequets;
import vn.nganluong.naba.channel.vib.request.GetAccountBalanceVIBRequets;
import vn.nganluong.naba.channel.vib.request.GetTransactionHistoryVIBRequets;
import vn.nganluong.naba.channel.vib.request.GetTransactionStatusVIBRequets;
import vn.nganluong.naba.channel.vib.request.ValidAccountVIBRequets;
import vn.nganluong.naba.channel.vib.response.GetAccountBalanceGWResponse;
import vn.nganluong.naba.channel.vib.response.GetAccountBalanceResponse;
import vn.nganluong.naba.channel.vib.response.GetTokenResponse;
import vn.nganluong.naba.channel.vib.response.GetTransactionHistoryGWResponse;
import vn.nganluong.naba.channel.vib.response.GetTransactionHistoryResponse;
import vn.nganluong.naba.channel.vib.response.GetTransactionStatusGWResponse;
import vn.nganluong.naba.channel.vib.response.GetTransactionStatusResponse;
import vn.nganluong.naba.channel.vib.response.ListTransactionStatusResponse;
import vn.nganluong.naba.channel.vib.response.ValidAccountGWResponse;
import vn.nganluong.naba.channel.vib.response.ValidAccountVIBResponse;
import vn.nganluong.naba.dto.AddTransactionGWResponse;
import vn.nganluong.naba.dto.LogConst;
import vn.nganluong.naba.dto.PaymentConst;
import vn.nganluong.naba.dto.ResponseJson;
import vn.nganluong.naba.entities.ChannelFunction;
import vn.nganluong.naba.entities.Payment;
import vn.nganluong.naba.entities.PaymentAccount;
import vn.nganluong.naba.service.*;
import vn.nganluong.naba.service.PaymentAccountService;
import vn.nganluong.naba.utils.MyDateUtil;
import vn.nganluong.naba.utils.RequestUtil;

@RestController
@RequestMapping(value = "/vib/ibft")
public class VIBIBFTController {

	private static final Logger logger = LogManager.getLogger(VIBIBFTController.class);
	
	public static final String SERVICE_NAME = "IBFT";

	@Autowired
	private PgLogChannelFunctionService pgLogChannelFunctionService;
	
	@Autowired
	private PaymentAccountService paymentAccountService;
	
	@Autowired
	private ChannelFunctionService channelFunctionService;
	
	@Autowired
	private PaymentService paymentService;
	
	@Autowired
	private CommonLogService commonLogService;
	
	@Autowired
	private CommonResponseService commonResponseService;
	
	public VIBIBFTController() {
		
	}
	
	@GetMapping("/token")
	public String getAccessToken() {
		logger.info(commonLogService.createContentLogStartEndFunction(VIBConst.CHANNEL_CODE, SERVICE_NAME, VIBConst.FUNCTION_CODE_ACCESS_TOKEN, true));

		RestTemplate restTemplate = null;
		try {
			restTemplate = new RestTemplate(createRequestFactory());
		} catch (KeyManagementException | NoSuchAlgorithmException e) {

			pgLogChannelFunctionService.writeLogChannelFunction(VIBConst.CHANNEL_CODE, VIBConst.CHANNEL_FUNCTION_ACCESS_TOKEN_CODE, false);
			
			String[] paramsLog = new String[] { e.getMessage() };
			logger.info(commonLogService.createContentLog(VIBConst.CHANNEL_CODE, SERVICE_NAME, VIBConst.FUNCTION_CODE_ACCESS_TOKEN, false, false, true, paramsLog));
		}
		
		ChannelFunction channelFunction = channelFunctionService.findChannelFunctionByCodeAndChannelCode(VIBConst.CHANNEL_FUNCTION_ACCESS_TOKEN_CODE, VIBConst.CHANNEL_CODE);
		
		if (channelFunction == null) {
			pgLogChannelFunctionService.writeLogChannelFunction(VIBConst.CHANNEL_CODE, VIBConst.CHANNEL_FUNCTION_ACCESS_TOKEN_CODE, false);
			
			String[] paramsLog = new String[] { VIBConst.CHANNEL_NOT_CONFIG_MSG };
			logger.info(commonLogService.createContentLog(VIBConst.CHANNEL_CODE, SERVICE_NAME, VIBConst.FUNCTION_CODE_ACCESS_TOKEN, false, false, true, paramsLog));
			return null;
		}

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.add("Authorization", channelFunction.getAuthKey());

		MultiValueMap<String, String> mapBody = new LinkedMultiValueMap<String, String>();
		mapBody.add("grant_type", "password");
		mapBody.add("username", channelFunction.getUser());
		mapBody.add("password", channelFunction.getPassword());

		HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<MultiValueMap<String, String>>(mapBody,
				headers);

		// post request
		ResponseEntity<GetTokenResponse> response = restTemplate
				.exchange(RequestUtil.createUrlChannelFunction(channelFunction.getHost(), channelFunction.getPort(),
						channelFunction.getUrl()), HttpMethod.POST, entity, GetTokenResponse.class);

		GetTokenResponse getTokenResponse = response.getBody();
		
		if (response.getStatusCode().equals(HttpStatus.OK)) {
			pgLogChannelFunctionService.writeLogChannelFunction(VIBConst.CHANNEL_CODE, VIBConst.CHANNEL_FUNCTION_ACCESS_TOKEN_CODE, true);
			
			String[] paramsLog = new String[] { getTokenResponse.getAccess_token() };
			logger.info(commonLogService.createContentLog(VIBConst.CHANNEL_CODE, SERVICE_NAME, VIBConst.FUNCTION_CODE_ACCESS_TOKEN, true, false, true, paramsLog));
			
		}
		else {
			pgLogChannelFunctionService.writeLogChannelFunction(VIBConst.CHANNEL_CODE, VIBConst.CHANNEL_FUNCTION_ACCESS_TOKEN_CODE, false);
			
			String[] paramsLog = new String[] { "Resonse httpstatus: " +  response.getStatusCode()};
			logger.info(commonLogService.createContentLog(VIBConst.CHANNEL_CODE, SERVICE_NAME, VIBConst.FUNCTION_CODE_ACCESS_TOKEN, false, false, true, paramsLog));
		}
		
		logger.info(commonLogService.createContentLogStartEndFunction(VIBConst.CHANNEL_CODE, SERVICE_NAME, VIBConst.FUNCTION_CODE_ACCESS_TOKEN, false));
		return getTokenResponse.getAccess_token();
	}

	/*@PostMapping(path = "/valid_account", consumes = { MediaType.APPLICATION_FORM_URLENCODED_VALUE }, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	ResponseEntity<?> checkInvalidAccount(ValidAccountVIBRequets accountRequets) throws KeyManagementException, NoSuchAlgorithmException {
		
		logger.info(commonLogService.createContentLogStartEndFunction(VIBConst.CHANNEL_CODE, SERVICE_NAME, VIBConst.FUNCTION_CODE_VALID_ACCOUNT, true));
		String accessToken = getAccessToken();
		
		if (StringUtils.isBlank(accessToken)) {
			logger.info(commonLogService.createContentLogStartEndFunction(VIBConst.CHANNEL_CODE, SERVICE_NAME, VIBConst.FUNCTION_CODE_VALID_ACCOUNT, false));
			return commonResponseService.returnBadGateway();
		}
		
		try {
			String channelFucntionId = VIBConst.CHANNEL_FUNCTION_INVALID_ACCOUNT_VIB_CODE;
			
			if (StringUtils.equals(accountRequets.getAccount_type(), "NAPAS")) {
				channelFucntionId = VIBConst.CHANNEL_FUNCTION_INVALID_ACCOUNT_NAPAS_CODE;
			}
			
			ChannelFunction channelFunction = channelFunctionService
					.findChannelFunctionByCodeAndChannelCode(channelFucntionId, VIBConst.CHANNEL_CODE);
			
			PaymentAccount paymentAccount = paymentAccountService.getPaymentAccountByUserCodeAndChannelId(VIBConst.PG_USER_CODE, channelFunction.getChannel().getId());

			RestTemplate restTemplate = new RestTemplate(createRequestFactory());

			String reqId = RandomStringUtils.randomAlphanumeric(6);

			String dataToSign = paymentAccount.getCif() + "|" + accountRequets.getAccount_no() + "|" + reqId;
			
			if (StringUtils.equals(channelFucntionId, VIBConst.CHANNEL_FUNCTION_INVALID_ACCOUNT_NAPAS_CODE)) {
				dataToSign = paymentAccount.getCif() + "|" + accountRequets.getAccount_no() + "|" + paymentAccount.getSourceAccountNo()
						+ "|" + accountRequets.getBank_id() + "|" + reqId;
			}
			
			
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.add("Authorization", "Bearer " + accessToken);
			headers.add("signeddata", VIBSignature.signSHA1withRSA(dataToSign));
			
			MultiValueMap<String, String> mapParams = new LinkedMultiValueMap<String, String>();
			
			if (StringUtils.equals(channelFucntionId, VIBConst.CHANNEL_FUNCTION_INVALID_ACCOUNT_NAPAS_CODE)) {
				mapParams.add("acctno", accountRequets.getAccount_no());
				mapParams.add("fromacctno", paymentAccount.getSourceAccountNo());
				mapParams.add("bankid", accountRequets.getBank_id());
			}
			
			mapParams.add("cif", paymentAccount.getCif() );
			mapParams.add("reqid", reqId);
			
			if (channelFunction == null) {
				pgLogChannelFunctionService.writeLogChannelFunction(VIBConst.CHANNEL_CODE, channelFucntionId, false);
				
				String[] paramsLog = new String[] { VIBConst.CHANNEL_NOT_CONFIG_MSG };
				logger.info(commonLogService.createContentLog(VIBConst.CHANNEL_CODE, SERVICE_NAME, VIBConst.FUNCTION_CODE_VALID_ACCOUNT, false, false, true, paramsLog));
				return commonResponseService.returnBadGateway();
			}

			HttpEntity<?> entity = new HttpEntity<>(headers);

			String resourceUrl = RequestUtil.createUrlChannelFunction(channelFunction.getHost(), channelFunction.getPort(),
					channelFunction.getUrl());

			UriComponentsBuilder builderUri = UriComponentsBuilder.fromHttpUrl(resourceUrl)
					.pathSegment(accountRequets.getAccount_no()).queryParams(mapParams);
			
			ValidAccountVIBResponse validAccountVIBResponse = null;
			String acctName = StringUtils.EMPTY;
			String acctStatus = StringUtils.EMPTY;
			
			ObjectMapper mapperObj = new ObjectMapper();
			String[] paramsLog = new String[] { mapperObj.writeValueAsString(mapParams) };
			logger.info(commonLogService.createContentLog(VIBConst.CHANNEL_CODE, SERVICE_NAME, VIBConst.FUNCTION_CODE_VALID_ACCOUNT, true, true, false, paramsLog));
			
			try {
				ResponseEntity<ValidAccountVIBResponse> response = restTemplate.exchange(builderUri.toUriString(),
						HttpMethod.GET, entity, ValidAccountVIBResponse.class);

				validAccountVIBResponse = response.getBody();
				
				if (response.getStatusCode().equals(HttpStatus.OK)) {

					if (StringUtils.equals(validAccountVIBResponse.getResult().getStatus_code(), "000000")) {
						
						acctStatus = validAccountVIBResponse.getResult().getData().getStatus();
						
						if (StringUtils.equals(channelFucntionId, VIBConst.CHANNEL_FUNCTION_INVALID_ACCOUNT_NAPAS_CODE)) {
							acctName = validAccountVIBResponse.getResult().getData().getAcct_name();
						}
						else {
							acctName = validAccountVIBResponse.getResult().getData().getAcct_desc();							
						}
					}

					pgLogChannelFunctionService.writeLogChannelFunction(VIBConst.CHANNEL_CODE,
							channelFucntionId, true);
				}
				
				paramsLog = new String[] { mapperObj.writeValueAsString(validAccountVIBResponse) };
				logger.info(commonLogService.createContentLog(VIBConst.CHANNEL_CODE, SERVICE_NAME, VIBConst.FUNCTION_CODE_VALID_ACCOUNT, true, false, true, paramsLog));

			}
			
			catch (HttpClientErrorException e) {

				pgLogChannelFunctionService.writeLogChannelFunction(VIBConst.CHANNEL_CODE,
						channelFucntionId, false);
				
				paramsLog = new String[] { e.getMessage() };
				logger.info(commonLogService.createContentLog(VIBConst.CHANNEL_CODE, SERVICE_NAME, VIBConst.FUNCTION_CODE_VALID_ACCOUNT, false, false,
						true, paramsLog));
				return commonResponseService.returnChannelBadRequest(e.getMessage());
			}
			
			catch (Exception e) {

				pgLogChannelFunctionService.writeLogChannelFunction(VIBConst.CHANNEL_CODE,
						channelFucntionId, false);
				paramsLog = new String[] { e.getMessage() };
				logger.info(commonLogService.createContentLog(VIBConst.CHANNEL_CODE, SERVICE_NAME, VIBConst.FUNCTION_CODE_VALID_ACCOUNT, false, false,
						true, paramsLog));
				return commonResponseService.returnChannelBadRequest(e.getMessage());
			}
			
			if (validAccountVIBResponse == null) {
				pgLogChannelFunctionService.writeLogChannelFunction(VIBConst.CHANNEL_CODE,
						channelFucntionId, false);
				paramsLog = new String[] { "Response is empty" };
				logger.info(commonLogService.createContentLog(VIBConst.CHANNEL_CODE, SERVICE_NAME, VIBConst.FUNCTION_CODE_VALID_ACCOUNT, false, false,
						true, paramsLog));
				logger.info(commonLogService.createContentLogStartEndFunction(VIBConst.CHANNEL_CODE, SERVICE_NAME, VIBConst.FUNCTION_CODE_VALID_ACCOUNT, false));
				return commonResponseService.returnChannelBadRequest("");
			}
			
			ResponseJson prefixResult = commonResponseService.returnGatewayRequestSuccessPrefix();

			ValidAccountGWResponse jsonObject = new ValidAccountGWResponse();
			jsonObject.setError_code(prefixResult.getError_code());
			jsonObject.setMessage(prefixResult.getMessage());
			
			jsonObject.setBank_error_code(validAccountVIBResponse.getResult().getStatus_code());
			jsonObject.setAccount_name(acctName);
			jsonObject.setAccount_status(acctStatus);

			logger.info(commonLogService.createContentLogStartEndFunction(VIBConst.CHANNEL_CODE, SERVICE_NAME, VIBConst.FUNCTION_CODE_VALID_ACCOUNT, false));
			return new ResponseEntity<ValidAccountGWResponse>(jsonObject, HttpStatus.OK);
		} catch (Exception e) {
			
			String[] paramsLog = new String[] { e.getMessage() };
			logger.info(commonLogService.createContentLog(VIBConst.CHANNEL_CODE, SERVICE_NAME, VIBConst.FUNCTION_CODE_VALID_ACCOUNT, false, false,
					true, paramsLog));
			logger.info(commonLogService.createContentLogStartEndFunction(VIBConst.CHANNEL_CODE, SERVICE_NAME, VIBConst.FUNCTION_CODE_VALID_ACCOUNT, false));
			return commonResponseService.returnBadGateway();
		}
		
	}*/

	
/*
	@PostMapping(path = "/get_account_balance", consumes = {
			MediaType.APPLICATION_FORM_URLENCODED_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	ResponseEntity<?> getAccountBalance(GetAccountBalanceVIBRequets accountBalanceRequets)
			throws IOException, SOAPException, KeyManagementException, NoSuchAlgorithmException, XMLStreamException, JAXBException {
		MessageFactory messageFactory = MessageFactory.newInstance();
		
		logger.info(commonLogService.createContentLogStartEndFunction(VIBConst.CHANNEL_CODE, SERVICE_NAME, VIBConst.FUNCTION_CODE_BALANCE_ACCOUNT, true));
		
		ChannelFunction channelFunction = channelFunctionService
				.findChannelFunctionByCodeAndChannelCode(VIBConst.CHANNEL_FUNCTION_BALANCE_ACCOUNT_CODE, VIBConst.CHANNEL_CODE);
		
		PaymentAccount paymentAccount = paymentAccountService.getPaymentAccountByUserCodeAndChannelId(VIBConst.PG_USER_CODE, channelFunction.getChannel().getId());

		String accountNo = accountBalanceRequets.getAccount_no();
		if (StringUtils.isBlank(accountNo)) {
			accountNo = paymentAccount.getSourceAccountNo();
		}
		
		RqAccountBalanceObject rqObject = new RqAccountBalanceObject();
		rqObject.CIF = paymentAccount.getCif();
		rqObject.accountNumber = accountNo;
		rqObject.username = paymentAccount.getUsername();

		String dataToSign = VIBSignature.SHA1AccountBalance(rqObject);
		String signature = VIBSignature.genSignature(dataToSign);

		String body = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\r\n"
				+ "<Envelope xmlns=\"http://schemas.xmlsoap.org/soap/envelope/\">\r\n" + "    <Body>\r\n"
				+ "        <getAccountBalance xmlns=\"http://vn.vib.fastpay.erp\">\r\n"
				+ "            <!-- Optional -->\r\n" + "            <rqObject>\r\n"
				+ "                <CIF xmlns=\"http://object.vn.vib.fastpay.erp/xsd\">"
				+ paymentAccount.getCif() + "</CIF>\r\n"
				+ "                <accountNumber xmlns=\"http://object.vn.vib.fastpay.erp/xsd\">"
				+ accountNo + "</accountNumber>\r\n"
				+ "                <signData xmlns=\"http://object.vn.vib.fastpay.erp/xsd\">" + signature
				+ "</signData>\r\n" + "                <username xmlns=\"http://object.vn.vib.fastpay.erp/xsd\">"
				+ paymentAccount.getUsername() + "</username>\r\n" + "            </rqObject>\r\n"
				+ "        </getAccountBalance>\r\n" + "    </Body>\r\n" + "</Envelope>";
		SOAPMessage soapMessage = messageFactory.createMessage(new MimeHeaders(),
				new ByteArrayInputStream(body.getBytes(Charset.forName("UTF-8"))));
//		SOAPPart soapPart = soapMessage.getSOAPPart();
//      SOAPEnvelope envelope = soapPart.getEnvelope();
//      SOAPBody soapBody = envelope.getBody();
		soapMessage.saveChanges();

		//String endpointUrl = "https://103.10.212.98/erp/services/GetAccountBalanceWS?wsdl";
		String endpointUrl = RequestUtil.createUrlChannelFunction(channelFunction.getHost(), channelFunction.getPort(),
				channelFunction.getUrl());

		SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
		SOAPConnection soapConnection = soapConnectionFactory.createConnection();
		
		String[] paramsLog = new String[] { body };
		logger.info(commonLogService.createContentLog(VIBConst.CHANNEL_CODE, SERVICE_NAME, VIBConst.FUNCTION_CODE_BALANCE_ACCOUNT, true, true,
				false, paramsLog));

		try {
			disableSslVerification();
			SOAPMessage soapResponse = soapConnection.call(soapMessage, endpointUrl);
			// soapResponse.writeTo(System.out); // Print response
			
			
//			SOAPBody soapBody = soapResponse.getSOAPBody();
//			Node getBalanceRes = soapBody.getFirstChild();
//			String curCode = VIBSOAPUtil.getValue(getBalanceRes, "return.acctRec.curCode");
//			String ballType = VIBSOAPUtil.getValue(getBalanceRes, "return.acctRec.listAcct.ballType");
//			String curAmt = VIBSOAPUtil.getValue(getBalanceRes, "return.acctRec.listAcct.curAmt");
			
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			soapResponse.writeTo(out);
			String strMsg = new String(out.toByteArray());
			XMLInputFactory xif = XMLInputFactory.newFactory();
			InputStream is = new ByteArrayInputStream( strMsg.getBytes(Charsets.UTF_8) );
		    InputStreamReader reader = new InputStreamReader(is);
            XMLStreamReader xsr = xif.createXMLStreamReader(reader);
            xsr.nextTag(); // Advance to Envelope tag
            xsr.nextTag(); // Advance to Body tag
        	xsr.nextTag(); //getAccountBalanceResponse
        	xsr.nextTag(); //return
//        	xsr.nextTag(); //acctRec
            // System.out.println(xsr.getLocalName());


            JAXBContext jc = JAXBContext.newInstance(GetAccountBalanceResponse.class);
            Unmarshaller unmarshaller = jc.createUnmarshaller();
            GetAccountBalanceResponse accountBalanceResponse = unmarshaller.unmarshal(xsr, GetAccountBalanceResponse.class).getValue(); // JAXBElement
            
            GetAccountBalanceGWResponse jsonObject = new GetAccountBalanceGWResponse();
            jsonObject.setBank_error_code(accountBalanceResponse.getErrorObject().getErrorCode());
			jsonObject.setBank_message(accountBalanceResponse.getErrorObject().getMessage());
			
            if (accountBalanceResponse.getErrorObject().getErrorCode().equals("000000")) {
            	// GetAccountBalanceGWResponse jsonObject = new GetAccountBalanceGWResponse();
            	
            	ResponseJson prefixResult = commonResponseService.returnGatewayRequestSuccessPrefix();
            	jsonObject.setError_code(prefixResult.getError_code());
    			jsonObject.setMessage(prefixResult.getMessage());
    			jsonObject.setListBalance(accountBalanceResponse.getAcctRec().getListAcct());
    			
    			pgLogChannelFunctionService.writeLogChannelFunction(VIBConst.CHANNEL_CODE, VIBConst.CHANNEL_FUNCTION_BALANCE_ACCOUNT_CODE, true);
    			
    			paramsLog = new String[] { strMsg };
				logger.info(commonLogService.createContentLog(VIBConst.CHANNEL_CODE, SERVICE_NAME, VIBConst.FUNCTION_CODE_BALANCE_ACCOUNT, true, false,
						true, paramsLog));
    			
            }
            else {


            	paramsLog = new String[] { strMsg };
				logger.info(commonLogService.createContentLog(VIBConst.CHANNEL_CODE, SERVICE_NAME, VIBConst.FUNCTION_CODE_BALANCE_ACCOUNT, false, false,
						true, paramsLog));
            	
            	ResponseJson prefixResult = commonResponseService.returnChannelBadRequestPrefix();
				jsonObject.setError_code(prefixResult.getError_code());
				jsonObject.setMessage(prefixResult.getMessage());

    			pgLogChannelFunctionService.writeLogChannelFunction(VIBConst.CHANNEL_CODE, VIBConst.CHANNEL_FUNCTION_BALANCE_ACCOUNT_CODE, false);
    			// return returnChannelBadRequest("Response channel code is not success");
            }
            
            logger.info(commonLogService.createContentLogStartEndFunction(VIBConst.CHANNEL_CODE, SERVICE_NAME, VIBConst.FUNCTION_CODE_BALANCE_ACCOUNT, false));
			return new ResponseEntity<GetAccountBalanceGWResponse>(jsonObject, HttpStatus.OK);

		} catch (ConnectException e) {
			pgLogChannelFunctionService.writeLogChannelFunction(VIBConst.CHANNEL_CODE, VIBConst.CHANNEL_FUNCTION_BALANCE_ACCOUNT_CODE, false);
			
			paramsLog = new String[] { e.getMessage() };
			logger.info(commonLogService.createContentLog(VIBConst.CHANNEL_CODE, SERVICE_NAME, VIBConst.FUNCTION_CODE_BALANCE_ACCOUNT, false, false,
					true, paramsLog));
			logger.info(commonLogService.createContentLogStartEndFunction(VIBConst.CHANNEL_CODE, SERVICE_NAME, VIBConst.FUNCTION_CODE_BALANCE_ACCOUNT, false));
			return commonResponseService.returnChannelTimeout();
			
		} catch (SOAPException e) {
			pgLogChannelFunctionService.writeLogChannelFunction(VIBConst.CHANNEL_CODE, VIBConst.CHANNEL_FUNCTION_BALANCE_ACCOUNT_CODE, false);
			
			paramsLog = new String[] { e.getMessage() };
			logger.info(commonLogService.createContentLog(VIBConst.CHANNEL_CODE, SERVICE_NAME, VIBConst.FUNCTION_CODE_BALANCE_ACCOUNT, false, false,
					true, paramsLog));
			logger.info(commonLogService.createContentLogStartEndFunction(VIBConst.CHANNEL_CODE, SERVICE_NAME, VIBConst.FUNCTION_CODE_BALANCE_ACCOUNT, false));
			
			return commonResponseService.returnChannelBadRequest(e.getMessage());
		}
		
	}
	*/
	
	@PostMapping(path = "/add_transaction", consumes = {
			MediaType.APPLICATION_FORM_URLENCODED_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	ResponseEntity<?> addTransaction(AddTransactionVIBRequets transactionInfo)
			throws IOException, SOAPException, KeyManagementException, NoSuchAlgorithmException {
		MessageFactory messageFactory = MessageFactory.newInstance();

		logger.info(commonLogService.createContentLogStartEndFunction(VIBConst.CHANNEL_CODE, SERVICE_NAME, VIBConst.FUNCTION_CODE_ADD_TRANSACTION, true));
		
		if (StringUtils.isBlank(transactionInfo.getClient_transaction_id())) {
			String[] paramsLog = new String[] { "Merchant transaction is empty" };
			logger.info(commonLogService.createContentLog(VIBConst.CHANNEL_CODE, SERVICE_NAME, VIBConst.FUNCTION_CODE_ADD_TRANSACTION, false, false,
					true, paramsLog));
			
			logger.info(commonLogService.createContentLogStartEndFunction(VIBConst.CHANNEL_CODE, SERVICE_NAME, VIBConst.FUNCTION_CODE_ADD_TRANSACTION, false));
			return commonResponseService.returnBadRequets_TransactionEmpty();
		}
		else {
			Payment paymentTocheckExist = paymentService.findByMerchantTransactionId(transactionInfo.getClient_transaction_id());	
			if (paymentTocheckExist != null) {

				String[] paramsLog = new String[] { "Merchant transaction id (trace id) already exist (" + transactionInfo.getClient_transaction_id() +")" };
				logger.info(commonLogService.createContentLog(VIBConst.CHANNEL_CODE, SERVICE_NAME, VIBConst.FUNCTION_CODE_ADD_TRANSACTION, false, false,
						true, paramsLog));
				
				logger.info(commonLogService.createContentLogStartEndFunction(VIBConst.CHANNEL_CODE, SERVICE_NAME, VIBConst.FUNCTION_CODE_ADD_TRANSACTION, false));
				return commonResponseService.returnBadRequets_TransactionExist();
			}
		}
		
		String channel_type = "ERP";
		String transaction_type = "SINGLE";
		
		ChannelFunction channelFunction = channelFunctionService
				.findChannelFunctionByCodeAndChannelCode(VIBConst.CHANNEL_FUNCTION_ADD_TRANSACTION, VIBConst.CHANNEL_CODE);

		PaymentAccount paymentAccount = paymentAccountService.getPaymentAccountByUserCodeAndChannelId(VIBConst.PG_USER_CODE, channelFunction.getChannel().getId());
		
		if (StringUtils.isBlank(transactionInfo.getFrom_account())) {
			transactionInfo.setFrom_account(paymentAccount.getSourceAccountNo());
		}

		
		RqInputTransactionExtObject rqObject = new RqInputTransactionExtObject();
		rqObject.Amount = transactionInfo.getAmount();
		rqObject.Ccy = VIBConst.CCY;
		rqObject.Channel_type = channel_type;
		rqObject.Client_no = paymentAccount.getCif();
		rqObject.Narrative = transactionInfo.getNarrative();
		rqObject.ServiceID = transactionInfo.getService_id();
		rqObject.Trans_type = transaction_type;
		rqObject.User_id = paymentAccount.getUsername();
		List<TransactionExtObj> transactionExtObjs = new ArrayList<TransactionExtObj>();
		TransactionExtObj tranObj = new  TransactionExtObj();
		
		
		tranObj.amount = transactionInfo.getAmount();
		tranObj.ccy = VIBConst.CCY;
		tranObj.ben_bankid = transactionInfo.getBen_bank_id();
		tranObj.toacct = transactionInfo.getTo_account();
		transactionExtObjs.add(tranObj);
		rqObject.TransList = transactionExtObjs;
		
		String dataToSign = VIBSignature.SHA1AddTransaction(rqObject);
		String signature  = VIBSignature.genSignature(dataToSign);

		String body = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + 
				"<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:vn=\"http://vn.vib.fastpay.erp\" xmlns:xsd=\"http://object.vn.vib.fastpay.erp/xsd\">\r\n" + 
				"   <soapenv:Header />\r\n" + 
				"   <soapenv:Body>\r\n" + 
				"      <vn:makeTransaction>\r\n" + 
				"         <vn:rqObject>\r\n" + 
				"            <xsd:amount>" + transactionInfo.getAmount()+ "</xsd:amount>\r\n" + 
				"            <xsd:ccy>" + VIBConst.CCY + "</xsd:ccy>\r\n" + 
				"            <xsd:chanel_type>" + rqObject.Channel_type + "</xsd:chanel_type>\r\n" + 
				"            <xsd:checkSum />\r\n" + 
				"            <xsd:client_no>" + rqObject.Client_no + "</xsd:client_no>\r\n" + 
				"            <xsd:narrative>" + rqObject.Narrative+ "</xsd:narrative>\r\n" + 
				"            <xsd:serviceID>" + rqObject.ServiceID + "</xsd:serviceID>\r\n" + 
				"            <xsd:signData>" + signature + "</xsd:signData>\r\n" + 
				"            <xsd:transList>\r\n" + 
				"               <xsd:acctname>" + StringUtils.trimToEmpty(transactionInfo.getAccount_name()) + "</xsd:acctname>\r\n" + 
				"               <xsd:amount>" + transactionInfo.getAmount()+ "</xsd:amount>\r\n" + 
				"               <xsd:ben_bankcity />\r\n" + 
				"               <xsd:ben_bankcountry />\r\n" + 
				"               <xsd:ben_bankid>" + StringUtils.trimToEmpty(transactionInfo.getBen_bank_id()) + "</xsd:ben_bankid>\r\n" + 
				"               <xsd:ben_bankname>" + StringUtils.trimToEmpty(transactionInfo.getBen_bank_name()) + "</xsd:ben_bankname>\r\n" + 
				"               <xsd:ben_branchcode />\r\n" + 
				"               <xsd:ben_branchname />\r\n" + 
				"               <xsd:benadd />\r\n" + 
				"               <xsd:bencity />\r\n" + 
				"               <xsd:bencountry />\r\n" + 
				"               <xsd:benname>" + StringUtils.trimToEmpty(transactionInfo.getBen_name()) + "</xsd:benname>\r\n" + 
				"               <xsd:billno />\r\n" + 
				"               <xsd:ccy>" + VIBConst.CCY + "</xsd:ccy>\r\n" + 
				"               <xsd:clientTransID>" + transactionInfo.getClient_transaction_id() + "</xsd:clientTransID>\r\n" + 
				"               <xsd:fee_amount />\r\n" + 
				"               <xsd:feeside>" + transactionInfo.getFeeside() + "</xsd:feeside>\r\n" + 
				"               <xsd:fromacct>" + transactionInfo.getFrom_account() + "</xsd:fromacct>\r\n" + 
				"               <xsd:inter_bankcity />\r\n" + 
				"               <xsd:inter_bankcountry />\r\n" + 
				"               <xsd:inter_bankname />\r\n" + 
				"               <xsd:inter_branchname />\r\n" + 
				"               <xsd:interbankid />\r\n" + 
				"               <xsd:narrative>" + rqObject.Narrative+ "</xsd:narrative>\r\n" + 
				"               <xsd:partnerid />\r\n" + 
				"               <xsd:response_code>000000</xsd:response_code>\r\n" + 
				"               <xsd:toacct>" + tranObj.toacct + "</xsd:toacct>\r\n" + 
				"               <xsd:transcode />\r\n" + 
				"               <xsd:transnumber />\r\n" + 
				"               <xsd:transtatus />\r\n" + 
				"               <xsd:transtatus_desc />\r\n" + 
				"            </xsd:transList>\r\n" + 
				"            <xsd:trans_type>" + rqObject.Trans_type + "</xsd:trans_type>\r\n" + 
				"            <xsd:user_id>" + rqObject.User_id + "</xsd:user_id>\r\n" + 
				"         </vn:rqObject>\r\n" + 
				"      </vn:makeTransaction>\r\n" + 
				"   </soapenv:Body>\r\n" + 
				"</soapenv:Envelope>";
		SOAPMessage soapMessage = messageFactory.createMessage(new MimeHeaders(),
				new ByteArrayInputStream(body.getBytes(Charset.forName("UTF-8"))));
		soapMessage.saveChanges();

		// String endpointUrl = "https://103.10.212.98/erp/services/AddTransactionWS?wsdl";
		String endpointUrl = RequestUtil.createUrlChannelFunction(channelFunction.getHost(), channelFunction.getPort(),
				channelFunction.getUrl());

		SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
		SOAPConnection soapConnection = soapConnectionFactory.createConnection();
		
		String[] paramsLog = new String[] { body };
		logger.info(commonLogService.createContentLog(VIBConst.CHANNEL_CODE, SERVICE_NAME, VIBConst.FUNCTION_CODE_ADD_TRANSACTION, true, true,
				false, paramsLog));
		PaymentDTO paymentDTO = new PaymentDTO();
		try {
			
			// Create payment before call api into channel
			// Add payment to database:
			
			paymentDTO.setChannelId(channelFunction.getChannel().getId());
			paymentDTO.setAmount(transactionInfo.getAmount());
			paymentDTO.setMerchantTransactionId(transactionInfo.getClient_transaction_id());
			paymentDTO.setAccountNo(transactionInfo.getTo_account());
			paymentDTO.setRawRequest(body);
			paymentDTO.setDescription(transactionInfo.getNarrative());
			
			if (StringUtils.contains(transactionInfo.getService_id(), VIBConst.EnumTransactionType.SMLACCT.name())) {
				paymentDTO.setChannelTransactionType(VIBConst.EnumTransactionType.SMLACCT.typeName());
			}
			else if (StringUtils.contains(transactionInfo.getService_id(), VIBConst.EnumTransactionType.VIBA.name())) {
				paymentDTO.setChannelTransactionType(VIBConst.EnumTransactionType.VIBA.typeName());
			}
			
			paymentDTO.setSourceAccount(transactionInfo.getFrom_account());
			
			paymentService.createPayment(paymentDTO);

			// Call api
			disableSslVerification();
			SOAPMessage soapResponse = soapConnection.call(soapMessage, endpointUrl);
			// soapResponse.writeTo(System.out); // Print response
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			soapResponse.writeTo(output);
			String soapMessageContent = new String(output.toByteArray());
			
			SOAPBody soapBody = soapResponse.getSOAPBody();
			Node addTransRes = soapBody.getFirstChild();
			String curCode = VIBSOAPUtil.getValue(addTransRes, "return.errorObject.errorCode");
			String message = VIBSOAPUtil.getValue(addTransRes, "return.errorObject.message");
			String transactionId = VIBSOAPUtil.getValue(addTransRes, "return.o_transid");
			
			AddTransactionGWResponse jsonObject = new AddTransactionGWResponse();
			jsonObject.setBank_message(message);
			jsonObject.setBank_error_code(curCode);
			jsonObject.setTransaction_id(transactionId);
			
			paymentDTO.setChannelTransactionId(transactionId);
			paymentDTO.setRawResponse(LogConst.LOG_CONTENT_PREFIX_ADD_TRANSACTION + soapMessageContent);
			
			
			if (StringUtils.equals(curCode, "MT0000")) {
				
				ResponseJson prefixResult = commonResponseService.returnGatewayRequestSuccessPrefix();
				jsonObject.setError_code(prefixResult.getError_code());
				jsonObject.setMessage(prefixResult.getMessage());
				pgLogChannelFunctionService.writeLogChannelFunction(VIBConst.CHANNEL_CODE, VIBConst.CHANNEL_FUNCTION_ADD_TRANSACTION, true);
				paymentDTO.setPgTransactionStatus(PaymentConst.EnumBankStatus.SUCCEEDED.code());
				
				paramsLog = new String[] { soapMessageContent };
				logger.info(commonLogService.createContentLog(VIBConst.CHANNEL_CODE, SERVICE_NAME, VIBConst.FUNCTION_CODE_ADD_TRANSACTION, true, false,
						true, paramsLog));
			}
			else {
				
				paymentDTO.setPgTransactionStatus(PaymentConst.EnumBankStatus.FAILED.code());
				paymentDTO.setChannelTransactionStatus(PaymentConst.EnumBankStatus.FAILED.code());
				ResponseJson prefixResult = commonResponseService.returnChannelBadRequestPrefix();
				jsonObject.setError_code(prefixResult.getError_code());
				jsonObject.setMessage(prefixResult.getMessage());
				
				pgLogChannelFunctionService.writeLogChannelFunction(VIBConst.CHANNEL_CODE, VIBConst.CHANNEL_FUNCTION_ADD_TRANSACTION, false);
				
				paramsLog = new String[] { soapMessageContent };
				logger.info(commonLogService.createContentLog(VIBConst.CHANNEL_CODE, SERVICE_NAME, VIBConst.FUNCTION_CODE_ADD_TRANSACTION, false, false,
						true, paramsLog));
			}
			
			paymentService.updateTransactionStatusAfterCreatedPayment(paymentDTO);

			logger.info(commonLogService.createContentLogStartEndFunction(VIBConst.CHANNEL_CODE, SERVICE_NAME, VIBConst.FUNCTION_CODE_ADD_TRANSACTION, false));

			return new ResponseEntity<AddTransactionGWResponse>(jsonObject, HttpStatus.OK);

		} catch (SOAPException e) {
			
			paramsLog = new String[] { e.getMessage() };
			logger.info(commonLogService.createContentLog(VIBConst.CHANNEL_CODE, SERVICE_NAME, VIBConst.FUNCTION_CODE_ADD_TRANSACTION, false, false,
					true, paramsLog));
			
			pgLogChannelFunctionService.writeLogChannelFunction(VIBConst.CHANNEL_CODE, VIBConst.CHANNEL_FUNCTION_ADD_TRANSACTION, false);
			
			paymentDTO.setPgTransactionStatus(PaymentConst.EnumBankStatus.FAILED.code());
			paymentDTO.setChannelTransactionStatus(PaymentConst.EnumBankStatus.FAILED.code());
			paymentService.updateTransactionStatusAfterCreatedPayment(paymentDTO);
			
			logger.info(commonLogService.createContentLogStartEndFunction(VIBConst.CHANNEL_CODE, SERVICE_NAME, VIBConst.FUNCTION_CODE_ADD_TRANSACTION, false));
			
			return commonResponseService.returnChannelBadRequest(e.getMessage());

		}
		
		catch (Exception e) {
			paramsLog = new String[] { e.getMessage() };
			logger.info(commonLogService.createContentLog(VIBConst.CHANNEL_CODE, SERVICE_NAME, VIBConst.FUNCTION_CODE_ADD_TRANSACTION, false, false,
					true, paramsLog));
			logger.info(commonLogService.createContentLogStartEndFunction(VIBConst.CHANNEL_CODE, SERVICE_NAME, VIBConst.FUNCTION_CODE_ADD_TRANSACTION, false));
			
			return commonResponseService.returnBadGateway();
		}
	}
	
	@PostMapping(path = "/get_status_transaction", consumes = {
			MediaType.APPLICATION_FORM_URLENCODED_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	ResponseEntity<?> getTransactionStatus(GetTransactionStatusVIBRequets transactionStatusRequets)
			throws IOException, SOAPException, KeyManagementException, NoSuchAlgorithmException, XMLStreamException, JAXBException {
		
		logger.info(commonLogService.createContentLogStartEndFunction(VIBConst.CHANNEL_CODE, SERVICE_NAME, VIBConst.FUNCTION_CODE_STATUS_TRANSACTION, true));
		
		ChannelFunction channelFunction = channelFunctionService
				.findChannelFunctionByCodeAndChannelCode(VIBConst.CHANNEL_FUNCTION_STATUS_TRANSACTION, VIBConst.CHANNEL_CODE);
		
		PaymentAccount paymentAccount = paymentAccountService.getPaymentAccountByUserCodeAndChannelId(VIBConst.PG_USER_CODE, channelFunction.getChannel().getId());
		
		RqERPLinkGetListTransObject rqObject = new RqERPLinkGetListTransObject();
		
		if (StringUtils.isAllBlank(transactionStatusRequets.getFrom_date(), transactionStatusRequets.getTo_date())) {
			String date = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
			String tomorrow = new SimpleDateFormat("dd/MM/yyyy").format(MyDateUtil.getTomorow());
			transactionStatusRequets.setFrom_date(date);
			transactionStatusRequets.setTo_date(tomorrow);
		}
		
		rqObject.i_client_no = paymentAccount.getCif();
		rqObject.i_from_date = transactionStatusRequets.getFrom_date();
		rqObject.i_page_num = "1";
		rqObject.i_page_size = "500";
		rqObject.i_service_type = transactionStatusRequets.getService_type();
		rqObject.i_to_date = transactionStatusRequets.getTo_date();
		rqObject.i_trans_id = StringUtils.trimToEmpty(transactionStatusRequets.getClient_transaction_id());
		rqObject.i_trans_type = StringUtils.trimToEmpty(transactionStatusRequets.getTransaction_type());
		rqObject.i_userid = paymentAccount.getUsername();

		String dataToSign = VIBSignature.SHA1TransactionStatus(rqObject);
		String signature  = VIBSignature.genSignature(dataToSign);

		String body = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:vn=\"http://vn.vib.fastpay.erp\" xmlns:xsd=\"http://object.vn.vib.fastpay.erp/xsd\">\r\n" + 
				"   <soapenv:Header/>\r\n" + 
				"   <soapenv:Body>\r\n" + 
				"      <vn:queryTransactionStatus>\r\n" + 
				"         <vn:rqObject>\r\n" + 
				"            <xsd:checkSum></xsd:checkSum>\r\n" + 
				"            <xsd:i_client_no>" + rqObject.i_client_no + "</xsd:i_client_no>\r\n" + 
				"            <xsd:i_from_date>" + rqObject.i_from_date + "</xsd:i_from_date>\r\n" + 
				"            <xsd:i_page_num>" + rqObject.i_page_num + "</xsd:i_page_num>\r\n" + 
				"            <xsd:i_page_size>" + rqObject.i_page_size + "</xsd:i_page_size>\r\n" + 
				"            <xsd:i_service_type>" + rqObject.i_service_type + "</xsd:i_service_type>\r\n" + 
				"            <xsd:i_to_date>" + rqObject.i_to_date + "</xsd:i_to_date>\r\n" + 
				"            <xsd:i_trans_id>" + rqObject.i_trans_id+ "</xsd:i_trans_id>\r\n" + 
				"            <xsd:i_trans_type>" + rqObject.i_trans_type + "</xsd:i_trans_type>\r\n" + 
				"            <xsd:i_userid>" + rqObject.i_userid + "</xsd:i_userid>\r\n" + 
				"            <xsd:signData>" + signature + "</xsd:signData>\r\n" + 
				"         </vn:rqObject>\r\n" + 
				"      </vn:queryTransactionStatus>\r\n" + 
				"   </soapenv:Body>\r\n" + 
				"</soapenv:Envelope>";
		
		MessageFactory messageFactory = MessageFactory.newInstance();
		SOAPMessage soapMessage = messageFactory.createMessage(new MimeHeaders(),
				new ByteArrayInputStream(body.getBytes(Charset.forName("UTF-8"))));

		soapMessage.saveChanges();

		// String endpointUrl = "https://103.10.212.98/erp/services/QueryTransactionStatusWS?wsdl";
		String endpointUrl = RequestUtil.createUrlChannelFunction(channelFunction.getHost(), channelFunction.getPort(),
				channelFunction.getUrl());

		SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
		SOAPConnection soapConnection = soapConnectionFactory.createConnection();
		
		String[] paramsLog = new String[] { body };
		logger.info(commonLogService.createContentLog(VIBConst.CHANNEL_CODE, SERVICE_NAME, VIBConst.FUNCTION_CODE_STATUS_TRANSACTION, true, true,
				false, paramsLog));

		try {
			disableSslVerification();
			SOAPMessage soapResponse = soapConnection.call(soapMessage, endpointUrl);
			
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			soapResponse.writeTo(out);
			
			String soapMessageContent = new String(out.toByteArray());
			
			String strMsg = new String(out.toByteArray());
			XMLInputFactory xif = XMLInputFactory.newFactory();
			InputStream is = new ByteArrayInputStream( strMsg.getBytes(Charsets.UTF_8) );
		    InputStreamReader reader = new InputStreamReader(is);
            XMLStreamReader xsr = xif.createXMLStreamReader(reader);
            xsr.nextTag(); // Advance to Envelope tag
            xsr.nextTag(); // Advance to Body tag
        	xsr.nextTag(); //getAccountBalanceResponse
        	xsr.nextTag(); //return
//        	xsr.nextTag(); //acctRec
            // System.out.println(xsr.getLocalName());


            JAXBContext jc = JAXBContext.newInstance(GetTransactionStatusResponse.class);
            Unmarshaller unmarshaller = jc.createUnmarshaller();
            GetTransactionStatusResponse transactionStatusResponse = unmarshaller.unmarshal(xsr, GetTransactionStatusResponse.class).getValue(); // JAXBElement
            GetTransactionStatusGWResponse jsonObject = new GetTransactionStatusGWResponse();
            jsonObject.setBank_error_code(transactionStatusResponse.getErrorObject().getErrorCode());
            jsonObject.setBank_message(transactionStatusResponse.getErrorObject().getMessage());
            jsonObject.setListTrans(transactionStatusResponse.getListTrans());
            
            pgLogChannelFunctionService.writeLogChannelFunction(VIBConst.CHANNEL_CODE, VIBConst.CHANNEL_FUNCTION_STATUS_TRANSACTION, true);
            if (StringUtils.equals(transactionStatusResponse.getErrorObject().getErrorCode(), "ITS000")) {
            	ResponseJson prefixResult = commonResponseService.returnGatewayRequestSuccessPrefix();
				jsonObject.setError_code(prefixResult.getError_code());
				jsonObject.setMessage(prefixResult.getMessage());			
				
				if (jsonObject.getListTrans() != null && jsonObject.getListTrans().size() > 0) {
	            	for (ListTransactionStatusResponse transactionStatus : jsonObject.getListTrans()) {
	            		
	            		PaymentDTO paymentDTO = new PaymentDTO();
	    				paymentDTO.setChannelId(channelFunction.getChannel().getId());
	    				paymentDTO.setAmount(transactionStatus.getAmount());
	    				
	    				if (StringUtils.isNotBlank(transactionStatus.getBank_status())) {
	    					paymentDTO.setChannelTransactionStatus(PaymentConst.EnumBankStatus.valueOf(transactionStatus.getBank_status()).code());
	    				}
	    				else {
	    					paymentDTO.setChannelTransactionStatus(PaymentConst.EnumBankStatus.EMPTY.code());
	    				}
	    				
	    				paymentDTO.setMerchantTransactionId(transactionStatus.getClient_transaction_id());
	    				paymentDTO.setChannelTransactionId(transactionStatus.getTransaction_id());
	    				
	    				paymentDTO.setRawResponse(LogConst.LOG_CONTENT_PREFIX_STATUS_TRANSACTION + soapMessageContent);
	            		
						paymentService.updateChannelTransactionStatusPayment(paymentDTO);
					}
	            }
				
				paramsLog = new String[] { strMsg };
				logger.info(commonLogService.createContentLog(VIBConst.CHANNEL_CODE, SERVICE_NAME, VIBConst.FUNCTION_CODE_STATUS_TRANSACTION, true, false,
						true, paramsLog));
				
            }
            else {
            	ResponseJson prefixResult = commonResponseService.returnChannelBadRequestPrefix();
				jsonObject.setError_code(prefixResult.getError_code());
				jsonObject.setMessage(prefixResult.getMessage());
				
				paramsLog = new String[] { strMsg };
				logger.info(commonLogService.createContentLog(VIBConst.CHANNEL_CODE, SERVICE_NAME, VIBConst.FUNCTION_CODE_STATUS_TRANSACTION, false, false,
						true, paramsLog));
            }
            
            logger.info(commonLogService.createContentLogStartEndFunction(VIBConst.CHANNEL_CODE, SERVICE_NAME, VIBConst.FUNCTION_CODE_STATUS_TRANSACTION, false));
            
			return new ResponseEntity<GetTransactionStatusGWResponse>(jsonObject, HttpStatus.OK);

		} catch (SOAPException e) {
			pgLogChannelFunctionService.writeLogChannelFunction(VIBConst.CHANNEL_CODE, VIBConst.CHANNEL_FUNCTION_STATUS_TRANSACTION, false);
			
			paramsLog = new String[] { e.getMessage() };
			logger.info(commonLogService.createContentLog(VIBConst.CHANNEL_CODE, SERVICE_NAME, VIBConst.FUNCTION_CODE_STATUS_TRANSACTION, false, false,
					true, paramsLog));
			logger.info(commonLogService.createContentLogStartEndFunction(VIBConst.CHANNEL_CODE, SERVICE_NAME, VIBConst.FUNCTION_CODE_STATUS_TRANSACTION, false));
			
			return commonResponseService.returnChannelBadRequest(e.getMessage());
		}
		catch (Exception e) {

			paramsLog = new String[] { e.getMessage() };
			logger.info(commonLogService.createContentLog(VIBConst.CHANNEL_CODE, SERVICE_NAME, VIBConst.FUNCTION_CODE_STATUS_TRANSACTION, false, false,
					true, paramsLog));
			logger.info(commonLogService.createContentLogStartEndFunction(VIBConst.CHANNEL_CODE, SERVICE_NAME, VIBConst.FUNCTION_CODE_STATUS_TRANSACTION, false));
			
			return commonResponseService.returnBadGateway();
		}
	}
	
	@PostMapping(path = "/get_history_transaction", consumes = {
			MediaType.APPLICATION_FORM_URLENCODED_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	ResponseEntity<?> getHistoryTransaction(GetTransactionHistoryVIBRequets transactionHistoryRequets)
			throws IOException, SOAPException, KeyManagementException, NoSuchAlgorithmException, XMLStreamException, JAXBException {
		
		logger.info(commonLogService.createContentLogStartEndFunction(VIBConst.CHANNEL_CODE, SERVICE_NAME, VIBConst.FUNCTION_CODE_STATUS_TRANSACTION, true));
		
		ChannelFunction channelFunction = channelFunctionService
				.findChannelFunctionByCodeAndChannelCode(VIBConst.CHANNEL_FUNCTION_HISTORY_TRANSACTION, VIBConst.CHANNEL_CODE);
		
		PaymentAccount paymentAccount = paymentAccountService.getPaymentAccountByUserCodeAndChannelId(VIBConst.PG_USER_CODE, channelFunction.getChannel().getId());

		if (StringUtils.isAllBlank(transactionHistoryRequets.getFrom_date(), transactionHistoryRequets.getTo_date())) {
			String date = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
			String tomorrow = new SimpleDateFormat("dd/MM/yyyy").format(MyDateUtil.getTomorow());
			transactionHistoryRequets.setFrom_date(date);
			transactionHistoryRequets.setTo_date(tomorrow);
		}
		
		if (StringUtils.isBlank(transactionHistoryRequets.getC_d())){
			transactionHistoryRequets.setC_d(StringUtils.EMPTY);
		}
		
		RqTransactionHistoryInqObject rqObject = new RqTransactionHistoryInqObject();
		rqObject.client_no = paymentAccount.getCif();
		rqObject.i_from_date = transactionHistoryRequets.getFrom_date();
		rqObject.i_account_no = transactionHistoryRequets.getAccount_no();
		rqObject.i_page_num = transactionHistoryRequets.getPage_num();
		rqObject.i_page_size = transactionHistoryRequets.getPage_size();
		rqObject.i_cr_dr = transactionHistoryRequets.getC_d(); // D, C or empty
		rqObject.RequestId = StringUtils.EMPTY;
		rqObject.i_to_date = transactionHistoryRequets.getTo_date();
		rqObject.client_id = paymentAccount.getUsername();

		String dataToSign = VIBSignature.SHA1TransactionHistory(rqObject);
		String signature  = VIBSignature.genSignature(dataToSign);

		String body = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:vn=\"http://vn.vib.fastpay.erp\" xmlns:xsd=\"http://object.vn.vib.fastpay.erp/xsd\">\r\n" + 
				"   <soapenv:Header/>\r\n" + 
				"   <soapenv:Body>\r\n" + 
				"      <vn:getTransHis>\r\n" + 
				"         <vn:rqObj>\r\n" + 
				"            <xsd:checkSum></xsd:checkSum>\r\n" + 
				"            <xsd:client_id>" + rqObject.client_id + "</xsd:client_id>\r\n" + 
				"            <xsd:client_no>" + rqObject.client_no + "</xsd:client_no>\r\n" + 
				"            <xsd:i_account_no>" + rqObject.i_account_no + "</xsd:i_account_no>\r\n" + 
				"            <xsd:i_cr_dr>" + rqObject.i_cr_dr + "</xsd:i_cr_dr>\r\n" + 
				"            <xsd:i_from_date>" + rqObject.i_from_date + "</xsd:i_from_date>\r\n" + 
				"            <xsd:i_page_num>" + rqObject.i_page_num + "</xsd:i_page_num>\r\n" + 
				"            <xsd:i_page_size>" + rqObject.i_page_size + "</xsd:i_page_size>\r\n" + 
				"            <xsd:i_to_date>" + rqObject.i_to_date + "</xsd:i_to_date>\r\n" + 
				"            <xsd:requestID></xsd:requestID>\r\n" + 
				"            <xsd:signData>" + signature + "</xsd:signData>\r\n" + 
				"         </vn:rqObj>\r\n" + 
				"      </vn:getTransHis>\r\n" + 
				"   </soapenv:Body>\r\n" + 
				"</soapenv:Envelope>";
		
		MessageFactory messageFactory = MessageFactory.newInstance();
		SOAPMessage soapMessage = messageFactory.createMessage(new MimeHeaders(),
				new ByteArrayInputStream(body.getBytes(Charset.forName("UTF-8"))));

		soapMessage.saveChanges();

		String endpointUrl = RequestUtil.createUrlChannelFunction(channelFunction.getHost(), channelFunction.getPort(),
				channelFunction.getUrl());

		SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
		SOAPConnection soapConnection = soapConnectionFactory.createConnection();
		
		String[] paramsLog = new String[] { body };
		logger.info(commonLogService.createContentLog(VIBConst.CHANNEL_CODE, SERVICE_NAME, VIBConst.FUNCTION_CODE_HISTORY_TRANSACTION, true, true,
				false, paramsLog));

		try {
			disableSslVerification();
			SOAPMessage soapResponse = soapConnection.call(soapMessage, endpointUrl);
			
			soapResponse.writeTo(System.out); // Print response
			
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			soapResponse.writeTo(out);
			String strMsg = new String(out.toByteArray());
			XMLInputFactory xif = XMLInputFactory.newFactory();
			InputStream is = new ByteArrayInputStream( strMsg.getBytes(Charsets.UTF_8) );
		    InputStreamReader reader = new InputStreamReader(is);
            XMLStreamReader xsr = xif.createXMLStreamReader(reader);
            xsr.nextTag(); // Advance to Envelope tag
            xsr.nextTag(); // Advance to Body tag
        	xsr.nextTag(); //getAccountBalanceResponse
        	xsr.nextTag(); //return
//        	xsr.nextTag(); //acctRec
            // System.out.println(xsr.getLocalName());


            JAXBContext jc = JAXBContext.newInstance(GetTransactionHistoryResponse.class);
            Unmarshaller unmarshaller = jc.createUnmarshaller();
            GetTransactionHistoryResponse transactionStatusResponse = unmarshaller.unmarshal(xsr, GetTransactionHistoryResponse.class).getValue(); // JAXBElement
            GetTransactionHistoryGWResponse jsonObject = new GetTransactionHistoryGWResponse();
            jsonObject.setBank_error_code(transactionStatusResponse.getErrorObject().getErrorCode());
            jsonObject.setBank_message(transactionStatusResponse.getErrorObject().getMessage());
            jsonObject.setListTrans(transactionStatusResponse.getListTrans());
            
            pgLogChannelFunctionService.writeLogChannelFunction(VIBConst.CHANNEL_CODE, VIBConst.CHANNEL_FUNCTION_HISTORY_TRANSACTION, true);
            if (StringUtils.equals(transactionStatusResponse.getErrorObject().getErrorCode(), "ITH000")) {
            	ResponseJson prefixResult = commonResponseService.returnGatewayRequestSuccessPrefix();
				jsonObject.setError_code(prefixResult.getError_code());
				jsonObject.setMessage(prefixResult.getMessage());			
				
				paramsLog = new String[] { strMsg };
				logger.info(commonLogService.createContentLog(VIBConst.CHANNEL_CODE, SERVICE_NAME, VIBConst.FUNCTION_CODE_HISTORY_TRANSACTION, true, false,
						true, paramsLog));
				
            }
            else {
            	ResponseJson prefixResult = commonResponseService.returnChannelBadRequestPrefix();
				jsonObject.setError_code(prefixResult.getError_code());
				jsonObject.setMessage(prefixResult.getMessage());
				
				paramsLog = new String[] { strMsg };
				logger.info(commonLogService.createContentLog(VIBConst.CHANNEL_CODE, SERVICE_NAME, VIBConst.FUNCTION_CODE_HISTORY_TRANSACTION, false, false,
						true, paramsLog));
            }
            
            logger.info(commonLogService.createContentLogStartEndFunction(VIBConst.CHANNEL_CODE, SERVICE_NAME, VIBConst.FUNCTION_CODE_HISTORY_TRANSACTION, false));
            
			return new ResponseEntity<GetTransactionHistoryGWResponse>(jsonObject, HttpStatus.OK);

		} catch (SOAPException e) {
			pgLogChannelFunctionService.writeLogChannelFunction(VIBConst.CHANNEL_CODE, VIBConst.CHANNEL_FUNCTION_HISTORY_TRANSACTION, false);
			
			paramsLog = new String[] { e.getMessage() };
			logger.info(commonLogService.createContentLog(VIBConst.CHANNEL_CODE, SERVICE_NAME, VIBConst.FUNCTION_CODE_HISTORY_TRANSACTION, false, false,
					true, paramsLog));
			logger.info(commonLogService.createContentLogStartEndFunction(VIBConst.CHANNEL_CODE, SERVICE_NAME, VIBConst.FUNCTION_CODE_HISTORY_TRANSACTION, false));
			
			return commonResponseService.returnChannelBadRequest(e.getMessage());
		}
		catch (Exception e) {

			paramsLog = new String[] { e.getMessage() };
			logger.info(commonLogService.createContentLog(VIBConst.CHANNEL_CODE, SERVICE_NAME, VIBConst.FUNCTION_CODE_HISTORY_TRANSACTION, false, false,
					true, paramsLog));
			logger.info(commonLogService.createContentLogStartEndFunction(VIBConst.CHANNEL_CODE, SERVICE_NAME, VIBConst.FUNCTION_CODE_HISTORY_TRANSACTION, false));
			
			return commonResponseService.returnBadGateway();
		}
	}

	/*private ResponseJson returnGatewayRequestSuccessPrefix() {
		
		ResponseJson jsonObject = new ResponseJson();
		
		PgErrorDefinition pgErrorDefinition = pgErrorDefinitionService.findByCode(ResponseUtil.ERROR_CODE_GATEWAY_REQUEST_SUCCESS);
		if (pgErrorDefinition != null) {
			jsonObject.setError_code(ResponseUtil.ERROR_CODE_GATEWAY_REQUEST_SUCCESS);
			jsonObject.setMessage(pgErrorDefinition.getMessage());
		}
		else {
			jsonObject.setError_code("000000");
			jsonObject.setMessage("Gateway request success");
		}
		return jsonObject;
	}
	
	private ResponseEntity<?> returnBadGateway() {
		
		ResponseJson jsonObject = new ResponseJson();
		
		PgErrorDefinition pgErrorDefinition = pgErrorDefinitionService.findByCode(ResponseUtil.ERROR_CODE_GATEWAY_REQUEST_FAIL);
		if (pgErrorDefinition != null) {
			jsonObject.setError_code(ResponseUtil.ERROR_CODE_GATEWAY_REQUEST_FAIL);
			jsonObject.setMessage(pgErrorDefinition.getMessage());
		}
		else {
			jsonObject.setError_code("999999");
			jsonObject.setMessage("Gateway request fail");
		}
		return new ResponseEntity<ResponseJson>(jsonObject, HttpStatus.BAD_REQUEST);
	}
	
	private ResponseEntity<?> returnBadRequets_TransactionExist() {
		
		ResponseJson jsonObject = new ResponseJson();
		
		PgErrorDefinition pgErrorDefinition = pgErrorDefinitionService.findByCode(ResponseUtil.ERROR_CODE_GATEWAY_TRANSACTION_ALREADY_EXIST);
		if (pgErrorDefinition != null) {
			jsonObject.setError_code(ResponseUtil.ERROR_CODE_GATEWAY_TRANSACTION_ALREADY_EXIST);
			jsonObject.setMessage(pgErrorDefinition.getMessage());
		}
		else {
			jsonObject.setError_code(ResponseUtil.ERROR_CODE_GATEWAY_TRANSACTION_ALREADY_EXIST);
			jsonObject.setMessage("Merchant transaction id already exist");
		}
		return new ResponseEntity<ResponseJson>(jsonObject, HttpStatus.BAD_REQUEST);
	}
	
	private ResponseEntity<?> returnBadRequets_TransactionEmpty() {
		
		ResponseJson jsonObject = new ResponseJson();
		
		PgErrorDefinition pgErrorDefinition = pgErrorDefinitionService.findByCode(ResponseUtil.ERROR_CODE_GATEWAY_TRANSACTION_IS_EMPTY);
		if (pgErrorDefinition != null) {
			jsonObject.setError_code(ResponseUtil.ERROR_CODE_GATEWAY_TRANSACTION_IS_EMPTY);
			jsonObject.setMessage(pgErrorDefinition.getMessage());
		}
		else {
			jsonObject.setError_code(ResponseUtil.ERROR_CODE_GATEWAY_TRANSACTION_IS_EMPTY);
			jsonObject.setMessage("Merchant transaction id is not empty");
		}
		return new ResponseEntity<ResponseJson>(jsonObject, HttpStatus.BAD_REQUEST);
	}
	
	public ResponseEntity<?> returnBadRequets_WithCause(String cause) {
		cause = ": "+ cause;
		ResponseJson jsonObject = new ResponseJson();
		
		PgErrorDefinition pgErrorDefinition = pgErrorDefinitionService.findByCode(ResponseUtil.ERROR_CODE_BAD_REQUEST);
		if (pgErrorDefinition != null) {
			jsonObject.setError_code(ResponseUtil.ERROR_CODE_BAD_REQUEST);
			jsonObject.setMessage(pgErrorDefinition.getMessage() + cause);
		}
		else {
			jsonObject.setError_code(ResponseUtil.ERROR_CODE_BAD_REQUEST);
			jsonObject.setMessage("Bad request" + cause);
		}
		return new ResponseEntity<ResponseJson>(jsonObject, HttpStatus.BAD_REQUEST);
	}
	
	private ResponseEntity<?> returnChannelTimeout() {

		ResponseJson jsonObject = new ResponseJson();

		PgErrorDefinition pgErrorDefinition = pgErrorDefinitionService
				.findByCode(ResponseUtil.ERROR_CODE_CHANNEL_REQUEST_TIMEOUT);
		if (pgErrorDefinition != null) {
			jsonObject.setError_code(ResponseUtil.ERROR_CODE_CHANNEL_REQUEST_TIMEOUT);
			jsonObject.setMessage(pgErrorDefinition.getMessage());
		} else {
			jsonObject.setError_code("CN0001");
			jsonObject.setMessage("Channel request timeout");
		}
		return new ResponseEntity<ResponseJson>(jsonObject, HttpStatus.GATEWAY_TIMEOUT);
	}
	
	private ResponseEntity<?> returnChannelBadRequest(String cause) {

		String appendCause = "";
		if (StringUtils.isNotEmpty(cause)) {
			appendCause = ": " + cause;
		}
		ResponseJson jsonObject = new ResponseJson();

		PgErrorDefinition pgErrorDefinition = pgErrorDefinitionService
				.findByCode(ResponseUtil.ERROR_CODE_CHANNEL_BAD_REQUEST);
		if (pgErrorDefinition != null) {
			jsonObject.setBank_error_code(ResponseUtil.ERROR_CODE_CHANNEL_BAD_REQUEST);
			jsonObject.setBank_message(pgErrorDefinition.getMessage()  + appendCause);
		} else {
			jsonObject.setBank_error_code("CN0002");
			jsonObject.setBank_message("Channel bad request" + appendCause);
		}
		
		pgErrorDefinition = pgErrorDefinitionService
				.findByCode(ResponseUtil.ERROR_CODE_GATEWAY_REQUEST_SUCCESS);
		jsonObject.setError_code(ResponseUtil.ERROR_CODE_GATEWAY_REQUEST_SUCCESS);
		jsonObject.setMessage(pgErrorDefinition.getMessage());
		
		return new ResponseEntity<ResponseJson>(jsonObject, HttpStatus.FORBIDDEN);
	}
	
	private ResponseJson returnChannelBadRequestPrefix() {

		ResponseJson jsonObject = new ResponseJson();

		PgErrorDefinition pgErrorDefinition = pgErrorDefinitionService
				.findByCode(ResponseUtil.ERROR_CODE_CHANNEL_BAD_REQUEST);
		if (pgErrorDefinition != null) {
			jsonObject.setError_code(ResponseUtil.ERROR_CODE_CHANNEL_BAD_REQUEST);
			jsonObject.setMessage(pgErrorDefinition.getMessage());
		} else {
			jsonObject.setError_code("CN0002");
			jsonObject.setMessage("Channel bad request");
		}
		
		return jsonObject;
	}*/

	private static HttpComponentsClientHttpRequestFactory createRequestFactory()
			throws NoSuchAlgorithmException, KeyManagementException {
		SSLContext sslContext = SSLContext.getInstance("TLS");
		TrustManager[] trust_mgr = get_trust_mgr();
		sslContext.init(null, trust_mgr, new SecureRandom());

		SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext);

		CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(csf).build();

		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();

		requestFactory.setHttpClient(httpClient);
		return requestFactory;
	}

	private static TrustManager[] get_trust_mgr() {
		TrustManager[] certs = new TrustManager[] { new X509TrustManager() {

			@Override
			public void checkClientTrusted(java.security.cert.X509Certificate[] xcs, String string)
					throws CertificateException {
			}

			@Override
			public void checkServerTrusted(java.security.cert.X509Certificate[] xcs, String string)
					throws CertificateException {

			}

			@Override
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return new java.security.cert.X509Certificate[] {};
			}
		} };
		return certs;
	}

	private static void disableSslVerification() {
		try {
			// Create a trust manager that does not validate certificate chains
			TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				public void checkClientTrusted(X509Certificate[] certs, String authType) {
				}

				public void checkServerTrusted(X509Certificate[] certs, String authType) {
				}
			} };

			// Install the all-trusting trust manager
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

			// Create all-trusting host name verifier
			HostnameVerifier allHostsValid = new HostnameVerifier() {
				public boolean verify(String hostname, SSLSession session) {
					return true;
				}
			};

			// Install the all-trusting host verifier
			HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
		} catch (NoSuchAlgorithmException e) {
			logger.info(ExceptionUtils.getStackTrace(e));
		} catch (KeyManagementException e) {
			logger.info(ExceptionUtils.getStackTrace(e));
		}
	}

}
