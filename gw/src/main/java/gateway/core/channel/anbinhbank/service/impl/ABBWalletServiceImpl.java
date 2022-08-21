package gateway.core.channel.anbinhbank.service.impl;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import gateway.core.channel.anbinhbank.AnBinhBank;
import gateway.core.channel.anbinhbank.dto.ABBankConstants;
import gateway.core.channel.anbinhbank.dto.req.RootRequest;
import gateway.core.channel.anbinhbank.dto.req.body.LinkAccountReq;
import gateway.core.channel.anbinhbank.dto.req.body.PayWithTokenReq;
import gateway.core.channel.anbinhbank.dto.req.body.UnLinkAccountReq;
import gateway.core.channel.anbinhbank.dto.req.body.WithdrawToTokenReq;
import gateway.core.channel.anbinhbank.dto.res.body.BaseResponse;
import gateway.core.channel.anbinhbank.service.ABBEcomService;
import gateway.core.channel.anbinhbank.service.ABBWalletService;
import gateway.core.dto.PGResponse;
import org.springframework.stereotype.Service;
import vn.nganluong.naba.entities.PaymentAccount;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

@Service
public class ABBWalletServiceImpl extends AnBinhBank implements ABBWalletService {
    public PGResponse LinkAccount(PaymentAccount paymentAccount, String inputStr) throws JsonParseException, JsonMappingException, IOException,
            KeyManagementException, NoSuchAlgorithmException {
        LinkAccountReq body = objectMapper.readValue(inputStr, LinkAccountReq.class);

        RootRequest rootReq = buildRootReq(paymentAccount, body);
        return callApi(rootReq, ABBankConstants.LINK_ACC_URL_SUFFIX, new TypeReference<BaseResponse>() {
        });
    }

    public PGResponse UnlinkAccount(PaymentAccount paymentAccount, String inputStr) throws JsonParseException, JsonMappingException, IOException,
            KeyManagementException, NoSuchAlgorithmException {
        UnLinkAccountReq body = objectMapper.readValue(inputStr, UnLinkAccountReq.class);

        RootRequest rootReq = buildRootReq(paymentAccount, body);
        return callApi(rootReq, ABBankConstants.UNLINK_ACC_URL_SUFFIX, new TypeReference<BaseResponse>() {
        });
    }

    public PGResponse PaymentWithToken(PaymentAccount paymentAccount, String inputStr) throws JsonParseException, JsonMappingException, IOException,
            KeyManagementException, NoSuchAlgorithmException {
        PayWithTokenReq body = objectMapper.readValue(inputStr, PayWithTokenReq.class);

        RootRequest rootReq = buildRootReq(paymentAccount, body);
        return callApi(rootReq, ABBankConstants.DEPOSIT_URL_SUFFIX, new TypeReference<BaseResponse>() {
        });
    }

    public PGResponse WithdrawToToken(PaymentAccount paymentAccount, String inputStr) throws JsonParseException, JsonMappingException, IOException,
            KeyManagementException, NoSuchAlgorithmException {
        WithdrawToTokenReq body = objectMapper.readValue(inputStr, WithdrawToTokenReq.class);
        RootRequest rootReq = buildRootReq(paymentAccount, body);
        return callApi(rootReq, ABBankConstants.WITHDRAW_URL_SUFFIX, new TypeReference<BaseResponse>() {
        });
    }

    @Override
    public PGResponse VerifyOtp(PaymentAccount paymentAccount, String inputStr) throws KeyManagementException, NoSuchAlgorithmException, IOException {
        return super.VerifyOtp(paymentAccount, inputStr);
    }

    @Override
    public PGResponse GetBalance(PaymentAccount paymentAccount, String inputStr) throws JsonParseException, JsonMappingException, IOException,
            KeyManagementException, NoSuchAlgorithmException {
        return super.GetBalance(paymentAccount, inputStr, ABBankConstants.GET_BALANCE_URL_SUFFIX);
    }

    @Override
    public PGResponse CheckTransStatus(PaymentAccount paymentAccount, String inputStr) throws JsonParseException, JsonMappingException, IOException,
            KeyManagementException, NoSuchAlgorithmException {
        return super.CheckTransStatus(paymentAccount, inputStr, ABBankConstants.CHECK_TRANS_URL_SUFFIX);
    }
}
