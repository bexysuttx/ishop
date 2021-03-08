package ishop.servlet.ajax;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ishop.form.ProductForm;
import ishop.model.ShoppingCart;
import ishop.utils.SessionUtils;

@WebServlet("/ajax/json/product/add")
public class AddProductController extends AbstractProductController {
	private static final long serialVersionUID = -179484196704832276L;

	@Override
	protected void proccessProductForm(ProductForm productForm, ShoppingCart shoppingCart, HttpServletRequest req,
			HttpServletResponse resp) {
		getOrderService().addProductToShoppingCart(productForm, shoppingCart);
		String cookieValue = getOrderService().serializeShoppingCart(shoppingCart);
		SessionUtils.updateCurrentShoppingCartCookie(cookieValue, resp);

	}
}
