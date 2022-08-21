package vn.nganluong.naba.service.impl;

import gateway.core.dto.PGResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import vn.nganluong.naba.entities.PgErrorDefinition;
import vn.nganluong.naba.repository.PgErrorDefinitionRepository;
import vn.nganluong.naba.service.CommonPGResponseService;
import vn.nganluong.naba.utils.ResponseUtil;

@Service
public class CommonPGResponseServiceImpl implements CommonPGResponseService {

    @Autowired
    private PgErrorDefinitionRepository pgErrorDefinitionRepository;

    @Override
    public PGResponse returnGatewayRequestSuccessPrefix() {
        PGResponse jsonObject = new PGResponse();

        PgErrorDefinition pgErrorDefinition = pgErrorDefinitionRepository
                .findByCodeAndStatus(ResponseUtil.ERROR_CODE_GATEWAY_REQUEST_SUCCESS);
        if (pgErrorDefinition != null) {
            jsonObject.setErrorCode(ResponseUtil.ERROR_CODE_GATEWAY_REQUEST_SUCCESS);
            jsonObject.setMessage(pgErrorDefinition.getMessage());
        } else {
            jsonObject.setErrorCode("00");
            jsonObject.setMessage("Gateway request success");
        }
        return jsonObject;
    }

    @Override
    public ResponseEntity<PGResponse> returnBadGateway() {

        PGResponse jsonObject = new PGResponse();

        PgErrorDefinition pgErrorDefinition = pgErrorDefinitionRepository
                .findByCodeAndStatus(ResponseUtil.ERROR_CODE_GATEWAY_REQUEST_FAIL);
        if (pgErrorDefinition != null) {
            jsonObject.setErrorCode(ResponseUtil.ERROR_CODE_GATEWAY_REQUEST_FAIL);
            jsonObject.setMessage(pgErrorDefinition.getMessage());
        } else {
            jsonObject.setErrorCode("99");
            jsonObject.setMessage("Gateway request fail");
        }
        return new ResponseEntity<PGResponse>(jsonObject, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<PGResponse> returnBadGatewayWithCause(String cause) {
        cause = ": " + cause;

        PGResponse jsonObject = new PGResponse();

        PgErrorDefinition pgErrorDefinition = pgErrorDefinitionRepository
                .findByCodeAndStatus(ResponseUtil.ERROR_CODE_GATEWAY_REQUEST_FAIL);
        if (pgErrorDefinition != null) {
            jsonObject.setErrorCode(ResponseUtil.ERROR_CODE_GATEWAY_REQUEST_FAIL);
            jsonObject.setMessage(pgErrorDefinition.getMessage() + cause);
        } else {
            jsonObject.setErrorCode("99");
            jsonObject.setMessage("Gateway request fail" + cause);
        }
        return new ResponseEntity<PGResponse>(jsonObject, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> returnBadRequets_TransactionExist() {
        PGResponse jsonObject = new PGResponse();

        PgErrorDefinition pgErrorDefinition = pgErrorDefinitionRepository
                .findByCodeAndStatus(ResponseUtil.ERROR_CODE_GATEWAY_TRANSACTION_ALREADY_EXIST);
        if (pgErrorDefinition != null) {
            jsonObject.setErrorCode(ResponseUtil.ERROR_CODE_GATEWAY_TRANSACTION_ALREADY_EXIST);
            jsonObject.setMessage(pgErrorDefinition.getMessage());
        } else {
            jsonObject.setErrorCode(ResponseUtil.ERROR_CODE_GATEWAY_TRANSACTION_ALREADY_EXIST);
            jsonObject.setMessage("Merchant transaction id already exist");
        }
        return new ResponseEntity<PGResponse>(jsonObject, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<PGResponse> returnBadRequest_TransactionNotExist() {
        PGResponse jsonObject = new PGResponse();

        PgErrorDefinition pgErrorDefinition = pgErrorDefinitionRepository
                .findByCodeAndStatus(ResponseUtil.ERROR_CODE_GATEWAY_TRANSACTION_NOT_EXIST);
        if (pgErrorDefinition != null) {
            jsonObject.setErrorCode(ResponseUtil.ERROR_CODE_GATEWAY_TRANSACTION_NOT_EXIST);
            jsonObject.setMessage(pgErrorDefinition.getMessage());
        } else {
            jsonObject.setErrorCode(ResponseUtil.ERROR_CODE_GATEWAY_TRANSACTION_NOT_EXIST);
            jsonObject.setMessage("Merchant transaction id not exist");
        }
        return new ResponseEntity<PGResponse>(jsonObject, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> returnBadRequets_TransactionEmpty() {
        PGResponse jsonObject = new PGResponse();

        PgErrorDefinition pgErrorDefinition = pgErrorDefinitionRepository
                .findByCodeAndStatus(ResponseUtil.ERROR_CODE_GATEWAY_TRANSACTION_IS_EMPTY);
        if (pgErrorDefinition != null) {
            jsonObject.setErrorCode(ResponseUtil.ERROR_CODE_GATEWAY_TRANSACTION_IS_EMPTY);
            jsonObject.setMessage(pgErrorDefinition.getMessage());
        } else {
            jsonObject.setErrorCode(ResponseUtil.ERROR_CODE_GATEWAY_TRANSACTION_IS_EMPTY);
            jsonObject.setMessage("Merchant transaction id is not empty");
        }
        return new ResponseEntity<PGResponse>(jsonObject, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> returnBadRequets_WithCause(String cause) {
        cause = ": " + cause;
        PGResponse jsonObject = new PGResponse();

        PgErrorDefinition pgErrorDefinition = pgErrorDefinitionRepository
                .findByCodeAndStatus(ResponseUtil.ERROR_CODE_BAD_REQUEST);
        if (pgErrorDefinition != null) {
            jsonObject.setErrorCode(ResponseUtil.ERROR_CODE_BAD_REQUEST);
            jsonObject.setMessage(pgErrorDefinition.getMessage() + cause);
        } else {
            jsonObject.setErrorCode(ResponseUtil.ERROR_CODE_BAD_REQUEST);
            jsonObject.setMessage("Bad request" + cause);
        }
        return new ResponseEntity<PGResponse>(jsonObject, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> returnChannelTimeout() {
        PGResponse jsonObject = new PGResponse();

        PgErrorDefinition pgErrorDefinition = pgErrorDefinitionRepository
                .findByCodeAndStatus(ResponseUtil.ERROR_CODE_CHANNEL_REQUEST_TIMEOUT);
        if (pgErrorDefinition != null) {
            jsonObject.setErrorCode(ResponseUtil.ERROR_CODE_CHANNEL_REQUEST_TIMEOUT);
            jsonObject.setMessage(pgErrorDefinition.getMessage());
        } else {
            jsonObject.setErrorCode("CN0001");
            jsonObject.setMessage("Channel request timeout");
        }
        return new ResponseEntity<PGResponse>(jsonObject, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> returnChannelBadRequest(String cause) {
        String appendCause = "";
        if (StringUtils.isNotEmpty(cause)) {
            appendCause = ": " + cause;
        }
        PGResponse jsonObject = new PGResponse();

        PgErrorDefinition pgErrorDefinition = pgErrorDefinitionRepository
                .findByCodeAndStatus(ResponseUtil.ERROR_CODE_CHANNEL_BAD_REQUEST);
        if (pgErrorDefinition != null) {
            jsonObject.setChannelErrorCode(ResponseUtil.ERROR_CODE_CHANNEL_BAD_REQUEST);
            jsonObject.setChannelMessage(pgErrorDefinition.getMessage() + appendCause);
        } else {
            jsonObject.setChannelErrorCode("CN0002");
            jsonObject.setChannelMessage("Channel bad request" + appendCause);
        }

        pgErrorDefinition = pgErrorDefinitionRepository
                .findByCodeAndStatus(ResponseUtil.ERROR_CODE_GATEWAY_REQUEST_SUCCESS);
        jsonObject.setErrorCode(ResponseUtil.ERROR_CODE_GATEWAY_REQUEST_SUCCESS);
        jsonObject.setMessage(pgErrorDefinition.getMessage());

        return new ResponseEntity<PGResponse>(jsonObject, HttpStatus.OK);
    }

    @Override
    public PGResponse returnChannelBadRequestPrefix() {
        PGResponse jsonObject = new PGResponse();

        PgErrorDefinition pgErrorDefinition = pgErrorDefinitionRepository
                .findByCodeAndStatus(ResponseUtil.ERROR_CODE_CHANNEL_BAD_REQUEST);
        if (pgErrorDefinition != null) {
            jsonObject.setErrorCode(ResponseUtil.ERROR_CODE_CHANNEL_BAD_REQUEST);
            jsonObject.setMessage(pgErrorDefinition.getMessage());
        } else {
            jsonObject.setErrorCode("CN0002");
            jsonObject.setMessage("Channel bad request");
        }

        return jsonObject;
    }

    @Override
    public ResponseEntity<PGResponse> returnBadGatewayWithCause(String cause, String requestId) {
        cause = ": " + cause;

        PGResponse jsonObject = new PGResponse();

        PgErrorDefinition pgErrorDefinition = pgErrorDefinitionRepository
                .findByCodeAndStatus(ResponseUtil.ERROR_CODE_GATEWAY_REQUEST_FAIL);
        if (pgErrorDefinition != null) {
            jsonObject.setErrorCode(ResponseUtil.ERROR_CODE_GATEWAY_REQUEST_FAIL);
            jsonObject.setMessage(pgErrorDefinition.getMessage() + cause);
            jsonObject.setRequestId(requestId);
        } else {
            jsonObject.setErrorCode("99");
            jsonObject.setMessage("Gateway request fail" + cause);
            jsonObject.setRequestId(requestId);
        }
        return new ResponseEntity<PGResponse>(jsonObject, HttpStatus.OK);
    }
}
