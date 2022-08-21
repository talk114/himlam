package gateway.core.channel.bidv.bidv_transfer_247.dto.req;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class DSRequest {

    private String fileType;
    private String messageCode;
    private String fileName;
    private String fileContent;
    // Doi soat theo Ngay
    private String tranDate;

    public DSRequest() {
    }

    public DSRequest(String fileType, String messageCode, String tranDate) {
        this.fileType = fileType;
        this.messageCode = messageCode;
        this.tranDate = tranDate;
    }

    public DSRequest(String fileType, String messageCode, String fileName, String fileContent, String tranDate) {
        this.fileType = fileType;
        this.messageCode = messageCode;
        this.fileName = fileName;
        this.fileContent = fileContent;
        this.tranDate = tranDate;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getMessageCode() {
        return messageCode;
    }

    public void setMessageCode(String messageCode) {
        this.messageCode = messageCode;
    }

    public String getTranDate() {
        return tranDate;
    }

    public void setTranDate(String tranDate) {
        this.tranDate = tranDate;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileContent() {
        return fileContent;
    }

    public void setFileContent(String fileContent) {
        this.fileContent = fileContent;
    }
}
