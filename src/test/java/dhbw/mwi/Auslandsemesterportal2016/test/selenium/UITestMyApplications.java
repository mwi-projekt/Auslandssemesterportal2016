package dhbw.mwi.Auslandsemesterportal2016.test.selenium;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class UITestMyApplications extends UIBaseClass {
//	WebDriver driver;
	WebElement myElement;
	WebElement cookiesElement;
	WebElement ClickElement;
	private String baseUrl = "http://10.3.15.45/";
	private String myApplicationsUrl = baseUrl + "bewerbungsportal.html";
	private static final String MENUE_ELEMENT_XPATH = "/html/body/div[5]/div/div[1]/nav/button/span";
	private static final String MY_APPLICATIONS_ELEMENT_XPATH = "/html/body/div[5]/div/div[1]/div/div/a[3]";
	private static final String SCOTLAND_ELEMENT_XPATH = "//*[@id=\"auslandsAngebote\"]/div/div/a[1]/div/div[2]";
	private static final String BULGARIA_ELEMENT_XPATH = "//*[@id=\"auslandsAngebote\"]/div/div/a[2]/div/div";
	private static final String DONGHWA_ELEMENT_XPATH = "//*[@id=\"auslandsAngebote\"]/div/div/a[3]/div/div";
	private static final String SANMARCOS_ELEMENT_XPATH = "//*[@id=\"auslandsAngebote\"]/div/div/a[4]/div/div";
	private static final String COSTARICA_ELEMENT_XPATH = "//*[@id=\"auslandsAngebote\"]/div/div/a[5]/div/div";
	private static final String FINLAND_ELEMENT_XPATH = "//*[@id=\"auslandsAngebote\"]/div/div/a[6]/div/div";
	private static final String DURBAN_ELEMENT_XPATH = "//*[@id=\"auslandsAngebote\"]/div/div/a[7]/div/div";
	private static final String APPLY_ELEMENT_XPATH = "//*[@id=\"nav2\"]";
	private static final String FAQ_ELEMENT_XPATH = "/html/body/div[5]/div/footer/div[1]/div/div/div[1]/nav/nav/ul/li/a";
	private static final String IMPRESSUM_ELEMENT_XPATH = "//*[@id=\"normalBereich\"]/footer/div[2]/div/div/div[2]/nav/a";

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
	public void testNavigationToMyApplicationsPage() throws InterruptedException {
		driver.get(baseUrl);
		driver.findElement(By.xpath(MENUE_ELEMENT_XPATH)).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath(MY_APPLICATIONS_ELEMENT_XPATH)).click();
		assertEquals(driver.getCurrentUrl(), myApplicationsUrl);

	}

	@Test
	public void testNavigationToUniversityScotland() throws InterruptedException {
		assertElement(SCOTLAND_ELEMENT_XPATH, myApplicationsUrl, "detailsSchottland-auslandsangebote.html");
	}

	@Test
	public void testNavigationToUniversityBulgaria() throws InterruptedException {
		assertElement(BULGARIA_ELEMENT_XPATH, myApplicationsUrl, "detailsBulgarien-auslandsangebote.html");
	}

	@Test
	public void testNavigationToUniversityDONGHWA() throws InterruptedException {
		assertElement(DONGHWA_ELEMENT_XPATH, myApplicationsUrl, "detailsTaiwan-auslandsangebote.html");
	}

	@Test
	public void testThatClickingUniversitySanMarcosLinkBringsUserToUniversity() throws InterruptedException {

		assertElement(SANMARCOS_ELEMENT_XPATH, myApplicationsUrl, "detailsSanMarcos-auslandsangebote.html");
	}

	@Test
	public void testNavigationToUniversityCostaRica() throws InterruptedException {

		assertElement(COSTARICA_ELEMENT_XPATH, myApplicationsUrl, "detailsCostaRica-auslandsangebote.html");
	}

	@Test
	public void testNavigationToUniversityFinland() throws InterruptedException {

		assertElement(FINLAND_ELEMENT_XPATH, myApplicationsUrl, "detailsFinnland-auslandsangebote.html");
	}

	@Test
	public void testNavigationToUniversityDurban() throws InterruptedException {

		assertElement(DURBAN_ELEMENT_XPATH, myApplicationsUrl, "detailsDurban-auslandsangebote.html");
	}

	@Test
	public void testNavigationToFAQ() throws InterruptedException {
		assertElement(FAQ_ELEMENT_XPATH, myApplicationsUrl, "faq.html");

	}

	@Test
	public void testNavigationToApply() throws InterruptedException {
		assertElement(APPLY_ELEMENT_XPATH, myApplicationsUrl, "bewerbungsportal.html#nav2");

	}

	@Test
	public void testNavigationToImpressum() throws InterruptedException {

		assertElement(IMPRESSUM_ELEMENT_XPATH, myApplicationsUrl, "impressum.html");

	}

}
