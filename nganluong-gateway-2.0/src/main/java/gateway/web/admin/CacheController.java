package gateway.web.admin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import vn.nganluong.naba.dto.CacheRequestDto;
import vn.nganluong.naba.dto.ResponseJson;
import vn.nganluong.naba.service.CacheService;
import vn.nganluong.naba.service.CommonResponseService;

@Controller
@RequestMapping(value = { "admin/cache" })
public class CacheController {

	private static final Logger logger = LogManager.getLogger(CacheController.class);

	@Autowired
	private CommonResponseService commonResponseService;

	@Autowired
	private CacheService cacheService;

	@GetMapping(path = { "/clear_all" })
	ResponseEntity<?> clearAllCache() {

		logger.info("Cache - Clear all Start");
		try {

			cacheService.clearCache();

			ResponseJson responseJson = commonResponseService.returnGatewayRequestSuccessPrefix();
			logger.info("Cache - Clear all End");
			return new ResponseEntity<ResponseJson>(responseJson, HttpStatus.OK);
		} catch (Exception e) {
			logger.info("Cache - Clear all Error: " + e.getMessage());
			return commonResponseService.returnBadGateway();
		}
	}

	@GetMapping(path = { "/clear_table/{tables}" })
	ResponseEntity<?> clearTablesCache(@PathVariable String tables) {
		logger.info("Cache - Clear table Start, tables: [" + tables + "]");
		try {
			cacheService.clearCacheByClassEntityName(tables);

			ResponseJson responseJson = commonResponseService.returnGatewayRequestSuccessPrefix();
			logger.info("Cache - Clear table End, tables: [" + tables + "]");
			return new ResponseEntity<ResponseJson>(responseJson, HttpStatus.OK);
		} catch (Exception e) {
			logger.info("Cache - Clear table error, tables: [" + tables + "]");
			return commonResponseService.returnBadGateway();
		}

	}

	@RequestMapping(value = { "index", "" }, method = RequestMethod.GET)
	public String index(Model model, @ModelAttribute CacheRequestDto cacheRequestDto) {

		model.addAttribute("cacheRequestDto", cacheRequestDto);

		return "admin.cache.index";
	}

	@RequestMapping(value = { "index", "" }, method = RequestMethod.POST)
	public String doClearCache(Model model, @ModelAttribute CacheRequestDto cacheRequestDto) {

		

		if (StringUtils.equals(cacheRequestDto.getRequestType(), "1")) {
			clearAllCache();
			cacheRequestDto.setResultMessage("Đã xóa tất cả bộ nhớ đệm (cache) của database");
		} else if (StringUtils.equals(cacheRequestDto.getRequestType(), "2")) {

			List<String> entitySuccessList = cacheService
					.clearCacheByClassEntityName(StringUtils.join(cacheRequestDto.getEntitiesSelect(), ','));
			logger.info("Cache - Clear table success, tables: [" + StringUtils.join(entitySuccessList, ',') + "]");
			if (CollectionUtils.isEmpty(entitySuccessList)) {
				cacheRequestDto.setResultMessage("Không có bảng nào được chọn để xóa bộ nhớ đệm (cache) của database");
			}
			else {
				cacheRequestDto.setResultMessage("Đã xóa bộ nhớ đệm (cache) của các bảng trong database: " + StringUtils.join(entitySuccessList, ','));	
			}
			

		}
		cacheRequestDto.setRequestType(StringUtils.EMPTY);
		cacheRequestDto.setEntitiesSelect(new String[] {});
		model.addAttribute("cacheRequestDto", cacheRequestDto);
		
		return "admin.cache.index";
	}

	@ModelAttribute("entitiesList")
	public Map<String, String> getEntitiesList() {
		List<String> entitiesName = cacheService.listEntiesName();
		Map<String, String> entitiesList = new HashMap<String, String>();
		for (String entity : entitiesName) {
			entitiesList.put(entity, entity);
		}

		return entitiesList;
	}
}
