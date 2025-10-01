package com.eg.user.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Random;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Util {

  public static String encodePassword(String password) {
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    return passwordEncoder.encode(password);
  }

  public static Integer generateRandomConfirmationCode() {
    Random random = new Random();
    return 100000 + random.nextInt(900000);
  }

  public static boolean checkHashMatch(String input, String hash) {
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    return passwordEncoder.matches(input, hash);
  }

}
