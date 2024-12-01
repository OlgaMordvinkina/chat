package org.mediagate.core.controllers;

import org.mediagate.core.dto.UserDto;
import org.mediagate.core.dto.UserFullDto;
import org.mediagate.core.dto.UserRegisterDto;
import org.mediagate.core.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.authentication.logout.CookieClearingLogoutHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.authentication.rememberme.AbstractRememberMeServices;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.security.Principal;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping
public class UserController {
    private final UserService service;

    @PostMapping("/login")
    public Object apiLogin(Principal user) {
        log.info("Login user");
        UsernamePasswordAuthenticationToken token = ((UsernamePasswordAuthenticationToken) user);
        return token.getPrincipal();
    }

    @PostMapping("/logout")
    @ResponseBody
    public Principal logout(Principal user, HttpServletRequest request, HttpServletResponse response) {
        CookieClearingLogoutHandler cookieClearingLogoutHandler = new CookieClearingLogoutHandler(
                AbstractRememberMeServices.SPRING_SECURITY_REMEMBER_ME_COOKIE_KEY
        );
        SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
        cookieClearingLogoutHandler.logout(request, response, null);
        securityContextLogoutHandler.logout(request, response, null);

        return user;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/registration")
    public UserDto createUser(@RequestBody UserRegisterDto newUser) {
        log.info("POST /users/registration request received");
        return service.createUser(newUser);
    }

    @PutMapping("/users/{userId}")
    public UserDto updateUser(@PathVariable Long userId,
                              @RequestBody UserRegisterDto updateUser) {
        log.info("PUT /users/registration/" + userId + " request received");
        return service.updateUser(userId, updateUser);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/users/{userId}")
    public void deleteUser(@PathVariable Long userId, Long deletedUserId) {
        log.info("DELETE /users/registration/" + userId + " request received");
        service.deleteUserById(userId, deletedUserId);
    }

    @GetMapping("/users/{userId}/search")
    public List<UserFullDto> searchUser(@PathVariable Long userId, @Valid @NotBlank @RequestParam String desired) {
        log.info("GET /users/" + userId + "/search request received");
        return service.searchUser(userId, desired);
    }

    @GetMapping("/users")
    public UserFullDto getUser(@RequestParam String email) {
        log.info("GET /users search request received");
        return service.getUser(email);
    }
}
