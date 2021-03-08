package ishop.servlet.ajax;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ishop.Constants;
import ishop.entity.Product;
import ishop.form.SearchForm;
import ishop.servlet.AbstractController;
import ishop.utils.RoutingUtils;

@WebServlet("/ajax/html/more/search")
public class SearchMoreController extends AbstractController {
	private static final long serialVersionUID = -1496658162278198732L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		SearchForm searchForm = createSearchForm(req);
		List<Product> products = getProductService().listProductsBySearchForm(searchForm, getPage(req),
				Constants.MAX_PRODUCTS_PER_HTML);
		req.setAttribute("products", products);
		RoutingUtils.forwardToFragment("product-list.jsp", req, resp);
	}
}
