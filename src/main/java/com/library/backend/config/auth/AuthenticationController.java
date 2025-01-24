package com.library.backend.config.auth;

import com.library.backend.dto.MapStructMapperImpl;
import com.library.backend.dto.UserFullDTO;
import com.library.backend.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;
    private final MapStructMapperImpl mapper;
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
    ){
        AuthenticationResponse res = service.register(request);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("authentication",
                res.getToken());

        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body(res);
//        return ResponseEntity.ok(service.register(request));

    }

    @PostMapping("/authentication")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody AuthenticationRequest request
    ){
        AuthenticationResponse res = service.authentication(request);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("authentication",
                res.getToken());

        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body(res);
    }

    @GetMapping("/profile")
    public ResponseEntity<UserFullDTO> showProfile(@RequestHeader("Authorization") String token){
        //System.out.println(token);
        token = token.substring(7);
        return ResponseEntity.ok().body(mapper.userToFullDto(service.authenticatedUser(token).orElseThrow()));
    }

    @GetMapping("/role")
    public ResponseEntity<Role> showRole(@RequestHeader("Authorization") String token){
        token = token.substring(7);
        return ResponseEntity.ok(service.authenticatedRole(token));
    }
}
