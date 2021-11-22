package com.deliverit.controllers.rest;

import com.deliverit.controllers.AuthenticationHelper;
import com.deliverit.exceptions.DuplicateEntityException;
import com.deliverit.exceptions.EntityNotFoundException;
import com.deliverit.exceptions.UnauthorizedOperationException;
import com.deliverit.models.*;
import com.deliverit.services.warehouse.WarehouseModelMapper;
import com.deliverit.services.warehouse.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/warehouses")
public class WarehouseController {
    private final WarehouseService service;
    private final WarehouseModelMapper modelMapper;
    private final AuthenticationHelper authenticationHelper;

    @Autowired
    public WarehouseController(WarehouseService service, WarehouseModelMapper modelMapper, AuthenticationHelper authenticationHelper) {
        this.service = service;
        this.modelMapper = modelMapper;
        this.authenticationHelper = authenticationHelper;
    }
    @GetMapping
    public List<Warehouse> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Warehouse getWarehouseById(@PathVariable int id) {
        try {
            return service.getById(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/{id}/nextArriving")
    public Shipment getNextArrivingShipment(@PathVariable int id, @RequestHeader HttpHeaders headers) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            return service.getNextArrivingShipment(id, user);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw  new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PostMapping
    public Warehouse create(@RequestHeader HttpHeaders headers, @Valid @RequestBody WarehouseDto warehouseDto) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            Warehouse warehouse = modelMapper.fromDto(warehouseDto);
            service.create(user, warehouse);
            return warehouse;
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw  new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public Warehouse update(@RequestHeader HttpHeaders headers, @PathVariable int id, @Valid @RequestBody WarehouseDto warehouseDto) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            Warehouse warehouse = modelMapper.fromDto(warehouseDto, id);
            service.update(warehouse, user);
            return warehouse;
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw  new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public void deleteWarehouse(@RequestHeader HttpHeaders headers, @PathVariable int id) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            service.delete(id, user);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
