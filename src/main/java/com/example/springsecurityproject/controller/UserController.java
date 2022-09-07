package com.example.springsecurityproject.controller;
import com.example.springsecurityproject.entity.RoleEntity;
import com.example.springsecurityproject.entity.UserEntity;
import com.example.springsecurityproject.service.RoleService;
import com.example.springsecurityproject.service.SelectForUserRole;
import com.example.springsecurityproject.service.UserPermissionService;
import com.example.springsecurityproject.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;


/** This is the UserController
 * Over here we're injecting some depenencies, the first one is UserService, that's returning all the function
 *
 */
@RestController
@AllArgsConstructor
@Slf4j
public class UserController {


    @Autowired
    private final UserService userService;
    private final RoleService roleService;
    private final UserPermissionService userPermissionService;

    @PostMapping(value = "/register")
    public ResponseEntity<String> registerUser(
            @RequestParam("username") String user,
            @RequestParam("password") String password
    ){
        try {
            if (userService.loadUserByUsername(user) != null){
                URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/register").toUriString());
                return ResponseEntity.created(uri).body("User already found in the database");
            }
            else {
                throw new NullPointerException();
            }

        }
        catch (NullPointerException e)
        {
            UserEntity newUser = new UserEntity();
            newUser.setUsername(user);
            newUser.setPassword(password);
            String confirmToken = newUser.getConfirmToken();
            String path = "/confirm?token=" + confirmToken;
            userService.addUser(newUser);
            URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path(path).toUriString().replace("%3F", "?"));
            return ResponseEntity.ok().body("User registered, enable your account here: \n" + uri +"");

        }
    }

    @GetMapping("/confirm")
    public ResponseEntity<String> confirmToken(@RequestParam("token") String token){

        if (token.isEmpty())
        {
            return new ResponseEntity<>("Url non valido", HttpStatus.BAD_REQUEST);
        }
        if (!userService.findToken(token))
        {
            return new ResponseEntity<>("Il token non esiste", HttpStatus.BAD_REQUEST);
        }
        else{
            userService.setToken(token);
            return new ResponseEntity<>("Token confermato", HttpStatus.CREATED);
        }

    }

    @PostMapping("/admin/permission")
    public ResponseEntity<String> setPermissionToUser(@RequestBody SelectForUserRole submit){
        UserEntity user = userService.findUserById(submit.getUserId());
        RoleEntity role = roleService.findById(submit.getRoleId());

        userPermissionService.addRoleToUser(role, user);
        return ResponseEntity.ok().body("Ruolo "+ role.getRoleName() +" aggiunto a " + user.getUsername());
    }

}
