package com.scrapy.pfe.spring.controllers;

import com.scrapy.pfe.spring.dtos.*;

import com.scrapy.pfe.spring.entities.Job;
import com.scrapy.pfe.spring.entities.Maroc;
import com.scrapy.pfe.spring.entities.User;
import com.scrapy.pfe.spring.repositories.UserRepository;
import com.scrapy.pfe.spring.services.AuthService;
import com.scrapy.pfe.spring.services.jwt.UserDetailsServiceImpl;
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
import java.util.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {


    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsService;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;


    public AuthController(AuthService authService, AuthenticationManager authenticationManager, UserDetailsServiceImpl userDetailsService, UserDetailsServiceImpl userDetailsService1, UserDetailsServiceImpl userDetailsService2, JwtUtil jwtUtil, UserRepository userRepository) {
        this.authService = authService;
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


    @GetMapping("/scrapeMaroc")
    public List<Maroc> scrapeMaroc() {
        List<Maroc> jobInfoList = new ArrayList<>();

        // Liste des URLs des pages à scraper
        String[] urls = {
                "https://www.marocannonces.com/categorie/309/Emploi/Offres-emploi.html",
                "https://www.marocannonces.com/categorie/309/Emploi/Offres-emploi/2.html"
        };

        try {
            for (String url : urls) {
                // Connect to the URL and specify the user-agent to avoid blocking
                Document doc = Jsoup.connect(url)
                        .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/102.0.0.0 Safari/537.36")
                        .get();

                // Select all elements containing job information
                Elements jobElements = doc.select(".cars-list li");

                // Loop through each element and extract necessary information
                for (Element jobElement : jobElements) {
                    // Extract the title of the job
                    String title = jobElement.select(".holder h3").text();

                    // Extract the description of the job
                    String description = jobElement.select(".niveauetude").text();
                    String city = jobElement.select(".location").text();

                    // Extract the salary of the job
                    String salary = jobElement.select(".salary").text();
                    String date = jobElement.select(".date").text();

                    String link = jobElement.select("a").attr("href");

                    // Create a new instance of Maroc with the extracted information
                    if (!title.isEmpty() && !description.isEmpty() && !city.isEmpty() && !salary.isEmpty() && !date.isEmpty() && !link.isEmpty()) {
                        Maroc maroc = new Maroc(title, description, city, salary, date, link);
                        // Add the Maroc object to the list
                        jobInfoList.add(maroc);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Handle scraping errors
        }

        return jobInfoList;
        }

    @GetMapping("/scrapeIn")
    public List<Job> scrapeStagiairesMa() {
        List<Job> jobOffers = new ArrayList<>();
        String baseUrl = "https://www.stagiaires.ma/offres-stages?page=";

        try {
            // Itérer à travers les trois premières pages
            for (int i = 1; i <= 3; i++) {
                // Connectez-vous à l'URL de la page actuelle et spécifiez l'user-agent pour éviter le blocage
                Document doc = Jsoup.connect(baseUrl + i)
                        .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/102.0.0.0 Safari/537.36")
                        .get();

                // Sélectionnez tous les éléments contenant les informations sur les offres de stage
                Elements jobElements = doc.select(".offer-container");

                // Parcourez chaque élément et extrayez les informations nécessaires
                for (Element jobElement : jobElements) {
                    // Extrait le titre, la description, la location, la date et le lien de chaque offre
                    String title = jobElement.select(".offer-title strong").text();
                    String description = jobElement.select(".mt2").text();
                    String location = jobElement.select(".actions").text();
                    String date = jobElement.select(".detail-container > small.pull-right.text-muted").text();
                    String link = jobElement.select(" a").attr("href");

                    // Crée une nouvelle instance de Job avec le titre, la description, la location, la date et le lien
                    Job jobOffer = new Job(title, description, location, date, link);

                    // Ajoute l'offre d'emploi à la liste
                    jobOffers.add(jobOffer);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Gérer les erreurs de scraping
        }

        return jobOffers;
    }



    @GetMapping("/scrapeThreePages")
    public List<Map<String, String>> scrapeThreePages(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String city) {
        List<Map<String, String>> allJobInfos = new ArrayList<>();

        try {
            int pageNumber = 1; // Commencez par la première page

            // Continuez la boucle tant qu'il y a des pages à parcourir et tant que pageNumber est inférieur ou égal à 3
            while (pageNumber <= 3) {
                Document doc = Jsoup.connect("https://www.stages-emplois.com/offres-emploi.php?page=" + pageNumber)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/94.0.4606.81 Safari/537.36")
                        .get();

                // Sélectionnez tous les éléments contenant les annonces sur la page actuelle
                Elements jobElements = doc.select(".job");

                // Si aucun élément n'est trouvé, cela signifie que vous avez atteint la dernière page
                if (jobElements.isEmpty()) {
                    break; // Sortez de la boucle
                }

                // Parcourez chaque élément et extrayez les informations nécessaires
                for (Element jobElement : jobElements) {
                    String jobTitle = jobElement.select(".jobtitle").text();
                    String jobDescription = jobElement.select(".jobdescription").text();
                    String jobCity = jobElement.select(".company-location").text();
                    String datePosted = jobElement.select(".publish-date").text();
                    String link = jobElement.select(".job a").attr("href");

                    // Filtrer par titre et par ville
                    boolean matchesTitle = (title == null || jobTitle.toLowerCase().contains(title.toLowerCase()));
                    boolean matchesCity = (city == null || jobCity.toLowerCase().contains(city.toLowerCase()));

                    if (matchesTitle && matchesCity) {
                        Map<String, String> jobInfo = new HashMap<>();
                        jobInfo.put("Titre", jobTitle);
                        jobInfo.put("Description", jobDescription);
                        jobInfo.put("Ville de l'entreprise", jobCity);
                        jobInfo.put("Date de publication", datePosted);
                        jobInfo.put("Lien", link);

                        // Ajoutez le HashMap à la liste
                        allJobInfos.add(jobInfo);
                    }
                }

                pageNumber++; // Passez à la page suivante
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Gérer les erreurs de scraping
        }

        return allJobInfos;
    }
}