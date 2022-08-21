package vn.nganluong.naba.service;

import gateway.core.dto.PGResponse;
import org.springframework.http.ResponseEntity;
import vn.nganluong.naba.dto.ResponseJson;

public interface CommonPGResponseService {

	public PGResponse returnGatewayRequestSuccessPrefix();

	public ResponseEntity<PGResponse> returnBadGateway();

	public ResponseEntity<PGResponse> returnBadGatewayWithCause(String cause);

	public ResponseEntity<PGResponse> returnBadGatewayWithCause(String cause, String requestId);

	public ResponseEntity<?> returnBadRequets_TransactionExist();

	public ResponseEntity<PGResponse> returnBadRequest_TransactionNotExist();

	public ResponseEntity<?> returnBadRequets_TransactionEmpty();

	public ResponseEntity<?> returnBadRequets_WithCause(String cause);

	public ResponseEntity<?> returnChannelTimeout();

	public ResponseEntity<?> returnChannelBadRequest(String cause);

	public PGResponse returnChannelBadRequestPrefix();
}
