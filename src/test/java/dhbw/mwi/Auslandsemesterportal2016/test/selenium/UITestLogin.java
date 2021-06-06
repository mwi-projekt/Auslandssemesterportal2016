package dhbw.mwi.Auslandsemesterportal2016.test.selenium;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

import io.github.bonigarcia.wdm.WebDriverManager;

public class UITestLogin {
	WebDriver driver;
	private String browser = "FIREFOX";
	private StringBuffer verificationErrors = new StringBuffer();
	private WebElement cookiesElement;
	private String baseUrl = "http://10.3.15.45/";
	private WebElement loginElement;
	private WebElement failedLoginElement;
	@Before
	public void setUp() throws InterruptedException {

		switch (browser) {
		case "CHROME":
			WebDriverManager.chromedriver().setup();
			driver = new ChromeDriver();
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
	
	@Test
	public void testThatUsingLoginWithUnvalidData() throws InterruptedException {
		loginElement = driver.findElement(By.xpath("/html/body/div[4]/div/header/div[2]/div/div[1]/button[1]"));
		loginElement.click();
		driver.findElement(By.id("inEmail")).sendKeys("test@email.com");
		driver.findElement(By.id("inPasswort")).sendKeys("test user");
		driver.findElement(By.id("btnLogin")).click();
		Thread.sleep(2000);
		failedLoginElement = driver.findElement(By.xpath("/html/body/div[8]/div/div[1]"));
		assertNotNull(failedLoginElement);
	}
	
	@Test
	public void testThatUsingLoginWithValidData() throws InterruptedException {
		loginElement = driver.findElement(By.xpath("/html/body/div[4]/div/header/div[2]/div/div[1]/button[1]"));
		loginElement.click();
		driver.findElement(By.id("inEmail")).sendKeys("test@student.dhbw-karlsruhe.de");
		driver.findElement(By.id("inPasswort")).sendKeys("Hallo1234!");
		driver.findElement(By.id("btnLogin")).click();
		Thread.sleep(2000);
		assertEquals( baseUrl + "bewerbungsportal.html", driver.getCurrentUrl());
	}
	
	
	@After
	public void tearDown() throws Exception {
		driver.quit();
		String verificationErrorString = verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			fail(verificationErrorString);
		}
	}
}
