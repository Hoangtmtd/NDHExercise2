package service;

import entity.Vehicle;

import java.util.List;

public interface VehicleService {
    boolean insert(Vehicle vehicle);
    Vehicle getById(Long id);
    boolean deleteById(Long id);
    List<Vehicle> getAllVehicle();
    List<Vehicle> findAllAvailableVehicle();
    boolean returnVehicle(Long vehicleId);
}
