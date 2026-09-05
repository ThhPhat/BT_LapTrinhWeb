package vn.iotstar.service.impl;

import java.util.List;

import vn.iotstar.dao.IProductDao;
import vn.iotstar.dao.impl.ProductDao;
import vn.iotstar.model.Product;
import vn.iotstar.service.IProductService;

public class ProductServiceImpl implements IProductService {

    private final IProductDao productDao = new ProductDao();

    @Override
    public void insert(Product product) {
        productDao.insert(product);
    }

    @Override
    public void update(Product product) {
        productDao.update(product);
    }

    @Override
    public void delete(int id) throws Exception {
        productDao.delete(id);
    }

    @Override
    public Product findById(int id) {
        return productDao.findById(id);
    }

    @Override
    public List<Product> findAll() {
        return productDao.findAll();
    }

    @Override
    public List<Product> findLatest(int limit) {
        return productDao.findLatest(limit);
    }

    @Override
    public List<Product> findAll(int page, int pagesize) {
        return productDao.findAll(page, pagesize);
    }

    @Override
    public List<Product> search(String keyword, int page, int pagesize) {
        return productDao.search(keyword, page, pagesize);
    }

    @Override
    public int count() {
        return productDao.count();
    }

    @Override
    public int countByKeyword(String keyword) {
        return productDao.countByKeyword(keyword);
    }
    @Override
    public List<Product> findByCategory(int categoryId, int page, int pagesize) {
        return productDao.findByCategory(categoryId, page, pagesize);
    }
    @Override
    public int countByCategory(int categoryId) {
        return productDao.countByCategory(categoryId);
    }
}
