package com.booking.BookingApp.e2e.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class OwnerHomePage {
    private WebDriver driver;

    @FindBy(xpath="//span[contains(text(),'My Accommodations')]")
    public WebElement createAccommodationButton;

    public OwnerHomePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public boolean isPageOpened() {

        WebElement myReservationsButton = (new WebDriverWait(driver, Duration.ofSeconds(10)))
                .until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("mat-toolbar button:nth-child(2)")));

        return myReservationsButton.isDisplayed();
    }

    public void performMyAccommodations(){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement createAccommodation = wait.until(ExpectedConditions.visibilityOf(createAccommodationButton));
        createAccommodation.click();

    }
}
