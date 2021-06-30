package dhbw.mwi.Auslandsemesterportal2016.test.selenium;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class UITestHomePage extends UIBaseClass {

	private StringBuffer verificationErrors = new StringBuffer();
	private WebElement universityElement;
	private String baseUrl = "http://10.3.15.45/";
	private WebElement navigationElement;
	private WebElement erfahrungsberichtElement;
	private static final String BULGARIA_ELEMENT_XPATH = "/html/body/div[5]/div/div[3]/div/div/a[2]/div/div[1]";
	private static final String SCOTLAND_ELEMENT_XPATH = "/html/body/div[5]/div/div[3]/div/div/a[1]/div/div[2]";
	private static final String DONGHWA_ELEMENT_XPATH = "/html/body/div[5]/div/div[3]/div/div/a[3]/div/div[2]";
	private static final String SANMARCOS_ELEMENT_XPATH = "/html/body/div[5]/div/div[3]/div/div/a[4]/div/div[1]";
	private static final String COSTARICA_ELEMENT_XPATH = "/html/body/div[5]/div/div[3]/div/div/a[5]/div/div[2]";
	private static final String FINLAND_ELEMENT_XPATH = "/html/body/div[5]/div/div[3]/div/div/a[6]/div/div[1]";
	private static final String DURBAN_ELEMENT_XPATH = "/html/body/div[5]/div/div[3]/div/div/a[7]/div/div[2]";
	private static final String FAQ_ELEMENT_XPATH = "/html/body/div[5]/div/footer/div[1]/div/div/div[1]/nav/nav/ul/li/a";
	private static final String IMPRESSUM_ELEMENT_XPATH = "/html/body/div[5]/div/footer/div[2]/div/div/div[2]/nav/a";
	private static final String ERFAHRUNGSBERICHT_ELEMENT_XPATH = "/html/body/div[5]/div/div[1]/div/div/a[2]";

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
	public void testThatClickingUniversityScotlandLinkBringsUserToUniversity() throws InterruptedException {

		assertElement(SCOTLAND_ELEMENT_XPATH, "detailsSchottland-auslandsangebote.html");
	}

	@Test
	public void testThatClickingUniversityBulgarienLinkBringsUserToUniversity() throws InterruptedException {

		assertElement(BULGARIA_ELEMENT_XPATH, "detailsBulgarien-auslandsangebote.html");
	}

	@Test
	public void testThatClickingUniversityDongHwaLinkBringsUserToUniversity() throws InterruptedException {

		assertElement(DONGHWA_ELEMENT_XPATH, "detailsTaiwan-auslandsangebote.html");
	}

	@Test
	public void testThatClickingUniversitySanMarcosLinkBringsUserToUniversity() throws InterruptedException {

		assertElement(SANMARCOS_ELEMENT_XPATH, "detailsSanMarcos-auslandsangebote.html");
	}

	@Test
	public void testThatClickingUniversityCostaRicaLinkBringsUserToUniversity() throws InterruptedException {

		assertElement(COSTARICA_ELEMENT_XPATH, "detailsCostaRica-auslandsangebote.html");
	}

	@Test
	public void testThatClickingUniversityFinlandLinkBringsUserToUniversity() throws InterruptedException {

		assertElement(FINLAND_ELEMENT_XPATH, "detailsFinnland-auslandsangebote.html");
	}

	@Test
	public void testThatClickingUniversityDurbanLinkBringsUserToUniversity() throws InterruptedException {

		assertElement(DURBAN_ELEMENT_XPATH, "detailsDurban-auslandsangebote.html");
	}

	@Test
	public void testThatClickingFAQLinkBringsUserToFAQ() throws InterruptedException {

		assertElement(FAQ_ELEMENT_XPATH, "faq.html");

	}

	@Test
	public void testThatClickingImpressumLinkBringsUserToImpressum() throws InterruptedException {

		assertElement(IMPRESSUM_ELEMENT_XPATH, "impressum.html");
	}

	@Test
	public void testThatClickingErfahrungsberichteFromFAQBringsUserToErfahrungsbericht() throws InterruptedException {
		driver.get(baseUrl);
		Thread.sleep(9000);
		navigationElement = driver.findElement(By.xpath("/html/body/div[5]/div/div[1]/nav/button"));
		navigationElement.click();
		Thread.sleep(3000);
		erfahrungsberichtElement = driver.findElement(By.xpath("/html/body/div[5]/div/div[1]/div/div/a[2]"));
		erfahrungsberichtElement.click();
		assertEquals(driver.getCurrentUrl(), baseUrl + "index.html#erfahrungsBerichte");
	}

	private void assertElement(String xPath, String expextedResultPage) throws InterruptedException {
		driver.get(baseUrl);
		universityElement = driver.findElement(By.xpath(xPath));
		Thread.sleep(3000);
		universityElement.click();
		Thread.sleep(3000);
		assertEquals(baseUrl + expextedResultPage, driver.getCurrentUrl());
	}

}
