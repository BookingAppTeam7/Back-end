package com.booking.BookingApp.e2e.tests.student1;

import com.booking.BookingApp.e2e.pages.*;
import com.booking.BookingApp.e2e.tests.TestBase;
import org.testng.annotations.Test;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class Student1Test extends TestBase {
    @Test
    public void test() throws InterruptedException {
        Thread thread = null;

        HomePage homePage = new HomePage(driver);
        homePage.performLogInAction();

        LogInPage logInPage=new LogInPage(driver);

        boolean isPageOpened=logInPage.isPageOpened();

        assertTrue(isPageOpened);

        logInPage.fillLogInForm("novivlasnik@gmail.com","novivlasnik");
        logInPage.performLogInAction();

        OwnerHomePage ownerHomePage=new OwnerHomePage(driver);

        isPageOpened=ownerHomePage.isPageOpened();
        assertTrue(isPageOpened);

        ownerHomePage.performMyAccommodations();

        MyAccommodationsPage myAccommodationsPage=new MyAccommodationsPage(driver);

        isPageOpened=myAccommodationsPage.isPageOpened();
        assertTrue(isPageOpened);

       myAccommodationsPage.editAccommodation("15","Snave");

       EditAccommodationPage editAccommodationPage=new EditAccommodationPage(driver);
       isPageOpened=editAccommodationPage.isPageOpened();
       assertTrue(isPageOpened);

       //test kada je korisnik uneo negativni rok za otkaz rezervacije
       editAccommodationPage.editCancellationDeadLine("-10");
       thread.sleep(2000);
       editAccommodationPage.clickSaveChanges();
        boolean isSnackBarNegativeVisible=editAccommodationPage.isSnackBarNegativeVisible();
       assertTrue(isSnackBarNegativeVisible);
        //test kada je korisnik uneo 0 za rok za otkaz rezervacije
        editAccommodationPage.editCancellationDeadLine("0");
        editAccommodationPage.clickSaveChanges();
        boolean isSnackZeroOrEmptyVisible=editAccommodationPage.isSnackBarZeroOrEmptyVisible();
        assertTrue(isSnackZeroOrEmptyVisible);
        //test kada korisnik nije uneo rok za otkaz rezervacije
        editAccommodationPage.editCancellationDeadLine("");
        isSnackZeroOrEmptyVisible=editAccommodationPage.isSnackBarZeroOrEmptyVisible();
        assertTrue(isSnackZeroOrEmptyVisible);
        editAccommodationPage.clickSaveChanges();
        ;
        editAccommodationPage.editCancellationDeadLine("2");
        //thread.sleep(5000);
        //testovi za dodavanje priceCarda

        //1. slucaj kad je validno

        editAccommodationPage.addPriceCard("2/11/2056","2/12/2056","2000");
        editAccommodationPage.clickSavePrice();
        //ovde jos provera dal se ispise i dal je u tabeli
        boolean isSnackBarAddedPriceCardVisible=editAccommodationPage.isSnackBarAddedPriceCardVisible();
        assertTrue(isSnackBarAddedPriceCardVisible);
        // da li je u tabeli
        boolean priceCardAddedInTable=editAccommodationPage.isPriceCardAddedInTable("2/11/56","2/12/56");
        assertTrue(priceCardAddedInTable);
        editAccommodationPage.deleteValidPriceCard("2/11/56","2/12/56");
        thread.sleep(5000);


        //2. slucaj kad je datum start u proslosti
        editAccommodationPage.addPriceCard("2/11/2021","2/12/2023","2000");
        editAccommodationPage.clickSavePrice();
        //ovde jos provera dal se ispise i dal je u tabeli
        boolean isSnackBarDatesMustBeInFutureVisible=editAccommodationPage.isSnackBarDatesinFuture();
        assertTrue(isSnackBarDatesMustBeInFutureVisible);
        // da li je u tabeli
       priceCardAddedInTable=editAccommodationPage.isPriceCardAddedInTable("2/11/21","2/12/23");
        assertFalse(priceCardAddedInTable);

        thread.sleep(2000);

        //3. slucaj kad je datum end u proslosti
        editAccommodationPage.addPriceCard("2/11/2019","2/12/2019","2000");
        editAccommodationPage.clickSavePrice();
        //ovde jos provera dal se ispise i dal je u tabeli
         isSnackBarDatesMustBeInFutureVisible=editAccommodationPage.isSnackBarDatesinFuture();
        assertTrue(isSnackBarDatesMustBeInFutureVisible);
        // da li je u tabeli
        priceCardAddedInTable=editAccommodationPage.isPriceCardAddedInTable("2/11/19","2/12/19");
        assertFalse(priceCardAddedInTable);

        thread.sleep(2000);


        //4. slucaj kad je end date pre start date
        editAccommodationPage.addPriceCard("2/12/2028","2/11/2028","2000");
        editAccommodationPage.clickSavePrice();
        //ovde jos provera dal se ispise i dal je u tabeli
        isSnackBarDatesMustBeInFutureVisible=editAccommodationPage.isSnackBarStartDateBeforeAndDate();
        assertTrue(isSnackBarDatesMustBeInFutureVisible);
        // da li je u tabeli
        priceCardAddedInTable=editAccommodationPage.isPriceCardAddedInTable("2/12/28","2/11/28");
        assertFalse(priceCardAddedInTable);

        thread.sleep(2000);

        //5. slucaj kad je price negativno
        editAccommodationPage.addPriceCard("2/11/2030","2/12/2030","-2000");
        editAccommodationPage.clickSavePrice();
        //ovde jos provera dal se ispise i dal je u tabeli

        // da li je u tabeli
        priceCardAddedInTable=editAccommodationPage.isPriceCardAddedInTable("2/11/30","2/12/30");
        assertFalse(priceCardAddedInTable);

        thread.sleep(2000);

        //6. slucaj kad je price 0
        editAccommodationPage.addPriceCard("2/11/2030","2/12/2030","0");
        editAccommodationPage.clickSavePrice();
        //ovde jos provera dal se ispise i dal je u tabeli

        // da li je u tabeli
        priceCardAddedInTable=editAccommodationPage.isPriceCardAddedInTable("2/11/30","2/12/30");
        assertFalse(priceCardAddedInTable);

        thread.sleep(2000);

        //7. slucaj kad je vec definisana cena za taj timeSLot
        editAccommodationPage.addPriceCard("4/17/2024","5/17/2024","2000");
        editAccommodationPage.clickSavePrice();
        //ovde jos provera dal se ispise i dal je u tabeli
        boolean isSnackBarAlreadyExistsVisible=editAccommodationPage.isSnackBarAlreadyExists();
        assertTrue(isSnackBarAlreadyExistsVisible);
        // da li je u tabeli
        boolean priceCardAddedInTableAgain=editAccommodationPage.isPriceCardAddedInTableAgain("4/17/24","5/17/24");
        assertFalse(priceCardAddedInTableAgain);

        thread.sleep(2000);

        //8. slucaj kad rezervacija vec postoji u celom tom intervalu
        editAccommodationPage.addPriceCard("5/6/2029","8/24/2029","2000");
        editAccommodationPage.clickSavePrice();
        //ovde jos provera dal se ispise i dal je u tabeli
       isSnackBarAlreadyExistsVisible=editAccommodationPage.isSnackBarAlreadyExists();
        assertTrue(isSnackBarAlreadyExistsVisible);
        // da li je u tabeli
        priceCardAddedInTableAgain=editAccommodationPage.isPriceCardAddedInTableAgain("5/6/2029","8/24/2029");
        assertFalse(priceCardAddedInTableAgain);

        thread.sleep(2000);


        //9. slucaj kad rezervacija vec postoji u okviru datuma
        editAccommodationPage.addPriceCard("3/6/2029","8/24/2029","2000");
        editAccommodationPage.clickSavePrice();
        //ovde jos provera dal se ispise i dal je u tabeli
        isSnackBarAlreadyExistsVisible=editAccommodationPage.isSnackBarAlreadyExists();
        assertTrue(isSnackBarAlreadyExistsVisible);
        // da li je u tabeli
        priceCardAddedInTableAgain=editAccommodationPage.isPriceCardAddedInTableAgain("3/6/2029","8/24/2029");
        assertFalse(priceCardAddedInTableAgain);

        thread.sleep(2000);


        //9. slucaj kad rezervacija vec postoji u okviru datuma
        editAccommodationPage.addPriceCard("5/10/2029","8/20/2029","2000");
        editAccommodationPage.clickSavePrice();
        //ovde jos provera dal se ispise i dal je u tabeli
        isSnackBarAlreadyExistsVisible=editAccommodationPage.isSnackBarAlreadyExists();
        assertTrue(isSnackBarAlreadyExistsVisible);
        // da li je u tabeli
        priceCardAddedInTableAgain=editAccommodationPage.isPriceCardAddedInTableAgain("5/10/2029","8/20/2029");
        assertFalse(priceCardAddedInTableAgain);

        thread.sleep(2000);


        //10. slucaj kad rezervacija vec postoji u okviru datuma
        editAccommodationPage.addPriceCard("6/6/2029","8/28/2029","2000");
        editAccommodationPage.clickSavePrice();
        //ovde jos provera dal se ispise i dal je u tabeli
        isSnackBarAlreadyExistsVisible=editAccommodationPage.isSnackBarAlreadyExists();
        assertTrue(isSnackBarAlreadyExistsVisible);
        // da li je u tabeli
        priceCardAddedInTableAgain=editAccommodationPage.isPriceCardAddedInTableAgain("6/6/2029","8/28/2029");
        assertFalse(priceCardAddedInTableAgain);

        thread.sleep(2000);

        //10. slucaj kad rezervacija vec postoji u okviru datuma
        editAccommodationPage.addPriceCard("5/6/2029","8/28/2029","2000");
        editAccommodationPage.clickSavePrice();
        //ovde jos provera dal se ispise i dal je u tabeli
        isSnackBarAlreadyExistsVisible=editAccommodationPage.isSnackBarAlreadyExists();
        assertTrue(isSnackBarAlreadyExistsVisible);
        // da li je u tabeli
        priceCardAddedInTableAgain=editAccommodationPage.isPriceCardAddedInTableAgain("5/6/2029","8/28/2029");
        assertFalse(priceCardAddedInTableAgain);

        thread.sleep(2000);



        //TESTIRANJE UPDATE-OVANJA DOSTUPNOSTI I CENA SMESTAJA
        editAccommodationPage.editValidPriceCard("1/19/39","2/17/39");
        boolean isPopUpVisible=editAccommodationPage.isPopUpEditVisible();
        assertTrue(isPopUpVisible);



        //1. slucaj kad je validno

        editAccommodationPage.editPriceCard("1/19/2039","2/17/2039","8000");
        editAccommodationPage.clickSavePriceEdit();
        thread.sleep(2000);
        //ovde jos provera dal se ispise i dal je u tabeli
//        boolean isSnackBarUpdatedPriceCardVisible=editAccommodationPage.isSnackBarUpdatedPriceCardVisible();
//        assertTrue(isSnackBarUpdatedPriceCardVisible);
        // da li je u tabeli
        boolean priceCardUpdatedInTable=editAccommodationPage.isPriceCardUpdatedInTable("1/19/39","2/17/39","8000");
        assertTrue(priceCardUpdatedInTable);

        isSnackBarAlreadyExistsVisible=editAccommodationPage.isSnackBarPriceIsUpdated();
        assertTrue(isSnackBarAlreadyExistsVisible);


        editAccommodationPage.editValidPriceCard("1/19/39","2/17/39");
        isPopUpVisible=editAccommodationPage.isPopUpEditVisible();
        assertTrue(isPopUpVisible);

//       // 2. slucaj kad je datum start u proslosti
//        editAccommodationPage.editPriceCard("1/19/2021","2/17/2021","8000");
//
//        editAccommodationPage.clickSavePrice();
//        //ovde jos provera dal se ispise i dal je u tabeli
//        isSnackBarDatesMustBeInFutureVisible=editAccommodationPage.isSnackBarDatesinFuture();
//        assertTrue(isSnackBarDatesMustBeInFutureVisible);
//        // da li je u tabeli
//        priceCardAddedInTable=editAccommodationPage.isPriceCardUpdatedInTable("1/19/21","2/17/23","8000");
//        assertFalse(priceCardAddedInTable);
//
//        thread.sleep(2000);


    }

}
