package com.deliverit.services.warehouse;

import com.deliverit.exceptions.DuplicateEntityException;
import com.deliverit.exceptions.EntityNotFoundException;
import com.deliverit.exceptions.UnauthorizedOperationException;
import com.deliverit.models.Shipment;
import com.deliverit.models.User;
import com.deliverit.models.Warehouse;
import com.deliverit.models.WarehouseDto;
import com.deliverit.repositories.warehouse.WarehouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WarehouseServiceImpl implements WarehouseService {
    public static final String LIST_ERROR_MESSAGE = "Only employees can list warehouses.";
    public static final String CREATE_ERROR_MESSAGE = "Only employees can create warehouses.";
    public static final String UPDATE_ERROR_MESSAGE = "Only employees can update warehouses.";
    public static final String SHIPMENTS_ERROR_MESSAGE = "Only employees can see next arriving shipments.";

    private final WarehouseRepository repository;

    @Autowired
    public WarehouseServiceImpl(WarehouseRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Warehouse> getAll() {
        return repository.getAll();
    }

    @Override
    public Warehouse getById(int id) {
        return repository.getById(id);
    }

    @Override
    public Shipment getNextArrivingShipment(int id, User user) {
        throwIfNotEmployee(user, SHIPMENTS_ERROR_MESSAGE);
        return repository.getNextArriving(id);
    }

    @Override
    public void create(User user, Warehouse warehouse) {
        throwIfNotEmployee(user, CREATE_ERROR_MESSAGE);
        boolean duplicateExists = true;
        try {
            repository.getByAddress(warehouse.getAddress());
        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }
        if (duplicateExists) {
            throw new DuplicateEntityException("Warehouse", "address", warehouse.getAddress().toString());
        }

        repository.create(warehouse);
    }

    @Override
    public void update(Warehouse warehouse, User user) {
        throwIfNotEmployee(user, UPDATE_ERROR_MESSAGE);
        boolean duplicateExists = true;
        try {
            repository.getByAddress(warehouse.getAddress());
        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }

        if (duplicateExists) {
            throw new DuplicateEntityException("Warehouse", "address", warehouse.getAddress().toString());
        }

        repository.update(warehouse);
    }

    @Override
    public void delete(int id, User user) {
        throwIfNotEmployee(user, UPDATE_ERROR_MESSAGE);
        repository.delete(id);
    }

    private void throwIfNotEmployee(User user, String errorMessage) {
        if (!user.isEmployee()) {
            throw new UnauthorizedOperationException(errorMessage);
        }
    }
}
