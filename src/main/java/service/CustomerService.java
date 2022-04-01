package service;

import entity.Customer;

public interface CustomerService {
    void insert(Customer customer);
    Customer getById(Long id);
}
