package vn.nganluong.naba.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.nganluong.naba.entities.Channel;
import vn.nganluong.naba.entities.PaymentAccount;
import vn.nganluong.naba.entities.PgUser;
import vn.nganluong.naba.repository.ChannelRepository;
import vn.nganluong.naba.repository.PaymentAccountRepository;
import vn.nganluong.naba.repository.PgUserRepository;
import vn.nganluong.naba.service.PaymentAccountService;

@Service
@Transactional
public class PaymentAccountServiceImpl implements PaymentAccountService {

    @Autowired
    private ChannelRepository channelRepository;

    @Autowired
    private PaymentAccountRepository paymentAccountRepository;

    @Autowired
    PgUserRepository pgUserRepository;

    @Override
    public PaymentAccount getPaymentAccountByUserCodeAndChannelCode(String userCode, String channelCode) {

        Channel channel = channelRepository.findByCode(channelCode);
        PgUser pgUser = pgUserRepository.findByCode(userCode);

        if (channel != null && pgUser != null) {
            return paymentAccountRepository.findTopByUserIdAndChannelId(pgUser.getId(), channel.getId());
        } else {
            return null;
        }
    }

    @Override
    public PaymentAccount getPaymentAccountByUserCodeAndChannelId(String userCode, Integer channelId) {
        PgUser pgUser = pgUserRepository.findByCode(userCode);

        if (pgUser != null) {
            return paymentAccountRepository.findTopByUserIdAndChannelId(pgUser.getId(), channelId);
        } else {
            return null;
        }
    }

    @Override
    public PaymentAccount getPaymentAccountByUserIdAndChannelId(Integer userId, Integer channelId) {
        return paymentAccountRepository.findTopByUserIdAndChannelId(userId, channelId);
    }

    @Override
    public PaymentAccount getPaymentAccountByMerchantCode(String merchantCode) {
        return paymentAccountRepository.findFirstByMerchantId(merchantCode);
    }
}