package gateway.core.dto.request;

public class NotifyReq
{
    private String fnc;

    private SendEmailReq data;

    public String getFnc() {
        return fnc;
    }

    public void setFnc(String fnc) {
        this.fnc = fnc;
    }

    public SendEmailReq getData() {
        return data;
    }

    public void setData(SendEmailReq data) {
        this.data = data;
    }
}
