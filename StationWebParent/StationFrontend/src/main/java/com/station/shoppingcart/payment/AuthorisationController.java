package com.station.shoppingcart.payment;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/authorise")
public class AuthorisationController {

    private static final Logger logger = LoggerFactory.getLogger(AuthorisationController.class);

    private final AuthoriseService authorisationService;

    public AuthorisationController(AuthoriseService authorisationService) {
        this.authorisationService = authorisationService;
    }

    @PostMapping
    public Map<String, String> authorise(@RequestBody AuthoriseRequestDTO request) throws Exception {
        logger.info("Controller request thread: {}", Thread.currentThread().getName());
        return authorisationService.authService(request);
    }
}

