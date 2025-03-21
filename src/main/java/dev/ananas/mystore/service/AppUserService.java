package dev.ananas.mystore.service;

import dev.ananas.mystore.models.AppUser;
import dev.ananas.mystore.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppUserService implements UserDetailsService {

    @Autowired
    private AppUserRepository repo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AppUser appUser = repo.findByEmail(email);

        if (appUser == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }

        // Spring interprète appUser.getRole() (ex: "admin") comme "ROLE_admin"
        return User.withUsername(appUser.getEmail())
                .password(appUser.getPassword())
                .roles(appUser.getRole())
                .build();
    }

    public void save(AppUser user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        repo.save(user);
    }

    public List<AppUser> getAllUsers() {
        return repo.findAll();
    }
}
