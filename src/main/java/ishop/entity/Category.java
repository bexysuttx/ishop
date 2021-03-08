package ishop.entity;

public class Category extends AbstractEntity<Integer> {
	private static final long serialVersionUID = -6556176778813775182L;
	private String name;
	private String url;
	private int productCount;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getProductCount() {
		return productCount;
	}

	public void setProductCount(int productCount) {
		this.productCount = productCount;
	}

	@Override
	public String toString() {
		return String.format("Category [Id()=%s, name=%s, url=%s, productCount=%s]", getId(), name, url,
				productCount);
	}

}
