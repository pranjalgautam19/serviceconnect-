package com.serviceconnect;

import com.serviceconnect.model.UserAccount;
import com.serviceconnect.repo.UserAccountRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@org.springframework.scheduling.annotation.EnableScheduling
public class ServiceConnectApplication {
  public static void main(String[] args) { SpringApplication.run(ServiceConnectApplication.class, args); }
  @Bean PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }
  @Bean CommandLineRunner seedAdmin(UserAccountRepository users, PasswordEncoder encoder,
      org.springframework.core.env.Environment env) {
    return args -> {
      String email=env.getProperty("app.admin.email","admin@serviceconnect.local");
      if (!users.existsByEmailIgnoreCase(email)) {
        UserAccount a=new UserAccount(); a.fullName="ServiceConnect Admin"; a.email=email;
        a.passwordHash=encoder.encode(env.getProperty("app.admin.password","ChangeMe123!"));
        a.role="ADMIN"; a.enabled=true; users.save(a);
      }
    };
  }
}
