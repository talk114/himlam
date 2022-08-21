package vn.nganluong.naba.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import vn.nganluong.naba.channel.vib.response.GetTokenResponse;
import vn.nganluong.naba.repository.ChannelRepository;
import vn.nganluong.naba.service.ChannelService;

@RestController
public class BankController {

	private final ChannelRepository repository;
	
	@Autowired
	ChannelService bankService;
	
	private static final Logger logger = LogManager.getLogger(BankController.class);
	
//	@Autowired
//	private BankDao bankDao;

	BankController(ChannelRepository repository) {
	    this.repository = repository;
	 }

	// Aggregate root

//	@GetMapping("/banks")
//	List<Bank> all() {
//		return repository.findAll();
//	}
//	
//	@GetMapping("/testRSA")
//	Bank testRsa() {
//		logger.info("testRSA message");
//		RSASignature rsa = new RSASignature();
//		try {
//			rsa.run();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			logger.info(ExceptionUtils.getStackTrace(e));
//		}	
//
//		return null;
//	}
	
//	@GetMapping("/test2")
//	Bank test2() {	
//		RestTemplate restTemplate = new RestTemplate();
//        String result = restTemplate.getForObject("http://localhost:8080/naba/banks", String.class);
//        Bank bank = restTemplate.getForObject("http://localhost:8080/naba/banks/1", Bank.class);
//        System.out.println(result);
//		return bank;
//	}
	
	@GetMapping("/test3")
	String testRefreshToken() throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException {
	    SSLContext sslContext = SSLContext.getInstance("TLS");
        TrustManager[] trust_mgr = get_trust_mgr();
        sslContext.init(null, trust_mgr, new SecureRandom());
        
	    SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext);

	    CloseableHttpClient httpClient = HttpClients.custom()
	                    .setSSLSocketFactory(csf)
	                    .build();

	    HttpComponentsClientHttpRequestFactory requestFactory =
	                    new HttpComponentsClientHttpRequestFactory();

	    requestFactory.setHttpClient(httpClient);
	    RestTemplate restTemplate = new RestTemplate(requestFactory);
	    
	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
	    headers.add("Authorization", "Basic YkUxNkpzc21ZNUlrZXlibk1nbFNTaWdQRVBNYTp2S3RwOHVsdkJWUEx2bG1yekZOY2ZaOG1xVTRh");
	    
	    MultiValueMap<String, String> mapBody = new LinkedMultiValueMap<String, String>();
	    mapBody.add("grant_type", "password");
	    mapBody.add("username", "nganluong");
	    mapBody.add("password", "123456");
	    
	    HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<MultiValueMap<String, String>>(mapBody, headers);

	    //post request
	    String fooResourceUrl = "https://uat-apim.vib.com.vn/token";
	    ResponseEntity<GetTokenResponse> response = restTemplate
	    		  .exchange(fooResourceUrl, HttpMethod.POST, entity, GetTokenResponse.class);

	    GetTokenResponse getTokenResponse = response.getBody();
	    
		return null;
	}
	
	@GetMapping("/testSOAP")
	String testSOAPRequest() throws IOException, SOAPException, KeyManagementException, NoSuchAlgorithmException {
		MessageFactory messageFactory = MessageFactory.newInstance();
		MediaType mediaType = MediaType.TEXT_PLAIN;
		String body = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\r\n"
				+ "<Envelope xmlns=\"http://schemas.xmlsoap.org/soap/envelope/\">\r\n"
				+ "    <Body>\r\n"
				+ "        <getAccountBalance xmlns=\"http://vn.vib.fastpay.erp\">\r\n"
				+ "            <!-- Optional -->\r\n"
				+ "            <rqObject>\r\n"
				+ "                <CIF xmlns=\"http://object.vn.vib.fastpay.erp/xsd\">00016506</CIF>\r\n"
				+ "                <accountNumber xmlns=\"http://object.vn.vib.fastpay.erp/xsd\">002704060002045</accountNumber>\r\n"
				+ "                <signData xmlns=\"http://object.vn.vib.fastpay.erp/xsd\">dCL5eSN76G9Abf4chC71fAM0oismsrseMIPE+8JdASXamZHJEwD8wcN1F51Aqzr9UWPuc2hUf6r/Mnk0IvQFX1Jo6F/4x57ZeMGUw0/YL/du6bPeQTfTAM+qAYI9w5lzSTlHztysJYnKLltN1zaDCrAZ0pXFRMFaA8TJSwsEJ/ZDc18DVdcXklt3TP3nyehzdprbXWwum3FfkIaa6M0l8WDKdXkoc6T9RGIjTAG335+p6L90dwMGez8yaayUnRzxmOE4xxJAwzPXhx+HYbGRaMGOOZNvEyXAr87a5Rg4ZiR+oOYCFPYV95zeZHrlCruzERE5BjlkpSnB+d5ZT8Gq3AVPGTNIzafIqsZnSCZ53b38P+chRqpO6G8luXGPMXcSHX6Ji0taK/d/4XFmEmFyY36SaZj/ITyXaVzAG56TiOvrEDO09xuzGam4ZwHV3Lexk/j55sHp2uOQj3Grs0F4TxIBHcNMzU/0Ft8aNOu0usBwclvifAUPNWF9eqXYC3gPozEi955W8dRa4InlYZjejLfowOHPa+UlIR7QeArpLCUYA16dijkjocnIcsr3czjFSj6FAOJyaU/jQTRQfyBBUT//65uS4rcatIx2u0JL0u8ajQ9APTg7u1CYO4+aMBkju7t+lVCjtfrGfTyWZ3BUzp6VVQCljKkHtWjx6op6z80=</signData>\r\n"
				+ "                <username xmlns=\"http://object.vn.vib.fastpay.erp/xsd\">NLnhap01</username>\r\n"
				+ "            </rqObject>\r\n"
				+ "        </getAccountBalance>\r\n"
				+ "    </Body>\r\n"
				+ "</Envelope>";
		SOAPMessage soapMessage = messageFactory.createMessage(new MimeHeaders(),
                new ByteArrayInputStream(body.getBytes(Charset.forName("UTF-8"))));
        SOAPPart soapPart = soapMessage.getSOAPPart();
        SOAPEnvelope envelope = soapPart.getEnvelope();
        SOAPBody soapBody = envelope.getBody();
        soapMessage.saveChanges();
        
        String endpointUrl = "https://103.10.212.98/erp/services/GetAccountBalanceWS?wsdl";
		
		SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
        SOAPConnection soapConnection = soapConnectionFactory.createConnection();
        

        try {
        	disableSslVerification();
			SOAPMessage soapResponse = soapConnection.call(soapMessage, endpointUrl);
			soapResponse.writeTo(System.out);
        } catch (SOAPException e) {
			// TODO Auto-generated catch block
			logger.info(ExceptionUtils.getStackTrace(e));
		}

		return "";
	}
	
	private static void disableSslVerification() {
	    try
	    {
	        // Create a trust manager that does not validate certificate chains
	        TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
	            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
	                return null;
	            }
	            public void checkClientTrusted(X509Certificate[] certs, String authType) {
	            }
	            public void checkServerTrusted(X509Certificate[] certs, String authType) {
	            }
	        }
	        };

	        // Install the all-trusting trust manager
	        SSLContext sc = SSLContext.getInstance("SSL");
	        sc.init(null, trustAllCerts, new java.security.SecureRandom());
	        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

	        // Create all-trusting host name verifier
	        HostnameVerifier allHostsValid = new HostnameVerifier() {
	            public boolean verify(String hostname, SSLSession session) {
	                return true;
	            }
	        };

	        // Install the all-trusting host verifier
	        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
	    } catch (NoSuchAlgorithmException e) {
			logger.info(ExceptionUtils.getStackTrace(e));
	    } catch (KeyManagementException e) {
			logger.info(ExceptionUtils.getStackTrace(e));
	    }
	}
	
	private static class TrustAllHosts implements HostnameVerifier {
		public boolean verify(String hostname, SSLSession session) {
			return true;
		}
	}
	
	
    private static TrustManager[] get_trust_mgr() {
        TrustManager[] certs = new TrustManager[]{new X509TrustManager() {

            @Override
            public void checkClientTrusted(java.security.cert.X509Certificate[] xcs, String string)
                    throws CertificateException {
            }

            @Override
            public void checkServerTrusted(java.security.cert.X509Certificate[] xcs, String string)
                    throws CertificateException {

            }

            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[]{};
            }
        }};
        return certs;
    }

//	@PostMapping(path = "/banks", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE}, produces = {MediaType.APPLICATION_ATOM_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
//	Bank newBank(Bank newBank) {
//		repository.save(newBank);
//		return null;
//	}
//
//	// Single item
//	@GetMapping("/banks/{id}")
//	Bank one(@PathVariable Integer id) throws NotFoundException {
//		return repository.findById(id).orElseThrow(() -> new NotFoundException("1"));
//	}
//
//	@PutMapping("/banks/{id}")
//	Bank replaceBank(@RequestBody Bank newBank, @PathVariable Integer id) {
//
//		return repository.findById(id).map(bank -> {
//			bank.setName(newBank.getName());
//			bank.setCode(newBank.getCode());
//			return repository.save(bank);
//		}).orElseGet(() -> {
//			newBank.setId(id.intValue());
//			return repository.save(newBank);
//		});
//	}
//
//	@DeleteMapping("/banks/{id}")
//	void deleteBank(@PathVariable Integer id) {
//		repository.deleteById(id);
//	}
}
