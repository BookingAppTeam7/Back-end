package com.booking.BookingApp.e2e.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LogInPage {
    private WebDriver driver;

    @FindBy(css=".login-header")
    public WebElement loginHeader;

    @FindBy(css="#mat-input-0")
    public WebElement usernameInput;

    @FindBy(css="#mat-input-1")
    public WebElement passwordInput;

    @FindBy(css="div.example-button-row > button")
    public WebElement logInButton;

    public LogInPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public boolean isPageOpened() {
        boolean isOpened = (new WebDriverWait(driver, Duration.ofSeconds(10)))
                .until(ExpectedConditions.textToBePresentInElement(loginHeader, ""));
        return isOpened;

    }

    public void fillLogInForm(String username,String password){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement usernameInputField = wait.until(ExpectedConditions.visibilityOf(usernameInput));
        WebElement passwordInputField = wait.until(ExpectedConditions.visibilityOf(passwordInput));
        usernameInputField.sendKeys(username);
        passwordInputField.sendKeys(password);
    }

    public void performLogInAction(){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement logIn = wait.until(ExpectedConditions.visibilityOf(logInButton));
        logIn.click();
    }




}
