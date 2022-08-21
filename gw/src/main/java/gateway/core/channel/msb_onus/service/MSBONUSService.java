package gateway.core.channel.msb_onus.service;

import gateway.core.dto.PGResponse;
import vn.nganluong.naba.entities.ChannelFunction;
import vn.nganluong.naba.entities.PaymentAccount;

public interface MSBONUSService {

    PGResponse createOrder(ChannelFunction channelFunction, PaymentAccount paymentAccount, String inputStr);

    PGResponse verifyTransaction(ChannelFunction channelFunction, PaymentAccount paymentAccount, String inputStr);

    PGResponse getTransaction(ChannelFunction channelFunction, PaymentAccount paymentAccount, String inputStr);

    PGResponse notifyTransStatus(ChannelFunction channelFunction, PaymentAccount paymentAccount, String data);
}
