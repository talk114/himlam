package vn.nganluong.naba.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vn.nganluong.naba.dao.VirtualAccountDao;
import vn.nganluong.naba.dto.VirtualAccountDto;
import vn.nganluong.naba.entities.Channel;
import vn.nganluong.naba.entities.VirtualAccount;
import vn.nganluong.naba.repository.ChannelRepository;
import vn.nganluong.naba.repository.VirtualAccountRepository;
import vn.nganluong.naba.service.VirtualAccountService;

@Service
@Transactional
public class VirtualAccountServiceImpl implements VirtualAccountService {

    @Autowired
    private VirtualAccountDao virtualAccountDao;

    @Autowired
    private VirtualAccountRepository virtualAccountRepository;

    @Autowired
    private ChannelRepository channelRepository;

    @Override
    public boolean createVirtualAccount(VirtualAccountDto virtualAccountDto) {
        try {
            Channel channel = channelRepository.findById(virtualAccountDto.getChannelId()).get();
            VirtualAccount virtualAccount = new VirtualAccount(channel, virtualAccountDto.getVirtualAccountNo(),
                    virtualAccountDto.getVirtualAccountName(), virtualAccountDto.getMerchantCode(),
                    virtualAccountDto.getPhoneNumber(), true, virtualAccountDto.getDescription(), null, null, true);
            virtualAccountRepository.save(virtualAccount);
            return true;
            // return virtualAccountDao.createVirtualAccount(virtualAccountDto);
        } catch (Exception e) {
            return false;
        }

    }

    @Override
    public boolean deleteVirtualAccount(String virtualAccountNo) {

        VirtualAccount virtualAccount = virtualAccountDao.findVirtualAccount(virtualAccountNo);
        if (virtualAccount != null) {
            virtualAccount.setStatus(false);
            virtualAccountRepository.save(virtualAccount);
            return true;
        }

        return false;
    }

    @Override
    public boolean updateStatusVirtualAccount(String virtualAccountNo, boolean status) {
        VirtualAccount virtualAccount = virtualAccountDao.findVirtualAccount(virtualAccountNo);
        if (virtualAccount != null) {
            virtualAccount.setEnable(status);
            virtualAccountRepository.save(virtualAccount);
            return true;
        }

        return false;
    }

    @Override
    public VirtualAccount findVirtualAccount(String virtualAccountCode) {
        return virtualAccountDao.findVirtualAccount(virtualAccountCode);
    }

    @Override
    public boolean isExistVirtualAccount(String virtualAccountCode) {
        if (virtualAccountDao.findVirtualAccount(virtualAccountCode) == null) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void updateVirtualAccount(VirtualAccount virtualAccount) {
        if (virtualAccount != null) {
            virtualAccountRepository.save(virtualAccount);
        }
    }
}