package ishop.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Properties;

import javax.servlet.ServletContext;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ishop.service.OrderService;
import ishop.service.ProductService;
import ishop.service.SocialService;

public class ServiceManager {
	private static final Logger LOGGER = LoggerFactory.getLogger(ServiceManager.class);

	public ProductService getProductService() {
		return productService;
	}

	public OrderService getOrderService() {
		return orderService;
	}

	public SocialService getSocialService() {
		return socialService;
	}

	public static ServiceManager getInstance(ServletContext context) {
		ServiceManager instance = (ServiceManager) context.getAttribute("SERVICE_MANAGER");
		if (instance == null) {
			instance = new ServiceManager(context);
			context.setAttribute("SERVICE_MANAGER", instance);
		}
		return instance;
	}

	public String getApplicationProperties(String key) {
		return applicationProperties.getProperty(key);
	}

	public void close() {
		try {
			dataSource.close();
		} catch (SQLException e) {
			LOGGER.error("Close datasource failed: " + e.getMessage(), e);
		}
	}

	private final Properties applicationProperties = new Properties();
	private final BasicDataSource dataSource;
	private final ProductService productService;
	private final OrderService orderService;
	private final SocialService socialService;
	final ServletContext applicationContext;

	private ServiceManager(ServletContext context) {
		this.applicationContext = context;
		loadApplicationProperties();
		dataSource = createDataSource();
		productService = new ProductServiceImpl(dataSource);
		orderService = new OrderServiceImpl(dataSource, this);
		socialService = new FacebookSocialService(this);
	}

	public BasicDataSource createDataSource() {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDefaultAutoCommit(false);
		dataSource.setRollbackOnReturn(true);
		dataSource.setDriverClassName(getApplicationProperties("db.driver"));
		dataSource.setUrl(getApplicationProperties("db.url"));
		dataSource.setUsername(getApplicationProperties("db.username"));
		dataSource.setPassword(getApplicationProperties("db.password"));
		dataSource.setInitialSize(Integer.parseInt(getApplicationProperties("db.pool.initSize")));
		dataSource.setMaxTotal(Integer.parseInt(getApplicationProperties("db.pool.maxSize")));

		return dataSource;
	}

	private void loadApplicationProperties() {
		try (InputStream in = ServiceManager.class.getClassLoader().getResourceAsStream("application.properties")) {
			applicationProperties.load(in);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
