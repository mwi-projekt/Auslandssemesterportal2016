package dhbw.mwi.Auslandsemesterportal2016.test.selenium;

import static org.junit.Assert.assertEquals;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

import io.github.bonigarcia.wdm.WebDriverManager;

public class UIBaseClass {
	WebDriver driver;
	WebElement cookiesElement;
	WebElement myElement;

	private static String baseUrl = "http://10.3.15.45/";

	public void  getDriver(String browser) throws InterruptedException {
		switch (browser) {
		case "CHROME":
			ChromeOptions options = new ChromeOptions();
			options.addArguments("headless");
			options.addArguments("start-maximized");
			WebDriverManager.chromedriver().setup();
			driver = new ChromeDriver(options);
			driver.manage().window().maximize();
			break;
		case "IE":
			WebDriverManager.iedriver().setup();
			driver = new InternetExplorerDriver();
			break;
		case "EDGE":
			WebDriverManager.edgedriver().setup();
			driver = new EdgeDriver();
			break;
		case "FIREFOX":
			WebDriverManager.firefoxdriver().setup();
			driver = new FirefoxDriver();
			break;
		}
		driver.get(baseUrl);
		cookiesElement = driver.findElement(By.className("cc-dismiss"));
		Thread.sleep(2000);
		cookiesElement.click();
		Thread.sleep(2000);
	}

	public void assertElement(String xPath, String getUrl, String expectedReasultPage) throws InterruptedException {
		driver.get(getUrl);
		driver.manage().window().maximize();
		myElement = driver.findElement(By.xpath(xPath));
		Thread.sleep(3000);
		myElement.click();
		Thread.sleep(3000);
		assertEquals(baseUrl + expectedReasultPage, driver.getCurrentUrl());
	}
}
