package com.library.backend.user;

import com.library.backend.dto.longReqDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/color")
class UserController {

    @Autowired
    private UserService userService;

    @PutMapping("/update")
    public ResponseEntity<String> updateColorToUser(@RequestBody longReqDTO req){
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

}
