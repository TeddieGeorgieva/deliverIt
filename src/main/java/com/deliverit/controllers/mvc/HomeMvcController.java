package com.deliverit.controllers.mvc;

import com.deliverit.models.LoginDto;
import com.deliverit.models.User;
import com.deliverit.services.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/")
public class HomeMvcController {

    private final UserService userService;

    @Autowired
    public HomeMvcController(UserService userService) {
        this.userService = userService;
    }

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

    @GetMapping
    public String showHomePage(Model model) {
        model.addAttribute("login", new LoginDto());
        model.addAttribute("usersCount", userService.getNumberOfUsers());
        return "index";
    }
}
