package com.sparta.api_testing_project;

import com.sparta.endpointtesting.pojoconfig.pojos.BrandList;
import com.sparta.endpointtesting.pojoconfig.pojos.BrandsItem;
import com.sparta.endpointtesting.pojoconfig.pojos.ProductListResponse;
import com.sparta.endpointtesting.pojoconfig.pojos.ProductsItem;
import com.sparta.endpointtesting.utils.ApiConfig;
import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class GetBrandTest {

    private static Response response;

    private static BrandList brandList;

    private static BrandsItem brandsItem;

    @BeforeAll
    static void beforeAll(){

        RestAssured.registerParser("text/html", Parser.JSON);

        response = RestAssured
                .given()
                .baseUri(ApiConfig.getBaseUri())
                .when()
                .get(ApiConfig.getBrandsList())
                .then()
                .log().all()
                .extract().response();

        brandList = response.as(BrandList.class);
        //productListResponse = response.as(ProductListResponse.class);


    }

    @Test
    @DisplayName("Status code 200 returned")
    void testStatusCode200(){

        MatcherAssert.assertThat(brandList.getResponseCode(), Matchers.is(200));
    }

    @Test
    @DisplayName("When requesting all brands, each brand should have the fields ID and name.")
    void testAllBrandsHaveFieldsIdAndName(){

        for(int i = 0; i < brandList.getBrands().size(); i++){
            brandsItem = brandList.getBrands().get(i);
            MatcherAssert.assertThat(brandsItem.isNotNull(), Matchers.notNullValue());
        }
        //MatcherAssert.assertThat(brandList.getBrands().size(), Matchers.is(34));
    }



    @Test
    @DisplayName("PUT request to brands endpoint returns 405")
    void testPutBrandsReturns405(){

        Response putResponse = RestAssured
                .given()
                .baseUri(ApiConfig.getBaseUri())
                .header("Content-Type", "application/json")
                .body("""
                {
                    "id": 1,
                    "brand": "Updated Brand"
                }
                """)
                .when()
                .put(ApiConfig.getBrandsList())
                .then()
                .log().all()
                .extract().response();

        MatcherAssert.assertThat(
                putResponse.getStatusCode(),
                Matchers.is(405)
        );

        //Defect: The API returns HTTP 200 for an unsupported HTTP method instead of HTTP 405 Method Not Allowed.
    }



}
