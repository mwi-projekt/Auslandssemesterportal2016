package dhbw.mwi.Auslandsemesterportal2016.test.selenium;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class UITestPasswordForgotten extends UIBaseClass {
	WebElement myElement;
	WebElement cookiesElement;
	private String baseUrl = "http://10.3.15.45/";
	private String PasswordForgottenUrl = baseUrl + "forgot_pw.html";
	private static final String LOGIN_ELEMENT_XPATH = "//header/div[1]/div[1]/div[1]/button[1]";
	private static final String PASSWORD_FORGOTTEN_ELEMENT_XPATH = "/html/body/div[3]/div/div/form/div[2]/div[2]/div/a";
	private static final String IMPRESSUM_ELEMENT_XPATH = "/html/body/div[5]/div/footer/div[2]/div/div/div[2]/nav/a";
	private static final String FAQ_ELEMENT_XPATH = "/html/body/div[5]/div/footer/div[1]/div/div/div[1]/nav/nav/ul/li/a";
	private static final String ENTER_EMAIL_ELEMENT_XPATH = "//*[@id=\"mail\"]";
	private static final String RESET_PASSWORD_ELEMENT_XPATH = "//*[@id=\"resetPassword\"]";

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
	public void testPasswordForgotten() throws InterruptedException {
		driver.get(baseUrl);
		driver.findElement(By.xpath(LOGIN_ELEMENT_XPATH)).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath(PASSWORD_FORGOTTEN_ELEMENT_XPATH)).click();
		assertEquals(driver.getCurrentUrl(), PasswordForgottenUrl);

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
		String text = driver.findElement(By.xpath("//*[@id=\"swal2-title\"]")).getText();
		assertEquals(text, "Diese Mailadresse ist uns nicht bekannt");

	}

	@Test
	public void testNavigationToImpressum() throws InterruptedException {

		assertElement(IMPRESSUM_ELEMENT_XPATH, PasswordForgottenUrl, "impressum.html");

	}
}
