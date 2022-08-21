package gateway.core.channel.migs.service;

import gateway.core.dto.PGResponse;
import vn.nganluong.naba.entities.ChannelFunction;
import vn.nganluong.naba.entities.PaymentAccount;

public interface MIGSService {
    PGResponse initiateAuthentication(ChannelFunction channelFunction, PaymentAccount paymentAccount, String inputStr) throws Exception;

    PGResponse authenticatePayer(ChannelFunction channelFunction, PaymentAccount paymentAccount, String inputStr) throws Exception;

    PGResponse check3DSEnrollment(ChannelFunction channelFunction, PaymentAccount paymentAccount, String inputStr) throws Exception;

    PGResponse processAcsResult(ChannelFunction channelFunction, PaymentAccount paymentAccount, String inputStr) throws Exception;

    PGResponse pay3DS2(ChannelFunction channelFunction, PaymentAccount paymentAccount, String inputStr) throws Exception;

    PGResponse pay3DS1(ChannelFunction channelFunction, PaymentAccount paymentAccount, String inputStr) throws Exception;

    PGResponse refundTrans(ChannelFunction channelFunction, PaymentAccount paymentAccount, String inputStr) throws Exception;

    PGResponse queryTrans(ChannelFunction channelFunction, PaymentAccount paymentAccount, String inputStr) throws Exception;

    String resultCheckEnroll(String request);
}
