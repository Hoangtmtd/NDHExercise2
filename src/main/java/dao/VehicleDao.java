package dao;

import entity.Vehicle;

import java.util.List;


public interface VehicleDao extends BaseDao<Vehicle, Long> {
    List<Vehicle> findAllAvailableVehicle();
}
