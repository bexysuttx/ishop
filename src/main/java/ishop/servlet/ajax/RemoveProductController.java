package ishop.servlet.ajax;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ishop.form.ProductForm;
import ishop.model.ShoppingCart;
import ishop.utils.SessionUtils;

@WebServlet("/ajax/json/product/remove")
public class RemoveProductController extends AbstractProductController {
	private static final long serialVersionUID = 1785273619611634811L;

	@Override
	protected void proccessProductForm(ProductForm productForm, ShoppingCart shoppingCart, HttpServletRequest req,
			HttpServletResponse resp) {
		getOrderService().removeProductFromShoppingCart(productForm, shoppingCart);
		if (shoppingCart.getItems().isEmpty()) {
			SessionUtils.clearCurrentShoppingCart(req, resp);
		} else {
			String cookieValue = getOrderService().serializeShoppingCart(shoppingCart);
			SessionUtils.updateCurrentShoppingCartCookie(cookieValue, resp);
		}
	}

}
