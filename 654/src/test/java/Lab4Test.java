import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.util.Objects;
import java.util.UUID;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.lang.model.element.Element;
import java.time.Duration;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Lab4Test {

	static ChromeDriver driver;
	static WebDriverWait wait;
	static {
		// System.setProperty("webdriver.chrome.driver",
		// "C:\\Users\\Mindaugas\\Desktop\\Eclipse\\chromedriver.exe");
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--remote-allow-origins=*");
		driver = new ChromeDriver(options);
		wait = new WebDriverWait(driver, Duration.ofMillis(20000));
	}
	static String firstname = "Vardenis";
	static String lastname = "Pavardenis";
	static String password = "123456789";
	static String phone = "860000000";
	static String email;

	String test1FileName = "src/data1.txt";
	String test2fileName = "src/data2.txt";

	@BeforeEach
	public void setUp() {
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--remote-allow-origins=*");
		// Ensure the driver is set up here and not reused from a static context if it
		// might have been quit.
		driver = new ChromeDriver(options);
		wait = new WebDriverWait(driver, Duration.ofMillis(20000));
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		driver.get("https://demowebshop.tricentis.com/");
	}

	@AfterEach
	public void tearDown() {
		if (driver != null) {
			driver.quit();
		}
	}

	@BeforeAll
	public static void register() {

		driver.get("https://demowebshop.tricentis.com/");

		driver.findElement(By.xpath("//a[@href='/login' and @class=\"ico-login\"]")).click();

		driver.findElement(By.xpath("//input[@class=\"button-1 register-button\"]")).click();

		driver.findElement(By.xpath("//input[@id=\"gender-male\"]")).click();
		driver.findElement(By.xpath("//input[@id=\"FirstName\" and @class=\"text-box single-line\"]"))
				.sendKeys(Lab4Test.firstname);
		driver.findElement(By.xpath("//input[@id=\"LastName\" and @class=\"text-box single-line\"]"))
				.sendKeys(Lab4Test.lastname);

		String email = String.valueOf(UUID.randomUUID());
		email += "@example.com";
		Lab4Test.email = email;

		driver.findElement(By.xpath("//input[@id=\"Email\"]")).sendKeys(email);

		driver.findElement(By.xpath("//input[@id=\"Password\"]")).sendKeys(Lab4Test.password);
		driver.findElement(By.xpath("//input[@id=\"ConfirmPassword\"]")).sendKeys(Lab4Test.password);

		driver.findElement(By.xpath("//input[@id=\"register-button\"]")).click();

		driver.findElement(By.xpath("//input[@class=\"button-1 register-continue-button\"]")).click();

		driver.quit();

	}

	@Test
	// @ParameterizedTest
	// @ValueSource(strings = {"src/data1.txt", "src/data2.txt"})
	public void test1(/* String fileName */) {

		driver.findElement(By.xpath("//a[@href='/login']")).click();

		driver.findElement(By.xpath("//input[@id=\"Email\"]")).sendKeys(email);
		driver.findElement(By.xpath("//input[@id=\"Password\"]")).sendKeys(password);

		driver.findElement(By.xpath("//input[@class=\"button-1 login-button\"]")).click();

		driver.findElement(By.xpath("//a[@href=\"/digital-downloads\"]")).click();

		try (BufferedReader br = new BufferedReader(new FileReader(/* fileName */test1FileName))) {
			String line;
			while ((line = br.readLine()) != null) {

				wait.until(d -> driver.findElement(By.xpath("//div[@class=\"ajax-loading-block-window\"]"))
						.getAttribute("style").trim().equals("display: none;"));
				driver.findElement(By.xpath(
						"(//input[@value=\"Add to cart\"])[ancestor::div[@class=\"details\"]/h2[@class=\"product-title\"]/a[contains(text(), '"
								+ line + "')]]"))
						.click();

			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		driver.findElement(By.xpath("//span[@class=\"cart-label\"]")).click();

		driver.findElement(By.xpath("//input[@id=\"termsofservice\"]")).click();

		driver.findElement(By.xpath("//button[@id=\"checkout\"]")).click();

		if (driver.findElement(By.xpath("//div[@class='section new-billing-address']")).getAttribute("style").trim()
				.equals("display: none;")) {

			driver.findElement(
					By.xpath("//input[@title=\"Continue\" and @class=\"button-1 new-address-next-step-button\"]"))
					.click();
		} else {

			// driver.findElement(By.xpath("//input[@id=\"BillingNewAddress_FirstName\"]")).sendKeys(firstname);
			// driver.findElement(By.xpath("//input[@id=\"BillingNewAddress_LastName\"]")).sendKeys(lastname);
			WebElement countryDropdown = driver.findElement(By.xpath("//select[@id=\"BillingNewAddress_CountryId\"]"));
			Select selectCountry = new Select(countryDropdown);
			selectCountry.selectByVisibleText("Lithuania");

			driver.findElement(By.xpath("//input[@id=\"BillingNewAddress_City\"]")).sendKeys("Vilnius");
			driver.findElement(By.xpath("//input[@id=\"BillingNewAddress_Address1\"]")).sendKeys("Naujanai");
			driver.findElement(By.xpath("//input[@id=\"BillingNewAddress_ZipPostalCode\"]")).sendKeys("12345");
			driver.findElement(By.xpath("//input[@id=\"BillingNewAddress_PhoneNumber\"]")).sendKeys(phone);

			driver.findElement(
					By.xpath("//input[@title=\"Continue\" and @class=\"button-1 new-address-next-step-button\"]"))
					.click();
		}

		driver.findElement(
				By.xpath("//input[@value=\"Continue\" and @class=\"button-1 payment-method-next-step-button\"]"))
				.click();

		// driver.findElement(By.xpath("//input[@value=\"Continue\" and
		// @class=\"button-1 shipping-method-next-step-button\"]")).click();

		driver.findElement(
				By.xpath("//input[@value=\"Continue\" and @class=\"button-1 payment-method-next-step-button\"]"))
				.click();

		driver.findElement(
				By.xpath("//input[@value=\"Continue\" and @class=\"button-1 payment-info-next-step-button\"]")).click();

		driver.findElement(By.xpath("//input[@class=\"button-1 confirm-order-next-step-button\"]")).click();

		WebElement orderInfo = driver.findElement(By.xpath("//div[@class=\"section order-completed\"]"));
		wait.until(ExpectedConditions.visibilityOfAllElements(orderInfo));

		assertTrue(orderInfo.isDisplayed());
		String titleMessage = driver
				.findElement(By.xpath("//div[@class=\"section order-completed\"]/div[@class=\"title\"]/strong"))
				.getText();
		assertEquals("Your order has been successfully processed!", titleMessage);

	}
}