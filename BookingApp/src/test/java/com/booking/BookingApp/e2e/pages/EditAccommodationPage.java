package com.booking.BookingApp.e2e.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class EditAccommodationPage {

    private WebDriver driver;

    @FindBy(css="h2[class='register-header']")
    public WebElement editAccommodationHeader;
    @FindBy(css="#cancellationDeadline")
   // @FindBy(xpath = "//*[@id=\"mat-input-6\"]")
    public WebElement editCancellationDeadLineInput;
    @FindBy(xpath="//form/div/div[8]/button[1]/span[2]")
    public WebElement saveChangesButton;

    @FindBy(css="#mat-snack-bar-container-live-0 > div > simple-snack-bar > div.mat-mdc-snack-bar-label.mdc-snackbar__label")
    public WebElement negativeSnackBar;

    @FindBy(css="#mat-snack-bar-container-live-1 > div > simple-snack-bar > div.mat-mdc-snack-bar-label.mdc-snackbar__label")
    public WebElement zeroEmptySnackBar;

    @FindBy(css="simple-snack-bar.mat-mdc-simple-snack-bar")
    public WebElement addedPriceCardSnackBar;

    @FindBy(css="simple-snack-bar.mat-mdc-simple-snack-bar")
    public WebElement datesInFutureSnackBar;

    @FindBy(css="#mat-input-14")
    public WebElement startDateElem;
    @FindBy(css="#endDate")
    public WebElement endDateElem;
    @FindBy(css="#price")
    public WebElement priceElem;

    @FindBy(css="table tbody tr")
    public List<WebElement> priceCards;

    @FindBy(css="#mat-radio-3-input")
    public WebElement perUnitButton;

    @FindBy(css="form > div > div.mat-elevation-z8 > div.data-container > button > span.mdc-button__label")
    public WebElement savePriceButtonElem;

    @FindBy(css="#h2Edit")
    public WebElement popUpEdit;

    @FindBy(css="#startDateEdit")
    public WebElement editStartDate;

    @FindBy(css="#endDateEdit")
    public WebElement editEndDate;

    @FindBy(css="#priceEdit")
    public  WebElement editPrice;

    @FindBy(css="#mat-mdc-dialog-0 > div > div > app-edit-price-card-dialog > div > form > div.dialog-actions > button.mdc-button.mat-mdc-button.mat-unthemed.mat-mdc-button-base > span.mdc-button__label")
    public WebElement editSavePrice;




    public EditAccommodationPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public boolean isPageOpened() {
        boolean isOpened = (new WebDriverWait(driver, Duration.ofSeconds(10)))
                .until(ExpectedConditions.textToBePresentInElement(editAccommodationHeader, "Edit Accommodations Data"));

        return isOpened;

    }

    public boolean isSnackBarNegativeVisible() {
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;

        // Scroll into view using JavaScriptExecutor
        jsExecutor.executeScript("arguments[0].scrollIntoView(true);", negativeSnackBar);


        boolean isVisible= (new WebDriverWait(driver, Duration.ofSeconds(10)))
                .until(ExpectedConditions.textToBePresentInElement(negativeSnackBar, "Cancellation deadline can not be negative."));

        return isVisible;

    }

    public void editCancellationDeadLine(String cancellationDeadLine) {
//        Actions actions = new Actions(driver);
//        actions.scrollToElement(editCancellationDeadLineInput).click().perform();
//        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//        WebElement editCancellationDeadLineInputField = wait.until(ExpectedConditions.visibilityOf(editCancellationDeadLineInput));
//        editCancellationDeadLineInputField.sendKeys(cancellationDeadLine);

        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;

        // Scroll into view using JavaScriptExecutor
        jsExecutor.executeScript("arguments[0].scrollIntoView(true);", editCancellationDeadLineInput);


        // Wait for the input field to be clickable
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement editCancellationDeadLineInputField = wait.until(ExpectedConditions.visibilityOf(editCancellationDeadLineInput));

        // Click the input field
       // editCancellationDeadLineInputField.click();

        // Enter the value into the input field
        editCancellationDeadLineInputField.clear();
        editCancellationDeadLineInputField.sendKeys(cancellationDeadLine);

    }

    public void clickSaveChanges() {
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;

        // Scroll into view using JavaScriptExecutor
        jsExecutor.executeScript("arguments[0].scrollIntoView(true);", saveChangesButton);


        // Wait for the input field to be clickable
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement saveChanges= wait.until(ExpectedConditions.elementToBeClickable(saveChangesButton));

        // Click the input field
        saveChanges.click();
    }

    public boolean isSnackBarZeroOrEmptyVisible() {
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;

        // Scroll into view using JavaScriptExecutor
        jsExecutor.executeScript("arguments[0].scrollIntoView(true);", zeroEmptySnackBar);

        boolean isVisible= (new WebDriverWait(driver, Duration.ofSeconds(10)))
                .until(ExpectedConditions.textToBePresentInElement(zeroEmptySnackBar, "Cancellation deadline can not be zero or empty"));

        return isVisible;
    }

    public void addPriceCard(String startDate, String endDate, String price) {




//        int yOffset = -100;
//        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
//
//        // Scroll into view with an offset using JavaScriptExecutor
//        jsExecutor.executeScript("arguments[0].scrollIntoView({block: 'start', inline: 'start', behavior: 'instant'}); window.scrollBy(0, arguments[1]);", startDateElem, yOffset);
//        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
//
//        // Scroll into view using JavaScriptExecutor
//        jsExecutor.executeScript("arguments[0].scrollIntoView(true);", startDateElem);


        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement startDateInputField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#startDate")));
        wait.until(ExpectedConditions.elementToBeClickable(startDateInputField));

        //startDateInputField.click();


        startDateInputField.clear();
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].value = '';", startDateInputField);
        startDateInputField.sendKeys(startDate);

        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement endDateInputField = wait.until(ExpectedConditions.visibilityOf(endDateElem));
        wait.until(ExpectedConditions.elementToBeClickable(endDateInputField));

        //endDateInputField.click();


        endDateInputField.clear();
        js.executeScript("arguments[0].value = '';", endDateInputField);

        endDateInputField.sendKeys(endDate);


        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement priceInputField = wait.until(ExpectedConditions.visibilityOf(priceElem));
        wait.until(ExpectedConditions.elementToBeClickable(priceInputField));

        // Click the input field
       // priceInputField.click();

        // Enter the value into the input field
        priceInputField.clear();
        js.executeScript("arguments[0].value = '';", priceInputField);
        priceInputField.sendKeys(price);

//        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//        WebElement perUnitButtonElem = wait.until(ExpectedConditions.visibilityOf(perUnitButton));

        // Click the input field
       perUnitButton.click();


    }

    public void clickSavePrice(){

        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;

        // Scroll into view using JavaScriptExecutor
        jsExecutor.executeScript("arguments[0].scrollIntoView(true);",savePriceButtonElem);


        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement savePriceButton = wait.until(ExpectedConditions.visibilityOf(savePriceButtonElem));

       savePriceButton.click();


    }

    public void deleteValidPriceCard(String startDate, String endDate) {
//        int yOffset = -1500;
//        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
//
//        // Scroll into view with an offset using JavaScriptExecutor
//        jsExecutor.executeScript("arguments[0].scrollIntoView({block: 'start', inline: 'start', behavior: 'instant'}); window.scrollBy(0, arguments[1]);",startDateElem, yOffset);
////        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
////

        for(WebElement priceCard:priceCards){
            WebElement startDateElem=priceCard.findElement(By.xpath("td[2]"));
            WebElement endDateElem=priceCard.findElement(By.xpath("td[3]"));
            String startText =startDateElem.getText().trim();

            String endText=endDateElem.getText().trim();

            if(startText.equals(startDate)&&(endText.equals(endDate))){
;
               WebElement deleteButton= priceCard.findElement(By.xpath("td[6]/button[2]/span[3]"));
                deleteButton.click();

            }
        }
    }






    public boolean isSnackBarAddedPriceCardVisible() {
        boolean isVisible = (new WebDriverWait(driver, Duration.ofSeconds(10)))
                .until(ExpectedConditions.textToBePresentInElement(addedPriceCardSnackBar, "Price card added!"));
        return isVisible;
    }

    public boolean isPriceCardAddedInTable(String startDate,String endDate) {

        int yOffset = -1500;
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;

        // Scroll into view with an offset using JavaScriptExecutor
        jsExecutor.executeScript("arguments[0].scrollIntoView({block: 'start', inline: 'start', behavior: 'instant'}); window.scrollBy(0, arguments[1]);",startDateElem, yOffset);
//        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
//

        for(WebElement priceCard:priceCards){
            WebElement startDateElem=priceCard.findElement(By.xpath("td[2]"));
            WebElement endDateElem=priceCard.findElement(By.xpath("td[3]"));
            String startText =startDateElem.getText().trim();
            String endText=endDateElem.getText().trim();
            if(startText.equals(startDate)&&(endText.equals(endDate))){
               return true;
            }
        }
        return  false;

    }

    public boolean isPriceCardAddedInTableAgain(String startDate,String endDate) {

        int yOffset = -1500;
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;

        // Scroll into view with an offset using JavaScriptExecutor
        jsExecutor.executeScript("arguments[0].scrollIntoView({block: 'start', inline: 'start', behavior: 'instant'}); window.scrollBy(0, arguments[1]);",startDateElem, yOffset);
//        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
//
        int br=0;
        for(WebElement priceCard:priceCards){
            WebElement startDateElem=priceCard.findElement(By.xpath("td[2]"));
            WebElement endDateElem=priceCard.findElement(By.xpath("td[3]"));
            String startText =startDateElem.getText().trim();
            String endText=endDateElem.getText().trim();
            if(startText.equals(startDate)&&(endText.equals(endDate))){
              br++;
            }
        }
        if(br==2){
            return  true;
        }
        return  false;

    }

    public boolean isSnackBarDatesinFuture() {

        boolean isVisible = (new WebDriverWait(driver, Duration.ofSeconds(10)))
                .until(ExpectedConditions.textToBePresentInElement(datesInFutureSnackBar, "Selected dates must be in the future."));
        return isVisible;

    }
    // Start date must be before end date.

    public boolean isSnackBarStartDateBeforeAndDate() {

        boolean isVisible = (new WebDriverWait(driver, Duration.ofSeconds(10)))
                .until(ExpectedConditions.textToBePresentInElement(datesInFutureSnackBar, "Start date must be before end date."));
        return isVisible;

    }

    public boolean isSnackBarAlreadyExists() {
        boolean isVisible = (new WebDriverWait(driver, Duration.ofSeconds(10)))
                .until(ExpectedConditions.textToBePresentInElement(datesInFutureSnackBar, "Price for this timeslot is already defined and reservations are confirmed!"));
        return isVisible;

    }

    public void editValidPriceCard(String startDate, String endDate) {

        for(WebElement priceCard:priceCards){
            WebElement startDateElem=priceCard.findElement(By.xpath("td[2]"));
            WebElement endDateElem=priceCard.findElement(By.xpath("td[3]"));
            String startText =startDateElem.getText().trim();

            String endText=endDateElem.getText().trim();

            if(startText.equals(startDate)&&(endText.equals(endDate))){

                WebElement deleteButton= priceCard.findElement(By.xpath("td[6]/button[1]/span[3]"));
                deleteButton.click();

            }
        }
    }

    public boolean isPopUpEditVisible() {
        boolean isOpened = (new WebDriverWait(driver, Duration.ofSeconds(10)))
                .until(ExpectedConditions.textToBePresentInElement(popUpEdit, "Edit Dates and Price"));

        return isOpened;
    }

    public void editPriceCard(String startDate, String endDate, String price) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement startDateInputField = wait.until(ExpectedConditions.visibilityOf(editStartDate));
        wait.until(ExpectedConditions.elementToBeClickable(startDateInputField));

        //startDateInputField.click();


        startDateInputField.clear();
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].value = '';", startDateInputField);
        startDateInputField.sendKeys(startDate);

        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement endDateInputField = wait.until(ExpectedConditions.visibilityOf(editEndDate));
        wait.until(ExpectedConditions.elementToBeClickable(endDateInputField));

        //endDateInputField.click();


        endDateInputField.clear();
        js.executeScript("arguments[0].value = '';", endDateInputField);

        endDateInputField.sendKeys(endDate);


        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement priceInputField = wait.until(ExpectedConditions.visibilityOf(editPrice));
        wait.until(ExpectedConditions.elementToBeClickable(priceInputField));

        // Click the input field
        // priceInputField.click();

        // Enter the value into the input field
        priceInputField.clear();
        js.executeScript("arguments[0].value = '';", priceInputField);
        priceInputField.sendKeys(price);

//        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//        WebElement perUnitButtonElem = wait.until(ExpectedConditions.visibilityOf(perUnitButton));

//        // Click the input field
//        perUnitButtonEdit.click();

    }

    public void clickSavePriceEdit() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement saveChanges= wait.until(ExpectedConditions.elementToBeClickable(editSavePrice));

        // Click the input field
        saveChanges.click();

    }

//    public boolean isSnackBarUpdatedPriceCardVisible() {
//    }

    public boolean isPriceCardUpdatedInTable(String startDate, String endDate, String price) {


        for(WebElement priceCard:priceCards){
            WebElement startDateElem = waitForElementVisibility(priceCard, By.xpath("td[2]"));
            WebElement endDateElem = waitForElementVisibility(priceCard, By.xpath("td[3]"));
            WebElement priceElem = waitForElementVisibility(priceCard, By.xpath("td[4]"));
            String startText =startDateElem.getText().trim();
            String endText=endDateElem.getText().trim();
            String priceText=priceElem.getText().trim();
            if(startText.equals(startDate)&&(endText.equals(endDate))&& priceText.equals(price)){
                return true;
            }
        }
        return  false;
    }
    private WebElement waitForElementVisibility(WebElement parentElement, By locator) {
        WebDriverWait wait=new WebDriverWait(driver,Duration.ofSeconds(10));
        return wait.until(ExpectedConditions.visibilityOf(parentElement.findElement(locator)));
    }


    public boolean isSnackBarPriceIsUpdated() {
        boolean isVisible = (new WebDriverWait(driver, Duration.ofSeconds(10)))
                .until(ExpectedConditions.textToBePresentInElement(datesInFutureSnackBar, "Price card is updated..."));
        return isVisible;

    }
}
