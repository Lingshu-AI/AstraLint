package org.linshuai.astralint;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Basic integration test for the AstraLint application.
 * This test ensures that the Spring Boot application context loads
 * successfully.
 */
@SpringBootTest
@ActiveProfiles("test")
class CodeVoyantApplicationTests {

  /**
   * Test that the application context loads without errors.
   * This is a smoke test to verify basic application startup.
   */
  @Test
  void contextLoads() {
    // This test will pass if the Spring application context loads successfully
    // If there are any configuration issues, this test will fail
  }

}
