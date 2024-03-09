package com.spring.login.controllers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.login.error.UsernameEmailIsExistedException;
import com.spring.login.models.ERole;
import com.spring.login.models.Role;
import com.spring.login.models.User;
import com.spring.login.payload.request.LoginRequest;
import com.spring.login.payload.request.SignupRequest;
import com.spring.login.payload.response.MessageResponse;
import com.spring.login.payload.response.UserInfoResponse;
import com.spring.login.repository.RoleRepository;
import com.spring.login.repository.UserRepository;
import com.spring.login.security.jwt.JwtUtils;
import com.spring.login.security.services.UserDetailsImpl;
import com.spring.login.util.ValidateUsernameEmail;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
  @Autowired
  AuthenticationManager authenticationManager;

  @Autowired
  UserRepository userRepository;

  @Autowired
  RoleRepository roleRepository;

  @Autowired
  PasswordEncoder encoder;

  @Autowired
  JwtUtils jwtUtils;

  @Autowired
  ValidateUsernameEmail validateUsernameEmail;

  @PostMapping("/signin")
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest,
      BindingResult bindingResult) {


    if (bindingResult.hasErrors()) {
      List<FieldError> errors = bindingResult.getFieldErrors();
      List<String> message = new ArrayList<>();
      for (FieldError e : errors) {
        message.add("@" + e.getField().toUpperCase() + ":" + e.getDefaultMessage());
        return ResponseEntity.badRequest().body(new MessageResponse(message.toString()));
      }

    }

    Authentication authentication = authenticationManager
        .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);

    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

    ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

    List<String> roles = userDetails.getAuthorities().stream()
        .map(item -> item.getAuthority())
        .collect(Collectors.toList());

    return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
        .body(new UserInfoResponse(userDetails.getId(),
            userDetails.getUsername(),
            userDetails.getNama(),
            userDetails.getNo_handphone(),
            userDetails.getJenis_kelamin(),
            userDetails.getEmail(),
            roles));
  }

  @PostMapping("/signup")
  public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest, BindingResult bindingResult) {

    try {
      validateUsernameEmail.isUsernameExist(signUpRequest.getUsername());
    } catch (UsernameEmailIsExistedException e) {
      return ResponseEntity.badRequest().body(new MessageResponse("username sudah digunakan"));
    }

    try {
      validateUsernameEmail.isEmailExist(signUpRequest.getEmail());
    } catch (UsernameEmailIsExistedException e) {
      return ResponseEntity.badRequest().body(new MessageResponse("email sudah digunakan"));
    }
    

    if (bindingResult.hasErrors()) {
      List<FieldError> errors = bindingResult.getFieldErrors();
      List<String> message = new ArrayList<>();
      for (FieldError e : errors) {
        message.add("@" + e.getField().toUpperCase() + ":" + e.getDefaultMessage());
        return ResponseEntity.badRequest().body(new MessageResponse(message.toString()));
      }

    }

    // Create new user's account
    User user = new User(signUpRequest.getUsername(),
        signUpRequest.getNama(),
        signUpRequest.getNo_handphone(),
        signUpRequest.getJenis_kelamin(),
        signUpRequest.getEmail(),
        encoder.encode(signUpRequest.getPassword()));

    Set<String> strRoles = signUpRequest.getRole();
    Set<Role> roles = new HashSet<>();

    System.out.println("size strRole = " + strRoles.size());

    strRoles.forEach(strRole -> {
      System.out.println("role = " + strRole);

    });

    strRoles.forEach(role -> {

      switch (role) {
        case "admin":
          Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
              .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
          roles.add(adminRole);
          break;

        case "user":
          Role userRole = roleRepository.findByName(ERole.ROLE_USER)
              .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
          roles.add(userRole);
          break;

        default:
          throw new RuntimeException("Error: Role is not found.");
      }
    });

    user.setRoles(roles);

    userRepository.save(user);

    return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
  }

  @PostMapping("/signout")
  public ResponseEntity<?> logoutUser() {
    ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
    return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
        .body(new MessageResponse("You've been signed out!"));
  }
}
