package com.booking.BookingApp.e2e.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class HomePage {

    private WebDriver driver;

    private static final String PAGE_URL="http://localhost:4200/";

    @FindBy(css="app-nav-bar button:nth-child(2)")
    public WebElement logInButton;

    public HomePage(WebDriver driver){
        this.driver=driver;
        driver.get(PAGE_URL);
        PageFactory.initElements(driver,this);
    }

    public void performLogInAction() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement logIn = wait.until(ExpectedConditions.visibilityOf(logInButton));
        logIn.click();
    }
}
