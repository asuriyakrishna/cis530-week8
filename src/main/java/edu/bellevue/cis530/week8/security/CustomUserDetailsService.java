package edu.bellevue.cis530.week8.security;

import edu.bellevue.cis530.week8.entity.Member;
import edu.bellevue.cis530.week8.repository.MemberRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    public CustomUserDetailsService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Member not found: " + username));

        return User.builder()
                .username(member.getUsername())
                .password(member.getPassword())
                .authorities(member.getRole())
                .disabled(!member.isEnabled())
                .build();
    }
}
