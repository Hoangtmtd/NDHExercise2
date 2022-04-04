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
        try (Session session = HibernateUtils.getSession()) {
            Query<Vehicle> query = session.createQuery("FROM Vehicle WHERE rental_id = null");
            return query.getResultList();
        } catch (PersistenceException e) {
            LOGGER.error(e.getLocalizedMessage());
        }
        return null;
    }


}
