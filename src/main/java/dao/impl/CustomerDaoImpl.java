package dao.impl;

import dao.CustomerDao;
import entity.Customer;
import entity.Vehicle;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.query.Query;
import util.HibernateUtils;
import util.LogFactory;

import javax.persistence.PersistenceException;

public class CustomerDaoImpl extends BaseDaoImpl<Customer, Long> implements CustomerDao {
    private final Logger LOGGER = LogFactory.getLogger();

    public CustomerDaoImpl() {
        super(Customer.class);
    }

    @Override
    public Customer getCustomerByRentalId(Long rentalId) {
        try(Session session = HibernateUtils.getSession()){
            Query<Customer> query = session.createQuery("FROM Customer WHERE rental_id = :rentalId");
            query.setParameter("rentalId", rentalId);
            return query.getSingleResult();
        } catch (PersistenceException e){
            LOGGER.error(e.getLocalizedMessage());
        }
        return null;
    }

    @Override
    public boolean updateRental(Long customerId) {
        try(Session session = HibernateUtils.getSession()){
            Query<Customer> query = session.createQuery("UPDATE Customer SET rental_id = null WHERE id = :customerId");
            query.setParameter("customerId", customerId);
            return true;
        } catch (PersistenceException e){
            LOGGER.error(e.getLocalizedMessage());
        }
        return false;
    }
}

