package gateway.core.channel.mb.service;

import gateway.core.dto.PGResponse;
import vn.nganluong.naba.entities.ChannelFunction;
import vn.nganluong.naba.entities.PaymentAccount;

public interface MBEcomService {


    PGResponse addTransaction(ChannelFunction channelFunction, PaymentAccount paymentAccount, String inputStr) throws
            Exception;

    PGResponse confirmTransaction(ChannelFunction channelFunction, PaymentAccount paymentAccount,
                                  String inputStr) throws Exception;

    PGResponse revertTransaction(ChannelFunction channelFunction, PaymentAccount paymentAccount, String inputStr) throws
            Exception;

    PGResponse statusTransaction(ChannelFunction channelFunction, PaymentAccount paymentAccount, String inputStr) throws
            Exception;

    PGResponse refund(ChannelFunction channelFunction, PaymentAccount paymentAccount, String inputStr) throws
            Exception;
}
