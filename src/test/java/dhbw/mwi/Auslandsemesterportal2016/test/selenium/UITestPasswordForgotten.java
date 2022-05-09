package dhbw.mwi.Auslandsemesterportal2016.test.selenium;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class UITestPasswordForgotten extends UIBaseClass {
	private String baseUrl = "http://10.3.15.45/";
	private String PasswordForgottenUrl = baseUrl + "forgot_pw.html";
	private static final String LOGIN_ELEMENT_XPATH = "//header/div[1]/div[1]/div[1]/button[1]";
	private static final String PASSWORD_FORGOTTEN_ELEMENT_XPATH = "/html/body/div[3]/div/div/form/div[2]/div[2]/div/a";
	private static final String IMPRESSUM_ELEMENT_XPATH = "/html/body/div[5]/div/footer/div[2]/div/div/div[2]/nav/a";
	private static final String FAQ_ELEMENT_XPATH = "/html/body/div[5]/div/footer/div[1]/div/div/div[1]/nav/nav/ul/li/a";
	private static final String ENTER_EMAIL_ELEMENT_XPATH = "//*[@id=\"mail\"]";
	private static final String RESET_PASSWORD_ELEMENT_XPATH = "//*[@id=\"resetPassword\"]";
	private static final String MESSAGE_ELEMENT_XPATH = "//*[@id=\"swal2-title\"]";

	private StringBuffer verificationErrors = new StringBuffer();

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
	public void testPasswordForgotten() throws InterruptedException {
		driver.get(baseUrl);
		driver.findElement(By.xpath(LOGIN_ELEMENT_XPATH)).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath(PASSWORD_FORGOTTEN_ELEMENT_XPATH)).click();
		assertEquals(PasswordForgottenUrl, driver.getCurrentUrl());

	}

	@Test
	public void testNavigationToFAQ() throws InterruptedException {
		assertElement(FAQ_ELEMENT_XPATH, PasswordForgottenUrl, "faq.html");

	}

	@Test
	public void testPasswordForgottenNegativeCase() throws InterruptedException {

		driver.get(baseUrl);
		driver.findElement(By.xpath(LOGIN_ELEMENT_XPATH)).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath(PASSWORD_FORGOTTEN_ELEMENT_XPATH)).click();
		assertEquals(driver.getCurrentUrl(), PasswordForgottenUrl);
		Thread.sleep(3000);
		driver.findElement(By.xpath(ENTER_EMAIL_ELEMENT_XPATH)).sendKeys("Test");
		driver.findElement(By.xpath(RESET_PASSWORD_ELEMENT_XPATH)).click();
		Thread.sleep(3000);
		String text = driver.findElement(By.xpath(MESSAGE_ELEMENT_XPATH)).getText();
		assertEquals("Diese Mailadresse ist uns nicht bekannt", text);
	}

	@Test
	public void testPasswordForgottenPositiveCase() throws InterruptedException {

		driver.get(baseUrl);
		driver.findElement(By.xpath(LOGIN_ELEMENT_XPATH)).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath(PASSWORD_FORGOTTEN_ELEMENT_XPATH)).click();
		assertEquals(driver.getCurrentUrl(), PasswordForgottenUrl);
		Thread.sleep(3000);
		driver.findElement(By.xpath(ENTER_EMAIL_ELEMENT_XPATH)).sendKeys("ali.sazin@student.dhbw-karlsruhe.de");
		driver.findElement(By.xpath(RESET_PASSWORD_ELEMENT_XPATH)).click();
		Thread.sleep(3000);
		String text = driver.findElement(By.xpath(MESSAGE_ELEMENT_XPATH)).getText();
		assertEquals("Kennwort zurückgesetzt", text);

	}

	@Test
	public void testNavigationToImpressum() throws InterruptedException {

		assertElement(IMPRESSUM_ELEMENT_XPATH, PasswordForgottenUrl, "impressum.html");

	}
}
