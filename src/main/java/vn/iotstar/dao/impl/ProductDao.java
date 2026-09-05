package vn.iotstar.dao.impl;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

import vn.iotstar.dao.IProductDao;
import vn.iotstar.model.Product;
import vn.iotstar.util.JPAUtil;

public class ProductDao implements IProductDao {

    @Override
    public void insert(Product product) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(product);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    @Override
    public void update(Product product) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(product);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    @Override
    public void delete(int id) throws Exception {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Product product = em.find(Product.class, id);
            if (product == null) {
                tx.rollback();
                throw new Exception("Khong tim thay san pham");
            }
            em.remove(product);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public Product findById(int id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(Product.class, id);
        } finally {
            em.close();
        }
    }

    @Override
    public List<Product> findAll() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT p FROM Product p ORDER BY p.id DESC", Product.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Product> findLatest(int limit) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT p FROM Product p ORDER BY p.id DESC", Product.class)
                    .setMaxResults(limit)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Product> findAll(int page, int pagesize) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT p FROM Product p ORDER BY p.id DESC", Product.class)
                    .setFirstResult(page * pagesize)
                    .setMaxResults(pagesize)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Product> search(String keyword, int page, int pagesize) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            boolean hasKeyword = keyword != null && !keyword.trim().isEmpty();
            String jpql = "SELECT p FROM Product p"
                    + (hasKeyword ? " WHERE p.name LIKE :kw" : "")
                    + " ORDER BY p.id DESC";
            TypedQuery<Product> query = em.createQuery(jpql, Product.class);
            if (hasKeyword) {
                query.setParameter("kw", "%" + keyword.trim() + "%");
            }
            query.setFirstResult(page * pagesize);
            query.setMaxResults(pagesize);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public int count() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT COUNT(p) FROM Product p", Long.class)
                    .getSingleResult()
                    .intValue();
        } finally {
            em.close();
        }
    }

    @Override
    public int countByKeyword(String keyword) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            boolean hasKeyword = keyword != null && !keyword.trim().isEmpty();
            String jpql = "SELECT COUNT(p) FROM Product p" + (hasKeyword ? " WHERE p.name LIKE :kw" : "");
            TypedQuery<Long> query = em.createQuery(jpql, Long.class);
            if (hasKeyword) {
                query.setParameter("kw", "%" + keyword.trim() + "%");
            }
            return query.getSingleResult().intValue();
        } finally {
            em.close();
        }
    }
}
