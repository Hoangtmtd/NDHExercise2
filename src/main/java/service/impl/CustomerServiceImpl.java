package service.impl;

import dao.CustomerDao;
import dao.impl.CustomerDaoImpl;
import entity.Customer;
import service.CustomerService;

public class CustomerServiceImpl  implements CustomerService {

    CustomerDao customerDao = new CustomerDaoImpl();

    @Override
    public void insert(Customer customer) {
        customerDao.insert(customer);
    }

    @Override
    public Customer getById(Long id) {
        return customerDao.getById(id);
    }
}
