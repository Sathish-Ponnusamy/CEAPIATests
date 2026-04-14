Feature: Student Management

  Scenario Outline: Student enrolls in a course - Positive Scenario
    Given a student is logged in
    When the student enrolls in the course with username "<username>" and course code "<courseCode>"
    Then the enrollment should be successful for username "<username>" and course code "<courseCode>"

    Examples:
      | username  | courseCode |
      | student01 | API001    |

  Scenario Outline: Verify active student enrollment - Positive Scenario
    Given a student is logged in
    When I request to verify active enrollment for username "<username>"
    Then active enrollment for username "<username>" should be verified successfully

    Examples:
      | username  |
      | student01 |

  Scenario Outline: Get student enrollment history - Positive Scenario
    Given a student is logged in
    When I request to view enrollment history for username "<username>"
    Then enrollment history for username "<username>" should be retrieved successfully

    Examples:
      | username  |
      | student01 |

  Scenario Outline: Student drops a course - Positive Scenario
    Given a student is logged in
    When the student drops the course with username "<username>" and course code "<courseCode>"
    Then the drop should be successful for username "<username>" and course code "<courseCode>"

    Examples:
      | username  | courseCode |
      | student01 | API001    |

  Scenario Outline: Student attempts to update a course - Negative Scenario
    Given a student is logged in
    When the student attempts to update the course "<courseId>" with title "Hacker Course"
    Then the course update should be denied

    Examples:
      | courseId                 |
      | 69da962cad5ceb1958e39f64 |

  Scenario Outline: Student attempts to delete a course - Negative Scenario
    Given a student is logged in
    When the student attempts to delete the course with ID "<courseId>"
    Then the course deletion should be denied

    Examples:
      | courseId                 |
      | 69da962cad5ceb1958e39f64 |


  Scenario Outline: Student enrolls to a course where he is already enrolled in past and didn't drop the course - Negative Scenario
    Given a student is logged in
    When the student enrolls in the course with username "<username>" and course code "<courseCode>"
    Then the enrollment should be successful for username "<username>" and course code "<courseCode>"

    Examples:
      | username  | courseCode |
      | student01 | API001     |

  Scenario Outline: Student enrolls in a course where there is no such username - Negative Scenarios
    Given a student is logged in
    When the student enrolls in the course with username "<username>" and course code "<courseCode>"
    Then the enrollment should be successful for username "<username>" and course code "<courseCode>"

    Examples:
      | username | courseCode |
      | ABCD     | JAVA101    |

  Scenario Outline: Student enrolls in a course where there is no such CourseCode - Negative Scenarios
    Given a student is logged in
    When the student enrolls in the course with username "<username>" and course code "<courseCode>"
    Then the enrollment should be successful for username "<username>" and course code "<courseCode>"

    Examples:
      | username  | courseCode |
      | student01 | XYZA       |

  Scenario Outline: Student enrolls in a course where there is no such username & CourseCode - Negative Scenarios
    Given a student is logged in
    When the student enrolls in the course with username "<username>" and course code "<courseCode>"
    Then the enrollment should be successful for username "<username>" and course code "<courseCode>"

    Examples:
      | username | courseCode |
      | ABCD     | XYZA       |


  Scenario Outline: Get student enrollment history for the username who doesn't exist - Negative scenarios
    Given a student is logged in
    When I request to view enrollment history for username "<username>"
    Then enrollment history for username "<username>" should be retrieved successfully

    Examples:
      | username |
      | ABCD     |

  Scenario Outline: Verify whether student enrollment is active when there is no such student username exist - Negative scenario
    Given a student is logged in
    When I request to verify active enrollment for username "<username>"
    Then active enrollment for username "<username>" should be verified successfully

    Examples:
      | username |
      | ABCD     |

  Scenario Outline: Student drops a course when already dropped a course - Negative scenario
    Given a student is logged in
    When the student drops the course with username "<username>" and course code "<courseCode>"
    Then the drop should be successful for username "<username>" and course code "<courseCode>"

    Examples:
      | username  | courseCode |
      | student01 | API001     |

  Scenario Outline: Student drops a course when there is no such username - Negative scenario
    Given a student is logged in
    When the student drops the course with username "<username>" and course code "<courseCode>"
    Then the drop should be successful for username "<username>" and course code "<courseCode>"

    Examples:
      | username | courseCode |
      | ABCD     | JAVA101    |

  Scenario Outline: Student drops a course when there is no such coursecode - Negative scenario
    Given a student is logged in
    When the student drops the course with username "<username>" and course code "<courseCode>"
    Then the drop should be successful for username "<username>" and course code "<courseCode>"

    Examples:
      | username  | courseCode |
      | student01 | XYZA       |


  Scenario Outline: Student drops a course when there is no such username and coursecode  - Negative scenario
    Given a student is logged in
    When the student drops the course with username "<username>" and course code "<courseCode>"
    Then the drop should be successful for username "<username>" and course code "<courseCode>"

    Examples:
      | username | courseCode |
      | ABCD     | XYZA       |

