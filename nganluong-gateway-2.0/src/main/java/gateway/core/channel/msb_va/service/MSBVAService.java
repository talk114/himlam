package gateway.core.channel.msb_va.service;

import gateway.core.dto.PGResponse;
import vn.nganluong.naba.entities.ChannelFunction;

/**
 * @author sonln
 */
public interface MSBVAService {
    PGResponse loginMSB(ChannelFunction channelFunction) throws Exception;

    PGResponse getTransactionHistory(ChannelFunction channelFunction, String req) throws Exception;

    PGResponse createMSBVA(ChannelFunction channelFunction, String req) throws Exception;

    PGResponse updateMSBVA(ChannelFunction channelFunction, String req) throws Exception;

    void MSBVANotify(String req) throws Exception;

}
