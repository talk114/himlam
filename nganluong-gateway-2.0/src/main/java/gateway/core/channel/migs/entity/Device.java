package gateway.core.channel.migs.entity;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Device {
    private String browser ;
    private BrowserDetails browserDetails;
    private String ipAddress;

    public String getBrowser() {
        return browser;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }

    public BrowserDetails getBrowserDetails() {
        return browserDetails;
    }

    public void setBrowserDetails(BrowserDetails browserDetails) {
        this.browserDetails = browserDetails;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
}
