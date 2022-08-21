package gateway.core.service;

import gateway.core.dto.PGRequest;
import gateway.core.util.PGSecurity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import vn.nganluong.naba.entities.PgEndpoint;

import java.security.NoSuchAlgorithmException;

public class PGValidate {

    private static final Logger logger = LogManager.getLogger(PGValidate.class);

//    public static boolean validateChecksum(PGRequest req, PgEndpoint endpoint) {
//        String checksum = null;
//        try {
//            String input = req.getData() + endpoint.getAuthKey();
//            checksum = PGSecurity.sha256(input);
//        } catch (NoSuchAlgorithmException e) {
//            logger.error(e);
//            return false;
//        }
//        return checksum.equalsIgnoreCase(req.getChecksum());
//    }
    public static boolean validateChecksum(PGRequest req, PgEndpoint endpoint) {
        try {
            String input = req.getData() + endpoint.getAuthKey();
            String checksumSha256 = PGSecurity.sha256(input);
            String checksumMD5 = PGSecurity.md5(input);

            if(checksumSha256.equalsIgnoreCase(req.getChecksum())){
                return true;
            }
            if(checksumMD5.equalsIgnoreCase(req.getChecksum())){
                return true;
            }
        } catch (NoSuchAlgorithmException e) {
            logger.error(e);
            return false;
        }
        return false;
    }
}
