package com.booking.BookingApp.e2e.tests.student3;

import com.booking.BookingApp.e2e.pages.HomePage;
import com.booking.BookingApp.e2e.pages.SearchedAccommodationsPage;
import com.booking.BookingApp.e2e.tests.TestBase;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;


public class Student3Test extends TestBase {

    @Test
    public void test() {
        HomePage homePage=new HomePage(driver);
        homePage.performSearchAction("Novi Sad","5","02/14/2024","02/15/2024");

        SearchedAccommodationsPage page=new SearchedAccommodationsPage(driver);
        assertTrue(page.isPageOpened());
        assertEquals(page.numberOfAccommodations(),2);

        page.scrollToBottom();
        page.performFilter("4000");

        assertEquals(page.numberOfAccommodations(),1);

        page.performFilter("100");
        assertEquals(page.numberOfAccommodations(),0);
    }
}
