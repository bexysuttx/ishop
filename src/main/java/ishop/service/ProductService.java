package ishop.service;

import java.util.List;

import ishop.entity.Category;
import ishop.entity.Producer;
import ishop.entity.Product;
import ishop.form.SearchForm;

public interface ProductService {

	List<Product> listAllProducts(int page, int limit);
	
	int countAllProducts();

	List<Product> listProductsByCategory(String categoryUrl, int page, int limit);
	
	int countProductsByCategory(String categoryUrl);

	List<Category> listAllCategories();

	List<Producer> listAllProducers();
	
	List<Product> listProductsBySearchForm(SearchForm searchForm, int page, int limit);
	
	int countProductsBySearchForm(SearchForm searchForm);
}
