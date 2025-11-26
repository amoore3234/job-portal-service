package com.portal.job_portal_service.util;

import com.portal.job_portal_service.client.dto.JobPostingRequestDTO;
import com.portal.job_portal_service.client.dto.JobPostingResponseDTO;
import com.portal.job_portal_service.model.User;

import java.time.ZonedDateTime;
import java.util.List;

public final class MockDataUtil {

	/**
	 * Create test data for the User entity.
	 *
	 * @return The user object.
	 */
	public static User getUserData() {

		User user = new User();
		user.setFirstName("testFirstName");
		user.setLastName("testLastName");
		user.setUserEmail("test@dev.com");
		user.setUsername("testUsername");
		user.setUserPassword("myPassword");
		user.setCreatedTimestamp(ZonedDateTime.now());
		user.setUpdatedTimestamp(ZonedDateTime.now());

		return user;
	}

	/**
	 * A response for generating a list of job postings.
	 *
	 * @return An array of job listings.
	 */
	public static List<JobPostingResponseDTO> getJobPostings() {
		JobPostingResponseDTO jobPostingResponse = JobPostingResponseDTO.builder()
				.id(1L)
				.jobTitle("Frontend Software Engineer")
				.jobUrl("https://example.com/jobs/1")
				.companayLogo("https://example.com/logo.png")
				.companyAddress("13423 Newport Blvd, Newport Beach, USA")
				.companySalary("$100,000")
				.companyMetadata(List.of("Java", "JavaScript","React"))
				.datePosted("2025-08-15")
				.build();

		return List.of(jobPostingResponse);
	}

	/**
	 * A request for saving a list of job postings.
	 *
	 * @return An array of job listings.
	 */
	public static List<JobPostingRequestDTO> addJobPostings() {
		JobPostingRequestDTO jobPostingRequest = JobPostingRequestDTO.builder()
				.jobTitle("Frontend Software Engineer")
				.jobUrl("https://example.com/jobs/1")
				.companayLogo("https://example.com/logo.png")
				.companyAddress("13423 Newport Blvd, Newport Beach, USA")
				.companySalary("$100,000")
				.companyMetadata(List.of("Java", "JavaScript","React"))
				.datePosted("2025-08-15")
				.build();

		return List.of(jobPostingRequest);
	}
}
