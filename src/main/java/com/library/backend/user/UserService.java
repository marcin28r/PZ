package com.library.backend.user;

import com.library.backend.config.auth.AuthenticationService;
import com.library.backend.dto.UserFullDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    public final AuthenticationService authService;


    //public UserFullDTO load()

}
