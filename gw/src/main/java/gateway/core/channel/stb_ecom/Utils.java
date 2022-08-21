package gateway.core.channel.stb_ecom;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;

public class Utils {


	protected static final String PASSWORD_ENCRYPT = "90a1b2e2132137d1";
	
	/**
	 * 
	 * @param input
	 *            ddMMyy
	 * @throws ParseException
	 */
	public static String getDateTime(String input) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		Date date = sdf.parse(input);
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		sdf2.setTimeZone(TimeZone.getTimeZone("UTC"));
		return sdf2.format(date);
	}

	public static String getDateTime(Date date) {
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		sdf2.setTimeZone(TimeZone.getTimeZone("UTC"));
		return sdf2.format(date);
	}

	public static String genReqUID() {
		UUID uuid = UUID.randomUUID();
		return uuid.toString();
	}
	
	public static String formatReqToXml(Object input)
			throws JAXBException {
		JAXBContext jaxbContextRes = JAXBContext.newInstance(input.getClass());
		Marshaller jaxbMarshallerRes = jaxbContextRes.createMarshaller();
		jaxbMarshallerRes.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.FALSE);
		jaxbMarshallerRes.setProperty("com.sun.xml.bind.xmlDeclaration", Boolean.FALSE);
		StringWriter swRes = new StringWriter();
		jaxbMarshallerRes.marshal(input, swRes);
		return swRes.toString();
	}

	@SuppressWarnings("unchecked")
	// TODO
//	public static <T extends gateway.core.channel.stb_ecom.cashout_2_0.res.BaseResponse> T convertXmlToObject(Class<T> clazz, String xml)
//			throws JAXBException {
//		JAXBContext jaxbContext = JAXBContext.newInstance(clazz);
//		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
//		T res = (T) jaxbUnmarshaller.unmarshal(new StringReader(xml));
//		return res;
//	}

//	public static String Encrypt3DES(String data, String encryptPass) {
//		if (StringUtils.isEmpty(encryptPass))
//			encryptPass = PASSWORD_ENCRYPT;
//		return STBSecurity.Encrypt3DES(data, encryptPass);
//	}
//
//	public static String Decrypt3DES(String data, String encryptPass) {
//		if (StringUtils.isEmpty(encryptPass))
//			encryptPass = PASSWORD_ENCRYPT;
//		return STBSecurity.Decrypt3DES(data, encryptPass);
//	}

	public static void main(String[] args) throws Exception {
		String data = "e0RKtCa1i8x3oByA7sSj98d6S0LsQah0oG2vrKjrq/dhOf6IDNST+Qd+emX0yyCppBsmlvvgznahuG2tsFqOC+qcxWiKxf5+12EbVXmznO0IvdXJ75lWz3IhumIT/9XQ/V43HVnIkIrJqm6xtJOXim/2DOfqk2Zt2Sg6XeczxXMty059QfJaZsHYOg29m1aP";
//		System.out.println(STBSecurity.Decrypt3DES(data, PASSWORD_ENCRYPT));

	}
}
