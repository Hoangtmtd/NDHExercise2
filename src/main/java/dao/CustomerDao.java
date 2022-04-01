package dao;

import entity.Customer;


public interface CustomerDao extends BaseDao<Customer, Long>{
    Customer getCustomerByRentalId(Long rentalId);
    boolean updateRental(Long customerId);
}
