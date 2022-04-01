package dao.impl;

import dao.BaseDao;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import util.HibernateUtils;
import util.LogFactory;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class BaseDaoImpl<E, ID extends Serializable> implements BaseDao<E, ID> {

    private static final Logger LOGGER = LogFactory.getLogger();
    private final Class<E> classType;

    public BaseDaoImpl(Class<E> classType) {
        super();
        this.classType = classType;
    }

    @Override
    public boolean insert(E e) {
        try (Session session = HibernateUtils.getSession()) {
            session.beginTransaction();
            Serializable newId = session.save(e);
            session.getTransaction().commit();
            return newId != null;
        } catch (HibernateException ex) {
            LOGGER.error(ex.getMessage());
        }
        return false;
    }

    @Override
    public boolean update(E e) {
        try (Session session = HibernateUtils.getSession()) {
            session.beginTransaction();
            session.update(e);
            session.getTransaction().commit();
            return true;
        } catch (HibernateException ex) {
            LOGGER.error(ex.getMessage());
        }
        return false;
    }

    @Override
    public boolean deleteById(ID id) {
        try (Session session = HibernateUtils.getSession()) {
            session.beginTransaction();
            E e = session.get(classType, id);
            if (Objects.nonNull(e)) {
                session.delete(e);
                session.getTransaction().commit();
                return true;
            }
        } catch (HibernateException ex) {
            LOGGER.error(ex.getMessage());
        }
        return false;
    }

    @Override
    public List<E> getAll() {
        try (Session session = HibernateUtils.getSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<E> cQuery = criteriaBuilder.createQuery(classType);
            Root<E> root = cQuery.from(classType);
            cQuery.select(root);
            return session.createQuery(cQuery).getResultList();
        } catch (HibernateException ex) {
            LOGGER.error(ex.getMessage());
        }
        return Collections.emptyList();
    }

    @Override
    public E getById(ID id) {
        try (Session session = HibernateUtils.getSession()) {
            return session.get(classType, id);
        } catch (HibernateException ex) {
            LOGGER.error(ex.getMessage());
        }
        return null;
    }

}
