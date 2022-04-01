package dao.impl;

import dao.RentalDao;
import entity.Customer;
import entity.Rental;
import entity.Vehicle;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.query.Query;
import util.HibernateUtils;
import util.LogFactory;

import javax.persistence.PersistenceException;

public class RentalDaoImpl extends BaseDaoImpl<Rental, Long> implements RentalDao {
    private final Logger LOGGER = LogFactory.getLogger();

    public RentalDaoImpl() {
        super(Rental.class);
    }

    @Override
    public Rental getRentalByVehicleId(Long vehicleId) {
        try(Session session = HibernateUtils.getSession()){
            Query<Rental> query = session.createQuery("FROM Rental WHERE vehicle_id = :vehicleId");
            query.setParameter("vehicleId", vehicleId);
            return query.getSingleResult();
        } catch (PersistenceException e){
            LOGGER.error(e.getLocalizedMessage());
        }
        return null;
    }
}
