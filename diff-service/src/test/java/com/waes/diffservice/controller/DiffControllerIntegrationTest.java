package com.waes.diffservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.waes.diffservice.DiffServiceApplication;
import com.waes.diffservice.data.DiffData;
import com.waes.diffservice.enums.DiffType;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.mapper.ObjectMapperType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static com.waes.diffservice.utils.TestUtils.*;
import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = RANDOM_PORT,
        classes = DiffServiceApplication.class)
@TestPropertySource(
        locations = "classpath:application-integrationtest.properties")
public class DiffControllerIntegrationTest {
    private static RequestSpecification spec;

    @LocalServerPort
    private int port;

    @Before
    public void setup() {
        spec = new RequestSpecBuilder().setContentType(ContentType.JSON).setAccept(ContentType.JSON).setPort(port).setBaseUri("http://localhost").build();
    }

    @Test
    public void saveDiffData_LeftSide_success() throws IOException {
        // given an DiffData with the content for the LEFT side
        Long diffId = getRandomId();
        DiffData diffDataWithLeftContent = createDiffData(diffId, encodeBase64String("testLeft"), null);

        // when executing the post endpoint to save the DiffData containing the LEFT side content
        Response response = apiPost(diffDataWithLeftContent);

        // then the status should be CREATED.
        response.then().assertThat().statusCode(HttpStatus.SC_CREATED);
    }

    @Test
    public void saveDiffContent_RightSide_success() throws IOException {
        // given an DiffData with the content for the LEFT side
        Long diffId = getRandomId();
        DiffData diffDataWithRightContent = createDiffData(diffId, null, encodeBase64String("testRight"));

        // when executing the post endpoint to save the DiffData containing the RIGHT side content
        Response response = apiPost(diffDataWithRightContent);

        // then the status should be CREATED.
        response.then().assertThat().statusCode(HttpStatus.SC_CREATED);
    }

    @Test
    public void executeRunDifferentiator_RightAndLeftSideNotNull_success() throws IOException {
        // given an id, from a DiffData with LEFT and RIGHT sides
        Long diffId = getRandomId();
        DiffData data = createDiffData(diffId, encodeBase64String("testLeft"), encodeBase64String("testRight"));
        apiPost(data);

        // when executing the diff endpoint, then the status should be OK.
        Response response = apiGetDiff(diffId);
        assertEquals(HttpStatus.SC_OK, response.getStatusCode());


        // and the result should contain the DiffData diff-ed, the ResultType and Insights
        DiffData dataResult = stringJsonToDiffResultDTO(response.getBody().asString());
        assertEquals(data.getLeftSide(), dataResult.getLeftSide());
        assertEquals(data.getRightSide(), dataResult.getRightSide());
        assertEquals(DiffType.DIFF, dataResult.getType());
        assertNotNull(dataResult.getInsights());
        assertEquals("[5-7, 9-11]", dataResult.getInsightsAsString());
    }

    private Response apiPost(DiffData data) {
        return given().spec(spec).body(data, ObjectMapperType.JACKSON_2).when().post("/diffs");
    }

    private Response apiGetDiff(Long diffId) {
        return given().spec(spec).get("/diffs/" + diffId);
    }

    private DiffData stringJsonToDiffData(String stringJson) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(stringJson, DiffData.class);
    }

    private DiffData stringJsonToDiffResultDTO(String stringJson) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(stringJson, DiffData.class);
    }
}
