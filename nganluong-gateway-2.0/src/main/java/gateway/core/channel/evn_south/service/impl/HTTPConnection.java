package gateway.core.channel.evn_south.service.impl;

import gateway.core.channel.ocb.service.OCBService;
import okhttp3.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

class HTTPConnection {

    private static final Logger logger = LogManager.getLogger(HTTPConnection.class);
    private final String url;
    private final String body;
    private final String soapAction;

    HTTPConnection(String url, String body, String soapAction){
        this.url = url;
        this.body = body;
        this.soapAction = "http://tempuri.org/IPaymentService/" + soapAction;
    }
    Response execute() throws IOException {
        logger.info("EVN REQUEST: " + body);

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS);

        OkHttpClient client = builder.build();

        MediaType mediaType = MediaType.parse("text/xml;charset=UTF-8");
        RequestBody requestBody = RequestBody.create(mediaType, body);

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .addHeader("Accept-Encoding","gzip,deflate")
                .addHeader("Content-Type","text/xml;charset=UTF-8")
                .addHeader("SOAPAction",soapAction)
                .build();
        Response response = client.newCall(request).execute();
        return response;
    }
}
