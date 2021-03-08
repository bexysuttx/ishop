package ishop.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ishop.entity.Account;
import ishop.entity.Category;
import ishop.entity.Order;
import ishop.entity.OrderItem;
import ishop.entity.Producer;
import ishop.entity.Product;

public class ResultSetHandlerFactory {

	public final static ResultSetHandler<Product> PRODUCT_RESULT_SET_HANDLER = new ResultSetHandler<Product>() {
		@Override
		public Product handle(ResultSet rs) throws SQLException {
			Product p = new Product();
			p.setId(rs.getInt("id"));
			p.setCategory(rs.getString("category"));
			p.setDesc(rs.getString("desc"));
			p.setImageLink(rs.getString("image_link"));
			p.setName(rs.getString("name"));
			p.setPrice(rs.getBigDecimal("price"));
			p.setProducer(rs.getString("producer"));
			return p;
		}
	};

	public final static ResultSetHandler<Category> CATEGORY_RESULT_SET_HANDLER = new ResultSetHandler<Category>() {
		@Override
		public Category handle(ResultSet rs) throws SQLException {
			Category category = new Category();
			category.setId(rs.getInt("id"));
			category.setName(rs.getString("name"));
			category.setUrl(rs.getString("url"));
			category.setProductCount(rs.getInt("product_count"));
			return category;
		}
	};

	public static final ResultSetHandler<Producer> PRODUCER_RESULT_SET_HANDLER = new ResultSetHandler<Producer>() {
		@Override
		public Producer handle(ResultSet rs) throws SQLException {
			Producer pr = new Producer();
			pr.setId(rs.getInt("id"));
			pr.setName(rs.getString("name"));
			pr.setProductCount(rs.getInt("product_count"));
			return pr;
		}
	};

	public final static ResultSetHandler<Account> ACCOUNT_RESULT_SET_HANDLER = new ResultSetHandler<Account>() {
		@Override
		public Account handle(ResultSet rs) throws SQLException {
			Account a = new Account();
			a.setId(rs.getInt("id"));
			a.setEmail(rs.getString("email"));
			a.setName(rs.getString("name"));
			return a;
		}
	};

	public static final ResultSetHandler<OrderItem> ORDER_ITEM_RESULT_SET_HANDLER = new ResultSetHandler<OrderItem>() {

		@Override
		public OrderItem handle(ResultSet rs) throws SQLException {
			OrderItem orderItem = new OrderItem();
			orderItem.setId(rs.getLong("id"));
			orderItem.setIdOrder(rs.getLong("id_order"));
			orderItem.setCount(rs.getInt("count"));
			Product p = PRODUCT_RESULT_SET_HANDLER.handle(rs);
			orderItem.setProduct(p);
			return orderItem;
		}
	};

	public static final ResultSetHandler<Order> ORDER_RESULT_SET_HANDLER = new ResultSetHandler<Order>() {

		@Override
		public Order handle(ResultSet rs) throws SQLException {
			Order o = new Order();
			o.setId(rs.getLong("id"));
			o.setIdAccount(rs.getInt("id_account"));
			o.setCreated(rs.getTimestamp("created"));
			return o;
		}
	};

	public static final ResultSetHandler<Integer> getCountResultSetHandler() {
		return new ResultSetHandler<Integer>() {

			@Override
			public Integer handle(ResultSet rs) throws SQLException {
				if (rs.next()) {
					return rs.getInt(1);
				} else {
					return 0;
				}
			}
		};
	}

	public final static <T> ResultSetHandler<List<T>> getListResultSetHandler(
			final ResultSetHandler<T> oneRowResultSetHandler) {
		return new ResultSetHandler<List<T>>() {

			public List<T> handle(ResultSet rs) throws SQLException {
				List<T> list = new ArrayList<T>();
				while (rs.next()) {
					list.add(oneRowResultSetHandler.handle(rs));
				}
				return list;
			}
		};
	}

	public final static <T> ResultSetHandler<T> getSingleResultSetHandler(
			final ResultSetHandler<T> oneRowResultSetHandler) {
		return new ResultSetHandler<T>() {

			@Override
			public T handle(ResultSet rs) throws SQLException {
				if (rs.next()) {
					return oneRowResultSetHandler.handle(rs);
				} else {
					return null;
				}
			}

		};
	}
}
