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
import service.RentalService;

import java.util.List;


public class RentalServiceImpl implements RentalService {

    private final RentalDao rentalDao = new RentalDaoImpl();
    private final CustomerDao customerDao = new CustomerDaoImpl();
    private final VehicleDao vehicleDao = new VehicleDaoImpl();

    @Override
    public boolean insert(Rental rental) {
        if (!rentalDao.insert(rental)) {
            return false;
        }
        Customer customer = rental.getCustomer();
        customer.setRentals(List.of(rental));
        Vehicle vehicle = rental.getVehicle();
        vehicle.setRental(rental);
        vehicleDao.update(vehicle);
        customerDao.update(customer);
        return true;
    }

}
