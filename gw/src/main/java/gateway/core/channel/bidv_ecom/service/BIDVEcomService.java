package gateway.core.channel.bidv_ecom.service;

import gateway.core.channel.bidv_ecom.dto.BidvCallbackRes;
import gateway.core.dto.PGResponse;
import vn.nganluong.naba.entities.ChannelFunction;
import vn.nganluong.naba.entities.PaymentAccount;

/**
 * @author sonln@nganluong.vn
 */
public interface BIDVEcomService {
    PGResponse inittrans(ChannelFunction channelFunction, PaymentAccount paymentAccount, String fnc, String req) throws Exception;

    PGResponse verify(ChannelFunction channelFunction, PaymentAccount paymentAccount, String fnc, String req) throws Exception;

    PGResponse inquiry(ChannelFunction channelFunction, PaymentAccount paymentAccount, String fnc, String req) throws Exception;

    BidvCallbackRes BidvNotify(String req) throws Exception;
}
