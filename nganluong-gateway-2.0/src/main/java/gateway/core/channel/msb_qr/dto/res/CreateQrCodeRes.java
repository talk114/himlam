package gateway.core.channel.msb_qr.dto.res;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateQrCodeRes implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String code;

    private String message;

    private String data;

    private String url;

    private boolean isDelete;

    private String idQrcode;

    private String checksum;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @JsonProperty("qrData")
    public String getData() {
        return data;
    }

    @JsonProperty("data")
    public void setData(String data) {
        this.data = data;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isDelete() {
        return isDelete;
    }

    public void setDelete(boolean isDelete) {
        this.isDelete = isDelete;
    }

    public String getIdQrcode() {
        return idQrcode;
    }

    public void setIdQrcode(String idQrcode) {
        this.idQrcode = idQrcode;
    }

    public String getChecksum() {
        return checksum;
    }

    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }

    private static final String CHARACTER_SUM = "|";

    public String rawData(String secretKey) {
        return code + CHARACTER_SUM + message + CHARACTER_SUM + data + CHARACTER_SUM + "" + CHARACTER_SUM + secretKey;
    }

    public static void main(String args[]) throws JsonParseException, JsonMappingException, IOException {
        String f = "{\"code\":\"00\",\"message\":\"Success\",\"data\":\"00020101021126220006970426010820192019520401025303704540510000550105802VN5909NGANLUONG6005HANOI6248011202BILL7760530313NGANLUONG20190706NL20190801F630409D1\",\"url\":\"\",\"checksum\":\"B1E3DB7A9DA59300C76629F74783B495\",\"isDelete\":true,\"idQrcode\":\"5769\"}";
        ObjectMapper mapper = new ObjectMapper();
        CreateQrCodeRes res = mapper.readValue(f, CreateQrCodeRes.class);
        System.out.println(res.getChecksum());
    }
}
