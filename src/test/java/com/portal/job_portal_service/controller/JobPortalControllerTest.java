package com.portal.job_portal_service.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import com.portal.job_portal_service.service.JwtService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ResponseStatusException;

import com.portal.job_portal_service.client.dto.JobPostingRequestDTO;
import com.portal.job_portal_service.client.dto.JobPostingResponse;
import com.portal.job_portal_service.client.dto.JobPostingResponseDTO;
import com.portal.job_portal_service.service.JobPortalService;
import com.portal.job_portal_service.service.UserService;
import com.portal.job_portal_service.util.MockDataUtil;

@SuppressWarnings("null")
@WebMvcTest(JobPortalController.class)
@AutoConfigureMockMvc(addFilters = false)
@RunWith(SpringRunner.class)
public class JobPortalControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    JwtService jwtService;

    @MockitoBean
    JobPortalController jobPortalController;

    @MockitoBean
    UserService userService;

    @MockitoBean
    JobPortalService jobPostingService;

    @MockitoBean
    PasswordEncoder passwordEncoder;

    @Test
    public void testGetPostings() throws Exception {

        // Arrange
        JobPostingResponse response = new JobPostingResponse();
        List<JobPostingResponseDTO> jobPostings = MockDataUtil.getJobPostings();

        when(jobPostingService.getJobPostings()).thenReturn(jobPostings);
        when(jwtService.generateToken(anyString())).thenReturn("token");
        response.setPostings(jobPostings);

        // Act and Assert
        mockMvc.perform(get("/service/portal/jobPostings")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    public void testGetPostings_400Error_unauthorized() throws Exception {

        // Arrange
        WebClientResponseException exception = WebClientResponseException.create(
                HttpStatus.UNAUTHORIZED.value(), "401 UNAUTHORIZED", null, null, null);
        when(jobPostingService.getJobPostings()).thenThrow(exception);
        when(jobPortalController.getPostings()).thenThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "401 Unauthorized"));

        // Act and Assert
        mockMvc.perform(get("/service/portal/jobPostings"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string(""));

    }

    @Test
    public void testGetPostings_500Error_serverError() throws Exception {

        // Arrange
        WebClientResponseException exception = WebClientResponseException.create(
                HttpStatus.INTERNAL_SERVER_ERROR.value(), "500 Internal Server Error", null, null, null);
        when(jobPostingService.getJobPostings()).thenThrow(exception);
        when(jobPortalController.getPostings()).thenThrow(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "500 Internal Server Error"));

        // Act and Assert
        mockMvc.perform(get("/service/portal/jobPostings"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(""));

    }

    @Test
    public void testAddJobPostings() throws Exception {

        // Arrange
        List<JobPostingRequestDTO> addPostings = MockDataUtil.addJobPostings();

        when(jobPostingService.addJobPostings()).thenReturn(addPostings);
        when(jwtService.generateToken(anyString())).thenReturn("token");

        // Act and Assert
        mockMvc.perform(post("/service/portal/addJobPostings")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    public void testAddPostings_400Error_unauthorized() throws Exception {

        // Arrange
        WebClientResponseException exception = WebClientResponseException.create(
                HttpStatus.UNAUTHORIZED.value(), "401 UNAUTHORIZED", null, null, null);
        when(jobPostingService.addJobPostings()).thenThrow(exception);
        when(jobPortalController.addJobPostings()).thenThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "401 Unauthorized"));

        // Act and Assert
        mockMvc.perform(post("/service/portal/addJobPostings"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string(""));

    }

    @Test
    public void testAddPostings_500Error_serverError() throws Exception {

        // Arrange
        WebClientResponseException exception = WebClientResponseException.create(
                HttpStatus.INTERNAL_SERVER_ERROR.value(), "500 Internal Server Error", null, null, null);
        when(jobPostingService.addJobPostings()).thenThrow(exception);
        when(jobPortalController.addJobPostings()).thenThrow(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "500 Internal Server Error"));

        // Act and Assert
        mockMvc.perform(post("/service/portal/addJobPostings"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(""));

    }

    @Test
    public void testHandleFileUpload() throws Exception {

        // Arrange
        MockMultipartFile mockFile = new MockMultipartFile(
                "document",
                "SoftwareEngineer.pdf",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                "Software Engineer Resume".getBytes()
        );

        // Act and Assert
        mockMvc.perform(multipart("/service/portal/uploadDocument").file(mockFile))
                .andExpect(status().isOk());
    }

    @Test
    public void testHandleFileUpload_400Error_incorrect_param() throws Exception {

        // Arrange
        MockMultipartFile mockFile = new MockMultipartFile(
                "file",
                "SoftwareEngineer.pdf",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                "Software Engineer Resume".getBytes()
        );

        // Act and Assert
        mockMvc.perform(multipart("/service/portal/uploadDocument").file(mockFile))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testHandleFileUpload_500Error_serverError() throws Exception {

        // Arrange
        MockMultipartFile mockFile = new MockMultipartFile(
                "document",
                "SoftwareEngineer.pdf",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                "Software Engineer Resume".getBytes()
        );
        when(jobPortalController.handleFileUpload(any())).thenThrow(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "500 Internal Server Error"));

        // Act and Assert
        mockMvc.perform(multipart("/service/portal/uploadDocument").file(mockFile))
                .andExpect(status().is5xxServerError());
    }
}
