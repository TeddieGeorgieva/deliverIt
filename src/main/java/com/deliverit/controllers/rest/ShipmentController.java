package com.deliverit.controllers.rest;

import com.deliverit.controllers.AuthenticationHelper;
import com.deliverit.exceptions.DuplicateEntityException;
import com.deliverit.exceptions.EntityNotFoundException;
import com.deliverit.exceptions.UnauthorizedOperationException;
import com.deliverit.models.Parcel;
import com.deliverit.models.Shipment;
import com.deliverit.models.ShipmentDto;
import com.deliverit.models.User;
import com.deliverit.services.shipment.ShipmentModelMapper;
import com.deliverit.services.shipment.ShipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/shipments")
public class ShipmentController {

    private final ShipmentService service;
    private final ShipmentModelMapper modelMapper;
    private final AuthenticationHelper authenticationHelper;

    @Autowired
    public ShipmentController(ShipmentService service, ShipmentModelMapper modelMapper, AuthenticationHelper authenticationHelper) {
        this.service = service;
        this.modelMapper = modelMapper;
        this.authenticationHelper = authenticationHelper;
    }

    @GetMapping
    public List<Shipment> getAll(@RequestHeader HttpHeaders headers) {
        User user = authenticationHelper.tryGetUser(headers);
        return service.getAll(user);
    }

    @GetMapping("/{id}")
    public Shipment getById(@PathVariable int id) {
        try {
            return service.getById(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/onTheWay")
    public List<Shipment> shipmentsOnTheWay(@RequestHeader HttpHeaders headers) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            return service.getShipmentsOnTheWay(user);
        } catch (UnauthorizedOperationException e) {
            throw  new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PostMapping
    public Shipment create(@RequestHeader HttpHeaders headers, @Valid @RequestBody ShipmentDto shipmentDto) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            Shipment shipment = modelMapper.fromDto(shipmentDto);
            service.create(user, shipment);
            return shipment;
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw  new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public Shipment update(@RequestHeader HttpHeaders headers, @PathVariable int id, @Valid @RequestBody ShipmentDto shipmentDto) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            Shipment shipment = modelMapper.fromDto(shipmentDto, id);
            service.update(user, shipment);
            return shipment;
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw  new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
    @GetMapping("/filter")
    public List<Shipment> filter(@RequestHeader HttpHeaders headers, @RequestParam(required = false)Optional<Integer> originWarehouseId,
                                 @RequestParam(required = false) Optional<Integer> destinationWarehouseId) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            return service.filter(user, originWarehouseId, destinationWarehouseId);
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @GetMapping("/filter/user/{userId}")
    public List<Shipment> filterByCustomer(@RequestHeader HttpHeaders headers, @PathVariable int userId) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            return service.filterByCustomer(user, userId);
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PatchMapping("/{shipmentId}/parcels/{parcelId}")
    public void addParcelToShipment(@RequestHeader HttpHeaders headers, @PathVariable int shipmentId, @PathVariable int parcelId) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            service.addParcel(user, shipmentId, parcelId);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @DeleteMapping("/{shipmentId}/parcels/{parcelId}")
    public void removeParcelFromShipment(@RequestHeader HttpHeaders headers, @PathVariable int shipmentId, @PathVariable int parcelId) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            service.removeParcel(user, shipmentId, parcelId);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        try {
            service.delete(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
