package com.bufs.jenkins;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {

	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	@Autowired
	private CrawlingService service;

	private static Map<String, Map<String, String>> mealCache = null;
	public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Model model) {

		if (mealCache == null) {
			mealCache = new HashMap<String, Map<String, String>>();
			setChache();
		}

		
		return "home";
	}

	public void setChache() {

		LocalDate now = LocalDate.now();
		synchronized (this) {
			mealCache.put(DATE_FORMAT.format(now), service.crawlMealTable(now));
		}
	}

	@ResponseBody
	@RequestMapping(value="/search")
	public Map<String, String> search(@RequestParam(name = "date") Long date) {

		LocalDate dateObj = Instant.ofEpochMilli(date).atZone(ZoneId.systemDefault()).toLocalDate();
		String dateStr = dateObj.format(DATE_FORMAT);
		Map<String, String> result = null;

		boolean isWeekDay = dateObj.getDayOfWeek().getValue() >= 1 && dateObj.getDayOfWeek().getValue() <= 5; 

		if (mealCache.containsKey(dateStr)) {
			
			result = mealCache.get(dateStr);
			

			if(isWeekDay && "false".equals(result.get("isWork"))) {
			
				result = service.crawlMealTable(dateObj);

				if(result != null) {
					
					synchronized (this) {
						mealCache.put(dateStr, result);
					}

				}
				
			}
			
			return result;

		
		} else {
			result = service.crawlMealTable(dateObj);
			if(result != null) {
				
				synchronized (this) {
					mealCache.put(dateStr, result);
				}

			}

		}
		

		return result;

	}
	
	
	@ResponseBody
	@RequestMapping(value="checkCache")
	public Map<String, Map<String, String>> getCache(){
		
		logger.info("cache.. ");
		System.out.println(mealCache);
		return mealCache;
		
	}
	

}
