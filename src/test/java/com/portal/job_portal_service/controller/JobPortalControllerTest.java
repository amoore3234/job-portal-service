package com.portal.job_portal_service.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.portal.job_portal_service.model.User;
import com.portal.job_portal_service.service.UserService;
import com.portal.job_portal_service.util.MockDataUtil;

@WebMvcTest(JobPortalController.class)
@AutoConfigureMockMvc(addFilters = false)
@RunWith(SpringRunner.class)
public class JobPortalControllerTest {

  @Autowired
  MockMvc mockMvc;

  @MockitoBean
  UserService userService;

  @MockitoBean
  PasswordEncoder passwordEncoder;

  @Test
  public void testUserRegistration() throws Exception {

    // Arrange
    User user = MockDataUtil.getUserData();
    String rawPassword = user.getUserPassword();
    String encodedPassword = "password_encoded";

    when(userService.createUser(any())).thenReturn(user);
    when(passwordEncoder.encode(rawPassword)).thenReturn(encodedPassword);

    // Act and Assert
    mockMvc.perform(post("/portal/register"))
                .andExpect(status().isOk())
                .andExpect(content().string("User registered successfully"));

  }
}
