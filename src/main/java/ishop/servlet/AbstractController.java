package ishop.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ishop.form.ProductForm;
import ishop.form.SearchForm;
import ishop.service.OrderService;
import ishop.service.ProductService;
import ishop.service.SocialService;
import ishop.service.impl.ServiceManager;

public abstract class AbstractController extends HttpServlet {
	private static final long serialVersionUID = -2343552911189320104L;
	protected Logger LOGGER = LoggerFactory.getLogger(getClass());
	private ProductService productService;
	private OrderService orderService;
	private SocialService socialService;

	@Override
	public void init() throws ServletException {
		productService = ServiceManager.getInstance(getServletContext()).getProductService();
		orderService = ServiceManager.getInstance(getServletContext()).getOrderService();
		socialService = ServiceManager.getInstance(getServletContext()).getSocialService();
	}

	public final ProductService getProductService() {
		return productService;
	}

	public final OrderService getOrderService() {
		return orderService;
	}

	public SocialService getSocialService() {
		return socialService;
	}

	public final int getPageCount(int totalCount, int itemPerPage) {
		int page = totalCount / itemPerPage;
		if (page * itemPerPage != totalCount) {
			page++;
		}
		return page;
	}

	public final int getPage(HttpServletRequest request) {
		try {
			return Integer.parseInt(request.getParameter("page"));
		} catch (NumberFormatException e) {
			return 1;
		}
	}

	public final SearchForm createSearchForm(HttpServletRequest request) {
		return new SearchForm(request.getParameter("query"), request.getParameterValues("category"),
				request.getParameterValues("producer"));
	}

	public final ProductForm createProductForm(HttpServletRequest request) {
		return new ProductForm(Integer.parseInt(request.getParameter("idProduct")),
				Integer.parseInt(request.getParameter("count")));

	}

}
