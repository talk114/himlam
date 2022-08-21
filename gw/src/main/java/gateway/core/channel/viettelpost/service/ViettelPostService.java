package gateway.core.channel.viettelpost.service;

import vn.nganluong.naba.entities.PaymentAccount;

public interface ViettelPostService {
    String ProcessRequest(PaymentAccount paymentAccount, String request) throws Exception;
}
