package gateway.core.channel.msb_qr.service;

import gateway.core.dto.PGResponse;
import vn.nganluong.naba.entities.ChannelFunction;
import vn.nganluong.naba.entities.PaymentAccount;

public interface MSBQRCodeService {

    PGResponse QrCodePayment(Integer channelId, PaymentAccount paymentAccount, String inputStr);
    PGResponse CheckQrOrder(PaymentAccount paymentAccount, String inputStr);

    String CompleteQrPayment(PaymentAccount paymentAccount, String data);
}
