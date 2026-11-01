package edu.bellevue.cis530.week8.config;

import edu.bellevue.cis530.week8.entity.Member;
import edu.bellevue.cis530.week8.repository.MemberRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class MemberDataInitializer {

    @Bean
    CommandLineRunner seedMembers(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (memberRepository.findByUsername("admin").isEmpty()) {
                Member admin = new Member();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setRole("ROLE_ADMIN");
                admin.setEnabled(true);
                memberRepository.save(admin);
            }

            if (memberRepository.findByUsername("user").isEmpty()) {
                Member user = new Member();
                user.setUsername("user");
                user.setPassword(passwordEncoder.encode("user123"));
                user.setRole("ROLE_USER");
                user.setEnabled(true);
                memberRepository.save(user);
            }
        };
    }
}
