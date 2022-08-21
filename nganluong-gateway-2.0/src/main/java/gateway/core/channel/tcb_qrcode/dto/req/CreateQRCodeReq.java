package gateway.core.channel.tcb_qrcode.dto.req;

import lombok.Data;

@Data
public class CreateQRCodeReq {
    private String  service_type_code;
    private String  service_code;
    private String  method_code;
    private  long   merchant_id;
    private String  merchant_request_id;
    private  long   amount;
    private String  bank_code;
    private String  description;
    private String  currency;
    private String  url_redirect;
    private String  url_notify;
    private String  url_cancel;
    private String  customer_fullname;
    private String  customer_email;
    private String  customer_mobile;

}
