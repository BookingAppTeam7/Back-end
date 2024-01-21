package com.booking.BookingApp.e2e.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class MyAccommodationsPage {
    private WebDriver driver;

    @FindBy(css="app-owners-accommodations.ng-star-inserted > h2:nth-child(1)")
    public WebElement createAccommodationHeader;

    @FindBy(css="table tbody tr")
    List<WebElement> accommodations;

    public MyAccommodationsPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public boolean isPageOpened() {
        boolean isOpened = (new WebDriverWait(driver, Duration.ofSeconds(10)))
                .until(ExpectedConditions.textToBePresentInElement(createAccommodationHeader, "My accommodations"));

        return isOpened;

    }

    public void editAccommodation(String id, String name) {

        for(WebElement accommodation:accommodations){
            WebElement idElement=accommodation.findElement(By.cssSelector("table > tbody > tr > td.mat-mdc-cell.mdc-data-table__cell.cdk-cell.cdk-column-Id.mat-column-Id.ng-star-inserted"));
            WebElement nameElement=accommodation.findElement(By.cssSelector("table > tbody > tr > td.mat-mdc-cell.mdc-data-table__cell.cdk-cell.cdk-column-Name.mat-column-Name.ng-star-inserted"));
            String nameText = nameElement.getText().trim();
            String idText=idElement.getText().trim();
            if(nameText.equals(name)&&(idText.equals(id))){
                //WebElement editButton = (new WebDriverWait(driver, Duration.ofSeconds(10))).until(ExpectedConditions.elementToBeClickable(accommodation.findElement(By.xpath(".//button[contains(@class,'btn-stilizovano')]"))));
                WebElement editButton = new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("table > tbody > tr > td.mat-mdc-cell.mdc-data-table__cell.cdk-cell.cdk-column-actions.mat-column-actions.ng-star-inserted > button.mdc-button.mdc-button--raised.mat-mdc-raised-button.mat-accent.mat-mdc-button-base > span.mdc-button__label")));
                //editButton.click();
                editButton.click();
                break;
            }
        }

    }
}
