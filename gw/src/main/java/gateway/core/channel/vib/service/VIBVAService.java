package gateway.core.channel.vib.service;

import gateway.core.dto.PGRequest;
import org.springframework.http.ResponseEntity;

public interface VIBVAService {



    /**
     * 3. Get Trans Hist
     * Get information about transaction history of Real Account.
     * @param pgRequest
     * @return
     */
    ResponseEntity<?> getHistoryTransactionOfRealAccount(PGRequest pgRequest);

    /**
     * 4. Get Trans Hist of VA
     * Get information about transaction history of Virtual Account. (VA CODE)
     * @param pgRequest
     * @return
     */
    ResponseEntity<?> getHistoryTransactionVA(PGRequest pgRequest);

    /**
     * 5. Get Trans Hist Error of VA CODE
     * Get transaction history error of Virtual Account: transaction success but virtual account not exist
     * @param pgRequest
     * @return
     */
    ResponseEntity<?> getErrorHistoryTransactionOfVACode(PGRequest pgRequest);

    /**
     * 6. Get list Virtual Account
     * @param pgRequest
     * @return
     */
    ResponseEntity<?> getListVirtualAccount(PGRequest pgRequest);

    /**
     * 7. Enable/Disable Virtual Account
     *
     * @param pgRequest
     * @return
     */
    ResponseEntity<?> enableVirtualAccount(PGRequest pgRequest);

    /**
     * 8. Create new Virtual Account
     * @param pgRequest
     * @return
     */
    ResponseEntity<?> createVirtualAccount(PGRequest pgRequest);

    /**
     * 9. Delete Virtual account.
     * @param pgRequest
     * @return
     */
    ResponseEntity<?> deleteVirtualAccount(PGRequest pgRequest);

    /**
     * 7. Get Detail Virtual Account
     * @param pgRequest
     * @return
     */
    ResponseEntity<?> getDetailVirtualAccount(PGRequest pgRequest);

    ResponseEntity<?> notifyListener(String signature, String input);
}
