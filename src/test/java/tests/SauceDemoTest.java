package tests;

import io.github.bonigarcia.wdm.WebDriverManager;

import io.qameta.allure.Attachment;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.time.Duration;

public class SauceDemoTest {

    WebDriver driver;

    @BeforeMethod
    public void setup() {

        // Setup ChromeDriver automatically
        WebDriverManager.chromedriver().setup();

        // Launch Browser
        driver = new ChromeDriver();

        // Maximize Browser
        driver.manage().window().maximize();

        // Implicit Wait
        driver.manage().timeouts()
                .implicitlyWait(Duration.ofSeconds(10));

        // Open Website
        driver.get("https://www.saucedemo.com/");
    }

    // Positive Login Test
    @Test(priority = 1)
    public void validLoginTest() {

        driver.findElement(By.id("user-name"))
                .sendKeys("standard_user");

        driver.findElement(By.id("password"))
                .sendKeys("secret_sauce");

        driver.findElement(By.id("login-button"))
                .click();

        // Validation
        String currentUrl = driver.getCurrentUrl();

        Assert.assertTrue(currentUrl.contains("inventory"));

        System.out.println("Valid Login Successful");
    }

    // Negative Login Test
    @Test(priority = 2)
    public void invalidLoginTest() {

        driver.findElement(By.id("user-name"))
                .sendKeys("wrong_user");

        driver.findElement(By.id("password"))
                .sendKeys("wrong_password");

        driver.findElement(By.id("login-button"))
                .click();

        // Capture Error Message
        String errorMessage = driver.findElement(
                        By.xpath("//h3[@data-test='error']"))
                .getText();

        // Validation
        Assert.assertTrue(errorMessage.contains(
                "Username and password do not match"));

        System.out.println("Invalid Login Error Validated");
    }

    // Add To Cart Test
    @Test(priority = 3)
    public void addToCartTest() {

        // Login
        driver.findElement(By.id("user-name"))
                .sendKeys("standard_user");

        driver.findElement(By.id("password"))
                .sendKeys("secret_sauce");

        driver.findElement(By.id("login-button"))
                .click();

        // Add Product
        driver.findElement(By.id("add-to-cart-sauce-labs-backpack"))
                .click();

        // Validate Cart Count
        String cartCount = driver.findElement(
                        By.className("shopping_cart_badge"))
                .getText();

        Assert.assertEquals(cartCount, "1");

        System.out.println("Product Added To Cart Successfully");
    }

    // Checkout Test
    @Test(priority = 4)
    public void checkoutTest() {

        // Login
        driver.findElement(By.id("user-name"))
                .sendKeys("standard_user");

        driver.findElement(By.id("password"))
                .sendKeys("secret_sauce");

        driver.findElement(By.id("login-button"))
                .click();

        // Add Product
        driver.findElement(By.id("add-to-cart-sauce-labs-backpack"))
                .click();

        // Open Cart
        driver.findElement(By.className("shopping_cart_link"))
                .click();

        // Checkout
        driver.findElement(By.id("checkout"))
                .click();

        // Fill Details
        driver.findElement(By.id("first-name"))
                .sendKeys("Mohita");

        driver.findElement(By.id("last-name"))
                .sendKeys("Harsh");

        driver.findElement(By.id("postal-code"))
                .sendKeys("560067");

        // Continue
        driver.findElement(By.id("continue"))
                .click();

        // Finish Order
        driver.findElement(By.id("finish"))
                .click();

        // Confirmation Validation
        String confirmation = driver.findElement(
                        By.className("complete-header"))
                .getText();

        Assert.assertEquals(
                confirmation,
                "Thank you for your order!"
        );

        System.out.println("Checkout Completed Successfully");
    }

    @AfterMethod
    public void tearDown(ITestResult result) throws IOException {

        if (ITestResult.FAILURE == result.getStatus()) {
            TakesScreenshot ts = (TakesScreenshot) driver;
            File source = ts.getScreenshotAs(OutputType.FILE);
            File destination = new File(
                    "./screenshots/" + result.getName() + ".png");
            FileUtils.copyFile(source, destination);
            System.out.println("Screenshot captured");
        }

        driver.quit();
        System.out.println("Browser closed");
    }
}