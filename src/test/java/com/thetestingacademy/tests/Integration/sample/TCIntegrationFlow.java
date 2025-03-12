package com.thetestingacademy.tests.Integration.sample;

import com.thetestingacademy.base.BaseTest;
import com.thetestingacademy.endpoints.APIConstants;
import com.thetestingacademy.pojos.Booking;
import com.thetestingacademy.pojos.BookingResponse;
import com.thetestingacademy.utils.PropertyReader;
import io.qameta.allure.Description;
import io.qameta.allure.Owner;
import io.restassured.RestAssured;
import io.restassured.internal.RestAssuredResponseImpl;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.Test;

//import static jdk.dynalink.linker.support.Guards.isNotNull;
import static org.assertj.core.api.Assertions.assertThat;

public class TCIntegrationFlow extends BaseTest {

    // Create A Booking, Create a Token
    // Get booking
    // Update the Booking
    // Delete the Booking


    @Test(groups = "integration", priority = 1)
    @Owner("Sumit")
    @Description("TC#INT1 - Step 1. Verify that the Booking can be Created")
    public void testCreateBooking(ITestContext iTestContext) {
        iTestContext.setAttribute("token", getToken());




        requestSpecification.basePath(APIConstants.CREATE_UPDATE_BOOKING_URL);
        response = RestAssured
                .given(requestSpecification)
                .when().body(payloadManager.createPayloadBookingAsString()).post();
        validatableResponse = response.then().log().all();
        //Validatable Assertion
        validatableResponse.statusCode(200);
        //   validatableResponse.body("booking.firstname", Matchers.equalTo("Pramod"));
        //DeSer
        BookingResponse bookingResponse = payloadManager.bookingResponseJava(response.asString());
        //  Assert J
        assertThat(bookingResponse.getBookingid()).isNotNull();
        assertThat(bookingResponse.getBooking().getFirstname()).isNotNull().isNotBlank();
        assertThat(bookingResponse.getBooking().getFirstname()).isEqualTo("Pramod");

        //Set the booking ID
        iTestContext.setAttribute("bookingid", bookingResponse.getBookingid());


    }
    @Test(groups = "integration", priority = 2)
    @Owner("Sumit")
    @Description("TC#INT1 - Step 2. Verify that the Booking By ID")
    public void testVerifyBookingID(ITestContext iTestContext) {
        Integer bookingid = (Integer) iTestContext.getAttribute("bookingid");
        //GET Req
        String  basePathGET = APIConstants.CREATE_UPDATE_BOOKING_URL+"/"+bookingid;
        System.out.println(basePathGET);
        requestSpecification.basePath(basePathGET);
        response = RestAssured
                .given(requestSpecification)
                .when().get();
        validatableResponse = response.then().log().all();
        //Validatable Assertion
        validatableResponse.statusCode(200);
        Booking booking = payloadManager.getResponseFromJSON(response.asString());
        assertThat(booking.getFirstname()).isNotNull().isNotBlank();
        assertThat(booking.getFirstname()).isEqualTo(PropertyReader.readkey("booking.firstname"));






    }
    @Test(groups = "integration", priority = 3)
    @Owner("Sumit")
    @Description("TC#INT1 - Step 3. Verify Updated Booking by ID")
    public void testUpdateBooingID(ITestContext iTestContext) {
        System.out.println("Token -"+ iTestContext.getAttribute("token"));
        String token = (String) iTestContext.getAttribute("token");

        //Put /patch
        Integer bookingid = (Integer) iTestContext.getAttribute("bookingid");
        String  basePathPUTPATCH = APIConstants.CREATE_UPDATE_BOOKING_URL+"/"+bookingid;
        System.out.println(basePathPUTPATCH);

        requestSpecification.basePath(basePathPUTPATCH);
        response = RestAssured
                .given(requestSpecification).cookie("token", token)
                .when().body(payloadManager.fullUpdatePayloadAsString()).put();
        validatableResponse = response.then().log().all();
        //Validatable Assertion
        validatableResponse.statusCode(200);
        Booking booking = payloadManager.getResponseFromJSON(response.asString());
        assertThat(booking.getFirstname()).isNotNull().isNotBlank();
        assertThat(booking.getFirstname()).isEqualTo(PropertyReader.readkey("booking.Put.firstname"));
        assertThat(booking.getLastname()).isEqualTo("Dutta");

    }
    @Test(groups = "integration", priority = 4)
    @Owner("Sumit")
    @Description("TC#INT1 - Step 4. Delete the Booking by ID")
    public void testDeleteBooingID(ITestContext iTestContext) {
        String token = (String) iTestContext.getAttribute("token");
        Assert.assertTrue(true);

        Integer bookingid = (Integer) iTestContext.getAttribute("bookingid");
        String  basePathDELETE = APIConstants.CREATE_UPDATE_BOOKING_URL+"/"+bookingid;
        System.out.println(basePathDELETE);


        requestSpecification.basePath(basePathDELETE).cookie("token", token);
        validatableResponse = RestAssured.given().spec(requestSpecification)
                .when().delete().then().log().all();
        validatableResponse.statusCode(201);
    }
}