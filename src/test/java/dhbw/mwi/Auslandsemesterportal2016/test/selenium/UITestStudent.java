package dhbw.mwi.Auslandsemesterportal2016.test.selenium;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.testng.Assert.assertEquals;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class UITestStudent extends UIBaseClass {
	WebElement myElement;
	WebElement cookiesElement, loginElement;
	private String baseUrl = "http://10.3.15.45/";
	private StringBuffer verificationErrors = new StringBuffer();

	private String myApplicationsUrl = baseUrl + "bewerbungsportal.html";
	private static final String MENUE_ELEMENT_XPATH = "/html/body/div[5]/div/div[1]/nav/button/span";
	private static final String MY_APPLICATIONS_ELEMENT_XPATH = "/html/body/div[5]/div/div[1]/div/div/a[3]";

	private static final String SCOTLAND_ELEMENT_XPATH = "//*[@id=\"auslandsAngebote\"]/div/div/a[1]/div/div[2]";
	private static final String BULGARIA_ELEMENT_XPATH = "//*[@id=\"auslandsAngebote\"]/div/div/a[2]/div/div";
	private static final String SANMARCOS_ELEMENT_XPATH = "//*[@id=\"auslandsAngebote\"]/div/div/a[3]/div/div";
	private static final String CALIFORNIA_ELEMENT_XPATH = "//*[@id=\"auslandsAngebote\"]/div/div/a[4]/div/div";
	private static final String COSTARICA_ELEMENT_XPATH = "//*[@id=\"auslandsAngebote\"]/div/div/a[5]/div/div";
	private static final String DURBAN_ELEMENT_XPATH = "//*[@id=\"auslandsAngebote\"]/div/div/a[6]/div/div";
	private static final String DONGHWA_ELEMENT_XPATH = "//*[@id=\"auslandsAngebote\"]/div/div/a[7]/div/div";
	private static final String FINLAND_ELEMENT_XPATH = "//*[@id=\"auslandsAngebote\"]/div/div/a[8]/div/div";
	private static final String LODZ_ELEMENT_XPATH = "//*[@id=\"auslandsAngebote\"]/div/div/a[9]/div/div";

	private static final String APPLY_ELEMENT_XPATH = "//*[@id=\"nav2\"]";
	private static final String FAQ_ELEMENT_XPATH = "/html/body/div[5]/div/footer/div[1]/div/div/div[1]/nav/nav/ul/li/a";
	private static final String IMPRESSUM_ELEMENT_XPATH = "//*[@id=\"normalBereich\"]/footer/div[2]/div/div/div[2]/nav/a";

	@BeforeMethod
	public void getDriver() throws InterruptedException {
		getDriver("CHROME");
		loginAsStudent();
	}

	private void loginAsStudent() throws InterruptedException {
		Thread.sleep(4000);
		loginElement = driver.findElement(By.xpath("/html/body/div[4]/div/header/div/div/div[1]/button[1]"));
		loginElement.click();
		Thread.sleep(2000);
		driver.findElement(By.id("inEmail")).sendKeys("test@student.dhbw-karlsruhe.de");
		driver.findElement(By.id("inPasswort")).sendKeys("Hallo1234!");
		driver.findElement(By.id("btnLogin")).click();
		Thread.sleep(2000);
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
		driver.findElement(By.xpath(MENUE_ELEMENT_XPATH)).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath(MY_APPLICATIONS_ELEMENT_XPATH)).click();
		assertEquals(myApplicationsUrl, driver.getCurrentUrl());

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
	public void testNavigationToUniversitySanMarcos() throws InterruptedException {

		assertElement(SANMARCOS_ELEMENT_XPATH, myApplicationsUrl, "detailsSanMarcos-auslandsangebote.html");
	}

	@Test
	public void testNavigationToUniversityCalifornia() throws InterruptedException {

		assertElement(CALIFORNIA_ELEMENT_XPATH, myApplicationsUrl, "detailsChannelIsland-auslandsangebote.html");
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
	public void testNavigationToUniversityLodz() throws InterruptedException {

		assertElement(LODZ_ELEMENT_XPATH, myApplicationsUrl, "detailsLodz-auslandsangebote.html");
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
	/*
	 * @Test public void testThatBringsStudentToNewApplication() throws
	 * InterruptedException { Thread.sleep(2000);
	 * driver.findElement(By.id("nav2")).click();
	 * driver.findElement(By.id("newBewProzess")).click(); Thread.sleep(4000);
	 * assertNotNull(driver.findElement(By.xpath("/html/body/div[6]/div"))); }
	 */
	/*
	 * @Test public void testThatBringsStudentToNewApplicationAndBack() throws
	 * InterruptedException { Thread.sleep(2000);
	 * driver.findElement(By.id("nav2")).click();
	 * driver.findElement(By.id("newBewProzess")).click(); Thread.sleep(4000);
	 * driver.findElement(By.xpath("/html/body/div[6]/div/div[3]/button[1]")).click(
	 * ); assertEquals(driver.getCurrentUrl(), baseUrl +
	 * "bewerbungsportal.html#nav2"); }
	 */
	/*
	 * @Test public void testThatChecksVisibility() throws InterruptedException {
	 * Thread.sleep(2000); driver.findElement(By.id("logout")).click();
	 * Thread.sleep(2000); driver.get(baseUrl + "bewerbungsportal.html");
	 * assertEquals(driver.getCurrentUrl(), baseUrl + "index.html"); }
	 */
}
