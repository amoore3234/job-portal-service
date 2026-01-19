package com.portal.job_portal_service.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.portal.job_portal_service.model.LoginRequest;
import com.portal.job_portal_service.model.User;
import com.portal.job_portal_service.service.JwtService;
import com.portal.job_portal_service.service.UserService;
import com.portal.job_portal_service.util.MockDataUtil;

@SuppressWarnings("null")
@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
@RunWith(SpringRunner.class)
public class AuthControllerTest {

  @Autowired
  MockMvc mockMvc;

  @MockitoBean
  UserService userService;

  @MockitoBean
  JwtService jwtService;

  @MockitoBean
  PasswordEncoder passwordEncoder;

  @Autowired
  ObjectMapper objectMapper;

  @MockitoBean
  AuthenticationManager authenticationManager;

  @Test
  public void testUserRegistration() throws Exception {

    // Arrange
    User user = MockDataUtil.getUserData();
    String rawPassword = user.getUserPassword();
    String encodedPassword = "password_encoded";

    when(userService.createUser(any())).thenReturn(user);
    when(passwordEncoder.encode(rawPassword)).thenReturn(encodedPassword);

    String userJson = objectMapper.writeValueAsString(user);

    // Act and Assert
    mockMvc.perform(post("/auth/register")
          .contentType(MediaType.APPLICATION_JSON)
          .content(userJson))
        .andExpect(status().is2xxSuccessful());

  }

  @Test
  public void testUserLogin() throws Exception {

    // Arrange
    LoginRequest loginRequest = MockDataUtil.getLoginRequest();
    String rawPassword = loginRequest.getUserPassword();

    Authentication authentication = new UsernamePasswordAuthenticationToken(loginRequest, rawPassword);
    when(authenticationManager.authenticate(any())).thenReturn(authentication);
    when(jwtService.generateToken(anyString())).thenReturn("token");

    String loginRequestJson = objectMapper.writeValueAsString(loginRequest);

    // Act and Assert
    mockMvc.perform(post("/auth/login")
          .contentType(MediaType.APPLICATION_JSON)
          .content(loginRequestJson))
        .andExpect(status().is2xxSuccessful());

  }

  @Test
  public void testUserLogin_401_error() throws Exception {

    // Arrange
    LoginRequest loginRequest = MockDataUtil.getLoginRequest();

    when(authenticationManager.authenticate(any(Authentication.class)))
      .thenThrow(new BadCredentialsException("Invalid credentials"));

    String loginRequestJson = objectMapper.writeValueAsString(loginRequest);

    // Act and Assert
    mockMvc.perform(post("/auth/login")
          .contentType(MediaType.APPLICATION_JSON)
          .content(loginRequestJson))
        .andExpect(status().isUnauthorized());

    verify(authenticationManager).authenticate(any(Authentication.class));

  }

  @Test
  public void testUserLogin_500_error() throws Exception {

    // Arrange
    LoginRequest loginRequest = MockDataUtil.getLoginRequest();

    when(authenticationManager.authenticate(any(Authentication.class)))
      .thenThrow(new RuntimeException("Internal server error"));

    String loginRequestJson = objectMapper.writeValueAsString(loginRequest);

    // Act and Assert
    mockMvc.perform(post("/auth/login")
          .contentType(MediaType.APPLICATION_JSON)
          .content(loginRequestJson))
        .andExpect(status().isInternalServerError());

    verify(authenticationManager).authenticate(any(Authentication.class));

  }
}
