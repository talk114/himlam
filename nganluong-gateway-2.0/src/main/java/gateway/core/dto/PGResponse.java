package gateway.core.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.ToString;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"status", "errorCode", "message", "channelErrorCode", "channelMessage", "description", "data"})
@Builder
@ToString
public class PGResponse {

    protected boolean status;

    protected String message;

    protected String description;

    @JsonProperty(value = "error_code")
    protected String errorCode;

    @JsonProperty(value = "channel_error_code")
    protected String channelErrorCode;

    @JsonProperty(value = "channel_message")
    protected String channelMessage;

    protected Object data;

    @JsonProperty(value = "request_id")
    protected String requestId;


    public PGResponse() {
    }

    public PGResponse(boolean status, String message, String description, String errorCode, String channelErrorCode, String channelMessage, Object data, String requestId) {
        this.status = status;
        this.message = message;
        this.description = description;
        this.errorCode = errorCode;
        this.channelErrorCode = channelErrorCode;
        this.channelMessage = channelMessage;
        this.data = data;
        this.requestId = requestId;
    }

    public PGResponse(boolean status, String message, String description, String errorCode, String channelErrorCode, String channelMessage, Object data) {
        this.status = status;
        this.message = message;
        this.description = description;
        this.errorCode = errorCode;
        this.channelErrorCode = channelErrorCode;
        this.channelMessage = channelMessage;
        this.data = data;
    }

    private PGResponse(boolean status, String message, String errorCode) {
        this.status = status;
        this.message = message;
        this.errorCode = errorCode;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getChannelErrorCode() {
        return channelErrorCode;
    }

    public void setChannelErrorCode(String channelErrorCode) {
        this.channelErrorCode = channelErrorCode != null ? channelErrorCode : "";
    }

    public String getChannelMessage() {
        return channelMessage;
    }

    public void setChannelMessage(String channelMessage) {
        this.channelMessage = channelMessage != null ? channelMessage : "";
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    @Override
    public String toString() {
        return "PGResponse{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", description='" + description + '\'' +
                ", errorCode='" + errorCode + '\'' +
                ", channelErrorCode='" + channelErrorCode + '\'' +
                ", channelMessage='" + channelMessage + '\'' +
                ", data=" + data +
                ", requestId='" + requestId + '\'' +
                '}';
    }

    /**
     * @author dungla@nganluong.vn
     */

    public static final String SUCCESS = "00";
    public static final String FAIL = "99";
    public static final String DATA_INVALID = "98";
    public static final String DATA_VALIDATION_FAILED = "97";
    public static final String CHANNEL_TIMEOUT = "11";
    public static final String REQUEST_TO_CHANNEL_INVALID = "12";
    public static final String TRANSACTION_EXIST = "21";
    public static final String TRANSACTION_NULL = "22";
    public static final String BAD_REQUEST = "01";
    public static final String TRANSACTION_NOT_EXIST = "23";
    public static final String VIRTUAL_ACCOUNT_IS_EXIST = "31";
    public static final String VIRTUAL_ACCOUNT_NOT_EXIST = "32";

    public static final Map<String, String> errorCodeMap = new HashMap<>();

    static {
        errorCodeMap.put("00", "Gateway request success");
        errorCodeMap.put("99", "Gateway request failed");
        errorCodeMap.put("98", "Data invalid");
        errorCodeMap.put("97", "Data validation failed");
        errorCodeMap.put("11", "Channel timeout");
        errorCodeMap.put("12", "Channel request invalid");
        errorCodeMap.put("21", "Client transaction id is exist");
        errorCodeMap.put("22", "Client transaction id is null or empty");
        errorCodeMap.put("01", "Gateway - Bad request");
        errorCodeMap.put("23", "Client transaction id is not exist");
        errorCodeMap.put("31", "Virtual Account is exist");
        errorCodeMap.put("32", "Virtual Account is not exist");
    }

    public static PGResponse getInstanceWhenError(String ErrorCode) {
        return ErrorCode.equals(SUCCESS) ?
                new PGResponse(true, errorCodeMap.get(ErrorCode), ErrorCode) :
                new PGResponse(false, errorCodeMap.get(ErrorCode), ErrorCode);
    }

    public static PGResponse getInstance(String ErrorCode,
                                         String channelErrorCode,
                                         String channelMessage,
                                         Object data) {

        PGResponse response = getInstanceWhenError(ErrorCode);
        response.channelErrorCode = channelErrorCode;
        response.channelMessage = channelMessage;
        response.data = data;
        return response;
    }

    public static PGResponse getInstanceWhenSuccess(String channelErrorCode, Object data){
        return PGResponse.builder()
                .status(true)
                .errorCode("00")
                .channelErrorCode(channelErrorCode)
                .data(data)
                .build();
    }

    public static PGResponse getInstanceFullValue(Boolean status,
                                                  Object data,
                                                  String channelErrorCode,
                                                  String channelMessage,
                                                  String errorCode) {

        PGResponse response = getInstance(errorCode, channelErrorCode, channelMessage, data);
        response.setStatus(status);
//        response.setMessage(message);
        return response;
    }

    public static PGResponse getExceptionMsg(Exception e) {
        return new PGResponse(false, ExceptionUtils.getMessage(e), errorCodeMap.get(FAIL));
    }

    public static PGResponse getExceptionMsg(Exception e, String description, String channelErrorCode, String channelMessage, Object data) {
        return new PGResponse(false, ExceptionUtils.getRootCauseMessage(e), description, PGResponse.FAIL, channelErrorCode, channelMessage, data);
    }
}
