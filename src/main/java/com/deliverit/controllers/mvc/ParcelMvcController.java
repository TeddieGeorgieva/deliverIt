package com.deliverit.controllers.mvc;

import com.deliverit.controllers.AuthenticationHelper;
import com.deliverit.exceptions.*;
import com.deliverit.models.*;
import com.deliverit.services.category.CategoryService;
import com.deliverit.services.parcel.ParcelModelMapper;
import com.deliverit.services.parcel.ParcelService;
import com.deliverit.services.shipment.ShipmentService;
import com.deliverit.services.users.UserService;
import com.deliverit.services.warehouse.WarehouseService;
import org.apache.tomcat.util.http.parser.HttpParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.*;

@Controller
@RequestMapping("/parcels")
public class ParcelMvcController {
    private final ParcelService parcelService;
    private final ParcelModelMapper modelMapper;
    private final UserService userService;
    private final CategoryService categoryService;
    private final WarehouseService warehouseService;
    private final ShipmentService shipmentService;
    private final AuthenticationHelper authenticationHelper;

    @Autowired
    public ParcelMvcController(ParcelService parcelService, ParcelModelMapper modelMapper, UserService userService, CategoryService categoryService, WarehouseService warehouseService, ShipmentService shipmentService, AuthenticationHelper authenticationHelper) {
        this.parcelService = parcelService;
        this.modelMapper = modelMapper;
        this.userService = userService;
        this.categoryService = categoryService;
        this.warehouseService = warehouseService;
        this.shipmentService = shipmentService;
        this.authenticationHelper = authenticationHelper;
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
    public String showAllParcels(Model model, HttpSession session) {
        User user;
        try {
            user = authenticationHelper.tryGetUser(session);
            model.addAttribute("parcels", parcelService.getAll(user));
            model.addAttribute("searchParcelDto", new SearchParcelDto());

            Map<String, Object> customers = new HashMap<>();
            Map<String, Object> warehouses = new HashMap<>();
            Map<String, Object> categories = new HashMap<>();

            insertCustomers(customers);
            insertCategories(categories);
            insertWarehouses(warehouses);

            model.addAttribute("customers", customers);
            model.addAttribute("warehouses", warehouses);
            model.addAttribute("categories", categories);
            model.addAttribute("currentUser", user);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
        return "parcels";
    }

    @GetMapping("/my")
    public String showParcels(Model model, HttpSession session, @ModelAttribute("parcelCustomerDto") ParcelCustomerDto dto) {
        try {
            User user = authenticationHelper.tryGetUser(session);
            model.addAttribute("customer", user);
            model.addAttribute("myParcels", parcelService.getAllForUser(user));
            List<Parcel> pastParcels = new ArrayList<>();
            List<Parcel> currentParcels = new ArrayList<>();

            populateParcelsByStatus(user, pastParcels, currentParcels);

            model.addAttribute("currentParcels", currentParcels);
            model.addAttribute("pastParcels", pastParcels);

//            Map<String, Object> deliveryTypes = new HashMap<>();
//            insertDeliveryTypes(deliveryTypes);
//            model.addAttribute("deliveryTypes", deliveryTypes);

        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
        return "myParcels";
    }

    private void populateParcelsByStatus(User user, List<Parcel> pastParcels, List<Parcel> currentParcels) {
        for (Parcel parcel : parcelService.getAllForUser(user)) {
            if (!parcel.getShipments().isEmpty()) {
                if (shipmentService.checkShipmentStatus(
                        parcelService.findShipment(parcel.getId())
                ).equals(Status.COMPLETED.toString())) {
                    pastParcels.add(parcel);
                } else {
                    currentParcels.add(parcel);
                }
            } else {
                currentParcels.add(parcel);
            }

        }
    }

    @GetMapping("/{id}")
    public String showSingleParcel(@PathVariable int id, Model model) {
        try {
            Parcel parcel = parcelService.getById(id);
            model.addAttribute("parcel", parcel);
            return "parcel";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        }

    }

    @GetMapping("/new")
    public String showCreateParcelPage(Model model, HttpSession session) {
        try {
            User user = authenticationHelper.tryGetUser(session);
            model.addAttribute("parcel", new ParcelDto());
            model.addAttribute("deliveryTypes", ParcelDeliveryType.values());
            addAttributesToModel(model, user);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }
        return "parcel-new";
    }

    @PostMapping("/new")
    public String createParcel(@Valid @ModelAttribute("parcel") ParcelDto parcelDto,
                               BindingResult errors,
                               Model model,
                               HttpSession session) {
        if (errors.hasErrors()) {
            User user = authenticationHelper.tryGetUser(session);
            addAttributesToModel(model, user);
            return "parcel-new";
        }

        try {
            User user = authenticationHelper.tryGetUser(session);
            Parcel parcel = modelMapper.fromDto(parcelDto);
            parcelService.create(parcel, user);

            return "redirect:/parcels";

        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        } catch (DuplicateEntityException e) {
            errors.rejectValue("name", "error.duplicate-parcel", e.getMessage());
            return "parcel-new";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        }
    }


    @GetMapping("/my/{id}/update")
    public String showEditParcelPageCustomer(@PathVariable int id, Model model) {
        try {
            Parcel parcel = parcelService.getById(id);
            ParcelDto parcelDto = modelMapper.toDto(parcel);

            model.addAttribute("parcelId", id);
            model.addAttribute("parcelDto", parcelDto);

            model.addAttribute("deliveryTypes", ParcelDeliveryType.values());

            return "customer-parcel-update";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        }
    }

    @PostMapping("/my/{id}/update")
    public String updateParcelCustomer(@PathVariable int id,
                                       @Valid @ModelAttribute("parcelDto") ParcelDto dto,
                                       BindingResult errors,
                                       Model model,
                                       HttpSession session) {
        if (errors.hasErrors()) {
            return "customer-parcel-update";
        }
        try {
            User user = authenticationHelper.tryGetUser(session);
            Parcel parcel = modelMapper.fromDto(dto, id);
            parcelService.update(parcel, user);

            return "redirect:/parcels/my";
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        }
    }


    @GetMapping("/{id}/update")
    public String showEditParcelPage(@PathVariable int id, Model model, HttpSession session) {
        try {
            User user = authenticationHelper.tryGetUser(session);
            Parcel parcel = parcelService.getById(id);
            ParcelDto parcelDto = modelMapper.toDto(parcel);
            model.addAttribute("parcelId", id);
            model.addAttribute("parcelDto", parcelDto);

            model.addAttribute("deliveryTypes", ParcelDeliveryType.values());

            addAttributesToModel(model, user);

            return "parcel-update";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        }
    }

    @PostMapping("/{id}/update")
    public String updateParcel(@PathVariable int id,
                               @Valid @ModelAttribute("parcelDto") ParcelDto dto,
                               BindingResult errors,
                               Model model,
                               HttpSession session) {
        if (errors.hasErrors()) {
            User user = authenticationHelper.tryGetUser(session);
            addAttributesToModel(model, user);
            return "parcel-update";
        }

        try {
            User user = authenticationHelper.tryGetUser(session);
            Parcel parcel = modelMapper.fromDto(dto, id);
            parcelService.update(parcel, user);
            return "redirect:/parcels";
        } catch (EntityUpdateException ignored) {
            return "redirect:/parcels";
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        } catch (DuplicateEntityException e) {
            errors.rejectValue("name", "duplicate_parcel", e.getMessage());
            return "parcel-update";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        } catch (UnauthorizedOperationException e) {
            errors.rejectValue("user", "error.access-denied", e.getMessage());
            return "redirect:/auth/login";
        }
    }

    @GetMapping("/{id}/delete")
    public String deleteParcel(@PathVariable int id, Model model, HttpSession session) {
        User user;
        try {
            user = authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }
        try {
            parcelService.delete(id, user);
            return "redirect:/parcels";

        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        }
    }

    private void insertCategories(Map<String, Object> categories) {
        for (int i = 1; i <= categoryService.getAll().size(); i++) {
            categories.put(categoryService.getById(i).getName(), i);
        }
    }

    private void insertWarehouses(Map<String, Object> warehouses) {
        for (int i = 1; i <= warehouseService.getAll().size(); i++) {
            warehouses.put(warehouseService.getById(i).getAddress().toString(), i);
        }
    }

    private void insertShipments(Map<String, Object> shipments, User user) {
        shipments.put("No shipment", -1);
        for (int i = 1; i <= shipmentService.getAll(user).size(); i++) {
            shipments.put(shipmentService.getById(i).getArrivalDate().toString()
                    + ' ' + shipmentService.getById(i).getDestinationWarehouse().getAddress().getCity().getName()
                    + ' ' + shipmentService.getById(i).getDestinationWarehouse().getAddress().getStreetName(), i);
        }
    }

    private void insertCustomers(Map<String, Object> customers) {
        for (int i = 1; i < userService.getAll().size(); i++) {
            customers.put(userService.getAll().get(i).getUsername(), i);
        }
    }

    private void addAttributesToModel(Model model, User user) {
        Map<String, Object> categories = new HashMap<>();
        insertCategories(categories);
        model.addAttribute("categories", categories);

        Map<String, Object> shipments = new HashMap<>();
        insertShipments(shipments, user);
        model.addAttribute("shipments", shipments);

        Map<String, Object> warehouses = new HashMap<>();
        insertWarehouses(warehouses);
        model.addAttribute("warehouses", warehouses);
    }

    @PostMapping("")
    public String filter(@ModelAttribute SearchParcelDto searchParcelDto, Model model, HttpSession session) {
        try {
            User user = authenticationHelper.tryGetUser(session);

            Map<String, Object> customers = new HashMap<>();
            Map<String, Object> warehouses = new HashMap<>();
            Map<String, Object> categories = new HashMap<>();

            insertCustomers(customers);
            insertCategories(categories);
            insertWarehouses(warehouses);

            model.addAttribute("customers", customers);
            model.addAttribute("warehouses", warehouses);
            model.addAttribute("categories", categories);
            model.addAttribute("currentUser", user);

            List<Parcel> parcelList = parcelService.filterParcels(user,
                    searchParcelDto.getCustomerId(),
                    searchParcelDto.getCategoryId(),
                    searchParcelDto.getWeight(),
                    searchParcelDto.getDestinationWarehouseId(),
                    searchParcelDto.getSearchAll());

            model.addAttribute("parcels", parcelList);
            return "parcels";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        }
    }

}
