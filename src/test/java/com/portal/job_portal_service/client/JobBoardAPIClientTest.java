package com.portal.job_portal_service.client;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.portal.job_portal_service.client.dto.JobPostingRequestDTO;
import com.portal.job_portal_service.client.dto.JobPostingResponseDTO;
import com.portal.job_portal_service.util.MockDataUtil;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

@SuppressWarnings("null")
public class JobBoardAPIClientTest {

  public static MockWebServer mockWebServer;

  private JobBoardAPIClient jobBoardAPIClient;

    @After
    public void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Before
    public void initialize() throws IOException {
      mockWebServer = new MockWebServer();
      mockWebServer.start();

      String baseUrl = String.format("http://localhost:%s", mockWebServer.getPort());
      WebClient webClient = WebClient.builder().baseUrl(baseUrl).build();
      jobBoardAPIClient = new JobBoardAPIClient(webClient);
    }

    @Test
    public void testGetPostings() throws IOException {
      List<JobPostingResponseDTO> mockJobPostings = MockDataUtil.getJobPostings();

      mockWebServer.enqueue(new MockResponse()
        .setBody(new ObjectMapper().writeValueAsString(mockJobPostings))
        .setResponseCode(200)
        .addHeader("Content-Type", "application/json"));

      List<JobPostingResponseDTO> result = jobBoardAPIClient.getPostings();

      assertTrue(result.size() == 1);
    }

    @Test
    public void testBetPostings_404_NotFound() throws IOException {

      mockWebServer.enqueue(new MockResponse()
        .setBody(new ObjectMapper().writeValueAsString(new ArrayList<>()))
        .setResponseCode(404)
        .addHeader("Content-Type", "application/json"));

      List<JobPostingResponseDTO> result = jobBoardAPIClient.getPostings();

      assertTrue(result.size() == 0);
    }

    @Test
    public void testAddJobPostings() throws IOException {
      List<JobPostingRequestDTO> mockJobPostings = MockDataUtil.addJobPostings();

      mockWebServer.enqueue(new MockResponse()
        .setBody(new ObjectMapper().writeValueAsString(mockJobPostings))
        .setResponseCode(200)
        .addHeader("Content-Type", "application/json"));

      List<JobPostingRequestDTO> result = jobBoardAPIClient.addPostings();

      assertTrue(result.size() == 1);
    }
}
