package vn.nganluong.naba.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import vn.nganluong.naba.dto.ResponseJson;
import vn.nganluong.naba.entities.PgErrorDefinition;
import vn.nganluong.naba.repository.PgErrorDefinitionRepository;
import vn.nganluong.naba.service.CommonResponseService;
import vn.nganluong.naba.utils.ResponseUtil;

@Service
public class CommonResponseServiceImpl implements CommonResponseService {

	@Autowired
	private PgErrorDefinitionRepository pgErrorDefinitionRepository;

	@Override
	public ResponseJson returnGatewayRequestSuccessPrefix() {
		ResponseJson jsonObject = new ResponseJson();

		PgErrorDefinition pgErrorDefinition = pgErrorDefinitionRepository
				.findByCodeAndStatus(ResponseUtil.ERROR_CODE_GATEWAY_REQUEST_SUCCESS);
		if (pgErrorDefinition != null) {
			jsonObject.setError_code(ResponseUtil.ERROR_CODE_GATEWAY_REQUEST_SUCCESS);
			jsonObject.setMessage(pgErrorDefinition.getMessage());
		} else {
			jsonObject.setError_code("000000");
			jsonObject.setMessage("Gateway request success");
		}
		return jsonObject;
	}

	@Override
	public ResponseEntity<?> returnBadGateway() {

		ResponseJson jsonObject = new ResponseJson();

		PgErrorDefinition pgErrorDefinition = pgErrorDefinitionRepository
				.findByCodeAndStatus(ResponseUtil.ERROR_CODE_GATEWAY_REQUEST_FAIL);
		if (pgErrorDefinition != null) {
			jsonObject.setError_code(ResponseUtil.ERROR_CODE_GATEWAY_REQUEST_FAIL);
			jsonObject.setMessage(pgErrorDefinition.getMessage());
		} else {
			jsonObject.setError_code("999999");
			jsonObject.setMessage("Gateway request fail");
		}
		return new ResponseEntity<ResponseJson>(jsonObject, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> returnBadRequets_TransactionExist() {
		ResponseJson jsonObject = new ResponseJson();

		PgErrorDefinition pgErrorDefinition = pgErrorDefinitionRepository
				.findByCodeAndStatus(ResponseUtil.ERROR_CODE_GATEWAY_TRANSACTION_ALREADY_EXIST);
		if (pgErrorDefinition != null) {
			jsonObject.setError_code(ResponseUtil.ERROR_CODE_GATEWAY_TRANSACTION_ALREADY_EXIST);
			jsonObject.setMessage(pgErrorDefinition.getMessage());
		} else {
			jsonObject.setError_code(ResponseUtil.ERROR_CODE_GATEWAY_TRANSACTION_ALREADY_EXIST);
			jsonObject.setMessage("Merchant transaction id already exist");
		}
		return new ResponseEntity<ResponseJson>(jsonObject, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> returnBadRequets_TransactionEmpty() {
		ResponseJson jsonObject = new ResponseJson();

		PgErrorDefinition pgErrorDefinition = pgErrorDefinitionRepository
				.findByCodeAndStatus(ResponseUtil.ERROR_CODE_GATEWAY_TRANSACTION_IS_EMPTY);
		if (pgErrorDefinition != null) {
			jsonObject.setError_code(ResponseUtil.ERROR_CODE_GATEWAY_TRANSACTION_IS_EMPTY);
			jsonObject.setMessage(pgErrorDefinition.getMessage());
		} else {
			jsonObject.setError_code(ResponseUtil.ERROR_CODE_GATEWAY_TRANSACTION_IS_EMPTY);
			jsonObject.setMessage("Merchant transaction id is not empty");
		}
		return new ResponseEntity<ResponseJson>(jsonObject, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> returnBadRequets_WithCause(String cause) {
		cause = ": " + cause;
		ResponseJson jsonObject = new ResponseJson();

		PgErrorDefinition pgErrorDefinition = pgErrorDefinitionRepository
				.findByCodeAndStatus(ResponseUtil.ERROR_CODE_BAD_REQUEST);
		if (pgErrorDefinition != null) {
			jsonObject.setError_code(ResponseUtil.ERROR_CODE_BAD_REQUEST);
			jsonObject.setMessage(pgErrorDefinition.getMessage() + cause);
		} else {
			jsonObject.setError_code(ResponseUtil.ERROR_CODE_BAD_REQUEST);
			jsonObject.setMessage("Bad request" + cause);
		}
		return new ResponseEntity<ResponseJson>(jsonObject, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> returnChannelTimeout() {
		ResponseJson jsonObject = new ResponseJson();

		PgErrorDefinition pgErrorDefinition = pgErrorDefinitionRepository
				.findByCodeAndStatus(ResponseUtil.ERROR_CODE_CHANNEL_REQUEST_TIMEOUT);
		if (pgErrorDefinition != null) {
			jsonObject.setError_code(ResponseUtil.ERROR_CODE_CHANNEL_REQUEST_TIMEOUT);
			jsonObject.setMessage(pgErrorDefinition.getMessage());
		} else {
			jsonObject.setError_code("CN0001");
			jsonObject.setMessage("Channel request timeout");
		}
		return new ResponseEntity<ResponseJson>(jsonObject, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> returnChannelBadRequest(String cause) {
		String appendCause = "";
		if (StringUtils.isNotEmpty(cause)) {
			appendCause = ": " + cause;
		}
		ResponseJson jsonObject = new ResponseJson();

		PgErrorDefinition pgErrorDefinition = pgErrorDefinitionRepository
				.findByCodeAndStatus(ResponseUtil.ERROR_CODE_CHANNEL_BAD_REQUEST);
		if (pgErrorDefinition != null) {
			jsonObject.setBank_error_code(ResponseUtil.ERROR_CODE_CHANNEL_BAD_REQUEST);
			jsonObject.setBank_message(pgErrorDefinition.getMessage() + appendCause);
		} else {
			jsonObject.setBank_error_code("CN0002");
			jsonObject.setBank_message("Channel bad request" + appendCause);
		}

		pgErrorDefinition = pgErrorDefinitionRepository
				.findByCodeAndStatus(ResponseUtil.ERROR_CODE_GATEWAY_REQUEST_SUCCESS);
		jsonObject.setError_code(ResponseUtil.ERROR_CODE_GATEWAY_REQUEST_SUCCESS);
		jsonObject.setMessage(pgErrorDefinition.getMessage());

		return new ResponseEntity<ResponseJson>(jsonObject, HttpStatus.OK);
	}

	@Override
	public ResponseJson returnChannelBadRequestPrefix() {
		ResponseJson jsonObject = new ResponseJson();

		PgErrorDefinition pgErrorDefinition = pgErrorDefinitionRepository
				.findByCodeAndStatus(ResponseUtil.ERROR_CODE_CHANNEL_BAD_REQUEST);
		if (pgErrorDefinition != null) {
			jsonObject.setError_code(ResponseUtil.ERROR_CODE_CHANNEL_BAD_REQUEST);
			jsonObject.setMessage(pgErrorDefinition.getMessage());
		} else {
			jsonObject.setError_code("CN0002");
			jsonObject.setMessage("Channel bad request");
		}

		return jsonObject;
	}

	@Override
	public ResponseJson returnReconciliationGatewayFail() {
		ResponseJson jsonObject = new ResponseJson();
		jsonObject.setError_code("51");
		jsonObject.setMessage("Reconciliation fail. Can't created reconciliation file");

//		PgErrorDefinition pgErrorDefinition = pgErrorDefinitionRepository
//				.findByCodeAndStatus(ResponseUtil.ERROR_CODE_CHANNEL_REQUEST_TIMEOUT);
//		if (pgErrorDefinition != null) {
//			jsonObject.setError_code(ResponseUtil.ERROR_CODE_CHANNEL_REQUEST_TIMEOUT);
//			jsonObject.setMessage(pgErrorDefinition.getMessage());
//		} else {
//			jsonObject.setError_code("51");
//			jsonObject.setMessage("Reconciliation fail. Can't created reconciliation file");
//		}
		return jsonObject;
		// return new ResponseEntity<ResponseJson>(jsonObject, HttpStatus.OK);
	}

}
