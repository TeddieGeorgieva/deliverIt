package com.deliverit.controllers.mvc;


import com.deliverit.controllers.AuthenticationHelper;
import com.deliverit.exceptions.AuthenticationFailureException;
import com.deliverit.exceptions.DuplicateEntityException;
import com.deliverit.exceptions.EntityNotFoundException;
import com.deliverit.exceptions.UnauthorizedOperationException;
import com.deliverit.models.*;
import com.deliverit.services.parcel.ParcelService;
import com.deliverit.services.shipment.ShipmentModelMapper;
import com.deliverit.services.shipment.ShipmentService;
import com.deliverit.services.users.UserService;
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

@Controller
@RequestMapping("/shipments")
public class ShipmentMvcController {
    private final ShipmentService shipmentService;
    private final UserService userService;
    private final ShipmentModelMapper modelMapper;
    private final WarehouseService warehouseService;
    private final ParcelService parcelService;
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
    public ShipmentMvcController(ShipmentService shipmentService, UserService userService, ShipmentModelMapper modelMapper, WarehouseService warehouseService, ParcelService parcelService, AuthenticationHelper authenticationHelper) {
        this.shipmentService = shipmentService;
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.warehouseService = warehouseService;
        this.parcelService = parcelService;
        this.authenticationHelper = authenticationHelper;
    }


    @GetMapping
    public String showAllShipments(Model model, HttpSession session) {
        try {
            User user = authenticationHelper.tryGetUser(session);
            model.addAttribute("shipments", shipmentService.getAll(user));
            model.addAttribute("searchShipmentDto", new SearchShipmentDto());

            Map<String, Object> customers = new HashMap<>();
            Map<String, Object> warehouses = new HashMap<>();

            insertCustomers(customers);
            insertWarehouses(warehouses);

            model.addAttribute("customers", customers);
            model.addAttribute("warehouses", warehouses);

            return "shipments";
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public String showShipment(@PathVariable int id, Model model) {
        try {
            Shipment shipment = shipmentService.getById(id);
            model.addAttribute("shipment", shipment);
            model.addAttribute("originWarehouse", shipment.getOriginWarehouse().getAddress());
            model.addAttribute("destinationWarehouse", shipment.getDestinationWarehouse().getAddress().toString());
            model.addAttribute("parcels", shipmentService.getById(shipment.getId()).getParcels());
            return "shipment";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        }
    }

    @GetMapping("/new")
    public String showNewShipmentPage(Model model) {

        Map<String, Object> warehouses = new HashMap<>();
        insertWarehouses(warehouses);
        model.addAttribute("warehouses", warehouses);

        model.addAttribute("shipment", new ShipmentDto());
        return "shipment-new";
    }

    @PostMapping("/new")
    public String createShipment(@Valid @ModelAttribute("shipment") ShipmentDto shipmentDto, BindingResult errors, Model model, HttpSession session) {
        if (errors.hasErrors()) {
            return "shipment-new";
        }
        try {
            User user = authenticationHelper.tryGetUser(session);
            Shipment shipment = modelMapper.fromDto(shipmentDto);
            shipmentService.create(user, shipment);
            return "redirect:/shipments";
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (DuplicateEntityException e) {
            errors.rejectValue("name", "duplicate_shipment", e.getMessage());
            return "shipment-new";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        }
    }

    @GetMapping("/update/{id}")
    public String showEditShipmentPage(@PathVariable int id, Model model) {
        try {
            Shipment shipment = shipmentService.getById(id);
            ShipmentDto shipmentDto = modelMapper.toDto(shipment);
            model.addAttribute("shipmentId", id);
            model.addAttribute("shipmentDto",shipmentDto);

            Map<String, Object> warehouses = new HashMap<>();
            insertWarehouses(warehouses);
            model.addAttribute("warehouses", warehouses);

            model.addAttribute("statuses", Status.values());

            model.addAttribute("parcels", shipmentService.getById(shipment.getId()).getParcels());

            return "shipment-update";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        }
    }


    @PostMapping("/update/{id}")
    public String updateShipment(@PathVariable int id,
                             @Valid @ModelAttribute("shipmentDto") ShipmentDto dto,
                             BindingResult errors,
                             Model model,
                             HttpSession session) {
        if (errors.hasErrors()) {
            return "shipment-update";
        }

        try {
            User user = authenticationHelper.tryGetUser(session);
            Shipment shipment = modelMapper.fromDto(dto, id);
            shipmentService.update(user, shipment);
            return "redirect:/shipments";
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        } catch (DuplicateEntityException e) {
            errors.rejectValue("name", "duplicate_shipment", e.getMessage());
            return "shipment";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        }
    }

    @GetMapping("/{id}/delete")
    public String deleteShipment(@PathVariable int id, Model model, HttpSession session) {
        try {
            authenticationHelper.tryGetUser(session);
            shipmentService.delete(id);
            return "redirect:/shipments";
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        }
    }

    @GetMapping("/delete/{shipmentId}/{parcelId}")
    public String removeParcelFromShipment(@PathVariable int shipmentId, @PathVariable int parcelId, HttpSession session) {
        try {
            User user = authenticationHelper.tryGetUser(session);
            shipmentService.removeParcel(user, shipmentId, parcelId);
            return "redirect:/shipments";
        } catch (EntityNotFoundException e) {
            return "redirect:/shipments";
        }
    }

    @PostMapping("")
    public String filter(@ModelAttribute SearchShipmentDto searchShipmentDto, Model model, HttpSession session){
        try {
            User user = authenticationHelper.tryGetUser(session);

            Map<String, Object> warehouses = new HashMap<>();
            Map<String, Object> customers = new HashMap<>();

            insertWarehouses(warehouses);
            insertWarehouses(customers);

            model.addAttribute("customers", customers);
            model.addAttribute("warehouses", warehouses);


            List<Shipment> shipmentList = shipmentService.filterShipmentsByWarehouse(
                    searchShipmentDto.getDestinationWarehouseId(),
                    searchShipmentDto.getOriginWarehouseId(),
                    searchShipmentDto.getCustomerId());


            model.addAttribute("shipments", shipmentList);
            return "shipments";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        }
    }

    private void insertWarehouses(Map<String, Object> warehouses) {
        for (int i = 1; i <= warehouseService.getAll().size(); i++) {
            warehouses.put(warehouseService.getById(i).getAddress().toString(), i);
        }
    }

    private void insertCustomers(Map<String, Object> customers) {
        for (int i = 1; i < userService.getAll().size(); i++) {
            customers.put(userService.getAll().get(i).getUsername(), i);
        }
    }

}
