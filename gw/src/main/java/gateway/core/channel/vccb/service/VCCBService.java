package gateway.core.channel.vccb.service;

import gateway.core.channel.vccb.dto.res.ApiResponse;
import gateway.core.dto.PGResponse;
import vn.nganluong.naba.entities.ChannelFunction;

import java.io.IOException;
import java.security.GeneralSecurityException;

public interface VCCBService {

    /**
     * Kiem tra so du tai khoan
     * @param channelFunction
     * @param inputStr
     * @return
     * @throws Exception
     */
    PGResponse CheckBalance(ChannelFunction channelFunction, String inputStr) throws Exception;

    /**
     * Truy van tai khoan IBFT on-us
     * @param channelFunction
     * @param request
     * @return
     * @throws Exception
     */
    PGResponse CheckCardVCCB(ChannelFunction channelFunction, String request) throws Exception;


    /**
     * Truy van tai khoan IBFT off-us
     * @param channelFunction
     * @param request
     * @return
     * @throws Exception
     */
    PGResponse CheckBankAccVCCB(ChannelFunction channelFunction, String request) throws Exception;

    PGResponse CheckCardIBFT(ChannelFunction channelFunction, String request) throws Exception;

    PGResponse CheckBankAccIBFT(ChannelFunction channelFunction, String request) throws Exception;

    PGResponse TransferCardVCCB(ChannelFunction channelFunction, String request) throws Exception;
    PGResponse TransferBankAccVCCB(ChannelFunction channelFunction, String request) throws Exception;
    PGResponse TransferCardIBFT(ChannelFunction channelFunction, String request) throws Exception;
    PGResponse TransferBankAccIBFT(ChannelFunction channelFunction, String request) throws Exception;
    PGResponse CheckBalanceBankAccIBFT(ChannelFunction channelFunction, String request) throws Exception;
    <T extends ApiResponse> PGResponse UploadReconciliationNGLA(ChannelFunction channelFunction, String request) throws
            Exception;
    PGResponse UploadReconciliationNGLB(ChannelFunction channelFunction, String request) throws Exception;

}
