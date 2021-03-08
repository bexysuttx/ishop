package ishop.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import ishop.Constants;
import ishop.entity.Product;
import ishop.exception.ValidationException;

public class ShoppingCart implements Serializable {
	private static final long serialVersionUID = 1223697529332894463L;
	private Map<Integer, ShoppingCartItem> products = new LinkedHashMap<>();
	private int totalCount = 0;
	private BigDecimal totalCost = BigDecimal.ZERO;

	public void addProduct(Product product, int count) throws ValidationException {
		validateShoppingCartSize(product.getId());
		ShoppingCartItem shoppingCartItem = products.get(product.getId());
		if (shoppingCartItem == null) {
			validateProductCount(count);
			shoppingCartItem = new ShoppingCartItem(product, count);
			products.put(product.getId(), shoppingCartItem);
		} else {
			validateProductCount(count + shoppingCartItem.getCount());
			shoppingCartItem.setCount(shoppingCartItem.getCount() + count);
		}
		refreshStatistics();
	}

	public void removeProduct(Integer idProduct, int count) {
		ShoppingCartItem shoppingCartItem = products.get(idProduct);
		if (shoppingCartItem != null) {
			if (shoppingCartItem.getCount() > count) {
				shoppingCartItem.setCount(shoppingCartItem.getCount() - count);
			} else {
				products.remove(idProduct);
			}
		}
		refreshStatistics();
	}

	public int getTotalCount() {
		return totalCount;
	}

	private void refreshStatistics() {
		totalCount = 0;
		totalCost = BigDecimal.ZERO;
		for (ShoppingCartItem shoppingCartItem : getItems()) {
			totalCount += shoppingCartItem.getCount();
			totalCost = totalCost.add(
					shoppingCartItem.getProduct().getPrice().multiply(BigDecimal.valueOf(shoppingCartItem.getCount())));
		}
	}

	public Collection<ShoppingCartItem> getItems() {
		return products.values();
	}

	private void validateProductCount(int count) {
		if (count > Constants.MAX_COUNT_PRODUCTS_CART) {
			throw new ValidationException("Limit for product count reached: count=" + count);
		}
	}

	private void validateShoppingCartSize(int idProduct) {
		if (products.size() > Constants.MAX_PRODUCTS_IN_CART
				|| (products.size() == Constants.MAX_PRODUCTS_IN_CART && !products.containsKey(idProduct))) {
			throw new ValidationException("Limit for shippingCart size reached: size=" + products.size());
		}
	}

	public String getView() {
		StringBuilder s = new StringBuilder();
		for (ShoppingCartItem shoppingCartItem : getItems()) {
			s.append(shoppingCartItem.getProduct()).append("-&gt;").append(shoppingCartItem.getCount()).append("<br>");
		}
		return s.toString();
	}

	public BigDecimal getTotalCost() {
		return totalCost;
	}

	@Override
	public String toString() {
		return String.format("ShoppingCart [products=%s, totalCount=%s, totalCost=%s]", products, totalCount,
				totalCost);
	}



}
