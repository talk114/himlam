package gateway.core.channel.tcb_qrcode.service;

import gateway.core.dto.PGResponse;
import vn.nganluong.naba.entities.ChannelFunction;
import vn.nganluong.naba.entities.PaymentAccount;

public interface TCB_QrcodeService {
    PGResponse GetQrBankCode(ChannelFunction channelFunction, PaymentAccount paymentAccount, String inputStr) throws Exception;
    PGResponse CreateQrCode(ChannelFunction channelFunction, PaymentAccount paymentAccount, String inputStr) throws Exception;
    PGResponse CheckPayment(ChannelFunction channelFunction, PaymentAccount paymentAccount, String inputStr) throws Exception;

    String NotifyTCB_QRCODE(String notify) throws Exception;


}
