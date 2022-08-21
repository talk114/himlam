package gateway.core.channel.bidv_ecom.safety;

import gateway.core.util.PGSecurity;
import org.apache.commons.lang3.StringUtils;

import java.security.NoSuchAlgorithmException;

/**
 * @author sonln@nganluong.vn
 */
public class BidvEcomSecurity {
    public static String md5(String[] param, String keyEncrypt) throws NoSuchAlgorithmException {
        String params = keyEncrypt + "|" + StringUtils.join(param, "|");
        return PGSecurity.md5(params);
    }
}
