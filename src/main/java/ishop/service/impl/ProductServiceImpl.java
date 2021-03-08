package ishop.service.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ishop.entity.Category;
import ishop.entity.Producer;
import ishop.entity.Product;
import ishop.exception.InternalServerErrorException;
import ishop.form.SearchForm;
import ishop.jdbc.JDBCUtils;
import ishop.jdbc.ResultSetHandler;
import ishop.jdbc.ResultSetHandlerFactory;
import ishop.jdbc.SearchQuery;
import ishop.service.ProductService;

class ProductServiceImpl implements ProductService {
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductServiceImpl.class);
	private final DataSource dataSource;

	public ProductServiceImpl(DataSource dataSource) {
		super();
		this.dataSource = dataSource;
	}

	private static final ResultSetHandler<List<Product>> productsResultSetHandler = ResultSetHandlerFactory
			.getListResultSetHandler(ResultSetHandlerFactory.PRODUCT_RESULT_SET_HANDLER);

	private final ResultSetHandler<List<Category>> categoryListResultHandler = ResultSetHandlerFactory
			.getListResultSetHandler(ResultSetHandlerFactory.CATEGORY_RESULT_SET_HANDLER);

	private final ResultSetHandler<List<Producer>> producerListResultHandler = ResultSetHandlerFactory
			.getListResultSetHandler(ResultSetHandlerFactory.PRODUCER_RESULT_SET_HANDLER);

	private final ResultSetHandler<Integer> countResultSetHandler = ResultSetHandlerFactory.getCountResultSetHandler();

	@Override
	public List<Product> listAllProducts(int page, int limit) {
		try (Connection c = dataSource.getConnection()) {
			int offset = (page - 1) * limit;
			return JDBCUtils.select(c,
					"select p.*, c.name as category, pr.name as producer from products p, producer pr, category c "
							+ "where c.id=p.id_category and pr.id=p.id_producer limit ? offset ?",
					productsResultSetHandler, limit, offset);
		} catch (SQLException e) {
			throw new InternalServerErrorException("Can't execute sql query: " + e.getMessage(), e);
		}
	}

	@Override
	public List<Product> listProductsByCategory(String categoryUrl, int page, int limit) {
		try (Connection c = dataSource.getConnection()) {
			int offset = (page - 1) * limit;
			return JDBCUtils.select(c,
					"select p.*, c.name as category, pr.name as producer from products p, category c, producer pr where c.url=? and pr.id=p.id_producer and c.id=p.id_category order by p.id limit ? offset ?",
					productsResultSetHandler, categoryUrl, limit, offset);
		} catch (SQLException e) {
			throw new InternalServerErrorException("Can't execute sql query: " + e.getMessage(), e);
		}
	}

	@Override
	public List<Category> listAllCategories() {
		try (Connection c = dataSource.getConnection()) {
			return JDBCUtils.select(c, "select * from category order by name", categoryListResultHandler);
		} catch (SQLException e) {
			throw new InternalServerErrorException("Can't execute sql query: " + e.getMessage(), e);
		}
	}

	@Override
	public List<Producer> listAllProducers() {
		try (Connection c = dataSource.getConnection()) {
			return JDBCUtils.select(c, "select * from producer order by name", producerListResultHandler);
		} catch (SQLException e) {
			throw new InternalServerErrorException("Can't execute sql query: " + e.getMessage(), e);
		}
	}

	@Override
	public int countAllProducts() {
		try (Connection c = dataSource.getConnection()) {
			return JDBCUtils.select(c, "select count(*) from products", countResultSetHandler);
		} catch (SQLException e) {
			throw new InternalServerErrorException("Can't execute sql query: " + e.getMessage(), e);
		}
	}

	@Override
	public int countProductsByCategory(String categoryUrl) {
		try (Connection c = dataSource.getConnection()) {
			return JDBCUtils.select(c,
					"select count(*) from products p, category c where c.id=p.id_category and c.url = ?",
					countResultSetHandler, categoryUrl);
		} catch (SQLException e) {
			throw new InternalServerErrorException("Can't execute sql query: " + e.getMessage(), e);
		}
	}

	@Override
	public List<Product> listProductsBySearchForm(SearchForm searchForm, int page, int limit) {
		try (Connection c = dataSource.getConnection()) {
			int offset = (page - 1) * limit;
			SearchQuery sq = buildSearchQuery("p.*, c.name as category, pr.name as producer", searchForm);
			sq.getSql().append(" order by p.id limit ? offset ?");
			sq.getParams().add(limit);
			sq.getParams().add(offset);
			LOGGER.debug("search query={} with params={}", sq.getSql(), sq.getParams());
			return JDBCUtils.select(c, sq.getSql().toString(), productsResultSetHandler, sq.getParams().toArray());
		} catch (SQLException e) {
			throw new InternalServerErrorException("Can't execute sql query: " + e.getMessage(), e);
		}
	}

	private SearchQuery buildSearchQuery(String string, SearchForm searchForm) {
		List<Object> params = new ArrayList<Object>();
		StringBuilder sql = new StringBuilder("select ");
		sql.append(string).append(
				" from products p, producer pr, category c where pr.id=p.id_producer and c.id=p.id_category and (p.name ilike ? or p.desc ilike ?)");
		params.add("%" + searchForm.getQuery() + "%");
		params.add("%" + searchForm.getQuery() + "%");
		JDBCUtils.populateSqlAndParams(sql, params, searchForm.getCategories(), "c.id=?");
		JDBCUtils.populateSqlAndParams(sql, params, searchForm.getProducers(), "pr.id=?");
		return new SearchQuery(sql, params);
	}

	@Override
	public int countProductsBySearchForm(SearchForm searchForm) {
		try (Connection c = dataSource.getConnection()) {
			SearchQuery sq = buildSearchQuery("count(*)", searchForm);
			LOGGER.debug("search query={} with params={}", sq.getSql(), sq.getParams());
			return JDBCUtils.select(c, sq.getSql().toString(), countResultSetHandler, sq.getParams().toArray());
		} catch (SQLException e) {
			throw new InternalServerErrorException("Can't execute sql query: " + e.getMessage(), e);
		}
	}

}
