package com.library.backend.stats;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LoginHoursService {
    private final LoginHoursRepository loginHoursRepository;

    public void loginRegistration(String hours) {
        List<LoginHours> data = loginHoursRepository.findAll();

        LoginHours existingEntry = data.stream()
                .filter(entry -> entry.getHours().equals(hours))
                .findFirst()
                .orElse(null);

        if (existingEntry != null) {
            existingEntry.setVisited(existingEntry.getVisited() + 1);
            loginHoursRepository.save(existingEntry);
        } else {
            LoginHours newEntry = LoginHours.builder()
                    .hours(hours)
                    .visited(1L)
                    .build();
            loginHoursRepository.save(newEntry);
        }
    }
}

