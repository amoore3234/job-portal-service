package com.portal.job_portal_service.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ResponseStatusException;

import com.portal.job_portal_service.client.JobBoardAPIClient;
import com.portal.job_portal_service.client.dto.JobPostingRequestDTO;
import com.portal.job_portal_service.client.dto.JobPostingResponseDTO;
import com.portal.job_portal_service.util.MockDataUtil;

@RunWith(SpringRunner.class)
@SuppressWarnings("null")
public class JobPortalServiceTest {

  @Mock
  private JobBoardAPIClient jobBoardAPIClient;

  @InjectMocks
  private JobPortalService jobPostingService;

  private List<JobPostingResponseDTO> jobPostings;

  private List<JobPostingRequestDTO> jobPostingRequest;

  @Before
  public void setUp() {
    jobPostings = MockDataUtil.getJobPostings();
    jobPostingRequest = MockDataUtil.addJobPostings();
  }

  @Test
  public void testGetJobPostings() {

    // Arrange
    when(jobBoardAPIClient.getPostings()).thenReturn(jobPostings);

    // Act
    List<JobPostingResponseDTO> actual = jobPostingService.getJobPostings();

    // Assert
    assertNotNull(actual);
    assertFalse(actual.isEmpty());
  }

  @Test
  public void testGetJobPostings_400Error_notFound() {

    // Arrange
    WebClientResponseException exception = WebClientResponseException.create(
      HttpStatus.NOT_FOUND.value(), "404 Not Found", null, null, null);
    when(jobBoardAPIClient.getPostings()).thenThrow(exception);

    // Act
    ResponseStatusException thrownException = assertThrows(ResponseStatusException.class, () -> {
        jobPostingService.getJobPostings();
    });

    // Assert
    assertEquals(HttpStatus.NOT_FOUND, thrownException.getStatusCode());
  }

  @Test
  public void testGetJobPostings_500Error_internalServerError() {

    // Arrange
    WebClientResponseException exception = WebClientResponseException.create(
      HttpStatus.INTERNAL_SERVER_ERROR.value(), "500 Internal Server Error", null, null, null);
    when(jobBoardAPIClient.getPostings()).thenThrow(exception);

    // Act
    ResponseStatusException thrownException = assertThrows(ResponseStatusException.class, () -> {
        jobPostingService.getJobPostings();
    });

    // Assert
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, thrownException.getStatusCode());
  }

  @Test
  public void testAddJobPostings() {

    // Arrange
    when(jobBoardAPIClient.addPostings()).thenReturn(jobPostingRequest);

    // Act
    List<JobPostingRequestDTO> actual = jobPostingService.addJobPostings();

    // Assert
    assertNotNull(actual);
    assertFalse(actual.isEmpty());
  }

  @Test
  public void testAddJobPostings_400Error_notFound() {

    // Arrange
    WebClientResponseException exception = WebClientResponseException.create(
      HttpStatus.NOT_FOUND.value(), "404 Not Found", null, null, null);
    when(jobBoardAPIClient.addPostings()).thenThrow(exception);

    // Act
    ResponseStatusException thrownException = assertThrows(ResponseStatusException.class, () -> {
        jobPostingService.addJobPostings();
    });

    // Assert
    assertEquals(HttpStatus.NOT_FOUND, thrownException.getStatusCode());
  }

  @Test
  public void testAddJobPostings_500Error_internalServerError() {

    // Arrange
    WebClientResponseException exception = WebClientResponseException.create(
      HttpStatus.INTERNAL_SERVER_ERROR.value(), "500 Internal Server Error", null, null, null);
    when(jobBoardAPIClient.addPostings()).thenThrow(exception);

    // Act
    ResponseStatusException thrownException = assertThrows(ResponseStatusException.class, () -> {
        jobPostingService.addJobPostings();
    });

    // Assert
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, thrownException.getStatusCode());
  }
}
