package gateway.core.channel.anbinhbank.service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import gateway.core.dto.PGResponse;
import vn.nganluong.naba.entities.ChannelFunction;
import vn.nganluong.naba.entities.PaymentAccount;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;


public interface ABBEcomService {
    PGResponse PaymentWithBankAcc(ChannelFunction channelFunction, PaymentAccount paymentAccount, String inputStr) throws IOException, KeyManagementException, NoSuchAlgorithmException, ParseException;

    PGResponse VerifyOtp(PaymentAccount paymentAccount, String inputStr) throws KeyManagementException, NoSuchAlgorithmException, IOException;

    PGResponse GetBalance(PaymentAccount paymentAccount, String inputStr) throws IOException, KeyManagementException, NoSuchAlgorithmException;

    PGResponse CheckTransStatus(PaymentAccount paymentAccount, String inputStr) throws IOException, KeyManagementException, NoSuchAlgorithmException;

    String DoiSoat() throws IOException;

    String DoiSoatGW(String req) throws IOException;
}
