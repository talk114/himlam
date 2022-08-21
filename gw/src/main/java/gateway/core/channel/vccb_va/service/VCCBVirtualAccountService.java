package gateway.core.channel.vccb_va.service;

import gateway.core.dto.PGResponse;
import org.springframework.http.ResponseEntity;
import vn.nganluong.naba.entities.ChannelFunction;

public interface VCCBVirtualAccountService {

    PGResponse viewAccountDetails(ChannelFunction channelFunction, String bodyRequest);

    PGResponse closeVirtualAccount(ChannelFunction channelFunction, String bodyRequest);

    PGResponse createVirtualAccount(ChannelFunction channelFunction, String bodyRequest);

    PGResponse getListVirtualAccount(ChannelFunction channelFunction, String bodyRequest);

    PGResponse updateVirtualAccount(ChannelFunction channelFunction, String bodyRequest);

    PGResponse reopenVirtualAccount(ChannelFunction channelFunction, String bodyRequest);

    PGResponse traceCallBack(ChannelFunction channelFunction, String bodyRequest);

    PGResponse getVAInformationInGateway(String bodyRequest);

    ResponseEntity<?> notifyVAVCCB(String signature, String request);
}
