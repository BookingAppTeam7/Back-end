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

    @FindBy(css="#mat-snack-bar-container-live-0 simple-snack-bar > div.mat-mdc-snack-bar-label.mdc-snackbar__label")
    public  WebElement snackBar;

    @FindBy(css="#mat-snack-bar-container-live-0 simple-snack-bar > div.mat-mdc-snack-bar-actions.mdc-snackbar__actions.ng-star-inserted > button > span.mat-mdc-button-persistent-ripple.mdc-button__ripple")
    public WebElement snackBarCloseButton;

    @FindBy(css="#cdk-overlay-0 > mat-snack-bar-container > div")
    public WebElement snackBarSuccess;

    public GuestReservationsPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public boolean isPageOpened() {
        boolean isOpened = (new WebDriverWait(driver, Duration.ofSeconds(10)))
                .until(ExpectedConditions.textToBePresentInElement(reservationsHeader, "Pending Reservations"));

        return isOpened;
    }

    public void cancelReservation(String startDate, String endDate,String reservationId) {

        List<WebElement> rows = driver.findElements(By.cssSelector("#approvedReservationsTable tbody tr"));

        for (WebElement row : rows) {

            String rowStartDate = row.findElement(By.cssSelector("td.mat-column-Start-Date")).getText();
            String rowEndDate = row.findElement(By.cssSelector("td.mat-column-End-Date")).getText();
            String rowId = row.findElement(By.cssSelector("td.mat-column-Id")).getText();


            if (rowStartDate.equals(startDate) && rowEndDate.equals(endDate) && rowId.equals(reservationId)) {
                WebElement cancelButton = row.findElement(By.cssSelector("#cancelReservationButton [class='mat-mdc-button-persistent-ripple mdc-button__ripple'"));

                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
                WebElement myReservations = wait.until(ExpectedConditions.visibilityOf(cancelButton));

                Actions actions = new Actions(driver);
                actions.moveToElement(myReservations).click().perform();

                break;
            }
        }
    }

    public List<ReservationTable> getCancelledReservations() {
        List<ReservationTable> result = new ArrayList<>();

        List<WebElement> cancelledRows = driver.findElements(By.cssSelector("#cancelledReservationsTable tbody tr"));

        for (WebElement row : cancelledRows) {
            String rowStartDate = row.findElement(By.cssSelector("td.mat-column-Start-Date")).getText();
            String rowEndDate = row.findElement(By.cssSelector("td.mat-column-End-Date")).getText();
            String rowStatus = row.findElement(By.cssSelector("td.mat-column-Status")).getText();
            String rowId = row.findElement(By.cssSelector("td.mat-column-Id")).getText();

            ReservationTable r = new ReservationTable(rowId, rowStartDate, rowEndDate, rowStatus);
            result.add(r);
        }

        return result;
    }

    public List<ReservationTable> getApprovedReservations() {
        List<ReservationTable> result = new ArrayList<>();
        List<WebElement> approvedReservations = driver.findElements(By.cssSelector("#approvedReservationsTable tbody tr"));

        for (WebElement element : approvedReservations) {
            String rowStartDate = element.findElement(By.cssSelector("td.mat-column-Start-Date")).getText();
            String rowEndDate = element.findElement(By.cssSelector("td.mat-column-End-Date")).getText();
            String rowStatus = element.findElement(By.cssSelector("td.mat-column-Status")).getText();
            String rowId = element.findElement(By.cssSelector("td.mat-column-Id")).getText();

            ReservationTable r = new ReservationTable(rowId, rowStartDate, rowEndDate, rowStatus);
            result.add(r);
        }

        return result;
    }


    public boolean snackBarAction(String text){
        boolean shown = (new WebDriverWait(driver, Duration.ofSeconds(10)))
                .until(ExpectedConditions.textToBePresentInElement(snackBar, text));

        return shown;
    }

    public void closeSnackBarAction(){

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement close = wait.until(ExpectedConditions.visibilityOf(snackBarCloseButton));

        Actions actions = new Actions(driver);
        actions.moveToElement(close).click().perform();

    }

    public boolean snackBarActionSuccess(String text) {
        By snackBarSelector = By.cssSelector("simple-snack-bar.mat-mdc-simple-snack-bar .mat-mdc-snack-bar-label");

        boolean shown = (new WebDriverWait(driver, Duration.ofSeconds(10)))
                .until(ExpectedConditions.textToBePresentInElementLocated(snackBarSelector, text));

        return shown;
    }


    public void closeSuccessSnackBar() {

        By snackBarSelector = By.cssSelector("simple-snack-bar.mat-mdc-simple-snack-bar");

        WebElement snackBar = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(snackBarSelector));

        WebElement okButton = snackBar.findElement(By.cssSelector("button.mat-mdc-snack-bar-action"));
        okButton.click();
    }



}
