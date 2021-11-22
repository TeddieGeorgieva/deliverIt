package com.deliverit.controllers.mvc;

import com.deliverit.controllers.AuthenticationHelper;
import com.deliverit.exceptions.*;
import com.deliverit.models.*;
import com.deliverit.services.address.AddressService;
import com.deliverit.services.country.CountryService;
import com.deliverit.services.shipment.ShipmentService;
import com.deliverit.services.users.UserService;
import com.deliverit.services.warehouse.WarehouseModelMapper;
import com.deliverit.services.warehouse.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/warehouses")
public class WarehouseMvcController {

    private final WarehouseService warehouseService;
    private final ShipmentService shipmentService;
    private final UserService userService;
    private final WarehouseModelMapper modelMapper;
    private final AddressService addressService;

    private final AuthenticationHelper authenticationHelper;

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

    @Autowired
    public WarehouseMvcController(WarehouseService warehouseService, ShipmentService shipmentService, UserService userService, WarehouseModelMapper modelMapper, AddressService addressService, AuthenticationHelper authenticationHelper) {
        this.warehouseService = warehouseService;
        this.shipmentService = shipmentService;
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.addressService = addressService;
        this.authenticationHelper = authenticationHelper;
    }

    @GetMapping
    public String showAllWarehouses(Model model) {
        try {
            model.addAttribute("warehouses", warehouseService.getAll());
            return "warehouses";
        } catch (AuthenticationFailureException e) {
            return "redirect:/login";
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public String showWarehouse(@PathVariable int id, Model model, HttpSession session) {
        try {
            User user = authenticationHelper.tryGetUser(session);
            Warehouse warehouse = warehouseService.getById(id);

            model.addAttribute("warehouse", warehouse);
            model.addAttribute("city", warehouse.getAddress().getCity().getName());
            model.addAttribute("country", warehouse.getAddress().getCity().getCountry().getName());
            model.addAttribute("streetName", warehouse.getAddress().getStreetName());

            model.addAttribute("shipments", insertShipments(user, warehouse.getId()));

            return "warehouse";
        } catch (AuthenticationFailureException e) {
            return "redirect:/login";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        }
    }

    @GetMapping("/{id}/nextArriving")
    public String showNextArrivingShipment(@PathVariable int id, Model model, HttpSession session) {
        try {

            User user = authenticationHelper.tryGetUser(session);
            Warehouse warehouse = warehouseService.getById(id);
            Shipment shipment = warehouseService.getNextArrivingShipment(id, user);

            model.addAttribute("warehouse", warehouse);
            model.addAttribute("shipment", shipment);
            model.addAttribute("shipmentId", shipment.getId());
            model.addAttribute("parcels", shipmentService.getById(shipment.getId()).getParcels());
            model.addAttribute("originWarehouse", shipment.getOriginWarehouse().getAddress());
            model.addAttribute("destinationWarehouse", shipment.getDestinationWarehouse().getAddress().toString());
            return "shipment";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "warehouses";
        }
    }

    private List<Shipment> insertShipments(User user, int warehouseId) {
        List<Shipment> list = shipmentService.getAll(user);
        return list.stream()
                .filter(s -> s.getDestinationWarehouse().getId() == warehouseId)
                .collect(Collectors.toList());
    }

    @PostMapping("/new")
    public String createWarehouse(@Valid @ModelAttribute("warehouse") WarehouseDto warehouseDto, BindingResult errors, Model model, HttpSession session) {
        if (errors.hasErrors()) {
            return "warehouse-new";
        }
        try {
            User user = authenticationHelper.tryGetUser(session);
            Warehouse warehouse = modelMapper.fromDto(warehouseDto);
            warehouseService.create(user, warehouse);
            return "redirect:/warehouses";
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (DuplicateEntityException e) {
            errors.rejectValue("name", "duplicate_warehouse", e.getMessage());
            return "warehouse-new";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        }
    }

    @GetMapping("/new")
    public String showCreateWarehousePage(Model model, HttpSession session) {
        try {
            authenticationHelper.tryGetUser(session);
            model.addAttribute("warehouse", new WarehouseDto());

            Map<String, Object> addresses = new HashMap<>();
            insertAddresses(addresses);
            model.addAttribute("addresses", addresses);

        } catch (AuthenticationFailureException e) {
            return "redirect:/login";
        }
        return "warehouse-new";
    }

    private void insertAddresses(Map<String, Object> addresses) {
        for (int i = 1; i <= addressService.getAll().size(); i++) {
            addresses.put(addressService.getById(i).toString(), i);
        }
    }

    @GetMapping("/{id}/update")
    public String showEditWarehousePage(@PathVariable int id, Model model, HttpSession session) {
        try {
            User user = authenticationHelper.tryGetUser(session);
            Warehouse warehouse = warehouseService.getById(id);
            WarehouseDto warehouseDto = modelMapper.toDto(warehouse);

            model.addAttribute("warehouseId", id);
            model.addAttribute("warehouseDto", warehouseDto);

            Map<String, Object> addresses = new HashMap<>();
            insertAddresses(addresses);

            model.addAttribute("addresses", addresses);

            return "warehouse-update";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        }
    }

    @PostMapping("/{id}/update")
    public String updateWarehouse(@PathVariable int id,
                                  @Valid @ModelAttribute("warehouseDto") WarehouseDto dto,
                                  BindingResult errors,
                                  Model model,
                                  HttpSession session) {
        if (errors.hasErrors()) {
            return "warehouse-update";
        }

        try {
            User user = authenticationHelper.tryGetUser(session);
            Warehouse warehouse = modelMapper.fromDto(dto, id);
            model.addAttribute("warehouse", warehouse);
            warehouseService.update(warehouse, user);

            return "redirect:/warehouses";
        } catch (EntityUpdateException ignored) {
            return "redirect:/warehouses";
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        } catch (DuplicateEntityException e) {
            errors.rejectValue("name", "duplicate_warehouse", e.getMessage());
            return "warehouse-update";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        } catch (UnauthorizedOperationException e) {
            errors.rejectValue("user", "error.access-denied", e.getMessage());
            return "redirect:/auth/login";
        }
    }
}
