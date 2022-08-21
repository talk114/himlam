package gateway.core.channel.viettelpost.service.impl;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import gateway.core.channel.PaymentGate;
import gateway.core.channel.viettelpost.dto.VTTPostConstants;
import gateway.core.channel.viettelpost.dto.req.ViettelPostReq;
import gateway.core.channel.viettelpost.service.ViettelPostService;
import gateway.core.dto.PGResponse;
import gateway.core.util.HttpUtil;
import gateway.core.util.PGSecurity;
import org.springframework.stereotype.Service;
import vn.nganluong.naba.entities.PaymentAccount;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.Map;

@Service
public class ViettelPostServiceImpl extends PaymentGate implements ViettelPostService {
    //  https://sandbox.nganluong.vn:8088/nl35/api/viettelPost

    @Override
    public String ProcessRequest(PaymentAccount paymentAccount, String request) throws Exception {

        String processRes = "";
        // TODO
        WriteInfoLog("2. VIETTEL POST REQ", request);

        ViettelPostReq vttReq = objectMapper.readValue(request, ViettelPostReq.class);
        if (!validateCheckSum(vttReq)) {
            return buildResponseError(VTTPostConstants.CHECKSUM_INVALID);
        }
        vttReq.setChecksum(null);

        // Call Api Vimo/Ngl
        processRes = processTransaction(paymentAccount, vttReq);
        // TODO
        WriteInfoLog("3. VIETTEL POST RES", processRes);

        return processRes;
    }

    private boolean validateCheckSum(ViettelPostReq req) {
        String rawData = req.dataBeforeChecksum(VTTPostConstants.AUTH_KEY, req.getFunction());
        try {
            String md5 = PGSecurity.md5(rawData);
            return md5.equalsIgnoreCase(req.getChecksum());
        } catch (NoSuchAlgorithmException e) {
            // TODO
            WriteErrorLog("VIETTEL POST CHECK SUM ERROR: " + req);
            return false;
        }
    }

    private static String buildResponseError(String errorCode) throws JsonProcessingException {
        PGResponse res = new PGResponse();
        res.setErrorCode(errorCode);
        res.setMessage(VTTPostConstants.getErrorMessge(errorCode));
        return objectMapper.writeValueAsString(res);
    }

    private String processTransaction(PaymentAccount paymentAccount, ViettelPostReq req) throws Exception {
        String request = objectMapper.writeValueAsString(req);
        // TODO
        WriteInfoLog("2. CALL NGANLUONG", request);

        Map<String, Object> map = new HashMap<>();
        map = objectMapper.readValue(request, new TypeReference<Map<String, Object>>() {
        });

        String userpass = paymentAccount.getUsername() + ":" + paymentAccount.getPassword();
        String resp = HttpUtil.send(VTTPostConstants.URL_API, map, userpass);
//		ViettelPostRes VttRes = objectMapper.readValue(resp, ViettelPostRes.class);
//		if (VttRes.getErrorCode().equals("05")) {
//			VttRes.setViewInfo(buildResponseError(VttRes.getErrorCode()));
//		}
        // TODO
        WriteInfoLog("3. NGANLUONG RES", objectMapper.writeValueAsString(resp));

        return resp;

//		if (req.getPaymentId().startsWith("PS1")) {
//		} else if (req.getPaymentId().startsWith("PS2")) {
//			return callNL(objectMapper.writeValueAsString(req));
//		}
//
//		return "";
    }

    private String callNL(PaymentAccount paymentAccount, String req) throws IOException {

        // TODO
        WriteInfoLog("2. CALL NGANLUONG", req);

        Map<String, Object> map = new HashMap<>();
        map = objectMapper.readValue(req, new TypeReference<Map<String, Object>>() {
        });

        String userpass = paymentAccount.getUsername() + ":" + paymentAccount.getPassword();
        String resp = HttpUtil.send(paymentAccount.getUrlApi(), map, userpass);

        // TODO
        WriteInfoLog("3. NGANLUONG RES", objectMapper.writeValueAsString(resp));

        return resp;
    }
}
