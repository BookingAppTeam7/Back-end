package com.booking.BookingApp.e2e.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class SearchedAccommodationsPage {
    private WebDriver driver;
    @FindBy(css=".header1")
    public WebElement header;
    @FindBy(css="body > app-root > app-searched-accommodation-cards > div")
    public WebElement container;
    @FindBy(css="#roomBtn")
    public WebElement radioBtnRoom;
    @FindBy(css="#apartmentBtn")
    public WebElement radioBtnApartment;
    @FindBy(css="#vipRoomBtn")
    public WebElement radioBtnVipRoom;
    @FindBy(css="#freeWiFi")
    public WebElement freeWiFiBtn;
    @FindBy(css="#kitchen")
    public WebElement kitchenBtn;
    @FindBy(css="#freeParking")
    public WebElement freeParkingBtn;
    @FindBy(css="#airConditioner")
    public WebElement airConditionerBtn;
    @FindBy(css="#minPriceInput")
    public WebElement minPriceInput;
    @FindBy(css="#maxPriceInput")
    public WebElement maxPriceInput;
    @FindBy(css="#filterBtn")
    public WebElement filterBtn;
    public SearchedAccommodationsPage(WebDriver driver){
        this.driver=driver;
        PageFactory.initElements(driver,this);
    }
    public boolean isPageOpened(){
        boolean isOpened=(new WebDriverWait(driver, Duration.ofSeconds(10)))
                .until(ExpectedConditions.textToBePresentInElement(header,"Accommodations that match your search:"));
        return isOpened;
    }
    public int numberOfAccommodations(){
        List<WebElement> accommodations=driver.findElements(By.className("mat-mdc-card-content"));
        return accommodations.size();
    }
    public void performFilter(String type,boolean wifi,boolean parking,boolean airConditioner,boolean kitchen, String minPrice, String maxPrice){
        WebDriverWait wait=new WebDriverWait(driver,Duration.ofSeconds(5));
        WebElement radioBtnRoom1=wait.until(ExpectedConditions.visibilityOf(radioBtnRoom));
        WebElement radioBtnApartment1=wait.until(ExpectedConditions.visibilityOf(radioBtnApartment));
        WebElement radioBtnVipRoom1=wait.until(ExpectedConditions.visibilityOf(radioBtnVipRoom));
        WebElement freeWiFiBtn1=wait.until(ExpectedConditions.visibilityOf(freeWiFiBtn));
        WebElement freeParkingBtn1=wait.until(ExpectedConditions.visibilityOf(freeParkingBtn));
        WebElement airConditionerBtn1=wait.until(ExpectedConditions.visibilityOf(airConditionerBtn));
        WebElement kitchenBtn1=wait.until(ExpectedConditions.visibilityOf(kitchenBtn));
        WebElement minPriceInputField=wait.until(ExpectedConditions.visibilityOf(minPriceInput));
        WebElement maxPriceInputField=wait.until(ExpectedConditions.visibilityOf(maxPriceInput));
        WebElement filterBtn1=wait.until(ExpectedConditions.visibilityOf(filterBtn));

        if(type.equals("ROOM")){
            radioBtnRoom1.click();
        }else if(type.equals("APARTMENT")){
            radioBtnApartment1.click();
        }else if(type.equals("VIP_ROOM")){
            radioBtnVipRoom1.click();
        }
        if(wifi){
            freeWiFiBtn1.click();
        }
        if(parking){
            freeParkingBtn1.click();
        }
        if(airConditioner){
            airConditionerBtn1.click();
        }
        if(kitchen){
            kitchenBtn1.click();
        }
        minPriceInputField.clear();
        minPriceInputField.sendKeys(minPrice);
        maxPriceInputField.clear();
        maxPriceInputField.sendKeys(maxPrice);
        filterBtn1.click();
    }
    public void scrollToBottom() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        JavascriptExecutor js = (JavascriptExecutor) driver;

        // Scroll to the bottom of the page
        js.executeScript("window.scrollTo(0, document.body.scrollHeight)");

        // Wait until the scroll position reaches the bottom
        wait.until(webDriver -> {
            Number innerHeight = (Number) js.executeScript("return window.innerHeight;");
            Number scrollY = (Number) js.executeScript("return window.scrollY;");
            Number bodyScrollHeight = (Number) js.executeScript("return document.body.scrollHeight;");

            double innerHeightValue = innerHeight.doubleValue();
            double scrollYValue = scrollY.doubleValue();
            double bodyScrollHeightValue = bodyScrollHeight.doubleValue();

            System.out.println("Checking scroll position - Inner height: " + innerHeightValue + ", Scroll Y: " + scrollYValue + ", Body scroll height: " + bodyScrollHeightValue);

            double margin = 10.0; // Adjust as needed
            return (innerHeightValue + scrollYValue + margin) >= bodyScrollHeightValue;
        });
    }
}
