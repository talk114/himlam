package gateway.core.channel.mb_qrcode;


import gateway.core.util.PGSecurity;

import java.security.NoSuchAlgorithmException;

public class MBBankQRCodeSecurity {

    public String dataForVerifyChecksum(String input) throws NoSuchAlgorithmException {
        return PGSecurity.md5(input);
    }
}
