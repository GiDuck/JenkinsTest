package com.bufs.jenkins;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
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
	public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Model model) {

		if (mealCache == null) {
			mealCache = new HashMap<String, Map<String, String>>();
			setChache();
		}

		
		return "home";
	}

	@Scheduled(cron = "0 0 6 * * MON-FRI")
	public void setChache() {

		Date now = new Date();
		mealCache.put(DATE_FORMAT.format(now), service.crawlMealTable(now));

	}

	@ResponseBody
	@RequestMapping(value="/search")
	public Map<String, String> search(@RequestParam(name = "date") Long date) {

		Date dateObj = new Date(date);
		String dateStr = DATE_FORMAT.format(dateObj);
		Map<String, String> result = null;
		if (mealCache.containsKey(dateStr)) {
			return mealCache.get(dateStr);
		} else {
			result = service.crawlMealTable(dateObj);
			if(result != null) {
				mealCache.put(dateStr, result);

			}

		}

		return result;

	}

}
