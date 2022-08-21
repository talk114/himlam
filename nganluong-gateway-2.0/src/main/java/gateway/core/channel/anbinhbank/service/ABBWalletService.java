package gateway.core.channel.anbinhbank.service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import gateway.core.dto.PGResponse;
import vn.nganluong.naba.entities.PaymentAccount;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

public interface ABBWalletService {
    PGResponse LinkAccount(PaymentAccount paymentAccount, String inputStr) throws IOException, KeyManagementException, NoSuchAlgorithmException;

    PGResponse UnlinkAccount(PaymentAccount paymentAccount, String inputStr) throws IOException, KeyManagementException, NoSuchAlgorithmException;

    PGResponse PaymentWithToken(PaymentAccount paymentAccount, String inputStr) throws IOException, KeyManagementException, NoSuchAlgorithmException;

    PGResponse WithdrawToToken(PaymentAccount paymentAccount, String inputStr) throws IOException, KeyManagementException, NoSuchAlgorithmException;

    PGResponse VerifyOtp(PaymentAccount paymentAccount, String inputStr) throws KeyManagementException, NoSuchAlgorithmException, IOException;

    PGResponse GetBalance(PaymentAccount paymentAccount, String inputStr) throws IOException, KeyManagementException, NoSuchAlgorithmException;

    PGResponse CheckTransStatus(PaymentAccount paymentAccount, String inputStr) throws IOException, KeyManagementException, NoSuchAlgorithmException;
}
