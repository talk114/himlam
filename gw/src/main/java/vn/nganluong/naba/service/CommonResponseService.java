package vn.nganluong.naba.service;

import org.springframework.http.ResponseEntity;

import vn.nganluong.naba.dto.ResponseJson;

public interface CommonResponseService {

	public ResponseJson returnGatewayRequestSuccessPrefix();

	public ResponseEntity<?> returnBadGateway();

	public ResponseEntity<?> returnBadRequets_TransactionExist();

	public ResponseEntity<?> returnBadRequets_TransactionEmpty();

	public ResponseEntity<?> returnBadRequets_WithCause(String cause);

	public ResponseEntity<?> returnChannelTimeout();

	public ResponseEntity<?> returnChannelBadRequest(String cause);

	public ResponseJson returnChannelBadRequestPrefix();

	public ResponseJson returnReconciliationGatewayFail();
}
