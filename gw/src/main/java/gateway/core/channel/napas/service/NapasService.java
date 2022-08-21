package gateway.core.channel.napas.service;

import gateway.core.dto.PGResponse;
import vn.nganluong.naba.entities.ChannelFunction;
import vn.nganluong.naba.entities.PaymentAccount;

import javax.servlet.http.HttpServletResponse;

public interface NapasService {

    PGResponse purchaseOtp(ChannelFunction channelFunction, PaymentAccount paymentAccount, String req) throws Exception;

    PGResponse resultPurchaseOtp(String req) throws Exception;

    PGResponse queryTransDomestic(ChannelFunction channelFunction, String req) throws Exception;

    PGResponse refundDomestic(ChannelFunction channelFunction, String req) throws Exception;

}
