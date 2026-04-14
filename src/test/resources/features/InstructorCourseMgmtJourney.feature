Feature: Instructor Course Management

  Scenario: View all courses - Positive Scenario
    When I request to view all courses
    Then all courses should be retrieved successfully

  Scenario Outline: Get all courses by instructor ID - Positive Scenario
    When I request to view all courses for instructor ID "<instructorId>"
    Then all courses for instructor ID "<instructorId>" should be retrieved successfully

    Examples:
      | instructorId   |
      | instructor0071 |

  Scenario Outline: Get courses by instructor ID instructor8901 which never exist- Negative scenario
    When I request to view all courses for instructor ID "<instructorId>"
    Then all courses for instructor ID "<instructorId>" should be retrieved successfully

    Examples:
      | instructorId   |
      | instructor8901 |

  Scenario Outline: Get course availability - Positive Scenario
    When I request to view availability for course code "<courseCode>"
    Then availability for course code "<courseCode>" should be retrieved successfully

    Examples:
      | courseCode     |
      | API091 |

  Scenario Outline: Get course availability for the course code which never exists - Negative Scenario
    When I request to view availability for course code "<courseCode>"
    Then availability for course code "<courseCode>" should be retrieved successfully

    Examples:
      | courseCode      |
      | DSA_17755538667 |

  Scenario Outline: Instructor creates new courses - Positive Scenario
    Given an instructor is logged in
    When the instructor creates a new course with title "<title>", instructor "<instructor>", course code, category "<category>", total capacity <totalCapacity>, start date "<startDate>", and end date "<endDate>"
    Then the course should be created successfully with title "<title>"

    Examples:
      | title                | instructor     |  | category    | totalCapacity | startDate  | endDate    |
      | Introduction to AICK | instructor7774 |  | Programming | 30            | 2026-03-01 | 2026-06-01 |

  Scenario Outline: Instructor creates existing courses - Negative Scenario
    Given an instructor is logged in
    When the instructor creates a course with title "<title>", instructor "<instructor>", course code "<courseCode>", category "<category>", total capacity <totalCapacity>, start date "<startDate>", and end date "<endDate>"
    Then the course should be created successfully with title "<title>"

    Examples:
      | title                | instructor     | courseCode | category    | totalCapacity | startDate  | endDate    |
      | Introduction to AIBK | instructor7774 | AIBK003    | Programming | 30            | 2026-03-01 | 2026-06-01 |

  Scenario Outline: Instructor updates courses details - Positive Scenario
    Given an instructor is logged in
    When the instructor updates the course "<courseId>" with title "<title>", total capacity <totalCapacity>, available slots <availableSlots>, and end date "<endDate>"
    Then the course "<courseId>" should be updated successfully with title "<title>"

    Examples:
      | courseId                 | title               | totalCapacity | availableSlots | endDate    |
      | 69d57021d72437acd10e8925 | APIAutomatedTesting | 0             | 0              | 2026-04-07 |

  Scenario Outline: Instructor tries to update courseID that doesn't belong to him - Negative scenario
    Given an instructor is logged in
    When the instructor updates the course "<courseId>" with title "<title>", total capacity <totalCapacity>, available slots <availableSlots>, and end date "<endDate>"
    Then the course "<courseId>" should be updated successfully with title "<title>"

    Examples:
      | courseId                 | title               | totalCapacity | availableSlots | endDate    |
      | 69d4cd4a51d014471d4162e2 | APIAutomatedTesting | 0             | 0              | 2026-04-07 |

  Scenario Outline: Instructor tries to update courseID that doesn't exists - Negative scenario
    Given an instructor is logged in
    When the instructor updates the course "<courseId>" with title "<title>", total capacity <totalCapacity>, available slots <availableSlots>, and end date "<endDate>"
    Then the course "<courseId>" should be updated successfully with title "<title>"

    Examples:
      | courseId                 | title               | totalCapacity | availableSlots | endDate    |
      | 69d4cd4a51d014471d4162e2 | APIAutomatedTesting | 0             | 0              | 2026-04-07 |

  Scenario Outline: Instructor deletes courses - Positive Scenario
    Given an instructor is logged in
    When the instructor deletes the course with ID "<courseId>"
    Then the course with ID "<courseId>" should be deleted successfully

    Examples:
      | courseId                 |
      | 69d86dabb871bdd4e126ca76 |

  Scenario Outline: Instructor deletes courses that doesn't belong to the instructor - Negative scenario
    Given an instructor is logged in
    When the instructor deletes the course with ID "<courseId>"
    Then the course with ID "<courseId>" should be deleted successfully

    Examples:
      | courseId                 |
      | 69d4cd4a51d014471d4162e2 |

  Scenario Outline: Instructor deletes courses that doesn't exist - Negative scenario
    Given an instructor is logged in
    When the instructor deletes the course with ID "<courseId>"
    Then the course with ID "<courseId>" should be deleted successfully

    Examples:
      | courseId                 |
      | 69d86dabb871bdd4e126ca75 |