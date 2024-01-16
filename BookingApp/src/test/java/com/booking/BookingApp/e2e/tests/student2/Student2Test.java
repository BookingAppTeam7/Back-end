package com.booking.BookingApp.e2e.tests.student2;

import com.booking.BookingApp.e2e.pages.GuestHomePage;
import com.booking.BookingApp.e2e.pages.GuestReservationsPage;
import com.booking.BookingApp.e2e.pages.HomePage;
import com.booking.BookingApp.e2e.pages.LogInPage;
import com.booking.BookingApp.e2e.tests.TestBase;
import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

public class Student2Test extends TestBase {
    @Test
    public void test() {

        HomePage homePage = new HomePage(driver);
        homePage.performLogInAction();

        LogInPage logInPage=new LogInPage(driver);

        boolean isPageOpened=logInPage.isPageOpened();

        assertTrue(isPageOpened);

        logInPage.fillLogInForm("GOST@gmail.com","gost");
        logInPage.performLogInAction();

        GuestHomePage guestHomePage=new GuestHomePage(driver);

        isPageOpened=guestHomePage.isPageOpened();
        assertTrue(isPageOpened);

        guestHomePage.performMyReservationsAction();

        GuestReservationsPage guestReservations=new GuestReservationsPage(driver);

        isPageOpened=guestReservations.isPageOpened();
        assertTrue(isPageOpened);


    }
}
