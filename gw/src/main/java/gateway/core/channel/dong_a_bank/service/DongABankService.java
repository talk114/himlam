package gateway.core.channel.dong_a_bank.service;

import gateway.core.dto.PGResponse;
import vn.nganluong.naba.entities.ChannelFunction;
import vn.nganluong.naba.entities.PaymentAccount;
import vn.nganluong.naba.entities.PgUser;

import javax.xml.rpc.ServiceException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.SignatureException;
import java.text.ParseException;

public interface DongABankService {

    PGResponse createOrder(ChannelFunction channelFunction, PaymentAccount paymentAccount, String inputStr)
            throws IOException, ParseException, ServiceException, SignatureException;

    PGResponse updateOrderStatus(ChannelFunction channelFunction, PaymentAccount paymentAccount, String inputStr)
            throws IOException, ServiceException, SignatureException;

    String notifyEcom( PaymentAccount paymentAccount, PgUser pgUser, String input) throws IOException, GeneralSecurityException;

    PGResponse checkOderStatus(ChannelFunction channelFunction, PaymentAccount paymentAccount, String inputStr) throws IOException,ServiceException, SignatureException;

    PGResponse getBillInfo(ChannelFunction channelFunction, PaymentAccount paymentAccount, String inputStr) throws IOException,ServiceException, SignatureException;

    PGResponse getBillInfoNL(ChannelFunction channelFunction, PaymentAccount paymentAccount, String inputStr) throws IOException,ServiceException, SignatureException;

    PGResponse getListBillingSize(ChannelFunction channelFunction, PaymentAccount paymentAccount, String inputStr) throws IOException,ServiceException, SignatureException;
}
