package gateway.core.channel.napas.service;

import gateway.core.dto.PGResponse;
import vn.nganluong.naba.entities.ChannelFunction;
import vn.nganluong.naba.entities.PaymentAccount;

import javax.servlet.http.HttpServletResponse;

/**
 * @author sonln
 */
public interface NapasWLService {
    PGResponse queryTransDomesticWL(ChannelFunction channelFunction, String req) throws Exception;

    PGResponse refundDomesticWL(ChannelFunction channelFunction, String req) throws Exception;

    PGResponse purchaseOtpWL(ChannelFunction channelFunction, PaymentAccount paymentAccount, String req);

    String returnUrlPurchaseOtpWL(String request);

    PGResponse ipnPurchaseOtpWL(String req) throws Exception;
}
