package gateway.core.channel.vnpost.service;

import vn.nganluong.naba.entities.PaymentAccount;

public interface VNPOSTService {
    String RedirectChannel(PaymentAccount paymentAccount, String request);
}
