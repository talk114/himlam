package gateway.core.channel.msb_ecom.service;

import gateway.core.dto.PGResponse;
import vn.nganluong.naba.entities.ChannelFunction;
import vn.nganluong.naba.entities.PaymentAccount;

public interface MSBEcomService {

    PGResponse createPayment(ChannelFunction channelFunction, PaymentAccount paymentAccount, String inputStr);

    PGResponse verifyTransaction(ChannelFunction channelFunction, PaymentAccount paymentAccount, String inputStr);

    PGResponse resendOTP(ChannelFunction channelFunction, PaymentAccount paymentAccount, String inputStr);

    PGResponse inquiryTransaction(ChannelFunction channelFunction, PaymentAccount paymentAccount, String inputStr);
}
