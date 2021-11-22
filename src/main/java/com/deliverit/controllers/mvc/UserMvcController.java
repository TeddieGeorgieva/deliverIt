package com.deliverit.controllers.mvc;

import com.deliverit.controllers.AuthenticationHelper;
import com.deliverit.services.parcel.ParcelModelMapper;
import com.deliverit.services.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/user")
public class UserMvcController {

    private final UserService service;
    private final ParcelModelMapper modelMapper;
    private final AuthenticationHelper authenticationHelper;

    @Autowired
    public UserMvcController(UserService service, ParcelModelMapper modelMapper, AuthenticationHelper authenticationHelper) {
        this.service = service;
        this.modelMapper = modelMapper;
        this.authenticationHelper = authenticationHelper;
    }

    @ModelAttribute("isEmployee")
    public boolean populateIsAuthenticatedEmployee(HttpSession session) {
        return session.getAttribute("currentUser") != null && service.getByUsername(session.getAttribute("currentUser").toString()).isEmployee();
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }

    @ModelAttribute("isCustomer")
    public boolean populateIsAuthenticatedCustomer(HttpSession session) {
        return session.getAttribute("currentUser") != null && service.getByUsername(session.getAttribute("currentUser").toString()).isCustomer();
    }

    @GetMapping("/numberOfUsers")
    public int getNumberOfUsers() {
        return service.getNumberOfUsers();
    }


}
