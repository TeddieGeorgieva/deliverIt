package com.deliverit.controllers.mvc;

import com.deliverit.controllers.AuthenticationHelper;
import com.deliverit.exceptions.EntityNotFoundException;
import com.deliverit.exceptions.UnauthorizedOperationException;
import com.deliverit.models.SearchCustomerDto;
import com.deliverit.models.User;
import com.deliverit.services.parcel.ParcelModelMapper;
import com.deliverit.services.parcel.ParcelService;
import com.deliverit.services.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/customers")
public class CustomerMvcController {

    private final UserService service;
    private final ParcelModelMapper modelMapper;
    private final AuthenticationHelper authenticationHelper;
    private final ParcelService parcelService;

    @Autowired
    public CustomerMvcController(UserService service, ParcelModelMapper modelMapper, AuthenticationHelper authenticationHelper, ParcelService parcelService) {
        this.service = service;
        this.modelMapper = modelMapper;
        this.authenticationHelper = authenticationHelper;
        this.parcelService = parcelService;
    }

    @ModelAttribute("users")
    public List<User> getAll(HttpSession session) {
        return service.getAll();
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }

    @ModelAttribute("isEmployee")
    public boolean populateIsAuthenticatedEmployee(HttpSession session) {
        return session.getAttribute("currentUser") != null && service.getByUsername(session.getAttribute("currentUser").toString()).isEmployee();
    }

    @ModelAttribute("isCustomer")
    public boolean populateIsAuthenticatedCustomer(HttpSession session) {
        return session.getAttribute("currentUser") != null && service.getByUsername(session.getAttribute("currentUser").toString()).isCustomer();
    }

    @GetMapping
    public String showAllCustomers(Model model, HttpSession session) {
        try {
            User employee = authenticationHelper.tryGetUser(session);
            model.addAttribute("customers", service.getAll().stream()
                    .filter(User::isCustomer).collect(Collectors.toList()));
            model.addAttribute("currentUser", employee);
            model.addAttribute("searchCustomerDto", new SearchCustomerDto());
            return "customers";
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public String showSingleCustomer(@PathVariable int id, Model model, HttpSession session) {
        try {
            User user = authenticationHelper.tryGetUser(session);
            model.addAttribute("customer", user);
            return "customer";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        }
    }

    @PostMapping("")
    public String filter(@ModelAttribute SearchCustomerDto searchCustomerDto, Model model, HttpSession session){
        try {
            User user = authenticationHelper.tryGetUser(session);
            List<User> userList = service.filter(searchCustomerDto.getFirstName(), searchCustomerDto.getLastName(), searchCustomerDto.getEmail(), searchCustomerDto.getSearchAll());
            model.addAttribute("currentUser", user);
            model.addAttribute("customers", userList);
            return "customers";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        }
    }
}
