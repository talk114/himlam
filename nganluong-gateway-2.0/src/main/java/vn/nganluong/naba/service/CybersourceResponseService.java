package vn.nganluong.naba.service;

import gateway.core.dto.PGResponse;
import vn.nganluong.naba.utils.CybersourceError;

public interface CybersourceResponseService {

    public PGResponse validateResponse(CybersourceError response);
}
