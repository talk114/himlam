package gateway.core.channel.onepay.service;

import gateway.core.dto.PGResponse;
import vn.nganluong.naba.entities.ChannelFunction;
import vn.nganluong.naba.entities.PaymentAccount;

public interface OnePayService {

    PGResponse verifyCard(ChannelFunction channelFunction, PaymentAccount paymentAccount, String inputStr) throws Exception;
    PGResponse verifyAuthen(ChannelFunction channelFunction, PaymentAccount paymentAccount, String reqStr) throws
            Exception;
    PGResponse query(ChannelFunction channelFunction, PaymentAccount paymentAccount, String reqStr) throws Exception;
    PGResponse refund(ChannelFunction channelFunction, PaymentAccount paymentAccount, String input) throws Exception;
}
