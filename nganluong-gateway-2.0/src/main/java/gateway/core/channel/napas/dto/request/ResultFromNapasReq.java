package gateway.core.channel.napas.dto.request;

public class ResultFromNapasReq {
    private String data;
    private String checksum;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getChecksum() {
        return checksum;
    }

    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }
}
