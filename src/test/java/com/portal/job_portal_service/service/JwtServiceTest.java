package com.portal.job_portal_service.service;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SuppressWarnings("null")
public class JwtServiceTest {

    @InjectMocks
    private JwtService jwtService;

    @Test
    public void testTokenGeneration() {
      // Arrange
      String username = "testUser";

      // Act
      String actual = jwtService.generateToken(username);

      // Assert
      assertNotNull(actual);
    }

    @Test
    public void testExtractUsername() {
      // Arrange
      String expected = "testUser";
      String token = jwtService.generateToken(expected);

      // Act
      String actual = jwtService.extractUsername(token);

      // Assert
      assertNotNull(actual);
      assertEquals(expected, actual);
    }

    @Test
    public void testTokenValidation_True() {
      // Arrange
      String username = "testUser";
      String token = jwtService.generateToken(username);

      // Act
      boolean actual = jwtService.isTokenValid(token);

      // Assert
      assertNotNull(actual);
      assertEquals(true, actual);
    }

    @Test
    public void testTokenValidation_False() {
      // Arrange
      String token = "badToken";

      // Act
      boolean actual = jwtService.isTokenValid(token);

      // Assert
      assertNotNull(actual);
      assertEquals(false, actual);
    }
}
