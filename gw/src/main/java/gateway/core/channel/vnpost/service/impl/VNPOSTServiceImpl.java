package gateway.core.channel.vnpost.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import gateway.core.channel.PaymentGate;
import gateway.core.channel.vnpost.VnPostSecurity;
import gateway.core.channel.vnpost.dto.req.BaseVnpostApiReq;
import gateway.core.channel.vnpost.dto.res.BaseVnpostApiRes;
import gateway.core.channel.vnpost.dto.res.TransactionInfoRes;
import gateway.core.channel.vnpost.dto.res.UserInfoRes;
import gateway.core.util.HttpUtil;
import gateway.core.util.PGSecurity;
import gateway.core.util.RandomUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import vn.nganluong.naba.entities.PaymentAccount;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class VNPOSTServiceImpl extends PaymentGate {
	private static final Logger logger = LogManager.getLogger(VNPOSTServiceImpl.class);

	public String RedirectChannel(PaymentAccount paymentAccount, String request) {
		// TODO
		WriteInfoLog("1. VNPOST REQ", request);
		try {
			BaseVnpostApiReq input = objectMapper.readValue(request, BaseVnpostApiReq.class);

			String cash = "";
			if (input.getVnpostServiceCode().contains("CASHOUT")) {
				cash = input.getCashoutId();
			} else if (input.getVnpostServiceCode().contains("CASHIN")) {
				cash = input.getCashinId();
			}

			if (StringUtils.isBlank(cash)) {
				return buildErrorRes("05");
			}

			String key = StringUtils.defaultString(paymentAccount.getEncryptKey(), VnPostSecurity.ENCRYPT_KEY_DEFAULT);

			String md5 = PGSecurity.md5(input.getFunc(), input.getAccessCode(), key, input.getVnpostServiceCode(),
					cash, input.getRequestId(), input.getDateTime());

			if (!md5.equalsIgnoreCase(input.getChecksum())) {
				return buildErrorRes("02");
			}

			if (input.getVnpostServiceCode().startsWith("NL")) {
				return callNL(paymentAccount, request, input.getFunc());
			}
			if (input.getVnpostServiceCode().startsWith("VM")) {
				 return callVimo(paymentAccount, request, input.getFunc());
			}

		} catch (Exception e) {
			logger.info(ExceptionUtils.getStackTrace(e));
		}
		return null;

	}

	private String callVimo(PaymentAccount paymentAccount, String req, String fnc)
			throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException,
			IllegalBlockSizeException, BadPaddingException, IOException {

		WriteInfoLog("2. CALL VIMO", req);

		Map<String, Object> map = new HashMap<>();
		String data = PGSecurity.encrypt3DES(req, paymentAccount.getEncryptKeyCallback());
		map.put("func", fnc);
		map.put("params", data);
		map.put("checksum", PGSecurity.md5(fnc, data));
		map.put("partner", "VNPOST");
		String userpass = paymentAccount.getUsername() + ":" + paymentAccount.getPassword();
		String resp = HttpUtil.send(paymentAccount.getUrlApi(), map, userpass);

		WriteInfoLog("3. VIMO RES", objectMapper.writeValueAsString(resp));

		return resp;
	}

	private String callNL(PaymentAccount paymentAccount, String req, String fnc)
			throws IOException {

		// TODO
		WriteInfoLog("2. CALL NGANLUONG", req);

		Map<String, Object> map = new HashMap<>();
		map = objectMapper.readValue(req, new TypeReference<Map<String, Object>>() {
		});

		String userpass = paymentAccount.getUsername() + ":" + paymentAccount.getPassword();
		String resp = HttpUtil.send(paymentAccount.getUrlApi(), map, userpass);

		// TODO
		WriteInfoLog("3. NGANLUONG RES", resp);

		return resp;
	}

	//
	// ******************************************
	// FAKE
	//
	private String buildErrorRes(String errorCode) throws JsonProcessingException {
		BaseVnpostApiRes res = new BaseVnpostApiRes();
		res.setErrorCode(errorCode);

		return objectMapper.writeValueAsString(res);
	}

	private String responseQuery(BaseVnpostApiReq req) throws JsonProcessingException {

		BaseVnpostApiRes res = new BaseVnpostApiRes();
		res.setErrorCode("00");
		res.setVerifyCode(RandomUtil.randomDigitString(4));
		res.setViewInfo("Rut tien tu vi Vimo su dung so dien thoai: 84988666999");

		UserInfoRes userInfoRes = new UserInfoRes();
		userInfoRes.setBirthday("01/01/1990");
		userInfoRes.setName("Trân Văn A");
		userInfoRes.setPid("129870987");
		res.setUserInfoRes(userInfoRes);

		TransactionInfoRes transInfoRes = new TransactionInfoRes();
		transInfoRes.setAmount("50000");
		transInfoRes.setStatus(1);
		transInfoRes.setTimeCreated(getTimeRes());
		transInfoRes.setCashoutId(req.getCashoutId());
		res.setTransactionInfoRes(transInfoRes);

		// VMCASHIN
		if (req.getVnpostServiceCode().contains("CASHIN")) {
			transInfoRes.setCashinId(req.getCashinId());
		} else if (req.getVnpostServiceCode().contains("CASHOUT")) {
			transInfoRes.setCashoutId(req.getCashoutId());
		}

		return objectMapper.writeValueAsString(res);
	}

	private String responseWithdraw(BaseVnpostApiReq req) throws JsonProcessingException {
		BaseVnpostApiRes res = new BaseVnpostApiRes();
		res.setErrorCode("00");
		res.setViewInfo("");

		TransactionInfoRes transInfoRes = new TransactionInfoRes();
		transInfoRes.setAmount(req.getAmount());
		transInfoRes.setStatus(1);
		transInfoRes.setTimeCreated(getTimeRes());
		transInfoRes.setCashoutId(req.getCashoutId());

		res.setTransactionInfoRes(transInfoRes);

		// VMCASHIN
		if (req.getVnpostServiceCode().contains("CASHIN")) {
			transInfoRes.setCashinId(req.getCashinId());
		} else if (req.getVnpostServiceCode().contains("CASHOUT")) {
			transInfoRes.setCashoutId(req.getCashoutId());
		}

		return objectMapper.writeValueAsString(res);
	}

	private String getTimeRes() {
		long time = new Date().getTime();
		return "" + time / 1000;
	}

}
