package dhbw.mwi.Auslandsemesterportal2016.test.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import org.testng.Assert;
import org.testng.AssertJUnit;
import org.testng.annotations.*;

public class UITestAdmin extends UIBaseClass {

	private StringBuffer verificationErrors = new StringBuffer();
	private WebElement loginElement;
	private String baseUrl = "http://10.3.15.45/";

	@BeforeMethod
	public void setUp() throws InterruptedException {
		getDriver("CHROME");
		driver.get(baseUrl);
		loginAsAdmin();
	}

	private void loginAsAdmin() throws InterruptedException {
		loginElement = driver.findElement(By.xpath("/html/body/div[4]/div/header/div/div/div[1]/button[1]"));
		loginElement.click();
		Thread.sleep(2000);
		driver.findElement(By.id("inEmail")).sendKeys("admin@admin.de");
		driver.findElement(By.id("inPasswort")).sendKeys("admin");
		driver.findElement(By.id("btnLogin")).click();
		Thread.sleep(2000);
	}

	@AfterMethod
	public void tearDown() throws Exception {
		driver.quit();
		String verificationErrorString = verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			Assert.fail(verificationErrorString);
		}
	}

	@Test
	public void testThatChecksIfAdminCanLogin() throws InterruptedException {
		AssertJUnit.assertEquals(baseUrl + "cms.html", driver.getCurrentUrl());
	}

	@Test
	public void testThatClickingApplicationBringsUserToEditPage() throws InterruptedException {
		Thread.sleep(2000);
		driver.findElement(By.xpath("/html/body/div[5]/div/div[2]/div/div[1]/div[2]")).click();
		AssertJUnit.assertEquals(baseUrl + "prozess.html", driver.getCurrentUrl());
	}

	@Test
	public void testThatClickingEditStudentBringsUserToEditPage() throws InterruptedException {
		Thread.sleep(2000);
		driver.findElement(By.xpath("/html/body/div[5]/div/div[2]/div/div[2]/div[1]")).click();
		AssertJUnit.assertEquals(baseUrl + "verwaltung_student.html", driver.getCurrentUrl());
	}

	@Test
	public void testThatClickingEditStudentLeiterBringsUserToEditPage() throws InterruptedException {
		Thread.sleep(2000);
		driver.findElement(By.xpath("/html/body/div[5]/div/div[2]/div/div[2]/div[2]")).click();
		AssertJUnit.assertEquals(baseUrl + "verwaltung_studiengangsleitung.html", driver.getCurrentUrl());
	}

	@Test
	public void testThatClickingEditEmployeeBringsUserToEditPage() throws InterruptedException {
		Thread.sleep(2000);
		driver.findElement(By.xpath("/html/body/div[5]/div/div[2]/div/div[2]/div[3]")).click();
		AssertJUnit.assertEquals(baseUrl + "verwaltung_auslandsamt.html", driver.getCurrentUrl());
	}

	@Test
	public void testThatClickingBackButtonFromApplicationEditBringsUserBackToMainPage() {
		driver.findElement(By.xpath("/html/body/div[5]/div/div[2]/div/div[1]/div[2]")).click();
		driver.findElement(By.xpath("/html/body/div[5]/div/div[2]/span[2]/a")).click();
		AssertJUnit.assertEquals(baseUrl + "cms.html", driver.getCurrentUrl());
	}

	@Test
	public void testThatClickingBackButtonFromEditStudentBringsUserToMainPage() throws InterruptedException {
		driver.findElement(By.xpath("/html/body/div[5]/div/div[2]/div/div[2]/div[1]")).click();
		driver.findElement(By.xpath("/html/body/div[5]/div/div[2]/div[1]/span[2]/a")).click();
		AssertJUnit.assertEquals(baseUrl + "cms.html", driver.getCurrentUrl());
	}

	@Test
	public void testThatClickingBackButtonFromEditStudentLeiterBringsUserToMainPage() throws InterruptedException {
		driver.findElement(By.xpath("/html/body/div[5]/div/div[2]/div/div[2]/div[2]")).click();
		driver.findElement(By.xpath("/html/body/div[6]/div/div[2]/div[1]/span[2]/a")).click();
		AssertJUnit.assertEquals(baseUrl + "cms.html", driver.getCurrentUrl());
	}

	@Test
	public void testThatClickingBackButtonFromEditEmployeeBringsUserToMainPage() throws InterruptedException {
		driver.findElement(By.xpath("/html/body/div[5]/div/div[2]/div/div[2]/div[3]")).click();
		driver.findElement(By.xpath("/html/body/div[6]/div/div[2]/div[1]/span[2]/a")).click();
		AssertJUnit.assertEquals(baseUrl + "cms.html", driver.getCurrentUrl());
	}

	@Test
	public void testThatClickingLogoutFromEditEmployeeBringsUserToHomePage() throws InterruptedException {
		driver.findElement(By.xpath("/html/body/div[5]/div/div[2]/div/div[2]/div[3]")).click();
		Thread.sleep(2000);
		driver.findElement(By.id("logout")).click();
		Thread.sleep(2000);
		AssertJUnit.assertEquals(baseUrl + "index.html", driver.getCurrentUrl());
	}

	@Test
	public void testThatClickingLogoutFromEditStudentLeiterBringsUserToHomePage() throws InterruptedException {
		driver.findElement(By.xpath("/html/body/div[5]/div/div[2]/div/div[2]/div[2]")).click();
		Thread.sleep(2000);
		driver.findElement(By.id("logout")).click();
		Thread.sleep(2000);
		AssertJUnit.assertEquals(baseUrl + "index.html", driver.getCurrentUrl());
	}

	@Test
	public void testThatClickingLogoutFromEditStudentBringsUserToHomePage() throws InterruptedException {
		driver.findElement(By.xpath("/html/body/div[5]/div/div[2]/div/div[2]/div[1]")).click();
		Thread.sleep(2000);
		driver.findElement(By.id("logout")).click();
		Thread.sleep(2000);
		AssertJUnit.assertEquals(baseUrl + "index.html", driver.getCurrentUrl());
	}

	@Test
	public void testThatClickingLogoutFromApplicationEditBringsUserBackToHomePage() throws InterruptedException {
		driver.findElement(By.xpath("/html/body/div[5]/div/div[2]/div/div[1]/div[2]")).click();
		Thread.sleep(2000);
		driver.findElement(By.id("logout")).click();
		Thread.sleep(2000);
		AssertJUnit.assertEquals(baseUrl + "index.html", driver.getCurrentUrl());
	}

	@Test
	public void testThatClickingLogoutFromMainPageEditBringsUserBackToHomePage() throws InterruptedException {
		Thread.sleep(2000);
		driver.findElement(By.id("logout")).click();
		Thread.sleep(2000);
		AssertJUnit.assertEquals(baseUrl + "index.html", driver.getCurrentUrl());
	}
	/*
	 * @Test public void testThatChecksIfAdminPageIsVisibleIfUserIsNotLogin() throws
	 * InterruptedException { Thread.sleep(2000);
	 * driver.findElement(By.id("logout")).click(); Thread.sleep(2000);
	 * driver.get("http://10.3.15.45/cms.html"); AssertJUnit.assertEquals( baseUrl +
	 * "index.html", driver.getCurrentUrl()); }
	 */
}
