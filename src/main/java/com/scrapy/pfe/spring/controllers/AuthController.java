package com.scrapy.pfe.spring.controllers;

import com.scrapy.pfe.spring.dtos.AuthenticationRequest;
import com.scrapy.pfe.spring.dtos.AuthenticationResponse;
import com.scrapy.pfe.spring.dtos.SignupRequest;
import com.scrapy.pfe.spring.dtos.UserDto;
import com.scrapy.pfe.spring.entities.User;
import com.scrapy.pfe.spring.repositories.UserRepository;
import com.scrapy.pfe.spring.services.auth.AuthService;
import com.scrapy.pfe.spring.services.auth.jwt.UserDetailsServiceImpl;
import com.scrapy.pfe.spring.util.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsService;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    public AuthController(AuthService authService, AuthenticationManager authenticationManager, UserDetailsServiceImpl userDetailsService, JwtUtil jwtUtil, UserRepository userRepository) {
        this.authService = authService;
        this.authenticationManager = authenticationManager;

        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signupUser(@RequestBody SignupRequest signupRequest) {
        UserDto createdUserDto = authService.createUser(signupRequest);
        if (createdUserDto == null) {
            return new ResponseEntity<>("User not created. Come again later", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(createdUserDto, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public AuthenticationResponse createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest, HttpServletResponse response) throws IOException {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getPassword()));
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("incorrect username or password");
        } catch (DisabledException disabledException) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "User not active");
            return null;
        }
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getEmail());
        final String jwt = jwtUtil.generateToken(userDetails.getUsername());

        Optional<User> optionalUser = userRepository.findFirstByEmail(userDetails.getUsername());
        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        if (optionalUser.isPresent()) {
            authenticationResponse.setJwt(jwt);
            authenticationResponse.setUserRole(optionalUser.get().getUserRole());
            authenticationResponse.setUserId(optionalUser.get().getId());

        }


        return authenticationResponse;
    }

    @GetMapping("/scrape")
    public List<String> scrapeEmplois() {
        List<String> articleTitles = new ArrayList<>();

        try {

            Document doc = Jsoup.connect("https://www.stages-emplois.com/offres-emploi.php#google_vignette").userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/94.0.4606.81 Safari/537.36")
                    .get();

            Elements articleElements = doc.select(".job"); // Modifiez ceci pour correspondre au bon sélecteur
            System.out.println("----------GESTION D'EMPLOIS-----------");
            for (Element articleElement : articleElements) {

                String title = articleElement.select(" div > a ").text();
                String description = articleElement.select(" .jobdescription  ").text();

                System.out.println(title + " - " + description);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return articleTitles;
    }

    }
