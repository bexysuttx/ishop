package ishop.model;

import java.io.Serializable;

import ishop.entity.Product;

public class ShoppingCartItem implements Serializable {
	private static final long serialVersionUID = -7045898507440187686L;
	private Product Product;
	private int count;
	public ShoppingCartItem() {
		super();
	}

	public ShoppingCartItem(Product product, int count) {
		super();
		Product = product;
		this.count = count;
	}
	public Product getProduct() {
		return Product;
	}

	public void setProduct(Product product) {
		Product = product;
	}
	

	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}

	@Override
	public String toString() {
		return String.format("ShoppingCartItem [Product=%s, count=%s]", Product, count);
	}
	

	
	

}
