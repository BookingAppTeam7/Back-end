package com.booking.BookingApp.e2e.pages;

import com.booking.BookingApp.e2e.tests.student2.ReservationTable;
import com.booking.BookingApp.models.reservations.Reservation;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class GuestReservationsPage {

    private WebDriver driver;

    @FindBy(css="app-guests-reservations  h2")
    public WebElement reservationsHeader;


    public GuestReservationsPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public boolean isPageOpened() {
        boolean isOpened = (new WebDriverWait(driver, Duration.ofSeconds(10)))
                .until(ExpectedConditions.textToBePresentInElement(reservationsHeader, "Pending Reservations"));

        return isOpened;
    }

    public void cancelReservation(String startDate, String endDate) {

        List<WebElement> rows = driver.findElements(By.cssSelector("app-guests-reservations > div:nth-child(2) > table"));

        for (WebElement row : rows) {
            String rowStartDate = row.findElement(By.cssSelector("app-guests-reservations > div:nth-child(2) > table tr:nth-child(1) > td.mat-mdc-cell.mdc-data-table__cell.cdk-cell.cdk-column-Start-Date.mat-column-Start-Date.ng-star-inserted")).getText();
            String rowEndDate = row.findElement(By.cssSelector("app-guests-reservations > div:nth-child(2) > table  tr:nth-child(1) > td.mat-mdc-cell.mdc-data-table__cell.cdk-cell.cdk-column-End-Date.mat-column-End-Date.ng-star-inserted")).getText();

            if (rowStartDate.equals(startDate) && rowEndDate.equals(endDate)) {

                WebElement cancelButton = row.findElement(By.cssSelector("#cancelReservationButton [class='mat-mdc-button-persistent-ripple mdc-button__ripple'"));

                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
                WebElement myReservations = wait.until(ExpectedConditions.visibilityOf(cancelButton));

                Actions actions = new Actions(driver);
                actions.moveToElement(myReservations).click().perform();

                break;
            }
        }
    }

    public List<ReservationTable> getCancelledReservations(){
        List<ReservationTable> result=new ArrayList<>();
        List<WebElement> cancelledReservations=driver.findElements(By.cssSelector("#cancelledReservationsTable"));
        for(WebElement element:cancelledReservations){
            WebElement startDateElement = new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("app-guests-reservations > div:nth-child(2) > table tr:nth-child(1) > td.mat-mdc-cell.mdc-data-table__cell.cdk-cell.cdk-column-Start-Date.mat-column-Start-Date.ng-star-inserted")));
            String rowStartDate=startDateElement.getText();

            WebElement endDateElement = new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("app-guests-reservations > div:nth-child(2) > table  tr:nth-child(1) > td.mat-mdc-cell.mdc-data-table__cell.cdk-cell.cdk-column-End-Date.mat-column-End-Date.ng-star-inserted")));
            String rowEndDate=endDateElement.getText();

            String rowStatus=element.findElement(By.cssSelector("app-guests-reservations > div:nth-child(3) > table > tbody > tr > td.mat-mdc-cell.mdc-data-table__cell.cdk-cell.cdk-column-Status.mat-column-Status.ng-star-inserted")).getText();

            ReservationTable r=new ReservationTable(rowStartDate,rowEndDate,rowStatus);

            result.add(r);
        }

        return result;
    }
}
