package service.impl;

import dao.CustomerDao;
import dao.RentalDao;
import dao.VehicleDao;
import dao.impl.CustomerDaoImpl;
import dao.impl.RentalDaoImpl;
import dao.impl.VehicleDaoImpl;
import entity.Customer;
import entity.Rental;
import entity.Vehicle;
import service.VehicleService;

import java.util.*;

public class VehicleServiceImpl implements VehicleService {

    private final VehicleDao vehicleDao = new VehicleDaoImpl();
    private final RentalDao rentalDao = new RentalDaoImpl();
    private final CustomerDao customerDao = new CustomerDaoImpl();

    @Override
    public boolean insert(Vehicle vehicle) {
        if (Objects.isNull(vehicle)) {
            throw new NullPointerException("This vehicle is null");
        }
        return vehicleDao.insert(vehicle);
    }

    @Override
    public Vehicle getById(Long id) {
        return vehicleDao.getById(id);
    }

    @Override
    public boolean deleteById(Long id) {

        return vehicleDao.deleteById(id);
    }

    @Override
    public List<Vehicle> getAllVehicle() {

        return vehicleDao.getAll();
    }

    @Override
    public List<Vehicle> findAllAvailableVehicle() {
        return vehicleDao.findAllAvailableVehicle();
    }

    @Override
    public boolean returnVehicle(Long vehicleId) {
        Vehicle vehicle = vehicleDao.getById(vehicleId);
        vehicle.setRental(null);
        Rental rental = rentalDao.getRentalByVehicleId(vehicleId);
        Customer customer = rental.getCustomer();
        customer.setRentals(Collections.emptyList());
//        customerDao.updateRental(customer.getId());
        customerDao.update(customer);
        vehicleDao.update(vehicle);
        if(!rentalDao.deleteById(rental.getId())){
            return false;
        }
        return true;
    }
}
