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

    private static final String PAGE_URL="http://localhost:4200/home";

    @FindBy(css="app-nav-bar button:nth-child(2)")
    public WebElement logInButton;
    @FindBy(css="#mat-input-0")
    public WebElement cityInput;
    @FindBy(css="#mat-input-1")
    public WebElement guestsInput;
    @FindBy(css="#mat-input-2")
    public WebElement calendar1Input;
    @FindBy(css="#mat-input-3")
    public WebElement calendar2Input;
    @FindBy(css="button[id='searchBtn'] span[class='mdc-button__label']")
    public WebElement btnSearch;

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
    public void performSearchAction(String city, String numGuests,String arrivalDate,String checkoutDate) {
        WebDriverWait wait=new WebDriverWait(driver,Duration.ofSeconds(10));
        WebElement cityInputField=wait.until(ExpectedConditions.visibilityOf(cityInput));
        WebElement guestsInputField=wait.until(ExpectedConditions.visibilityOf(guestsInput));
        WebElement calendar1InputField=wait.until(ExpectedConditions.visibilityOf(calendar1Input));
        WebElement calendar2InputField=wait.until(ExpectedConditions.visibilityOf(calendar2Input));
        WebElement btnSearch1=wait.until(ExpectedConditions.visibilityOf(btnSearch));

        cityInputField.sendKeys(city);
        guestsInputField.clear();
        guestsInputField.sendKeys(numGuests);
        calendar1InputField.sendKeys(arrivalDate);
        calendar2InputField.sendKeys(checkoutDate);
        btnSearch1.click();

    }
}
