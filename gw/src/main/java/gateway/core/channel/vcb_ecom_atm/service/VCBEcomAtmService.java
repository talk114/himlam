package gateway.core.channel.vcb_ecom_atm.service;

import gateway.core.dto.PGResponse;

public interface VCBEcomAtmService {
    PGResponse VerifyCard(String inputStr) throws Exception;
    PGResponse VerifyOtp(String inputStr) throws Exception;
    PGResponse Query(String inputStr) throws Exception;
    PGResponse Refund(String inputStr) throws Exception;
    PGResponse VerifyCard2(String inputStr) throws Exception;
    PGResponse VerifyOtp2(String inputStr) throws Exception;
}
