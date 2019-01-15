package com.bufs.jenkins;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class CrawlingService {

	//드라이버 및 URL 정보
	//..On window SetUp
	//public static final String DRIVER_PATH = "C:/chromedriver_win32/chromedriver.exe";
	
	//..ON Linux SetUp
	public static final String DRIVER_PATH = "/usr/bin/chromedriver";

	
	public static final String DRIVER_NAME = "webdriver.chrome.driver";
	public static final String MEAL_TABLE_URL = "http://www.dreamforone.com/~wy/api/get_data.php?store=dawa&new=1&selectday=";
	//셀레늄을 사용하기 위한 웹 드라이버
	private WebDriver driver;
	
	public Map<String, String> crawlMealTable(Date date) {
		
		//크롬 드라이버로 설정
		ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--headless");
        chromeOptions.addArguments("--no-sandbox");
		

        //On window setUp
		//System.setProperty(DRIVER_NAME, DRIVER_PATH);
		
        //On Linux setUp
		System.setProperty(DRIVER_PATH, DRIVER_NAME);
		
		
		
		driver = new ChromeDriver(chromeOptions);
		
		driver.get(MEAL_TABLE_URL + HomeController.DATE_FORMAT.format(date));

		String source;
		WebElement element = null;
		
		element = driver.findElement(By.tagName("body"));
		source = element.getText();
		
		ObjectMapper mapper = new ObjectMapper();
		Map<String, String> params = null;
		try {
			params = mapper.readValue(source, HashMap.class);
			
			String[] lunchMenu = params.get("wr_1").split("\\r");
			String[] dinnerMenu = params.get("wr_2").split("\\r");;
			
			params.put("lunch", mapper.writeValueAsString(lunchMenu));
			params.put("dinner", mapper.writeValueAsString(dinnerMenu));

		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			
			driver.close();
			
		}

		return params;
		
		
	}
	
	
	
}
