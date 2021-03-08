package ishop.servlet.page;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ishop.Constants;
import ishop.entity.Product;
import ishop.servlet.AbstractController;
import ishop.utils.RoutingUtils;

@WebServlet("/products")
public class AllProductsController extends AbstractController {
	private static final long serialVersionUID = 3118454236476684838L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		List<Product> products = getProductService().listAllProducts(1, Constants.MAX_PRODUCTS_PER_HTML);
		req.setAttribute("products", products);
		int totalCount = getProductService().countAllProducts();
		req.setAttribute("pageCount", getPageCount(totalCount, Constants.MAX_PRODUCTS_PER_HTML));
		RoutingUtils.forwardToPage("products.jsp", req, resp);
	}
}
