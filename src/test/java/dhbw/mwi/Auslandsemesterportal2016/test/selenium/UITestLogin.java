package dhbw.mwi.Auslandsemesterportal2016.test.selenium;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import static org.junit.jupiter.api.Assertions.*;

public class UITestLogin extends UIBaseClass {

	
	private StringBuffer verificationErrors = new StringBuffer();
	private String baseUrl = "http://10.3.15.45/";
	private WebElement loginElement;
	private WebElement failedLoginElement;
	
	@BeforeEach
	public void setUp() throws InterruptedException {
		getDriver("CHROME");
		findLoginButton();
	}

	private void findLoginButton() throws InterruptedException {
		loginElement = driver.findElement(By.xpath("/html/body/div[4]/div/header/div/div/div[1]/button[1]"));
		Thread.sleep(2000);
		loginElement.click();
		Thread.sleep(2000);
	}
	
	@Test
	public void testThatUsingLoginWithUnvalidData() throws InterruptedException {
		driver.findElement(By.id("inEmail")).sendKeys("test@email.com");
		driver.findElement(By.id("inPasswort")).sendKeys("test user");
		driver.findElement(By.id("btnLogin")).click();
		Thread.sleep(2000);
		failedLoginElement = driver.findElement(By.xpath("/html/body/div[8]/div/div[1]"));
		assertNotNull(failedLoginElement);
	}
	
	@Test
	public void testThatUsingLoginWithValidData() throws InterruptedException {
		driver.findElement(By.id("inEmail")).sendKeys("test@student.dhbw-karlsruhe.de");
		driver.findElement(By.id("inPasswort")).sendKeys("Hallo1234!");
		driver.findElement(By.id("btnLogin")).click();
		Thread.sleep(2000);
		assertEquals( baseUrl + "bewerbungsportal.html", driver.getCurrentUrl());
	}
	
	
	@AfterEach
	public void tearDown() throws Exception {
		driver.quit();
		String verificationErrorString = verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			fail(verificationErrorString);
		}
	}
}
