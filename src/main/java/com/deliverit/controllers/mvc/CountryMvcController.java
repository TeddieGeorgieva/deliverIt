package com.deliverit.controllers.mvc;

import com.deliverit.controllers.AuthenticationHelper;
import com.deliverit.exceptions.UnauthorizedOperationException;
import com.deliverit.models.User;
import com.deliverit.services.country.CountryService;
import com.deliverit.services.users.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/countries")
public class CountryMvcController {

    private final CountryService countryService;
    private final AuthenticationHelper authenticationHelper;
    private final UserService userService;

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }

    @ModelAttribute("isEmployee")
    public boolean populateIsAuthenticatedEmployee(HttpSession session) {
        return session.getAttribute("currentUser") != null && userService.getByUsername(session.getAttribute("currentUser").toString()).isEmployee();
    }

    @ModelAttribute("isCustomer")
    public boolean populateIsAuthenticatedCustomer(HttpSession session) {
        return session.getAttribute("currentUser") != null && userService.getByUsername(session.getAttribute("currentUser").toString()).isCustomer();
    }

    public CountryMvcController(CountryService countryService, AuthenticationHelper authenticationHelper, UserService userService) {
        this.countryService = countryService;
        this.authenticationHelper = authenticationHelper;
        this.userService = userService;
    }

    @GetMapping
    public String showAllCountries(Model model, HttpSession session) {
        try {
            User user = authenticationHelper.tryGetUser(session);
            model.addAttribute("countries", countryService.getAll());
            return "countries";
        } catch (UnauthorizedOperationException e) {
            throw  new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
}
