package com.portal.job_portal_service.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ResponseStatusException;

import com.portal.job_portal_service.client.JobBoardAPIClient;
import com.portal.job_portal_service.client.dto.JobPostingRequestDTO;
import com.portal.job_portal_service.client.dto.JobPostingResponseDTO;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@AllArgsConstructor
public class JobPortalService {

  private final JobBoardAPIClient jobBoardAPIClient;

  public List<JobPostingResponseDTO> getJobPostings() {
    try {
      return jobBoardAPIClient.getPostings();
    } catch (WebClientResponseException e) {
      if (e.getStatusCode().is4xxClientError()) {
        log.error("Unable to return job posting data", e);
        throw new ResponseStatusException(e.getStatusCode());
      } else {
        log.error("An error occur within the server", e);
        throw new ResponseStatusException(e.getStatusCode());
      }
    }
  }

  public List<JobPostingRequestDTO> addJobPostings() {
    try {
      return jobBoardAPIClient.addPostings();
    } catch (WebClientResponseException e) {
      if (e.getStatusCode().is4xxClientError()) {
        log.error("Unable to fetch and add job posting data", e);
        throw new ResponseStatusException(e.getStatusCode());
      } else {
        log.error("An error occur within the server", e);
        throw new ResponseStatusException(e.getStatusCode());
      }
    }
  }
}
