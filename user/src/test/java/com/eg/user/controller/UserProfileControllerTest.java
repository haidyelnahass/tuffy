package com.eg.user.controller;

import com.eg.common.config.SecurityConfig;
import com.eg.common.util.JwtUtil;
import com.eg.user.service.UserAuthenticationService;
import com.eg.user.service.UserProfileService;
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
import static com.eg.user.util.TestConstants.DETAILS_URL;
import static com.eg.user.util.TestConstants.LOGIN_URL;
import static com.eg.user.util.TestConstants.PROFILE_URL;
import static com.eg.user.util.TestConstants.USER_ID_HEADER;
import static com.eg.user.util.TestConstants.buildProfileDetailsResponse;
import static com.eg.user.util.TestConstants.buildProfileUpdateRequest;
import static com.eg.user.util.TestConstants.buildUserLoginRequest;
import static com.eg.user.util.TestConstants.buildUserLoginResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {UserProfileController.class, InternalUserDetailsController.class})
@Import(SecurityConfig.class)
public class UserProfileControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private UserProfileService userProfileService;

  @MockitoBean
  private JwtUtil jwtUtil;

  @Autowired
  private ObjectMapper objectMapper;


  @Test
  public void testUpdateProfileDetailsSuccessfully() throws Exception {
    doNothing().when(userProfileService).updateProfileDetails(any(),
      any());

    mockMvc.perform(MockMvcRequestBuilders.patch(BASE_URL + PROFILE_URL)
        .contentType(MediaType.APPLICATION_JSON)
        .header(USER_ID_HEADER, 1L)
        .content(objectMapper.writeValueAsString(buildProfileUpdateRequest())))
      .andExpect(status().is2xxSuccessful());
  }

  @Test
  public void testGetProfileDetailsSuccessfully() throws Exception {
    when(userProfileService.getProfileDetails(any()))
      .thenReturn(buildProfileDetailsResponse());

    mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + PROFILE_URL)
        .header(USER_ID_HEADER, 1L)
        .contentType(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$.Id").value(1L));
  }

  @Test
  public void testInternalGetProfileDetailsSuccessfully() throws Exception {
    when(userProfileService.getProfileDetails(any()))
      .thenReturn(buildProfileDetailsResponse());

    mockMvc.perform(MockMvcRequestBuilders.get("/internal" + BASE_URL + DETAILS_URL)
        .header(USER_ID_HEADER, 1L)
        .contentType(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$.Id").value(1L));
  }

}
