package vn.gov.prison.secure.infrastructure.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import vn.gov.prison.secure.domain.user.User;
import vn.gov.prison.secure.domain.user.UserRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPasswordHash())
                .authorities(getAuthorities(user))
                .accountExpired(false)
                .accountLocked(!user.isActive())
                .credentialsExpired(false)
                .disabled(!user.isActive())
                .build();
    }

    private Collection<? extends GrantedAuthority> getAuthorities(User user) {
        List<GrantedAuthority> authorities = new ArrayList<>();

        // Add role-based authority
        authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getUserType().name()));

        // Add specific permissions based on user type
        switch (user.getUserType()) {
            case WARDEN:
                authorities.add(new SimpleGrantedAuthority("PRISONER_CREATE"));
                authorities.add(new SimpleGrantedAuthority("PRISONER_READ"));
                authorities.add(new SimpleGrantedAuthority("PRISONER_UPDATE"));
                authorities.add(new SimpleGrantedAuthority("PRISONER_DELETE"));
                authorities.add(new SimpleGrantedAuthority("PRISONER_RELEASE"));
                authorities.add(new SimpleGrantedAuthority("ESCAPE_APPROVE_WARDEN"));
                authorities.add(new SimpleGrantedAuthority("STATISTICS_VIEW"));
                break;
            case GUARD:
                authorities.add(new SimpleGrantedAuthority("PRISONER_READ"));
                authorities.add(new SimpleGrantedAuthority("PRISONER_UPDATE"));
                authorities.add(new SimpleGrantedAuthority("ESCAPE_APPROVE_GUARD"));
                authorities.add(new SimpleGrantedAuthority("ATTENDANCE_VIEW"));
                break;
            case PRISONER:
                authorities.add(new SimpleGrantedAuthority("ATTENDANCE_CHECKIN"));
                authorities.add(new SimpleGrantedAuthority("ATTENDANCE_CHECKOUT"));
                authorities.add(new SimpleGrantedAuthority("ESCAPE_REQUEST"));
                break;
        }

        return authorities;
    }
}
