package gateway.core.channel.anbinhbank;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import gateway.core.channel.ChannelCommonConfig;
import gateway.core.channel.PaymentGate;
import gateway.core.channel.anbinhbank.dto.ABBankConstants;
import gateway.core.channel.anbinhbank.dto.req.GetAccessTokenReq;
import gateway.core.channel.anbinhbank.dto.req.HeaderRequest;
import gateway.core.channel.anbinhbank.dto.req.RootRequest;
import gateway.core.channel.anbinhbank.dto.req.body.BaseRequest;
import gateway.core.channel.anbinhbank.dto.req.body.CheckBalanceReq;
import gateway.core.channel.anbinhbank.dto.req.body.CheckTransReq;
import gateway.core.channel.anbinhbank.dto.req.body.VerifyOtpReq;
import gateway.core.channel.anbinhbank.dto.res.GetAccessTokenRes;
import gateway.core.channel.anbinhbank.dto.res.RootResponse;
import gateway.core.channel.anbinhbank.dto.res.body.BaseResponse;
import gateway.core.channel.anbinhbank.dto.res.body.CheckBalanceRes;
import gateway.core.channel.anbinhbank.dto.res.body.CheckTransRes;
import gateway.core.channel.anbinhbank.dto.res.body.VerifyOtpRes;
import gateway.core.dto.PGResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import vn.nganluong.naba.entities.PaymentAccount;

import javax.ws.rs.core.MediaType;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.nio.file.Files;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

public abstract class AnBinhBank extends PaymentGate {
	private static final Logger logger = LogManager.getLogger(AnBinhBank.class);


	// https://10.2.9.50:9443/abb/sandbox
	// https://apigw-abbdev.abbank.vn:9443/abb/sandbox
	// public https://apigw-abbdev.abbank.vn/api/sandbox

	private static String SESSION_FILE_PATH = "/var/lib/payment_gateway_test/key/an_binh_bank/";
	private static File fileSession = null;
	private int MAX_TIME_LOGIN = 3;

	//	String PARTNER_ID = "VIMO";
	//Key Doi Soat: ABBVIMO2019ABBVIMO2019ABBVIMO2019
	String CLIENT_ID = "99291dbc79c363fe28b69b48273396bf";//98301196eb3cbfc1234c2347c8f7c7e1
	String CLIENT_SECRET = "535f95eaf71ae61f965f0cba0dc48313";//98a32d476b629aed538fe8ead8bd0ec0
	String SCOPE = "wallets payments verifications";
	String GRANT_TYPE = "client_credentials";

	// http status 200, 201 có out body -> can xac thuc signature
	// con lai ko can

	public PGResponse VerifyOtp(PaymentAccount paymentAccount, String inputStr) throws KeyManagementException, NoSuchAlgorithmException, IOException {
		VerifyOtpReq body = objectMapper.readValue(inputStr, VerifyOtpReq.class);

		RootRequest rootReq = buildRootReq(paymentAccount, body);
		return callApi(rootReq, ABBankConstants.VERIFY_OTP_URL_SUFFIX, new TypeReference<VerifyOtpRes>() {
		});
	}

	protected PGResponse GetBalance(PaymentAccount paymentAccount, String inputStr, String suffixApi) throws IOException,
			KeyManagementException, NoSuchAlgorithmException {
		CheckBalanceReq body = objectMapper.readValue(inputStr, CheckBalanceReq.class);
		RootRequest rootReq = buildRootReq(paymentAccount, body);
		return callApi(rootReq, suffixApi, new TypeReference<CheckBalanceRes>() {
		});
	}

	protected PGResponse CheckTransStatus(PaymentAccount paymentAccount, String inputStr, String suffixApi) throws JsonParseException, JsonMappingException, IOException,
			KeyManagementException, NoSuchAlgorithmException {
		CheckTransReq body = objectMapper.readValue(inputStr, CheckTransReq.class);
		RootRequest rootReq = buildRootReq(paymentAccount, body);
		return callApi(rootReq, suffixApi, new TypeReference<CheckTransRes>() {
		});
	}

	/**
	 * ===========================================================
	 * ===========================================================
	 * ===========================================================
	 */

	protected <T extends BaseResponse> PGResponse callApi(RootRequest rootReq, String action, TypeReference<T> objRes)
			throws IOException, KeyManagementException, NoSuchAlgorithmException {
		// TODO
		WriteInfoLog("2. ABB ROOT REQ", objectMapper.writeValueAsString(rootReq));

		RootResponse rootRes = ClientRequest.sendRequest(ChannelCommonConfig.ABB_URL_API + action, rootReq,
				MediaType.APPLICATION_JSON);

		// TODO
		WriteInfoLog("3. ABB ROOT RES", objectMapper.writeValueAsString(rootRes));
		T res = null;
		PGResponse pgRes = new PGResponse();
		String resCode = "";

		int htppStatus = rootRes.getHttpStatus();
		if (htppStatus == HttpURLConnection.HTTP_OK || htppStatus == HttpURLConnection.HTTP_CREATED) {
			res = objectMapper.readValue(rootRes.getBodyRes(), objRes);
			res.setTransId(rootReq.getBodyReq().getTransId());

			// verify signature
			String signatureRes = rootRes.getSignatureHeaderRes();
			if (!ABBankSecurity.verifyP12(signatureRes, res.rawData(), ChannelCommonConfig.PUBLIC_KEY_ABB_PATH)) {
				WriteErrorLog(
						"ABB VERIFY SIGNATURE ERROR, RES DATA: " + res.rawData() + "\t RES SIGN: " + signatureRes);

				pgRes.setStatus(true);
				pgRes.setData(objectMapper.writeValueAsString(res));
				pgRes.setErrorCode("05");
				pgRes.setMessage("Verify Signature ABB FAIL");
				//return objectMapper.writeValueAsString(pgRes);
				return pgRes;
			}
			resCode = ABBankConstants.MAP_HTTP_STT.get(htppStatus);
		} else if (htppStatus == HttpURLConnection.HTTP_BAD_REQUEST) {
			res = objectMapper.readValue(rootRes.getBodyRes(), objRes);
			resCode = res.getCode();
			WriteErrorLog(HttpURLConnection.HTTP_BAD_REQUEST + "\t" + resCode);
		} else if (htppStatus == HttpURLConnection.HTTP_UNAUTHORIZED) {
			String accessToken = getTokenApi(0);
			rootReq.getHeaderReq().setAccessToken(accessToken);
			if (StringUtils.isNotBlank(accessToken))
				return callApi(rootReq, action, objRes);
			else
				resCode = "ZZ";
		} else {
			// loi ko xac dinh
			resCode = "99";
		}

		pgRes.setStatus(true);
		pgRes.setData(objectMapper.writeValueAsString(res));
		pgRes.setErrorCode(resCode);
		pgRes.setMessage(ABBankConstants.getErrorMessage(resCode));
		//return objectMapper.writeValueAsString(pgRes);
		return pgRes;
	}

	protected RootRequest buildRootReq(PaymentAccount paymentAccount, BaseRequest body) {
		body.setPartnerId(paymentAccount.getProviderId());
		body.setMerchantId(paymentAccount.getMerchantId());
		body.setMerchantName(paymentAccount.getMerchantName());

		HeaderRequest header = new HeaderRequest();
		header.setAccessToken(readFileSession());
		header.setSignature(ABBankSecurity.signP12(ChannelCommonConfig.PRIVATE_KEY_PATH,
				ChannelCommonConfig.PRIVATE_KEY_PASSWORD, body.rawData()));
		header.setVimoTransId(body.getTransId());

		RootRequest rootReq = new RootRequest();
		rootReq.setBodyReq(body);
		rootReq.setHeaderReq(header);
		return rootReq;
	}

	protected synchronized String getTokenApi(int timeLogin)
			throws JsonProcessingException, IOException, KeyManagementException, NoSuchAlgorithmException {
		GetAccessTokenReq req = new GetAccessTokenReq();
		req.setScope(SCOPE);
		CLIENT_ID = ChannelCommonConfig.AUTH_KEY_ABB.split(":")[0];
		CLIENT_SECRET = ChannelCommonConfig.AUTH_KEY_ABB.split(":")[1];
		req.setClientId(CLIENT_ID);
		req.setClientSecret(CLIENT_SECRET);
		req.setGrantType(GRANT_TYPE);

		RootRequest rootReq = new RootRequest();
		rootReq.setBodyReq(req);

		// TODO
		WriteInfoLog("Get Token Req: " + timeLogin, objectMapper.writeValueAsString(rootReq));
		RootResponse rootRes = ClientRequest.sendRequest(
				ChannelCommonConfig.ABB_URL_API + ABBankConstants.GET_TOKEN_URL_SUFFIX, rootReq,
				MediaType.APPLICATION_FORM_URLENCODED);

		// TODO
		WriteInfoLog("Get Token Res Time: " + timeLogin, objectMapper.writeValueAsString(rootRes));
		GetAccessTokenRes res = objectMapper.readValue(rootRes.getBodyRes(), GetAccessTokenRes.class);

		if (StringUtils.isNotBlank(res.getAccessToken())) {
			writeFileSession(res.getAccessToken());
			return res.getAccessToken();
		}
		timeLogin += 1;
		if (timeLogin < MAX_TIME_LOGIN)
			return getTokenApi(timeLogin);
		else
			return "";
	}

	private String readFileSession() {
		String accessToken = "";
		try {
			ClassLoader classLoader = new ABBankSecurity().getClass().getClassLoader();
			fileSession =  new File(classLoader.getResource(ChannelCommonConfig.ABB_SESSION_FILE_TOKEN_PATH).getFile());
			if (!fileSession.exists()) {
				fileSession.createNewFile();
			}
			byte[] contentByte = Files.readAllBytes(fileSession.toPath());
			accessToken = new String(contentByte);
		} catch (IOException e) {
			logger.info(ExceptionUtils.getStackTrace(e));
		}
		return accessToken.replaceAll("\\n", "").replaceAll("\\t", "").trim();
	}

	private void writeFileSession(String newSession) {
		FileWriter fw = null;
		BufferedWriter bw = null;
		StringBuilder sb = new StringBuilder(newSession);
		try {
			if (fileSession == null) {
				ClassLoader classLoader = new ABBankSecurity().getClass().getClassLoader();
				fileSession =  new File(classLoader.getResource(ChannelCommonConfig.ABB_SESSION_FILE_TOKEN_PATH).getFile());
				if (!fileSession.exists()) {
					fileSession.createNewFile();
				}
			}

			fw = new FileWriter(fileSession.getAbsolutePath());
			bw = new BufferedWriter(fw);
			bw.write(sb.toString());
			bw.close();
			fw.close();
		} catch (IOException e) {
			logger.info(ExceptionUtils.getStackTrace(e));
		} finally {
			try {
				if (bw != null)
					bw.close();
				if (fw != null)
					fw.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
}
