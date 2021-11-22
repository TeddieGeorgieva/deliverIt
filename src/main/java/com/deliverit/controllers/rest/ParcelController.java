package com.deliverit.controllers.rest;

import com.deliverit.controllers.AuthenticationHelper;
import com.deliverit.exceptions.DuplicateEntityException;
import com.deliverit.exceptions.EntityNotFoundException;
import com.deliverit.exceptions.UnauthorizedOperationException;
import com.deliverit.models.Parcel;
import com.deliverit.models.ParcelDto;
import com.deliverit.models.User;
import com.deliverit.models.UserDto;
import com.deliverit.services.parcel.ParcelModelMapper;
import com.deliverit.services.parcel.ParcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/parcels")
public class ParcelController {

    private final ParcelService service;
    private final ParcelModelMapper modelMapper;
    private final AuthenticationHelper authenticationHelper;

    @Autowired
    public ParcelController(ParcelService service, ParcelModelMapper modelMapper, AuthenticationHelper authenticationHelper) {
        this.service = service;
        this.modelMapper = modelMapper;
        this.authenticationHelper = authenticationHelper;
    }

    @GetMapping
    public List<Parcel> getAll(@RequestHeader HttpHeaders headers) {
        User user = authenticationHelper.tryGetUser(headers);
        return service.getAll(user);
    }

    @GetMapping("/my")
    public List<Parcel> getAllForUser(@RequestHeader HttpHeaders headers) {
        User user = authenticationHelper.tryGetUser(headers);
        return service.getAllForUser(user);
    }

    @GetMapping("/{id}")
    public Parcel getById(@PathVariable int id) {
        try {
            return service.getById(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage()
            );
        }
    }

    @PostMapping
    public Parcel create(@RequestHeader HttpHeaders headers, @Valid @RequestBody ParcelDto parcelDto) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            Parcel parcel = modelMapper.fromDto(parcelDto);
            service.create(parcel, user);
            return parcel;
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw  new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public Parcel update(@RequestHeader HttpHeaders headers, @PathVariable int id, @Valid @RequestBody ParcelDto parcelDto) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            Parcel parcel = modelMapper.fromDto(parcelDto, id);
            service.update(parcel, user);
            return parcel;
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public void delete(@RequestHeader HttpHeaders headers, @PathVariable int id) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            service.delete(id, user);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/filter")
    public List<Parcel> filter(@RequestHeader HttpHeaders headers, @RequestParam(required = false) Optional<Double> weight,
                               @RequestParam(required = false) Optional<Integer> customerId,
                               @RequestParam(required = false) Optional<Integer> warehouseId,
                               @RequestParam(required = false) Optional<Integer> categoryId,
                               @RequestParam(required = false) Optional<String> sort) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            return service.filter(user, weight, customerId, warehouseId, categoryId, sort);
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @GetMapping("/{id}/status")
    public String checkStatus(@RequestHeader HttpHeaders headers, @PathVariable int id) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            return service.checkShipmentStatus(user, id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

}
