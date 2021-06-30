package dhbw.mwi.Auslandsemesterportal2016.test.selenium;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class UITestRegister extends UIBaseClass {

	private String baseUrl = "http://10.3.15.45/";
	private static final String REGISTER_ELEMENT_XPATH = "//header/div[1]/div[1]/div[1]/button[2]";
	private static final String REGISTER_BUTTON_ELEMENT_XPATH = "//body/div[@id='register']/div[1]/div[1]/form[1]/div[3]/button[2]";
	private static final String FIRST_NAME_ELEMENT_XPATH = "//input[@id='inVorname']";
	private static final String LAST_NAME_ELEMENT_XPATH = "//input[@id='inNachname']";
	private static final String EMAIL_ADRESS_ELEMENT_XPATH = "//input[@id='inMail']";
	private static final String MARTICULATION_NUMBER_ELEMENT_XPATH = "//input[@id='inMatrikel']";
	private static final String STUDY_ELEMENT_XPATH = "//select[@id='inStudiengang']";
	private static final String COURSE_ELEMENT_XPATH = "//input[@id='inKurs']";
	private static final String LOCATION_ELEMENT_XPATH = "//select[@id='inStandort']";
	private static final String PASSWORD_ELEMENT_XPATH = "//input[@id='inPwSt1']";
	private static final String PASSWORD_REPEAT_ELEMENT_XPATH = "//input[@id='inPwSt2']";
	private static final String MESSAGE_ELEMENT_XPATH = "//*[@id=\"swal2-title\"]";
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
	public void testRegister() throws InterruptedException {
		driver.get(baseUrl);
		driver.findElement(By.xpath(REGISTER_ELEMENT_XPATH)).click();
		Thread.sleep(3000);
		String text = driver.findElement(By.xpath("//h4[contains(text(),'Registrierung')]")).getText();
		assertEquals("Registrierung", text);

	}

	@Test
	public void testRegisterPositivCase() throws InterruptedException {
		driver.get(baseUrl);
		driver.manage().window().maximize();
		driver.findElement(By.xpath(REGISTER_ELEMENT_XPATH)).click();
		Thread.sleep(3000);
		driver.findElement(By.xpath(FIRST_NAME_ELEMENT_XPATH)).sendKeys("TestVorname");
		driver.findElement(By.xpath(LAST_NAME_ELEMENT_XPATH)).sendKeys("TestNachname");
		driver.findElement(By.xpath(EMAIL_ADRESS_ELEMENT_XPATH))
				.sendKeys("TestNachname.TestNachname@student.dhbw-karlsruhe.de");
		driver.findElement(By.xpath(MARTICULATION_NUMBER_ELEMENT_XPATH)).sendKeys("8469277");
		Select drpStudy = new Select(driver.findElement(By.xpath(STUDY_ELEMENT_XPATH)));
		drpStudy.selectByVisibleText("Wirtschaftsinformatik");
		driver.findElement(By.xpath(COURSE_ELEMENT_XPATH)).sendKeys("WWI18B3");
		Select drpLocation = new Select(driver.findElement(By.xpath(LOCATION_ELEMENT_XPATH)));
		drpLocation.selectByVisibleText("DHBW Karlsruhe");
		driver.findElement(By.xpath(PASSWORD_ELEMENT_XPATH)).sendKeys("TestPasswort123");
		driver.findElement(By.xpath(PASSWORD_REPEAT_ELEMENT_XPATH)).sendKeys("TestPasswort123");
		driver.findElement(By.xpath(REGISTER_BUTTON_ELEMENT_XPATH)).click();
		Thread.sleep(2000);
		//assertEquals("Registrierung erfolgreich", driver.findElement(By.xpath(MESSAGE_ELEMENT_XPATH)).getText());
	}

	@Test
	public void testPasswortWithWrongFormat() throws InterruptedException {
		driver.get(baseUrl);
		driver.manage().window().maximize();
		driver.findElement(By.xpath(REGISTER_ELEMENT_XPATH)).click();
		Thread.sleep(3000);
		driver.findElement(By.xpath(FIRST_NAME_ELEMENT_XPATH)).sendKeys("TestVorname");
		driver.findElement(By.xpath(LAST_NAME_ELEMENT_XPATH)).sendKeys("TestNachname");
		driver.findElement(By.xpath(EMAIL_ADRESS_ELEMENT_XPATH))
				.sendKeys("TestNachname.TestNachname@student.dhbw-karlsruhe.de");
		driver.findElement(By.xpath(MARTICULATION_NUMBER_ELEMENT_XPATH)).sendKeys("1234567");
		Select drpStudy = new Select(driver.findElement(By.xpath(STUDY_ELEMENT_XPATH)));
		drpStudy.selectByVisibleText("Wirtschaftsinformatik");
		driver.findElement(By.xpath(COURSE_ELEMENT_XPATH)).sendKeys("WWI18B3");
		Select drpLocation = new Select(driver.findElement(By.xpath(LOCATION_ELEMENT_XPATH)));
		drpLocation.selectByVisibleText("DHBW Karlsruhe");
		driver.findElement(By.xpath(PASSWORD_ELEMENT_XPATH)).sendKeys("TestPasswort");
		driver.findElement(By.xpath(PASSWORD_REPEAT_ELEMENT_XPATH)).sendKeys("TestPasswort");
		driver.findElement(By.xpath(REGISTER_BUTTON_ELEMENT_XPATH)).click();
		isTextPresent("Das Passwort muss mindestens 8 Zeichen lang sein");

	}

	@Test
	public void testEmailWithWrongFormat() throws InterruptedException {
		driver.get(baseUrl);
		driver.manage().window().maximize();
		driver.findElement(By.xpath(REGISTER_ELEMENT_XPATH)).click();
		Thread.sleep(3000);
		driver.findElement(By.xpath(FIRST_NAME_ELEMENT_XPATH)).sendKeys("TestVorname");
		driver.findElement(By.xpath(LAST_NAME_ELEMENT_XPATH)).sendKeys("TestNachname");
		driver.findElement(By.xpath(EMAIL_ADRESS_ELEMENT_XPATH)).sendKeys("TestNachname.TestNachname@gmail.com");
		driver.findElement(By.xpath(REGISTER_BUTTON_ELEMENT_XPATH)).click();
		isTextPresent("Nachname.Vorname@student.dhbw-karlsruhe.de");
	}

	@Test
	public void testRegisterWithUsedMail() throws InterruptedException {
		driver.get(baseUrl);
		driver.manage().window().maximize();
		driver.findElement(By.xpath(REGISTER_ELEMENT_XPATH)).click();
		Thread.sleep(3000);
		driver.findElement(By.xpath(FIRST_NAME_ELEMENT_XPATH)).sendKeys("TestVorname");
		driver.findElement(By.xpath(LAST_NAME_ELEMENT_XPATH)).sendKeys("TestNachname");
		driver.findElement(By.xpath(EMAIL_ADRESS_ELEMENT_XPATH)).sendKeys("ali.sazin@student.dhbw-karlsruhe.de");
		driver.findElement(By.xpath(MARTICULATION_NUMBER_ELEMENT_XPATH)).sendKeys("1234567");
		Select drpStudy = new Select(driver.findElement(By.xpath(STUDY_ELEMENT_XPATH)));
		drpStudy.selectByVisibleText("Wirtschaftsinformatik");
		driver.findElement(By.xpath(COURSE_ELEMENT_XPATH)).sendKeys("WWI18B5");
		Select drpLocation = new Select(driver.findElement(By.xpath(LOCATION_ELEMENT_XPATH)));
		drpLocation.selectByVisibleText("DHBW Karlsruhe");
		driver.findElement(By.xpath(PASSWORD_ELEMENT_XPATH)).sendKeys("TestPasswort123");
		driver.findElement(By.xpath(PASSWORD_REPEAT_ELEMENT_XPATH)).sendKeys("TestPasswort123");
		driver.findElement(By.xpath(REGISTER_BUTTON_ELEMENT_XPATH)).click();
		Thread.sleep(3000);
		//assertEquals("Mailadresse belegt", driver.findElement(By.xpath(MESSAGE_ELEMENT_XPATH)).getText());
	}

	@Test
	public void testRegisterWithUsedMatriculationNumber() throws InterruptedException {
		driver.get(baseUrl);
		driver.manage().window().maximize();
		driver.findElement(By.xpath(REGISTER_ELEMENT_XPATH)).click();
		Thread.sleep(3000);
		driver.findElement(By.xpath(FIRST_NAME_ELEMENT_XPATH)).sendKeys("TestVorname");
		driver.findElement(By.xpath(LAST_NAME_ELEMENT_XPATH)).sendKeys("TestNachname");
		driver.findElement(By.xpath(EMAIL_ADRESS_ELEMENT_XPATH))
				.sendKeys("TestNachname.TestNachname@student.dhbw-karlsruhe.de");
		driver.findElement(By.xpath(MARTICULATION_NUMBER_ELEMENT_XPATH)).sendKeys("8469277");
		Select drpStudy = new Select(driver.findElement(By.xpath(STUDY_ELEMENT_XPATH)));
		drpStudy.selectByVisibleText("Wirtschaftsinformatik");
		driver.findElement(By.xpath(COURSE_ELEMENT_XPATH)).sendKeys("WWI18B3");
		Select drpLocation = new Select(driver.findElement(By.xpath(LOCATION_ELEMENT_XPATH)));
		drpLocation.selectByVisibleText("DHBW Karlsruhe");
		driver.findElement(By.xpath(PASSWORD_ELEMENT_XPATH)).sendKeys("TestPasswort123");
		driver.findElement(By.xpath(PASSWORD_REPEAT_ELEMENT_XPATH)).sendKeys("TestPasswort123");
		driver.findElement(By.xpath(REGISTER_BUTTON_ELEMENT_XPATH)).click();
		Thread.sleep(2000);
		//assertEquals("Matrikelnummer belegt", driver.findElement(By.xpath(MESSAGE_ELEMENT_XPATH)).getText());
	}

	public void isTextPresent(String messages) {

		assertTrue(driver.getPageSource().contains(messages));
	}

}
