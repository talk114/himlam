package gateway.core.util;

import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.PhaseInterceptorChain;
import org.apache.cxf.transport.http.AbstractHTTPDestination;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.xml.security.utils.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import gateway.core.dto.PGRequest;
import gateway.core.dto.PGResponse;
import vn.nganluong.naba.entities.PgEndpoint;

@Component
public class PGValidator {
	private static final Logger logger = LogManager.getLogger(PGValidator.class);

	@Autowired
	private Validator baseValidator;

	private static final Logger LOG = LogManager.getLogger(PGValidator.class);

	public <T> Map<String, String> validate(T object) {
		Map<String, String> errors = new HashMap<>();
		if (object == null) {
			return errors;
		}
		Set<ConstraintViolation<T>> constraints = baseValidator.validate(object);
		for (ConstraintViolation<T> constraint : constraints) {
			errors.put(constraint.getPropertyPath().toString(), constraint.getMessage());
		}
		return errors;
	}

	public <T> String validateParam(T object) {
		if (object == null) {
			return null;
		}
		Set<ConstraintViolation<T>> constraints = baseValidator.validate(object);
		for (ConstraintViolation<T> constraint : constraints) {
			return constraint.getMessage();
		}
		return null;
	}

	public <T> void validate1(T object) throws PGException {
		if (object == null) {
			return;
		}
		Set<ConstraintViolation<T>> constraints = baseValidator.validate(object);
		for (ConstraintViolation<T> constraint : constraints) {
			throw new PGException(constraint.getMessage());
		}
	}

	public static String createErrorResponse(String message){
		ObjectMapper mapper = new ObjectMapper();
		PGResponse resp = new PGResponse();
		resp.setStatus(false);
		resp.setDescription(message);
		try {
			return mapper.writeValueAsString(resp);
		} catch (JsonProcessingException ex) {
			LOG.info(ex.getMessage());
		}
		return null;
	}

	public static boolean validateIP(PgEndpoint endpoint) {
		String ipAddress = getIpClient();
		LOG.info("=========IP CONNECT: " + ipAddress);
		String ips = endpoint.getIp();
		if (ips.equals("*")) {
			return true;
		}
		String[] ipRangeArray = ips.split(",");
		return Arrays.asList(ipRangeArray).contains(ipAddress);
	}

	public static String getIpClient() {
		Message message = PhaseInterceptorChain.getCurrentMessage();
		HttpServletRequest request = (HttpServletRequest) message.get(AbstractHTTPDestination.HTTP_REQUEST);
		// HttpServletRequest httpReq = context.getHttpServletRequest();
		String ipAddress = request.getHeader("X-FORWARDED-FOR");
		if (ipAddress == null) {
			ipAddress = request.getRemoteAddr();
		}
		return ipAddress;
	}

	public static boolean validateHttpAuthen(PgEndpoint endpoint) {
		Message message = PhaseInterceptorChain.getCurrentMessage();
		HttpServletRequest request = (HttpServletRequest) message.get(AbstractHTTPDestination.HTTP_REQUEST);
		String auth = request.getHeader("Authorization");
		if (auth != null && auth.length() > 0) {
			auth = auth.substring(6);
			String userPassword = endpoint.getHttpUser() + ":" + endpoint.getHttpPassword();
			return auth.equals(Base64.encode(userPassword.getBytes()));
		} else {
			return false;
		}
	}

	public static boolean validateChecksum(PGRequest req, PgEndpoint endpoint) {
		String checksum = null;
		try {
			checksum = PGSecurity.md5(req.getData() + endpoint.getAuthKey());
		} catch (NoSuchAlgorithmException e) {
			logger.info(ExceptionUtils.getStackTrace(e));
			return false;
		}
		return checksum.equalsIgnoreCase(req.getChecksum());
	}

}
