package vn.nganluong.naba.service;

import vn.nganluong.naba.entities.PaymentAccount;

public interface PaymentAccountService {

	PaymentAccount getPaymentAccountByUserCodeAndChannelCode(String userCode, String channelCode);

	PaymentAccount getPaymentAccountByUserCodeAndChannelId(String userCode, Integer channelId);

	PaymentAccount getPaymentAccountByUserIdAndChannelId(Integer userId, Integer channelId);

	PaymentAccount getPaymentAccountByMerchantCode(String merchantCode);

}