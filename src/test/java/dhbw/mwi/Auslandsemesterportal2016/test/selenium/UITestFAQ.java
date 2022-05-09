package dhbw.mwi.Auslandsemesterportal2016.test.selenium;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import static org.junit.jupiter.api.Assertions.*;

public class UITestFAQ extends UIBaseClass {
	private StringBuffer verificationErrors = new StringBuffer();
	private WebElement universityElement;
	private String baseUrl = "http://10.3.15.45/";
	private WebElement loginElement;
	private WebElement answerTextElement;
	private WebElement questionTextElement;
	private WebElement cancelElement;
	private static final String FAQ_ELEMENT = "/html/body/div[5]/div/footer/div[1]/div/div/div[1]/nav/nav/ul/li/a";
	private static final String IMPRESSUM_ELEMENT = "/html/body/div[5]/div/footer/div[2]/div/div/div[2]/nav/a";
	private static final String CANCEL_ELEMENT = "/html/body/div[2]/div/div/form/div[1]/button";
	private static final String REGISTER_ELEMENT = "/html/body/div[4]/div/header/div/div/div[1]/button[2]";

	@BeforeEach
	public void getDriver() throws InterruptedException {
		getDriver("CHROME");

	}

	@AfterEach
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
		driver.findElement(By.xpath(FAQ_ELEMENT)).click();
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
		driver.findElement(By.xpath(FAQ_ELEMENT)).click();
		Thread.sleep(2000);
		questionTextElement = driver.findElement(By.xpath("/html/body/div[5]/div/div[2]/div/div[1]/h3"));
		questionTextElement.click();
		answerTextElement = driver.findElement(By.xpath("/html/body/div[5]/div/div[2]/div/div[1]/p"));
		assertNotNull(answerTextElement);
	}

	@Test
	public void testThatClickingRegistrierungFromFAQBringsUserToRegistrierung() throws InterruptedException {
		driver.findElement(By.xpath(FAQ_ELEMENT)).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath(REGISTER_ELEMENT)).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath(CANCEL_ELEMENT)).click();
		assertEquals(driver.getCurrentUrl(), "http://10.3.15.45/faq.html");
	}

	@Test
	public void testThatClickingImpressumFromFAQBringsUserToImpressum() throws InterruptedException {
		driver.findElement(By.xpath(FAQ_ELEMENT)).click();
		Thread.sleep(3000);
		driver.findElement(By.xpath(IMPRESSUM_ELEMENT)).click();
		assertEquals(driver.getCurrentUrl(), baseUrl + "impressum.html");
	}

	@Test
	public void testThatClickingFAQFromFAQBringsUserToFAQ() throws InterruptedException {
		driver.findElement(By.xpath(FAQ_ELEMENT)).click();
		Thread.sleep(3000);
		driver.findElement(By.xpath(FAQ_ELEMENT)).click();
		assertEquals(driver.getCurrentUrl(), baseUrl + "faq.html");
	}

	private void assertUniversityToFAQ(String xPath) throws InterruptedException {
		universityElement = driver.findElement(By.xpath(xPath));
		Thread.sleep(3000);
		universityElement.click();
		Thread.sleep(3000);
		driver.findElement(By.xpath(FAQ_ELEMENT)).click();
		Thread.sleep(2000);
		assertEquals(driver.getCurrentUrl(), baseUrl + "faq.html");
	}
}
