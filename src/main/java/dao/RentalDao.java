package dao;

import entity.Customer;
import entity.Rental;

public interface RentalDao extends BaseDao<Rental, Long> {
    Rental getRentalByVehicleId(Long vehicleId);
}
