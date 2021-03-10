package ishop.service.impl;

import java.io.IOException;
import java.io.InputStream;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ishop.entity.Account;
import ishop.entity.Order;
import ishop.entity.OrderItem;
import ishop.entity.Product;
import ishop.exception.AccessDeniedException;
import ishop.exception.InternalServerErrorException;
import ishop.exception.ResourceNotFoundException;
import ishop.form.ProductForm;
import ishop.jdbc.JDBCUtils;
import ishop.jdbc.ResultSetHandler;
import ishop.jdbc.ResultSetHandlerFactory;
import ishop.model.CurrentAccount;
import ishop.model.ShoppingCart;
import ishop.model.ShoppingCartItem;
import ishop.model.SocialAccount;
import ishop.service.OrderService;

class OrderServiceImpl implements OrderService {
	private static final Logger LOGGER = LoggerFactory.getLogger(OrderServiceImpl.class);
	public static final ResultSetHandler<Product> productResultSetHandler = ResultSetHandlerFactory
			.getSingleResultSetHandler(ResultSetHandlerFactory.PRODUCT_RESULT_SET_HANDLER);

	public static final ResultSetHandler<Account> accountResultSetHandler = ResultSetHandlerFactory
			.getSingleResultSetHandler(ResultSetHandlerFactory.ACCOUNT_RESULT_SET_HANDLER);

	private final ResultSetHandler<Order> orderResultSetHandler = ResultSetHandlerFactory
			.getSingleResultSetHandler(ResultSetHandlerFactory.ORDER_RESULT_SET_HANDLER);

	private final ResultSetHandler<List<OrderItem>> orderItemListResultSetHandler = ResultSetHandlerFactory
			.getListResultSetHandler(ResultSetHandlerFactory.ORDER_ITEM_RESULT_SET_HANDLER);

	private final ResultSetHandler<List<Order>> ordersResultSetHandler = ResultSetHandlerFactory
			.getListResultSetHandler(ResultSetHandlerFactory.ORDER_RESULT_SET_HANDLER);

	private final ResultSetHandler<Integer> countResultSetHandler = ResultSetHandlerFactory.getCountResultSetHandler();

	private BasicDataSource dataSource;
	private final String rootDir;
	private String smtpServer;
	private String smtpPort;
	private String smtpUsername;
	private String smtpPassword;
	private String host;
	private String fromAddress;

	public OrderServiceImpl(BasicDataSource basicDataSource, ServiceManager serviceManager) {
		super();
		this.dataSource = basicDataSource;
		this.rootDir = normalizeMediaDirPath(serviceManager.applicationContext.getRealPath("/")) + "/media/avatar";
		this.smtpServer = serviceManager.getApplicationProperties("email.smtp.server");
		this.smtpPort = serviceManager.getApplicationProperties("email.smtp.port");
		this.smtpUsername = serviceManager.getApplicationProperties("email.smtp.username");
		this.smtpPassword = serviceManager.getApplicationProperties("email.smtp.password");
		this.host = serviceManager.getApplicationProperties("app.host");
		this.fromAddress = serviceManager.getApplicationProperties("email.smtp.fromAddress");
	}

	private String normalizeMediaDirPath(String realPath) {
		realPath = realPath.replace("\\", "/");
		if (realPath.endsWith("/")) {
			realPath.substring(0, realPath.length() - 1);
		}
		return realPath;

	}

	@Override
	public void addProductToShoppingCart(ProductForm productForm, ShoppingCart shoppingCart) {
		try (Connection c = dataSource.getConnection()) {
			Product product = JDBCUtils.select(c,
					"select p.*,c.name as category, pr.name as producer from products p, category c, producer pr where c.id=p.id_category and pr.id=p.id_producer and p.id=?",
					productResultSetHandler, productForm.getProductId());
			if (product == null) {
				throw new InternalServerErrorException("Product not found by id: " + productForm.getProductId());
			}
			shoppingCart.addProduct(product, productForm.getCount());
		} catch (SQLException e) {
			throw new InternalServerErrorException("Can't execute sql query: " + e.getMessage(), e);
		}
	}

	@Override
	public String serializeShoppingCart(ShoppingCart shoppingCart) {
		StringBuilder s = new StringBuilder();
		for (ShoppingCartItem item : shoppingCart.getItems()) {
			s.append(item.getProduct().getId()).append("-").append(item.getCount()).append("|");
		}
		if (s.length() > 0) {
			s.deleteCharAt(s.length() - 1);
		}
		return s.toString();
	}

	@Override
	public void removeProductFromShoppingCart(ProductForm productForm, ShoppingCart shoppingCart) {
		shoppingCart.removeProduct(productForm.getProductId(), productForm.getCount());

	}

	@Override
	public ShoppingCart deserializeShoppingCart(String string) {
		ShoppingCart shoppingCart = new ShoppingCart();
		String[] items = string.split("\\|");
		for (String item : items) {
			try {
				String data[] = item.split("-");
				int idProduct = Integer.parseInt(data[0]);
				int count = Integer.parseInt(data[1]);
				addProductToShoppingCart(new ProductForm(idProduct, count), shoppingCart);
			} catch (RuntimeException e) {
				LOGGER.error("Can't add product to shopping cart during deserialization: item=" + item, e);
			}
		}
		return shoppingCart.getItems().isEmpty() ? null : shoppingCart;
	}

	@Override
	public CurrentAccount authentificate(SocialAccount socialAccount) {
		try (Connection c = dataSource.getConnection()) {
			Account account = JDBCUtils.select(c, "select * from account where email=?", accountResultSetHandler,
					socialAccount.getEmail());
			if (account == null) {
				String uid = UUID.randomUUID().toString() + ".jpg";
				Path filePathToSave = Paths.get(rootDir + "/" + uid);
				downloadAvatar(socialAccount.getAvatarUrl(), filePathToSave);
				account = JDBCUtils.insert(c, "insert into account values (nextval('account_seq'),?,?,?)",
						accountResultSetHandler, socialAccount.getName(), socialAccount.getEmail(),
						"/media/avatar/" + uid);
				c.commit();
			}
			return account;
		} catch (SQLException e) {
			throw new InternalServerErrorException("Can't execute SQL request: " + e.getMessage(), e);
		} catch (IOException e) {
			throw new InternalServerErrorException("Can't process avatar link: " + e.getMessage(), e);
		}
	}

	protected void downloadAvatar(String avatarUrl, Path filePathToSave) throws IOException {
		try (InputStream in = new URL(avatarUrl).openStream()) {
			Files.copy(in, filePathToSave);
		}
	}

	@Override
	public long makeOrder(ShoppingCart shoppingCart, CurrentAccount currentAccount) {
		if (shoppingCart == null || shoppingCart.getItems().isEmpty()) {
			throw new InternalServerErrorException("shoppingCart is null or empty");
		}
		try (Connection c = dataSource.getConnection()) {
			Order order = JDBCUtils.insert(c, "insert into \"order\" values(nextval('order_seq'),?,?)",
					orderResultSetHandler, currentAccount.getId(), new Timestamp(System.currentTimeMillis()));
			JDBCUtils.insertBatch(c, "insert into order_item values(nextval('order_item_seq'), ?,?,?)",
					toOrderItemParameterList(order.getId(), shoppingCart.getItems()));
			c.commit();
			sendEmail(currentAccount.getEmail(), order);
			return order.getId();
		} catch (SQLException e) {
			throw new InternalServerErrorException("Can't execute SQL request: " + e.getMessage(), e);

		}
	}

	private void sendEmail(String emailAddress, Order order) {
		SimpleEmail email = new SimpleEmail();
		try {
			email.setCharset("utf-8");
			email.setHostName(smtpServer);
			email.setSSLOnConnect(true);
			email.setSslSmtpPort(smtpPort);
			email.setAuthenticator(new DefaultAuthenticator(smtpUsername, smtpPassword));
			email.setSubject("New order");
			email.setMsg(host + "/order?id=" + order.getId());
			email.addTo(emailAddress);
			email.setFrom(fromAddress);
			email.send();
		} catch (EmailException e) {
			LOGGER.error("Error during send email: " + e.getMessage(), e);
		}
	}

	private List<Object[]> toOrderItemParameterList(long id, Collection<ShoppingCartItem> items) {
		List<Object[]> parametersList = new ArrayList<>();
		for (ShoppingCartItem item : items) {
			parametersList.add(new Object[] { id, item.getProduct().getId(), item.getCount() });
		}
		return parametersList;
	}

	@Override
	public Order findOrderById(long orderId, CurrentAccount currentAccount) {
		try (Connection c = dataSource.getConnection()) {
			Order order = JDBCUtils.select(c, "select * from \"order\" where id=?", orderResultSetHandler, orderId);
			if (order == null) {
				throw new ResourceNotFoundException("Order not found by id: " + orderId);
			}
			if (!order.getIdAccount().equals(currentAccount.getId())) {
				throw new AccessDeniedException(
						"Account with id " + currentAccount.getId() + "is not owner for order with id=" + orderId);
			}
			List<OrderItem> list = JDBCUtils.select(c,
					"select o.id as oid, o.id_order as id_order, o.id_product, o.count, p.*, c.name as category ,pr.name as producer from order_item o, products p,category c, producer pr where pr.id = p.id_producer and  c.id = p.id_category and o.id_product=p.id and o.id_order=?",
					orderItemListResultSetHandler, orderId);
			order.setItems(list);
			return order;

		} catch (SQLException e) {
			throw new InternalServerErrorException("Can't execute SQL request: " + e.getMessage(), e);
		}
	}

	@Override
	public List<Order> listMyOrders(CurrentAccount currentAccount, int page, int limit) {
		int offset = (page - 1) * limit;
		try (Connection c = dataSource.getConnection()) {
			List<Order> orders = JDBCUtils.select(c,
					"select * from \"order\" where id_account=? order by id desc limit ? offset ?",
					ordersResultSetHandler, currentAccount.getId(), limit, offset);
			return orders;
		} catch (SQLException e) {
			throw new InternalServerErrorException("Can't execute SQL request: " + e.getMessage(), e);
		}
	}

	@Override
	public int countMyOrders(CurrentAccount currentAccount) {
		try (Connection c = dataSource.getConnection()) {
			return JDBCUtils.select(c, "select count(*) from \"order\" where id_account=?", countResultSetHandler,
					currentAccount.getId());
		} catch (SQLException e) {
			throw new InternalServerErrorException("Can't execute SQL request: " + e.getMessage(), e);
		}
	}

}
