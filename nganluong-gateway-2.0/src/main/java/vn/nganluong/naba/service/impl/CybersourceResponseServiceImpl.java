package vn.nganluong.naba.service.impl;

import gateway.core.dto.PGResponse;
import org.springframework.stereotype.Service;
import vn.nganluong.naba.service.CybersourceResponseService;
import vn.nganluong.naba.utils.CybersourceError;

@Service
public class CybersourceResponseServiceImpl implements CybersourceResponseService {

    @Override
    public PGResponse validateResponse(CybersourceError response) {
        PGResponse responseError = new PGResponse();
        if (response.equals(CybersourceError.CURRENCY_EMPTY)) {
            responseError.setErrorCode(CybersourceError.CURRENCY_EMPTY.getCode());
            responseError.setMessage(CybersourceError.CURRENCY_EMPTY.getMessage());
        }
        return responseError;
    }
}