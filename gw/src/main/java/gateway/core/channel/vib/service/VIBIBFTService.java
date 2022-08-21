package gateway.core.channel.vib.service;

import gateway.core.dto.PGRequest;
import org.springframework.http.ResponseEntity;

public interface VIBIBFTService {

    String getAccessToken();

    ResponseEntity<?> checkInvalidAccount(PGRequest pgRequest);

    ResponseEntity<?> getAccountBalance(PGRequest pgRequest);

    ResponseEntity<?> addTransaction(PGRequest pgRequest);

    ResponseEntity<?> getTransactionStatus(PGRequest pgRequest);

    ResponseEntity<?> getHistoryTransaction(PGRequest pgRequest);
}
