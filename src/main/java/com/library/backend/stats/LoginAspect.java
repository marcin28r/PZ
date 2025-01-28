package com.library.backend.stats;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Aspect
@Component
public class LoginAspect {

    private final LoginHoursService loginHoursService;

    public LoginAspect(LoginHoursService loginHoursService) {
        this.loginHoursService = loginHoursService;
    }

    @Before("execution(* com.library.backend.config.auth.AuthenticationService.authentication(..))")
    public void countLogins() {
        LocalTime now = LocalTime.now();

        this.loginHoursService.loginRegistration(this.getHourRange(now));

        System.out.println("Logowanie zarejestrowane :" + now);
    }

    private String getHourRange(LocalTime time) {
        int hour = time.getHour();
        return hour + ":00-" + (hour + 1) + ":00";
    }
}
