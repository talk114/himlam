package vn.nganluong.naba.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.nganluong.naba.entities.Channel;
import vn.nganluong.naba.entities.PgFunction;
import vn.nganluong.naba.repository.ChannelRepository;
import vn.nganluong.naba.repository.PgFunctionRepository;
import vn.nganluong.naba.service.PgFunctionService;

@Service
@Transactional
public class PgFunctionServiceImpl implements PgFunctionService {

    @Autowired
    PgFunctionRepository pgFunctionRepository;

    @Autowired
    ChannelRepository channelRepository;

    @Override
    public PgFunction findByCodeAndChannelCode(String code, String channelCode) {

        Integer channelId = null;
        Channel channel = channelRepository.findFirstByCodeAndStatus(channelCode, true);
        if (channel != null) {
            channelId = channel.getId();
        }
        return pgFunctionRepository.findFirstByCodeAndChannelIdAndStatus(code, channelId, true);
    }

    @Override
    public PgFunction findPgFunctionByCode(String pgFuntion) {
        return pgFunctionRepository.findPgFunctionByCode(pgFuntion);
    }
}