package gateway.web;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("admin/dashboard")
public class DashboardController {
	private static final Logger logger = LogManager.getLogger(DashboardController.class);

	@RequestMapping(value = "dashboard1", method = RequestMethod.GET)
	public String dashboard() {
		logger.info("");
		return "admin.dashboard.dashboard1";
		// admin\dashboard\dashboard1.jsp
	}
}
