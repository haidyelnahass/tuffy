package com.eg.user.controller;

import com.eg.common.config.SecurityConfig;
import com.eg.common.util.JwtUtil;
import com.eg.user.service.UserAuthenticationService;
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
import static com.eg.user.util.TestConstants.LOGIN_URL;
import static com.eg.user.util.TestConstants.REGISTRATION_URL;
import static com.eg.user.util.TestConstants.SEND_CONFIRMATION_URL;
import static com.eg.user.util.TestConstants.buildEmailConfirmationRequest;
import static com.eg.user.util.TestConstants.buildProfileConfirmationRequest;
import static com.eg.user.util.TestConstants.buildRiderUserRegistrationRequest;
import static com.eg.user.util.TestConstants.buildUserLoginRequest;
import static com.eg.user.util.TestConstants.buildUserLoginResponse;
import static com.eg.user.util.TestConstants.buildUserRegistrationResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserAuthenticationController.class)// goes through filter chain!!!
@Import(SecurityConfig.class) // include our security config class to exclude certain apis
public class UserAuthenticationControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private UserAuthenticationService userAuthenticationService;

  @MockitoBean
  private JwtUtil jwtUtil;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  public void testLoginSuccessfully() throws Exception {
    when(userAuthenticationService.login(any()))
      .thenReturn(buildUserLoginResponse());


    mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL + LOGIN_URL)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(buildUserLoginRequest())))
      .andExpect(status().is2xxSuccessful())
      .andExpect(jsonPath("$.AccessToken").value("accessToken"));

    verify(userAuthenticationService, times(1)).login(any());
  }

}
