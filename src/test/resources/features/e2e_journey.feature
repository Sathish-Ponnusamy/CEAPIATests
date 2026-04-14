Feature: End-to-End Course Enrollment Journey

  Scenario Outline: Complete journey from application check to student enrollment and drop
    When I send GET request to status endpoint
    Then the response status code should be 200
    Given an instructor is logged in
    When the instructor creates a new course with title "<title>", instructor "<instructor>", course code, category "<category>", total capacity <totalCapacity>, start date "<startDate>", and end date "<endDate>"
    Then the course should be created successfully with title "<title>"
    And I request to view all courses
    Then all courses should be retrieved successfully
    And I request to view all courses for instructor ID "<instructorId>"
    Then all courses for instructor ID "<instructorId>" should be retrieved successfully
    Given a student is logged in
    When I request to view all courses
    Then all courses should be retrieved successfully
    And I request to view availability for course code "<courseCode>"
    Then availability for course code "<enrolcourseCode>" should be retrieved successfully
    And I request to view all courses for instructor ID "<instructorId>"
    Then all courses for instructor ID "<instructorId>" should be retrieved successfully
    When the student enrolls in the course with username "<studentUsername>" and course code "<enrolcourseCode>"
    Then the enrollment should be successful for username "<studentUsername>" and course code "<enrolcourseCode>"
    And I request to verify active enrollment for username "<studentUsername>"
    Then active enrollment for username "<studentUsername>" should be verified successfully
    When the student drops the course with username "<studentUsername>" and course code "<enrolcourseCode>"
    Then the drop should be successful for username "<studentUsername>" and course code "<enrolcourseCode>"
    And I request to view enrollment history for username "<studentUsername>"
    Then enrollment history for username "<studentUsername>" should be retrieved successfully
    And I verify enrollment history for "<studentUsername>" contains course "<enrolcourseCode>" with status "dropped" and enrollment date as today

    Examples:
      | title                      | instructorId   | courseCode | category    | totalCapacity | startDate  | endDate    | studentUsername | enrolcourseCode |
      | Masterclass in MartialArts | instructor0092 | API001    | Programming | 50            | 2026-04-01 | 2026-07-01 | student01       | API001         |
