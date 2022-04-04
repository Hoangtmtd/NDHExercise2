package dao.impl;

import dao.CustomerDao;
import entity.Customer;
import org.apache.logging.log4j.Logger;
import util.LogFactory;

public class CustomerDaoImpl extends BaseDaoImpl<Customer, Long> implements CustomerDao {
    private final Logger LOGGER = LogFactory.getLogger();

    public CustomerDaoImpl() {
        super(Customer.class);
    }


}

