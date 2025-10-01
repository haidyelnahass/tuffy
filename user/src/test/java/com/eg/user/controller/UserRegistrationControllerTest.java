package com.eg.user.controller;

import com.eg.common.config.SecurityConfig;
import com.eg.common.util.JwtUtil;
import com.eg.user.service.UserRegistrationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.eg.user.util.TestConstants.BASE_URL;
import static com.eg.user.util.TestConstants.CONFIRMATION_URL;
import static com.eg.user.util.TestConstants.REGISTRATION_URL;
import static com.eg.user.util.TestConstants.SEND_CONFIRMATION_URL;
import static com.eg.user.util.TestConstants.buildEmailConfirmationRequest;
import static com.eg.user.util.TestConstants.buildProfileConfirmationRequest;
import static com.eg.user.util.TestConstants.buildRiderUserRegistrationRequest;
import static com.eg.user.util.TestConstants.buildUserRegistrationResponse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserRegistrationController.class)// goes through filter chain!!!
@Import(SecurityConfig.class) // include our security config class to exclude certain apis
public class UserRegistrationControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private UserRegistrationService userRegistrationService;

  @MockitoBean
  private JwtUtil jwtUtil;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  public void testRegisterUserSuccessfully() throws Exception {
    when(userRegistrationService.registerUser(any()))
      .thenReturn(buildUserRegistrationResponse());


    mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL + REGISTRATION_URL)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(buildRiderUserRegistrationRequest())))
      .andExpect(status().is2xxSuccessful())
      .andExpect(jsonPath("$.GeneratedUserId").value(1L));

    verify(userRegistrationService, times(1)).registerUser(any());
  }

  @Test
  public void testSendConfirmationEmailSuccessfully() throws Exception {
    doNothing().when(userRegistrationService).sendConfirmationEmail(any());


    mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL + SEND_CONFIRMATION_URL)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(buildEmailConfirmationRequest())))
      .andExpect(status().is2xxSuccessful());

    verify(userRegistrationService, times(1)).sendConfirmationEmail(any());
  }

  @Test
  public void testConfirmEmailSuccessfully() throws Exception {
    doNothing().when(userRegistrationService).confirmProfile(any());


    mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL + CONFIRMATION_URL)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(buildProfileConfirmationRequest())))
      .andExpect(status().is2xxSuccessful());

    verify(userRegistrationService, times(1)).confirmProfile(any());
  }

}
