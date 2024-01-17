package com.booking.BookingApp.e2e.tests.student2;

import com.booking.BookingApp.e2e.pages.GuestHomePage;
import com.booking.BookingApp.e2e.pages.GuestReservationsPage;
import com.booking.BookingApp.e2e.pages.HomePage;
import com.booking.BookingApp.e2e.pages.LogInPage;
import com.booking.BookingApp.e2e.tests.TestBase;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.testng.Assert.assertTrue;

public class Student2Test extends TestBase {

    public String USERNAME="GOST@gmail.com";
    public String PASSWORD="gost";

    public String RESERVATION_START_DATE="5/24/24";
    public String RESERVATION_END_DATE="5/26/24";

    public String INVALID_RESERVATION_START_DATE="1/25/24";
    public String INVALID_RESERVATION_END_DATE="1/28/24";
    public String RESERVATION_ID="27";      //reservation to be cancelled
    public String INVALID_RESERVATION_ID="23";  //APPROVED  --> can not be cancelled because of cancellation deadline

    public String APPROVED_RESERVATION_ID="30";  //PENDING --> will be automatically approved

    @Test
    public void test() throws InterruptedException {

        HomePage homePage = new HomePage(driver);
        homePage.performLogInAction();

        LogInPage logInPage=new LogInPage(driver);

        boolean isPageOpened=logInPage.isPageOpened();

        assertTrue(isPageOpened);

        logInPage.fillLogInForm(USERNAME,PASSWORD);
        logInPage.performLogInAction();

        GuestHomePage guestHomePage=new GuestHomePage(driver);

        isPageOpened=guestHomePage.isPageOpened();
        assertTrue(isPageOpened);

        guestHomePage.performMyReservationsAction();

        GuestReservationsPage guestReservations=new GuestReservationsPage(driver);

        isPageOpened=guestReservations.isPageOpened();
        assertTrue(isPageOpened);

        guestReservations.cancelReservation(INVALID_RESERVATION_START_DATE,INVALID_RESERVATION_END_DATE,INVALID_RESERVATION_ID);

        guestReservations.snackBarAction("Reservation can not be cancelled because of cancellation deadline.");

        List<ReservationTable> cancelledReservations=guestReservations.getCancelledReservations();

        guestReservations.closeSnackBarAction();


        boolean reservationFound = cancelledReservations.stream()
                .anyMatch(reservation ->
                        reservation.getStartDate().equals(INVALID_RESERVATION_START_DATE) &&
                                reservation.getEndDate().equals(INVALID_RESERVATION_END_DATE) &&
                                reservation.getStatus().equals("CANCELLED") &&
                                reservation.getId().equals(INVALID_RESERVATION_ID));

        assertFalse(reservationFound);

        Thread.sleep(5);


        guestReservations.cancelReservation(RESERVATION_START_DATE,RESERVATION_END_DATE,RESERVATION_ID);

        guestReservations.snackBarActionSuccess("Reservation is successfully CANCELLED!");
        guestReservations.closeSuccessSnackBar();

        driver.navigate().refresh();

        cancelledReservations=guestReservations.getCancelledReservations();


       reservationFound = cancelledReservations.stream()
                .anyMatch(reservation ->
                        reservation.getStartDate().equals(RESERVATION_START_DATE) &&
                                reservation.getEndDate().equals(RESERVATION_END_DATE) &&
                                reservation.getStatus().equals("CANCELLED") &&
                                reservation.getId().equals(RESERVATION_ID));

        assertTrue(reservationFound);


        driver.navigate().refresh();

        List<ReservationTable> approvedReservations=guestReservations.getApprovedReservations();


        reservationFound = approvedReservations.stream()
                .anyMatch(reservation ->
                        reservation.getStartDate().equals(RESERVATION_START_DATE) &&
                                reservation.getEndDate().equals(RESERVATION_END_DATE) &&
                                reservation.getStatus().equals("APPROVED") &&
                                reservation.getId().equals(APPROVED_RESERVATION_ID));

        assertTrue(reservationFound);

        reservationFound = approvedReservations.stream()
                .anyMatch(reservation ->
                        reservation.getStartDate().equals(RESERVATION_START_DATE) &&
                                reservation.getEndDate().equals(RESERVATION_END_DATE) &&
                                reservation.getStatus().equals("APPROVED") &&
                                reservation.getId().equals(RESERVATION_ID));

        assertFalse(reservationFound);
    }
}
