package ishop.service;

import java.util.List;

import ishop.entity.Order;
import ishop.form.ProductForm;
import ishop.model.CurrentAccount;
import ishop.model.ShoppingCart;
import ishop.model.SocialAccount;

public interface OrderService {

	void removeProductFromShoppingCart(ProductForm productForm, ShoppingCart shoppingCart);

	void addProductToShoppingCart(ProductForm productForm, ShoppingCart shoppingCart);

	String serializeShoppingCart(ShoppingCart shoppingCart);
	
	ShoppingCart deserializeShoppingCart(String string);
	
	CurrentAccount authentificate (SocialAccount socialAccount);
	
	long makeOrder (ShoppingCart shoppingCart, CurrentAccount currentAccount);
	
	Order findOrderById(long orderId, CurrentAccount currentAccount);
	
	List<Order> listMyOrders(CurrentAccount currentAccount, int page, int limit);
	
	int countMyOrders(CurrentAccount currentAccount);
}
