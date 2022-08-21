package gateway.core.channel.msb_offus.service;

import gateway.core.channel.msb_offus.request.NotificationOrderRequest;
import gateway.core.dto.PGResponse;
import java.io.IOException;
import vn.nganluong.naba.entities.ChannelFunction;
import vn.nganluong.naba.entities.PaymentAccount;

public interface MSBOFFUSService {

    PGResponse createOrder(ChannelFunction channelFunction, PaymentAccount paymentAccount, String inputStr);

    PGResponse createOrderEcom(ChannelFunction channelFunction, PaymentAccount paymentAccount, String inputStr);

    PGResponse getTransaction(ChannelFunction channelFunction, PaymentAccount paymentAccount, String inputStr);

    PGResponse getTransactionByCashin(ChannelFunction channelFunction, PaymentAccount paymentAccount, String inputStr);
    
    PGResponse createReversalTransaction(ChannelFunction channelFunction, PaymentAccount paymentAccount, String inputStr);
    
    PGResponse getInquiryRefundTransaction(ChannelFunction channelFunction, PaymentAccount paymentAccount, String inputStr);
    
    NotificationOrderRequest msbOffUsCallbackNL(PaymentAccount paymentAccount, String request) throws IOException;
    
    PGResponse updateTransaction(ChannelFunction channelFunction, PaymentAccount paymentAccount, String inputStr) throws IOException;
}
