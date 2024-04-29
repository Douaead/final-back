package com.scrapy.pfe.spring.controllers;

import com.scrapy.pfe.spring.dtos.*;
import com.scrapy.pfe.spring.entities.User;
import com.scrapy.pfe.spring.repositories.UserRepository;
import com.scrapy.pfe.spring.services.AuthService;
import com.scrapy.pfe.spring.services.jwt.UserDetailsServiceImpl;
import com.scrapy.pfe.spring.util.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.jsoup.Connection;
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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

    public AuthController(AuthService authService, AuthenticationManager authenticationManager, UserDetailsServiceImpl userDetailsService, UserDetailsServiceImpl userDetailsService1, UserDetailsServiceImpl userDetailsService2, JwtUtil jwtUtil, UserRepository userRepository){
        this.authService=authService;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService2;


        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }
    @PostMapping("/signup")
    public ResponseEntity<?> signupUser(@RequestBody SignupRequest signupRequest) {
        UserDto createdUserDto = authService.createUser(signupRequest);
        return createdUserDto != null
                ? new ResponseEntity<>(createdUserDto, HttpStatus.CREATED)
                : new ResponseEntity<>("User not created. Try again later.", HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/login")
    public AuthenticationResponse createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest, HttpServletResponse response) throws IOException {
        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getPassword()));
        }catch(BadCredentialsException e){
            throw new BadCredentialsException("incorrect username or password");
        }catch(DisabledException disabledException){
            response.sendError(HttpServletResponse.SC_NOT_FOUND,"User not active");
            return null;
        }
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getEmail());
        final String jwt = jwtUtil.generateToken(userDetails.getUsername());

        Optional<User> optionalUser = userRepository.findFirstByEmail(userDetails.getUsername());
        AuthenticationResponse authenticationResponse= new AuthenticationResponse();
        if(optionalUser.isPresent()){
            authenticationResponse.setJwt(jwt);
            authenticationResponse.setUserRole(optionalUser.get().getUserRole());
            authenticationResponse.setUserId(optionalUser.get().getId());

        }


        return authenticationResponse;
    }

    @GetMapping("/scrape")
    public List<String> scrapeArticles() {
        List<String> articleTitles = new ArrayList<>();

        try {

            Document doc = Jsoup.connect("https://www.stages-emplois.com/offres-emploi.php#google_vignette").userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/94.0.4606.81 Safari/537.36")
                    .get();

            Elements articleElements = doc.select(".job"); // Modifiez ceci pour correspondre au bon sélecteur
            System.out.println("------------------------------------------");
            for (Element articleElement : articleElements) {

                String title = articleElement.select(" div > a ").text();
                String ville = articleElement.select(" .jobdescription  ").text();

                System.out.println( title + " - " +ville);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return articleTitles;
    }
    @GetMapping("/scrapeIndeed")
    public String getpage_inded() throws IOException {

        Document doc=Jsoup.connect("https://ma.indeed.com/jobs?q=stage+web&fromage=1%22&vjk=fac97b1b77f155ce")
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Safari/537.36 Edg/110.0.1587.57")
                .header("Accept","/")
                .header("Accept-Encoding","gzip, deflate, br")
                .header("Connection","keep-alive")
                .header("Cookie","CTK=1hse0gmejjrig801; __cf_bm=E83E2TjUtcJisrulBK0XJaxOmnMM4JXYsl8UQarR1aU-1714165097-1.0.1.1-8nypwPLsaVkx0paWTE9.lBktvjq3aQcrAFJ5..4QIFhzVEuc5X0pQzw1a7nrWVyMB3_lfMx1FxevpTMqAgN1rg; _cfuvid=UxXiY7HtfEukyT.TokhcgqLvDbg58gPee2HXQ5ciqUc-1714162260801-0.0.1.1-604800000; INDEED_CSRF_TOKEN=HRmSwAR4fYmXQPfePXYY48XbPhTJuVww; LV=\"LA=1714162260:CV=1714162260:TS=1714162260\"; RQ=\"q=stage+web&l=&ts=1714162418757&rbsalmin=0&rbsalmax=0\"")
                .timeout(5000)
                .method(Connection.Method.GET)
                .get();
        return doc.html();
    }
   }