package gateway.web.operator;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import vn.nganluong.naba.dto.PaymentConst;
import vn.nganluong.naba.dto.PaymentDto;
import vn.nganluong.naba.dto.PaymentSearchDto;
import vn.nganluong.naba.service.ChannelService;
import vn.nganluong.naba.service.CommonLogService;
import vn.nganluong.naba.service.PaymentService;
import vn.nganluong.naba.utils.MyDateUtil;
import vn.nganluong.naba.utils.PaginationResult;

@Controller
@RequestMapping(value = {"admin/payment", "/admin"})
public class PaymentHistoryController {

	private static final Logger logger = LogManager.getLogger(PaymentHistoryController.class);

	private static final String SERVICE_NAME = "ADMIN_COMMON";

	private static final String FUNCTION_CODE_PAYMENT_SEARCH = "ADMIN_PAYMENT";

	@Autowired
	private PaymentService paymentService;

	@Autowired
	private CommonLogService commonLogService;
	
	@Autowired
	private ChannelService channelService;

	@RequestMapping(value = {"index", "search", ""}, method = RequestMethod.GET)
	public String dashboard(Model model, @ModelAttribute PaymentSearchDto paymentSearchDto, Integer page) {
		logger.info("Start admin payment");

//		PaymentSearchDto paymentSearchDto = new PaymentSearchDto();

		String fromDate = MyDateUtil.toDateString(PaymentConst.DATE_FORMAT_PAYMENT_SEARCH, MyDateUtil.getTodayStart());
		String toDate = MyDateUtil.toDateString(PaymentConst.DATE_FORMAT_PAYMENT_SEARCH, MyDateUtil.getTodayEnd());
		paymentSearchDto.setFromDate(fromDate);
		paymentSearchDto.setToDate(toDate);
		PaginationResult<PaymentDto> pageData = paymentService.listPaymentHistory(paymentSearchDto);

		List<PaymentDto> payments = pageData.getList();

		model.addAttribute("paymentSearchDto", paymentSearchDto);
		model.addAttribute("payments", payments);
		model.addAttribute("paginationProducts", pageData);

		return "admin.payment.index";
	}
	
	@ModelAttribute("channelList")
	public Map<String, String> getEntitiesList() {
		return channelService.getMapChannelList(channelService);
	}


	@RequestMapping(value = "search", method = RequestMethod.POST)
	public String searchPaymentHistory(Model model, @ModelAttribute PaymentSearchDto paymentSearchDto) {

		logger.info("Start admin payment");

		PaginationResult<PaymentDto> pageData = paymentService.listPaymentHistory(paymentSearchDto);

		List<PaymentDto> payments = pageData.getList();

		model.addAttribute("paymentSearchDto", paymentSearchDto);
		model.addAttribute("payments", payments);
		model.addAttribute("paginationProducts", pageData);

		String[] paramsLog = new String[] { paymentSearchDto.getChannelCode(), paymentSearchDto.getAccountNo(),
				paymentSearchDto.getClientTransactionId(),
				paymentSearchDto.getFromDate() + " ~ " + paymentSearchDto.getToDate() };
		logger.info(commonLogService.createContentLog(null, SERVICE_NAME, FUNCTION_CODE_PAYMENT_SEARCH, true, true,
				false, paramsLog));

		return "admin.payment.index";
	}

	@RequestMapping(value = "detail/{id}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public PaymentDto getDetailPayment(@PathVariable("id") String id) {

		return paymentService.findDetailPaymentById(NumberUtils.createInteger(id));
	}
}
