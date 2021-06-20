package dhbw.mwi.Auslandsemesterportal2016.test.selenium;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.testng.Assert.assertEquals;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.AssertJUnit;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class UITestStudent extends UIBaseClass {
	WebElement myElement;
	WebElement cookiesElement, loginElement;
	private String baseUrl = "http://10.3.15.45/";
	private StringBuffer verificationErrors = new StringBuffer();
	
	@BeforeMethod
	public void getDriver() throws InterruptedException {
		getDriver("CHROME");
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
	public void testThatBringsStudentToUniversityScotland() throws InterruptedException {
		Thread.sleep(2000);
		driver.findElement(By.xpath("/html/body/div[5]/div/div[2]/div[2]/div[2]/div/div[2]/div/div/a[1]")).click();
		assertEquals(driver.getCurrentUrl(), baseUrl + "detailsSchottland-auslandsangebote.html");
	}
	
	@Test
	public void testThatBringsStudentToUniversityBulgaria() throws InterruptedException {
		Thread.sleep(2000);
		driver.findElement(By.xpath("/html/body/div[5]/div/div[2]/div[2]/div[2]/div/div[2]/div/div/a[2]")).click();
		assertEquals(driver.getCurrentUrl(), baseUrl + "detailsBulgarien-auslandsangebote.html");
	}
	
	@Test
	public void testThatBringsStudentToUniversityDongHwa() throws InterruptedException {
		Thread.sleep(2000);
		driver.findElement(By.xpath("/html/body/div[5]/div/div[2]/div[2]/div[2]/div/div[2]/div/div/a[3]/div")).click();
		assertEquals(driver.getCurrentUrl(), baseUrl + "detailsTaiwan-auslandsangebote.html");
	}
	
	@Test
	public void testThatBringsStudentToUniversitySanMarcos() throws InterruptedException {
		Thread.sleep(2000);
		driver.findElement(By.xpath("/html/body/div[5]/div/div[2]/div[2]/div[2]/div/div[2]/div/div/a[4]/div")).click();
		assertEquals(driver.getCurrentUrl(), baseUrl + "detailsSanMarcos-auslandsangebote.html");
	}
	
	@Test
	public void testThatBringsStudentToUniversityCostaRica() throws InterruptedException {
		Thread.sleep(2000);
		driver.findElement(By.xpath("/html/body/div[5]/div/div[2]/div[2]/div[2]/div/div[2]/div/div/a[5]/div")).click();
		assertEquals(driver.getCurrentUrl(), baseUrl + "detailsCostaRica-auslandsangebote.html");
	}
	
	@Test
	public void testThatBringsStudentToUniversityFinland() throws InterruptedException {
		Thread.sleep(2000);
		driver.findElement(By.xpath("/html/body/div[5]/div/div[2]/div[2]/div[2]/div/div[2]/div/div/a[6]/div")).click();
		assertEquals(driver.getCurrentUrl(), baseUrl + "detailsFinnland-auslandsangebote.html");
	}
	
	@Test
	public void testThatBringsStudentToUniversityDurban() throws InterruptedException {
		Thread.sleep(2000);
		driver.findElement(By.xpath("/html/body/div[5]/div/div[2]/div[2]/div[2]/div/div[2]/div/div/a[7]/div")).click();
		assertEquals(driver.getCurrentUrl(), baseUrl + "detailsDurban-auslandsangebote.html");
	}
	
	@Test
	public void testThatBringsStudentToFAQ() throws InterruptedException {
		Thread.sleep(2000);
		driver.findElement(By.xpath("/html/body/div[5]/div/footer/div[1]/div/div/div[1]/nav/nav/ul/li/a")).click();
		assertEquals(driver.getCurrentUrl(), baseUrl + "faq.html");
	}
	
	@Test
	public void testThatBringsStudentToImpressum() throws InterruptedException {
		Thread.sleep(2000);
		driver.findElement(By.xpath("/html/body/div[5]/div/footer/div[2]/div/div/div[2]/nav/a")).click();
		assertEquals(driver.getCurrentUrl(), baseUrl + "impressum.html");
	}
	
	@Test
	public void testThatBringsStudentToHomePage() throws InterruptedException {
		Thread.sleep(2000);
		driver.findElement(By.id("logout")).click();
		Thread.sleep(2000);
		assertEquals(driver.getCurrentUrl(), baseUrl + "index.html");
	}
	
	@Test
	public void testThatBringsStudentApplication() throws InterruptedException {
		Thread.sleep(2000);
		driver.findElement(By.id("nav2")).click();
		assertEquals(driver.getCurrentUrl(), baseUrl + "bewerbungsportal.html#nav2");
	}
	
	@Test
	public void testThatBringsStudentToNewApplication() throws InterruptedException {
		Thread.sleep(2000);
		driver.findElement(By.id("nav2")).click();
		driver.findElement(By.id("newBewProzess")).click();
		Thread.sleep(4000);
		assertNotNull(driver.findElement(By.xpath("/html/body/div[6]/div")));
	}
	
	@Test
	public void testThatBringsStudentToNewApplicationAndBack() throws InterruptedException {
		Thread.sleep(2000);
		driver.findElement(By.id("nav2")).click();
		driver.findElement(By.id("newBewProzess")).click();
		Thread.sleep(4000);
		driver.findElement(By.xpath("/html/body/div[6]/div/div[3]/button[1]")).click();
		assertEquals(driver.getCurrentUrl(), baseUrl + "bewerbungsportal.html#nav2");
	}
	
	/*
	@Test
	public void testThatChecksVisibility() throws InterruptedException {
		Thread.sleep(2000);
		driver.findElement(By.id("logout")).click();
		Thread.sleep(2000);
		driver.get(baseUrl + "bewerbungsportal.html");
		assertEquals(driver.getCurrentUrl(), baseUrl + "index.html");
	}
	*/
}
