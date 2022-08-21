/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gateway.core.channel.tcb.service;

import gateway.core.dto.PGResponse;
import vn.nganluong.naba.entities.ChannelFunction;
import vn.nganluong.naba.entities.PaymentAccount;

/**
 * @author taind
 */
public interface TCBService {

    PGResponse InqListBankInfo(ChannelFunction channelFunction, PaymentAccount paymentAccount, String inputStr) throws Exception;

    PGResponse FundTransfer(ChannelFunction channelFunction, PaymentAccount paymentAccount, String inputStr) throws Exception;

    PGResponse InqTransactionStatus(ChannelFunction channelFunction, PaymentAccount paymentAccount, String inputStr) throws Exception;

    PGResponse InqBatchTxnStatus(ChannelFunction channelFunction, PaymentAccount paymentAccount, String inputStr) throws Exception;

    String updateTransStatus(String req, ChannelFunction channelFunction) throws Exception;
}
