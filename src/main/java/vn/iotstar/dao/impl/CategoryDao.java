package vn.iotstar.dao.impl;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

import vn.iotstar.dao.ICategoryDao;
import vn.iotstar.model.Category;
import vn.iotstar.util.JPAUtil;

/**
 * Cài đặt CRUD cho Category bằng JPA (Hibernate), thay thế bản JDBC thuần trước đây.
 */
public class CategoryDao implements ICategoryDao {

    @Override
    public void insert(Category category) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(category);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    @Override
    public void update(Category category) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(category);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    @Override
    public void delete(int cateid) throws Exception {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Category category = em.find(Category.class, cateid);
            if (category == null) {
                tx.rollback();
                throw new Exception("Khong tim thay danh muc");
            }
            em.remove(category);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public Category findById(int cateid) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(Category.class, cateid);
        } finally {
            em.close();
        }
    }

    @Override
    public Category findByCategoryname(String name) throws Exception {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Category> query = em.createQuery(
                    "SELECT c FROM Category c WHERE c.name = :name", Category.class);
            query.setParameter("name", name);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Category> findAll() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT c FROM Category c ORDER BY c.id DESC", Category.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Category> searchByName(String catname) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery(
                    "SELECT c FROM Category c WHERE c.name LIKE :kw ORDER BY c.id DESC", Category.class)
                    .setParameter("kw", "%" + catname + "%")
                    .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Category> findAll(int page, int pagesize) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT c FROM Category c ORDER BY c.id DESC", Category.class)
                    .setFirstResult(page * pagesize)
                    .setMaxResults(pagesize)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Category> search(String keyword, int page, int pagesize) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            boolean hasKeyword = keyword != null && !keyword.trim().isEmpty();
            String jpql = "SELECT c FROM Category c"
                    + (hasKeyword ? " WHERE c.name LIKE :kw" : "")
                    + " ORDER BY c.id DESC";
            TypedQuery<Category> query = em.createQuery(jpql, Category.class);
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
            return em.createQuery("SELECT COUNT(c) FROM Category c", Long.class)
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
            String jpql = "SELECT COUNT(c) FROM Category c" + (hasKeyword ? " WHERE c.name LIKE :kw" : "");
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
