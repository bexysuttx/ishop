package ishop.servlet.ajax;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import ishop.form.ProductForm;
import ishop.model.ShoppingCart;
import ishop.servlet.AbstractController;
import ishop.utils.RoutingUtils;
import ishop.utils.SessionUtils;

public abstract class AbstractProductController extends AbstractController {
	private static final long serialVersionUID = -3406360877028277890L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ProductForm productForm = createProductForm(req);
		ShoppingCart shoppingCart = SessionUtils.getCurrentShoppingCart(req);
		proccessProductForm(productForm, shoppingCart, req, resp);
		sendResponse(shoppingCart, req, resp);
	}

	private void sendResponse(ShoppingCart shoppingCart, HttpServletRequest req, HttpServletResponse resp) throws IOException {
		JSONObject r = new JSONObject();
		r.put("totalCount", shoppingCart.getTotalCount());
		r.put("totalCost", shoppingCart.getTotalCost());
		RoutingUtils.sendJsonFragment(r, req, resp);
	}

	protected abstract void proccessProductForm(ProductForm productForm, ShoppingCart shoppingCart,
			HttpServletRequest req, HttpServletResponse resp);
}
