package dao.impl;

import dao.VehicleDao;
import entity.Vehicle;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.query.Query;
import util.HibernateUtils;
import util.LogFactory;

import javax.persistence.PersistenceException;
import java.util.List;

public class VehicleDaoImpl extends BaseDaoImpl<Vehicle, Long> implements VehicleDao {
    private final Logger LOGGER = LogFactory.getLogger();

    public VehicleDaoImpl() {
        super(Vehicle.class);
    }

    @Override
    public List<Vehicle> findAllAvailableVehicle() {
        LOGGER.info("find all available vehicle dao");
        try(Session session = HibernateUtils.getSession()){
            Query<Vehicle> query = session.createQuery("FROM Vehicle WHERE rental_id = null");
            return query.getResultList();
        } catch (PersistenceException e){
            LOGGER.error(e.getLocalizedMessage());
        }
        return null;
    }

    @Override
    public Vehicle getVehicleByRentalId(Long rentalId) {
        try(Session session = HibernateUtils.getSession()){
            Query<Vehicle> query = session.createQuery("FROM Vehicle WHERE rental_id = :rental");
            query.setParameter("rental", rentalId);
            return query.getSingleResult();
        } catch (PersistenceException e){
            LOGGER.error(e.getLocalizedMessage());
        }
        return null;
    }

    @Override
    public boolean updateRental(Long vehicleId) {
        try(Session session = HibernateUtils.getSession()){
            Query<Vehicle> query = session.createQuery("UPDATE Vehicle SET rental_id = null WHERE id = :vehicleId");
            query.setParameter("vehicleId", vehicleId);
            return true;
        } catch (PersistenceException e){
            LOGGER.error(e.getLocalizedMessage());
        }
        return false;
    }

}
