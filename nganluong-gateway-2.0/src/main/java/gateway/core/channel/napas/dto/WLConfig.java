package gateway.core.channel.napas.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author sonln@nganluong.vn
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WLConfig {
    private String mid;
    private String user;
    private String pass;
    private String clientSecret;

    public static WLConfig getConfigService(String mid) {
        switch (mid) {
            case NapasConstans.MERCHANT_ID_WL1:
                return new WLConfig(
                        NapasConstans.MERCHANT_ID_WL1,
                        NapasConstans.USER_NAME_WL1,
                        NapasConstans.PASSWORD_WL1,
                        NapasConstans.CLIENT_SECRET_KEY_WL1);
            case NapasConstans.MERCHANT_ID_WL2:
                return new WLConfig(
                        NapasConstans.MERCHANT_ID_WL2,
                        NapasConstans.USER_NAME_WL2,
                        NapasConstans.PASSWORD_WL2,
                        NapasConstans.CLIENT_SECRET_KEY_WL2);
            case NapasConstans.MERCHANT_ID_WL3:
                return new WLConfig(
                        NapasConstans.MERCHANT_ID_WL3,
                        NapasConstans.USER_NAME_WL3,
                        NapasConstans.PASSWORD_WL3,
                        NapasConstans.CLIENT_SECRET_KEY_WL3);
            default:
                return new WLConfig();
        }

    }
}
