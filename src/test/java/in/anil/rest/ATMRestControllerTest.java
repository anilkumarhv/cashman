package in.anil.rest;

import com.jayway.restassured.RestAssured;
import in.anil.ATMTestConfiguration;
import in.anil.model.ATMDenomination;
import in.anil.services.ATMUtil;
import in.anil.services.dto.ATMDto;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static com.jayway.restassured.RestAssured.given;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ATMTestConfiguration.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ATMRestControllerTest {

    @LocalServerPort
    int randomServerPort;

    @Before
    public void setup() {
        RestAssured.port = randomServerPort;
    }

    @Test
    public void testATMRESTWithdraw() throws Exception {

        Integer startValue = given()
                .when()
                .get("/atm/admin/totalholdings")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract().as(Integer.class);

        ATMDto result = given()
                .queryParam("amount", "10")
                .when()
                .post("/atm/withdraw")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract().as(ATMDto.class);

        Assert.assertEquals(10, ATMUtil.calculateTotalValue(result.getTheMoney()), 0);

        Integer newValue = given()
                .when()
                .get("/atm/admin/totalholdings")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract().as(Integer.class);

        Assert.assertTrue((startValue - 10) == newValue);

        result = given()
                .queryParam("amount", "80")
                .when()
                .post("/atm/withdraw")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract().as(ATMDto.class);

        Assert.assertEquals(80, ATMUtil.calculateTotalValue(result.getTheMoney()), 0);

        result = given()
                .queryParam("amount", "380")
                .when()
                .post("/atm/withdraw")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract().as(ATMDto.class);

        Assert.assertEquals(380, ATMUtil.calculateTotalValue(result.getTheMoney()), 0);

        result = given()
                .queryParam("amount", startValue - 10 - 80 - 380)
                .when()
                .post("/atm/withdraw")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract().as(ATMDto.class);

        Assert.assertEquals(startValue - 10 - 80 - 380, ATMUtil.calculateTotalValue(result.getTheMoney()), 0);

        Integer finalValue = given()
                .when()
                .get("/atm/admin/totalholdings")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract().as(Integer.class);

        Assert.assertTrue(0 == finalValue);

        given()
                .queryParam("amount", 10)
                .when()
                .post("/atm/withdraw")
                .then()
                .statusCode(HttpStatus.PRECONDITION_FAILED.value());
    }

    @Test
    public void testATMRESTDeposit() throws Exception {

        Integer startValue = given()
                .when()
                .get("/atm/admin/totalholdings")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract().as(Integer.class);

        ATMDto deposit = new ATMDto();
        deposit.getTheMoney().put(ATMDenomination.FIFTY, 1);

        given()
                .contentType("application/json")
                .body(deposit)
                .when()
                .post("/atm/deposit")
                .then()
                .statusCode(HttpStatus.OK.value());

        Integer newValue = given()
                .when()
                .get("/atm/admin/totalholdings")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract().as(Integer.class);

        Assert.assertTrue((startValue + 50) == newValue);
    }
}