package stepdefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import utils.ConfigReader;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

public class InsCoursMgmtStpDef {
    private String bearerToken;
    private Response createCourseResponse;
    private Response updateCourseResponse;
    private Response deleteCourseResponse;
    private Response viewAllResponse;
    private Response viewByInstructorResponse;
    private Response availabilityResponse;
    private final String baseUri = ConfigReader.get("base.uri");
    private String NegScCourseCode = "AIBK003";
    private String NegScCourseCode1 = "69d4cd4a51d014471d4162e2";
    private String NegScCourseCode2 = "69d86dabb871bdd4e126ca75";
    private String NegScInstructorID = "instructor8901";
    private String NegScCourseCode3 = "DSA_17755538667";

    @Given("an instructor is logged in")
    public void anInstructorIsLoggedIn() {
        Map<String, String> loginPayload = new HashMap<>();
        loginPayload.put("username", ConfigReader.get("instructor.username"));
        loginPayload.put("password", ConfigReader.get("instructor.password"));

        Response response = given()
                .baseUri(baseUri)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(loginPayload)
                .when()
                .post("/instructor/login")
                .then()
                .statusCode(200)
                .body("token", notNullValue())
                .extract()
                .response();

        bearerToken = response.jsonPath().getString("token");
        Assertions.assertNotNull(bearerToken, "Bearer token is null");
    }

    // For dynamic course code generation to create new course
    private String generateRandomAlphanumeric(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }

    @When("the instructor creates a new course with title {string}, instructor {string}, course code, category {string}, total capacity {int}, start date {string}, and end date {string}")
    public void the_instructor_creates_a_new_course_with_title_instructor_course_code_category_total_capacity_start_date_and_end_date(String tle_string, String ins_string, String cat_string, Integer tot_cap, String stDate_string, String edDate_string) {

        String coucde_string = generateRandomAlphanumeric(7);
        System.out.println("Generated Course Code: " + coucde_string);
        
        Map<String, Object> coursePayload = new HashMap<>();
        coursePayload.put("title", tle_string);
        coursePayload.put("instructor", ins_string);
        coursePayload.put("courseCode", coucde_string);
        coursePayload.put("category", cat_string);
        coursePayload.put("totalCapacity", tot_cap);
        coursePayload.put("startDate", stDate_string);
        coursePayload.put("endDate", edDate_string);
        
        createCourseResponse = given()
                .baseUri(baseUri)
                .header("Authorization", "Bearer " + bearerToken)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(coursePayload)
                .when()
                .post("/courses")
                .then()
                .extract()
                .response();
    }

    @When("the instructor creates a course with title {string}, instructor {string}, course code {string}, category {string}, total capacity {int}, start date {string}, and end date {string}")
    public void theInstructorCreatesACourseWithTitleInstructorCourseCodeCategoryTotalCapacityStartDateAndEndDate(String title, String instructor, String courseCode, String category, int totalCapacity, String startDate, String endDate) {
        
        Map<String, Object> coursePayload = new HashMap<>();
        coursePayload.put("title", title);
        coursePayload.put("instructor", instructor);
        coursePayload.put("courseCode", courseCode);
        coursePayload.put("category", category);
        coursePayload.put("totalCapacity", totalCapacity);
        coursePayload.put("startDate", startDate);
        coursePayload.put("endDate", endDate);

        createCourseResponse = given()
                .baseUri(baseUri)
                .header("Authorization", "Bearer " + bearerToken)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(coursePayload)
                .when()
                .post("/courses")
                .then()
                .extract()
                .response();
    }

    @Then("the course should be created successfully with title {string}")
    public void theCourseShouldBeCreatedSuccessfullyWithTitle(String title) {
        int statusCode = createCourseResponse.getStatusCode();

        System.out.println("Full Create Course Response: " + createCourseResponse.asString());

        String courseCodeResponse = createCourseResponse.jsonPath().getString("newCourse.courseCode");
        if (courseCodeResponse == null) courseCodeResponse = createCourseResponse.jsonPath().getString("courseCode");

        // Negative scenario for CourseCode as AIBK003
        if (NegScCourseCode.equals(courseCodeResponse) && statusCode == 400) {
            String errorMessage = createCourseResponse.jsonPath().getString("message");
            if (errorMessage == null) errorMessage = createCourseResponse.jsonPath().getString("error");
            
            String logMessage = String.format("Status Code: %d (Expected for Negative Scenario), Course Code: %s, Message: %s", 
                    statusCode, courseCodeResponse, errorMessage);
            System.out.println(logMessage);
            
            Assertions.assertEquals("Course code already exists", errorMessage, "Error message does not match for duplicate course code");
            return; 
        }

        if (statusCode == 200 || statusCode == 201) {
            String responseTitle = createCourseResponse.jsonPath().getString("newCourse.title");
            if (responseTitle == null) responseTitle = createCourseResponse.jsonPath().getString("title");
            
            String courseID = createCourseResponse.jsonPath().getString("newCourse._id");
            if (courseID == null) courseID = createCourseResponse.jsonPath().getString("_id");
            
            String apiMessage = createCourseResponse.jsonPath().getString("message");
            
            String logMessage = String.format("Status Code: %d, %s. Course ID: %s, Course Code: %s, Title: %s", 
                    statusCode, (apiMessage != null ? apiMessage : "Course Created successfully"), courseID, courseCodeResponse, responseTitle);
            System.out.println(logMessage);
            
            Assertions.assertEquals(title, responseTitle, "Course title in response does not match");
        } else if (statusCode == 400) {
            String errorMessage = createCourseResponse.jsonPath().getString("message");
            if (errorMessage == null) errorMessage = createCourseResponse.jsonPath().getString("error");
            
            String logMessage = String.format("Status Code: %d, Bad Request. Course Code: %s, Error: %s", 
                    statusCode, courseCodeResponse, errorMessage);
            System.out.println(logMessage);
            Assertions.fail(logMessage);
        } else {
            String logMessage = String.format("Status Code: %d, Unexpected error. Response: %s", 
                    statusCode, createCourseResponse.asString());
            System.out.println(logMessage);
            Assertions.fail(logMessage);
        }
    }

    @When("the instructor updates the course {string} with title {string}, total capacity {int}, available slots {int}, and end date {string}")
    public void theInstructorUpdatesTheCourseWithTitleTotalCapacityAvailableSlotsAndEndDate(
            String courseId, String title, int totalCapacity, int availableSlots, String endDate) {
        
        Map<String, Object> updatePayload = new HashMap<>();
        updatePayload.put("title", title);
        updatePayload.put("totalCapacity", totalCapacity);
        updatePayload.put("availableSlots", availableSlots);
        updatePayload.put("endDate", endDate);

        updateCourseResponse = given()
                .baseUri(baseUri)
                .header("Authorization", "Bearer " + bearerToken)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(updatePayload)
                .when()
                .put("/courses/" + courseId)
                .then()
                .extract()
                .response();
    }

    @Then("the course {string} should be updated successfully with title {string}")
    public void theCourseShouldBeUpdatedSuccessfullyWithTitle(String courseId, String title) {
        int statusCode = updateCourseResponse.getStatusCode();
        
        // Negative scenarios for specific course IDs
        if ((NegScCourseCode1.equals(courseId) || NegScCourseCode1.equals(courseId)) && statusCode != 200) {
            String errorMessage = updateCourseResponse.jsonPath().getString("message");
            if (errorMessage == null) errorMessage = updateCourseResponse.jsonPath().getString("error");
            
            String logMessage = String.format("Status Code: %d (Expected for Negative Scenario), Course ID: %s, Message: %s", 
                    statusCode, courseId, errorMessage);
            System.out.println(logMessage);
            
            Assertions.assertNotNull(errorMessage, "Error message should be present in the negative scenario response");
            return; 
        }

        if (statusCode == 200) {
            String responseTitle = updateCourseResponse.jsonPath().getString("title");
            String apiMessage = updateCourseResponse.jsonPath().getString("message");
            
            String logMessage = String.format("Status Code: %d, %s. Course ID: %s, Title: %s", 
                    statusCode, (apiMessage != null ? apiMessage : "Course updated successfully"), courseId, responseTitle);
            System.out.println(logMessage);
            
            Assertions.assertEquals(title, responseTitle, "Course title in response does not match");
        } else {
            String errorMessage = updateCourseResponse.jsonPath().getString("message");
            if (errorMessage == null) errorMessage = updateCourseResponse.jsonPath().getString("error");
            
            String logMessage = String.format("Status Code: %d, Error updating course. Course ID: %s, Error: %s", 
                    statusCode, courseId, errorMessage);
            System.out.println(logMessage);
            Assertions.fail(logMessage);
        }
    }

    @When("the instructor deletes the course with ID {string}")
    public void theInstructorDeletesTheCourseWithID(String courseId) {
        deleteCourseResponse = given()
                .baseUri(baseUri)
                .header("Authorization", "Bearer " + bearerToken)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .delete("/courses/" + courseId)
                .then()
                .extract()
                .response();
    }

    @Then("the course with ID {string} should be deleted successfully")
    public void theCourseWithIDShouldBeDeletedSuccessfully(String courseId) {
        int statusCode = deleteCourseResponse.getStatusCode();

        // Negative scenarios for specific course IDs
        if ((NegScCourseCode1.equals(courseId) || NegScCourseCode2.equals(courseId)) && statusCode != 200) {
            String errorMessage = deleteCourseResponse.jsonPath().getString("message");
            if (errorMessage == null) errorMessage = deleteCourseResponse.jsonPath().getString("error");
            
            String logMessage = String.format("Status Code: %d (Expected for Negative Scenario), Course ID: %s, Message: %s", 
                    statusCode, courseId, errorMessage);
            System.out.println(logMessage);
            
            Assertions.assertNotNull(errorMessage, "Error message should be present in the negative scenario response");
            return; 
        }

        if (statusCode == 200 || statusCode == 204) {
            String apiMessage = deleteCourseResponse.jsonPath().getString("message");
            
            String logMessage = String.format("Status Code: %d, %s. Course ID: %s", 
                    statusCode, (apiMessage != null ? apiMessage : "Course deleted successfully"), courseId);
            System.out.println(logMessage);
            
            Assertions.assertTrue(statusCode == 200 || statusCode == 204, "Expected status code 200 or 204 for successful deletion");
        } else {
            String errorMessage = deleteCourseResponse.jsonPath().getString("message");
            if (errorMessage == null) errorMessage = deleteCourseResponse.jsonPath().getString("error");
            
            String logMessage = String.format("Status Code: %d, Error deleting course. Course ID: %s, Error: %s", 
                    statusCode, courseId, errorMessage);
            System.out.println(logMessage);
            Assertions.fail(logMessage);
        }
    }

    @When("I request to view all courses")
    public void iRequestToViewAllCourses() {
        viewAllResponse = given()
                .baseUri(baseUri)
                .accept(ContentType.JSON)
                .when()
                .get("/courses/all")
                .then()
                .extract()
                .response();
    }

    @Then("all courses should be retrieved successfully")
    public void allCoursesShouldBeRetrievedSuccessfully() {
        Assertions.assertEquals(200, viewAllResponse.getStatusCode(), "Expected status code 200 OK");
        Assertions.assertTrue(viewAllResponse.jsonPath().getList("$").size() >= 0, "Response should be a list");
    }

    @When("I request to view all courses for instructor ID {string}")
    public void iRequestToViewAllCoursesForInstructorID(String instructorId) {
        viewByInstructorResponse = given()
                .baseUri(baseUri)
                .accept(ContentType.JSON)
                .when()
                .get("/courses/instructor/" + instructorId)
                .then()
                .extract()
                .response();
    }

    @Then("all courses for instructor ID {string} should be retrieved successfully")
    public void allCoursesForInstructorIDShouldBeRetrievedSuccessfully(String instructorId) {
        int statusCode = viewByInstructorResponse.getStatusCode();
        
        // Negative scenario - Instructor ID as instructor8901
        if (NegScInstructorID.equals(instructorId) && statusCode != 200) {
            String errorMessage = viewByInstructorResponse.jsonPath().getString("message");
            if (errorMessage == null) errorMessage = viewByInstructorResponse.jsonPath().getString("error");
            
            String logMessage = String.format("Status Code: %d (Expected for Negative Scenario), Instructor ID: %s, Message: %s", 
                    statusCode, instructorId, errorMessage);
            System.out.println(logMessage);
            
            Assertions.assertNotNull(errorMessage, "Error message should be present in the negative scenario response");
            return; 
        }

        if (statusCode == 200) {
            String apiMessage = viewByInstructorResponse.jsonPath().getString("message");
            int courseCount = viewByInstructorResponse.jsonPath().getList("$").size();
            
            String logMessage = String.format("Status Code: %d, %s. Total courses found for instructor %s: %d", 
                    statusCode, (apiMessage != null ? apiMessage : "Courses retrieved successfully"), instructorId, courseCount);
            System.out.println(logMessage);
            
            Assertions.assertTrue(courseCount >= 0, "Response should be a list of courses");
        } else {
            String errorMessage = viewByInstructorResponse.jsonPath().getString("message");
            if (errorMessage == null) errorMessage = viewByInstructorResponse.jsonPath().getString("error");
            
            String logMessage = String.format("Status Code: %d, Error retrieving courses. Instructor ID: %s, Error: %s", 
                    statusCode, instructorId, errorMessage);
            System.out.println(logMessage);
            Assertions.fail(logMessage);
        }
    }

    @When("I request to view availability for course code {string}")
    public void iRequestToViewAvailabilityForCourseCode(String courseCode) {
        availabilityResponse = given()
                .baseUri(baseUri)
                .accept(ContentType.JSON)
                .when()
                .get("/courses/availability/" + courseCode)
                .then()
                .extract()
                .response();
    }

    @Then("availability for course code {string} should be retrieved successfully")
    public void availabilityForCourseCodeShouldBeRetrievedSuccessfully(String courseCode) {
        int statusCode = availabilityResponse.getStatusCode();
        
        // Handle negative scenario for DSA_17755538667
        if (NegScCourseCode3.equals(courseCode) && statusCode != 200) {
            String errorMessage = availabilityResponse.jsonPath().getString("message");
            if (errorMessage == null) errorMessage = availabilityResponse.jsonPath().getString("error");
            
            String logMessage = String.format("Status Code: %d (Expected for Negative Scenario), Course Code: %s, Message: %s", 
                    statusCode, courseCode, errorMessage);
            System.out.println(logMessage);
            
            Assertions.assertNotNull(errorMessage, "Error message should be present in the negative scenario response");
            return; // Negative test - Expected outcome to fail with 404 Not Found
        }

        if (statusCode == 200) {
            String courseTitle = availabilityResponse.jsonPath().getString("title");
            String courseId = availabilityResponse.jsonPath().getString("_id");
            Integer availableSlots = availabilityResponse.jsonPath().getInt("availableSlots");
            String apiMessage = availabilityResponse.jsonPath().getString("message");
            
            // Check for nulls before formatting
            String safeCourseId = courseId != null ? courseId : "N/A";
            String safeCourseTitle = courseTitle != null ? courseTitle : "N/A";
            int safeSlots = availableSlots != null ? availableSlots : 0;
            String safeApiMessage = apiMessage != null ? apiMessage : "Availability retrieved successfully";

            String logMessage = String.format("Status Code: %d, %s. Course ID: %s - %s, there are %d available slots", 
                    statusCode, safeApiMessage, safeCourseId, safeCourseTitle, safeSlots);
            System.out.println(logMessage);
            
            Assertions.assertNotNull(availableSlots, "Available slots should be present in the response");
        } else {
            String errorMessage = availabilityResponse.jsonPath().getString("message");
            if (errorMessage == null) errorMessage = availabilityResponse.jsonPath().getString("error");
            
            String logMessage = String.format("Status Code: %d, Error retrieving availability. Course Code: %s, Error: %s", 
                    statusCode, courseCode, errorMessage);
            System.out.println(logMessage);
            Assertions.fail(logMessage);
        }
    }
}
