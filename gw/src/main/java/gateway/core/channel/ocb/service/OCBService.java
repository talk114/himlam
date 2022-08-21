package gateway.core.channel.ocb.service;

import gateway.core.dto.PGResponse;
import vn.nganluong.naba.entities.ChannelFunction;
import vn.nganluong.naba.entities.PaymentAccount;

public interface OCBService {
    PGResponse paymentStep1(ChannelFunction channelFunction, PaymentAccount paymentAccount, String req);

    PGResponse paymentStep2(ChannelFunction channelFunction, PaymentAccount paymentAccount, String req);

    PGResponse statusPayment(ChannelFunction channelFunction, PaymentAccount paymentAccount, String req);

    PGResponse resendOTP(ChannelFunction channelFunction, PaymentAccount paymentAccount, String req);

    PGResponse checkTransHistory(ChannelFunction channelFunction, PaymentAccount paymentAccount, String req);
}
