package vn.iotstar.dao;

import java.util.List;
import vn.iotstar.model.Product;

public interface IProductDao {
    void insert(Product product);
    void update(Product product);
    void delete(int id) throws Exception;
    Product findById(int id);
    List<Product> findAll();
    List<Product> findLatest(int limit);
    List<Product> findAll(int page, int pagesize);
    List<Product> search(String keyword, int page, int pagesize);
    int count();
    int countByKeyword(String keyword);
}
