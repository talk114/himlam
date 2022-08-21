package gateway.core.channel.bidv.bidv_transfer_247.service;

import gateway.core.dto.PGResponse;
import vn.nganluong.naba.entities.PaymentAccount;

public interface BIDVTransfer247Service {
    PGResponse getFileBIDVDaily(String request) throws Exception;
    PGResponse getListBank247(PaymentAccount paymentAccount, String request) throws Exception;
    PGResponse getName247(PaymentAccount paymentAccount, String request) throws Exception;
    PGResponse tranfer2Bank247(PaymentAccount paymentAccount, String request) throws Exception;
    PGResponse getNameBidv(PaymentAccount paymentAccount, String request) throws Exception;
    PGResponse inquery(PaymentAccount paymentAccount, String inputStr) throws Exception;
    PGResponse checkProviderBalance(PaymentAccount paymentAccount, String request) throws Exception;
}
