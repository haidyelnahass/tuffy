package com.eg.user.util;

import com.eg.user.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Slf4j
public class RefreshTokenJob {

  private final RefreshTokenRepository refreshTokenRepository;

  @Scheduled(cron = "0 0 * * *")
  private void revokeOldRefreshTokens() {
    refreshTokenRepository.deleteByExpiryDateBefore(LocalDateTime.now());
  }
}
