package gateway.core.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SendEmailReq {

    @JsonProperty("from_email")
    private String fromEmail = "support@nganluong.vn";

    private String subject;

    private String content;

    @JsonProperty("to_email")
    private String toEmail;

    private String alias = "GATEWAY";

    public String getFromEmail() {
        return fromEmail;
    }

    public void setFromEmail(String fromEmail) {
        this.fromEmail = fromEmail;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getToEmail() {
        return toEmail;
    }

    public void setToEmail(String toEmail) {
        this.toEmail = toEmail;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }
}
