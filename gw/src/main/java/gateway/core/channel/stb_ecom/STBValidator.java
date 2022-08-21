package gateway.core.channel.stb_ecom;

import gateway.core.channel.stb_ecom.dto.req.LinkCardReq;
import gateway.core.dto.request.DataRequest;
import gateway.core.util.PGValidator;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class STBValidator extends PGValidator {

	public static String validateCheckCardParam(DataRequest param) {
		StringBuilder errors = new StringBuilder();
		if (StringUtils.isEmpty(param.getTransId())) {
			errors.append("TransId is required;");
		}
		if (StringUtils.isEmpty(param.getTransTime())) {
			errors.append("TransTime is required (format: yyyyMMddHHmmss);");
		}
		if (StringUtils.isEmpty(param.getCardNumber())) {
			errors.append("CardNumber is required;");
		}
		if (param.getAmount() == null || param.getAmount() < 0) {
			errors.append("Amount invalid;");
		}

		return errors.toString();
	}

	public static String validateTransferToCardParam(DataRequest param) {
		StringBuilder errors = new StringBuilder();
		if (StringUtils.isEmpty(param.getTransId())) {
			errors.append("TransId is required;");
		}
		if (StringUtils.isEmpty(param.getInquiryTransId())) {
			errors.append("InquiryTransId is required;");
		}
		if (StringUtils.isEmpty(param.getTransTime())) {
			errors.append("TransTime is required (format: yyyyMMddHHmmss);");
		}
		if (StringUtils.isEmpty(param.getCardNumber())) {
			errors.append("CardNumber is required;");
		}
		if (param.getAmount() == null || param.getAmount() <= 0) {
			errors.append("Amount invalid;");
		}

		return errors.toString();
	}

	public static String validateQueryParam(DataRequest param) {
		StringBuilder errors = new StringBuilder();
		if (StringUtils.isEmpty(param.getQueryTransId())) {
			errors.append("QueryTransId is required;");
		}
		if (StringUtils.isEmpty(param.getTransTime())) {
			errors.append("TransTime is required (format: yyyyMMddHHmmss);");
		}
		if (StringUtils.isEmpty(param.getQueryType())) {
			errors.append("QueryType is required (default: DomesticFundTransfer);");
		}
		if (StringUtils.isEmpty(param.getQueryTransDate())) {
			errors.append("QueryDate is required (format: yyyyMMdd);");
		}

		return errors.toString();
	}

	public static String validateLinkCard(LinkCardReq param){
		StringBuilder errors = new StringBuilder();
		String FirstName = param.getFirstName().replaceAll("[^a-zA-Z0-9]+","0");
		String LastName = param.getLastName().replaceAll("[^a-zA-Z0-9]+","0");
		String Address = param.getAddress().replaceAll("[^a-zA-Z0-9]+","0");
		param.setFirstName(FirstName);
		param.setLastName(LastName);
		param.setAddress(Address);
		if (StringUtils.isEmpty(param.getTransactionID())) {
			errors.append("TransactionID is required;");
		}
		if (StringUtils.isEmpty(param.getLanguage())) {
			errors.append("Language is required;");
		}
		if (StringUtils.isEmpty(param.getDescription())) {
			errors.append("Description is required;");
		}
		if (StringUtils.isEmpty(param.getSsn())) {
			errors.append("SSN is required;");
		}
		if (StringUtils.isEmpty(FirstName)) {
			errors.append("FirstName is required;");
		}
		if (StringUtils.isEmpty(param.getCurrency())) {
			errors.append("Currency is required;");
		}
		if (StringUtils.isEmpty(LastName)){
			errors.append("LastName is required;");
		}
		if (StringUtils.isEmpty(param.getGender())) {
			errors.append("Gender is required;");
		}
		if (StringUtils.isEmpty(Address)) {
			errors.append("Address is required;");
		}
		if (StringUtils.isEmpty(param.getDistrict())) {
			errors.append("District is required;");
		}
		if (StringUtils.isEmpty(param.getCity())) {
			errors.append("City is required;");
		}
		if (StringUtils.isEmpty(param.getCountry())) {
			errors.append("Country is required;");
		}
		if(!regexCheck(param.getEmail(),"^(.+)@(\\S+)$")){
			errors.append("Email is required;");
		}
		if (!regexCheck(param.getMobile(),"^[0-9]{1,15}$")) {
			errors.append("Mobile is required;");
		}
		if (StringUtils.isEmpty(param.getReturnUrl())) {
			errors.append("ReturnUrl is required;");
		}
		if (StringUtils.isEmpty(param.getCancelUrl())) {
			errors.append("CancelUrl is required;");
		}
		if (param.getTotalAmount() == null || Integer.parseInt(param.getTotalAmount()) <= 1000) {
			errors.append("Amount is invalid;");
		}
		if (!regexCheck(param.getInternationalFee(),"^[0-9]{1,15}$")) {
			errors.append("InternationalFee is required;");
		}
		if (!regexCheck(param.getDomesticFee(),"^[0-9]{1,15}$")) {
			errors.append("DomesticFee is required;");
		}
		return errors.toString();

	}
	private static boolean regexCheck(String textValue, String patternValue){
		Pattern pattern = Pattern.compile(patternValue);
		Matcher matcher = pattern.matcher(textValue);
		return matcher.find();
	}

}
