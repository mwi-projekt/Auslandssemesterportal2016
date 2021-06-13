package dhbw.mwi.Auslandsemesterportal2016.test.selenium;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;


import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class UITestChatbot extends UIBaseClass {

	WebElement myElement;
	WebElement cookiesElement;
	private String baseUrl = "http://10.3.15.45/";
	private static final String OPEN_CHATBOT_ELEMENT_XPATH = "/html[1]/body[1]/div[6]/div[1]/div[1]";
	private static final String CLOSE_CHATBOT_ELEMENT_XPATH = "/html/body/div[6]/div/div[2]/div[1]/div[2]/img";
	private static final String ENTER_TEXT_ELEMENT_XPATH = "//body/div[@id='cai-webchat-div']/div[1]/div[2]/div[3]/textarea[1]";
	private static final String BULGARIA_ELEMENT_XPATH = "/html/body/div[5]/div/div[3]/div/div/a[2]/div/div[1]";
	private StringBuffer verificationErrors = new StringBuffer();

	@BeforeMethod
	public void getDriver() throws InterruptedException {
		getDriver("CHROME");
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
	public void testClickChatbotAfterScroll() throws InterruptedException {
		driver.get(baseUrl);
		driver.manage().window().maximize();
		Thread.sleep(3000);
		WebElement element = driver.findElement(By.xpath(BULGARIA_ELEMENT_XPATH));
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
		Thread.sleep(3000);
		driver.findElement(By.xpath(OPEN_CHATBOT_ELEMENT_XPATH)).click();
		String text = driver.findElement(By.xpath("/html/body/div[6]/div/div[2]/div[1]/div[1]")).getText();
		assertEquals(text, "Auslandsbot");
		driver.findElement(By.xpath(ENTER_TEXT_ELEMENT_XPATH)).sendKeys("Test");
		Thread.sleep(3000);
		driver.findElement(By.xpath(CLOSE_CHATBOT_ELEMENT_XPATH)).click();

	}

	@Test
	public void testClickChatbo() throws InterruptedException {
		driver.get(baseUrl);
		driver.manage().window().maximize();
		Thread.sleep(3000);
		driver.findElement(By.xpath(OPEN_CHATBOT_ELEMENT_XPATH)).click();
		String text = driver.findElement(By.xpath("/html/body/div[6]/div/div[2]/div[1]/div[1]")).getText();
		assertEquals(text, "Auslandsbot");
		driver.findElement(By.xpath(ENTER_TEXT_ELEMENT_XPATH)).sendKeys("<kjfhkjd");
		Thread.sleep(3000);
		driver.findElement(By.xpath(CLOSE_CHATBOT_ELEMENT_XPATH)).click();

	}

}
