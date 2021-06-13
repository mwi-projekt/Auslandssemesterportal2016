package dhbw.mwi.Auslandsemesterportal2016.test.selenium;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

import io.github.bonigarcia.wdm.WebDriverManager;

import org.testng.annotations.*;


public class UITestFAQ {
	WebDriver driver;
	private String browser = "CHROME";
	private StringBuffer verificationErrors = new StringBuffer();
	private WebElement universityElement;
	private WebElement cookiesElement;
	private String baseUrl = "http://10.3.15.45/";
	private WebElement FAQElement;
	private WebElement loginElement; 
	private WebElement answerTextElement;
	private WebElement questionTextElement;
	private WebElement cancelElement; 
	private WebElement registerElement;
	private WebElement impressumElement; 
	
	
	@BeforeMethod
	public void getDriver() throws InterruptedException {

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
	}

	@AfterMethod
	public void tearDown() throws Exception {
		driver.quit();
		String verificationErrorString = verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			fail(verificationErrorString);
		}
	}
	@Test
	public void testThatClickingFAQFromUniversityScotlandLinkBringsUserToFAQ() throws InterruptedException {
	
		assertUniversityToFAQ("/html/body/div[5]/div/div[3]/div/div/a[1]/div/div[2]");
	}
	
	@Test
	public void testThatClickingFAQFromBulgariaLinkBringsUserToFAQ() throws InterruptedException {
	
		assertUniversityToFAQ("/html/body/div[5]/div/div[3]/div/div/a[2]/div/div[1]");
	}
	
	@Test
	public void testThatClickingFAQFromDongHwaLinkBringsUserToFAQ() throws InterruptedException {
	
		assertUniversityToFAQ("/html/body/div[5]/div/div[3]/div/div/a[3]/div/div[2]");
	}
	
	@Test
	public void testThatClickingFAQFromSanMarcosLinkBringsUserToFAQ() throws InterruptedException {
	
		assertUniversityToFAQ("/html/body/div[5]/div/div[3]/div/div/a[4]/div/div[1]");
	}
	
	@Test
	public void testThatClickingFAQFromCostaRicaLinkBringsUserToFAQ() throws InterruptedException {
	
		assertUniversityToFAQ("/html/body/div[5]/div/div[3]/div/div/a[5]/div/div[2]");
	}
	
	@Test
	public void testThatClickingFAQFromFinlandLinkBringsUserToFAQ() throws InterruptedException {
	
		assertUniversityToFAQ("/html/body/div[5]/div/div[3]/div/div/a[6]/div/div[1]");
	}
	
	@Test
	public void testThatClickingFAQFromDurbanLinkBringsUserToFAQ() throws InterruptedException {
	
		assertUniversityToFAQ("/html/body/div[5]/div/div[3]/div/div/a[7]/div/div[2]");
	}
	
	@Test
	public void testThatClickingLoginFromFAQBringsUserToLogin() throws InterruptedException {
		driver.get(baseUrl);
		Thread.sleep(2000);
		FAQElement = driver.findElement(By.xpath("/html/body/div[5]/div/footer/div[1]/div/div/div[1]/nav/nav/ul/li/a"));
		Thread.sleep(2000);
		FAQElement.click();
		Thread.sleep(2000);
		loginElement = driver.findElement(By.xpath("/html/body/div[4]/div/header/div/div/div[1]/button[1]"));
		loginElement.click();
		Thread.sleep(2000);
		cancelElement = driver.findElement(By.xpath("/html/body/div[3]/div/div/form/div[3]/button[1]"));
		cancelElement.click();
		assertEquals(driver.getCurrentUrl(), baseUrl + "faq.html");
	}
	
	@Test
	public void testThatClickingQuestionFromFAQBringsUserToAnswer() throws InterruptedException {
		driver.get(baseUrl);
		FAQElement = driver.findElement(By.xpath("/html/body/div[5]/div/footer/div[1]/div/div/div[1]/nav/nav/ul/li/a"));
		FAQElement.click();
		Thread.sleep(2000);
		questionTextElement = driver.findElement(By.xpath("/html/body/div[5]/div/div[2]/div/div[1]/h3"));
		questionTextElement.click();
		answerTextElement = driver.findElement(By.xpath("/html/body/div[5]/div/div[2]/div/div[1]/p"));
		assertNotNull(answerTextElement);
	}
	
	@Test
	public void testThatClickingRegistrierungFromFAQBringsUserToRegistrierung() throws InterruptedException {
		driver.get(baseUrl);
		Thread.sleep(2000);
		FAQElement = driver.findElement(By.xpath("/html/body/div[5]/div/footer/div[1]/div/div/div[1]/nav/nav/ul/li/a"));
		Thread.sleep(2000);
		FAQElement.click();
		Thread.sleep(3000);	
		registerElement = driver.findElement(By.xpath("/html/body/div[4]/div/header/div/div/div[1]/button[2]"));
		Thread.sleep(2000);
		registerElement.click();
		Thread.sleep(2000);
		cancelElement = driver.findElement(By.xpath("/html/body/div[2]/div/div/form/div[1]/button"));
		cancelElement.click();
		assertEquals(driver.getCurrentUrl(), "http://10.3.15.45/faq.html");
	}
	
	@Test
	public void testThatClickingImpressumFromFAQBringsUserToImpressum() throws InterruptedException {
		driver.get(baseUrl);
		Thread.sleep(6000);
		FAQElement = driver.findElement(By.xpath("/html/body/div[5]/div/footer/div[1]/div/div/div[1]/nav/nav/ul/li/a"));
		FAQElement.click();
		Thread.sleep(3000);
		impressumElement = driver.findElement(By.xpath("/html/body/div[5]/div/footer/div[2]/div/div/div[2]/nav/a"));
		impressumElement.click();
		assertEquals(driver.getCurrentUrl(), baseUrl + "impressum.html");
	}
	
	@Test
	public void testThatClickingFAQFromFAQBringsUserToFAQ() throws InterruptedException {
		driver.get(baseUrl);
		Thread.sleep(3000);
		FAQElement = driver.findElement(By.xpath("/html/body/div[5]/div/footer/div[1]/div/div/div[1]/nav/nav/ul/li/a"));
		FAQElement.click();
		Thread.sleep(3000);
		FAQElement = driver.findElement(By.xpath("/html/body/div[5]/div/footer/div[1]/div/div/div[1]/nav/nav/ul/li/a"));
		FAQElement.click();
		assertEquals(driver.getCurrentUrl(), baseUrl + "faq.html");
	}
	
	
	private void assertUniversityToFAQ(String xPath) throws InterruptedException {
		driver.get(baseUrl);
		universityElement = driver.findElement(By.xpath(xPath));	
		Thread.sleep(3000);
		universityElement.click();
		Thread.sleep(3000);
		FAQElement = driver.findElement(By.xpath("/html/body/div[5]/div/footer/div[1]/div/div/div[1]/nav/nav/ul/li/a")); 
		FAQElement.click();
		assertEquals(driver.getCurrentUrl(), baseUrl + "faq.html");
	}
}
