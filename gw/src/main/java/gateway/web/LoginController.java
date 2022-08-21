package gateway.web;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("admin")
public class LoginController {
	private static final Logger logger = LogManager.getLogger(LoginController.class);

	@RequestMapping(value = "login", method = RequestMethod.GET)
	public String dashboard() {
		logger.info("Start Login");
		return "admin.login";
	}
}
