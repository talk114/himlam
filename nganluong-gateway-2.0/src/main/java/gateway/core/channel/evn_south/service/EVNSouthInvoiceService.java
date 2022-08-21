package gateway.core.channel.evn_south.service;

import gateway.core.dto.PGResponse;
import vn.nganluong.naba.entities.ChannelFunction;

public interface EVNSouthInvoiceService {
    PGResponse getCustomerFeeInfo(ChannelFunction channelFunction, String data);

    PGResponse payBillFeesByCustomerCode(ChannelFunction channelFunction, String data);

    PGResponse cancelBillFeesByCustomerCode(ChannelFunction channelFunction, String data);

    /* GetPhieuThuTienDichVu*/
    PGResponse getReceipt(ChannelFunction channelFunction, String data);

    PGResponse checkBill(ChannelFunction channelFunction, String data);

    PGResponse getBillByBank(ChannelFunction channelFunction, String data);
}
