package ishop.entity;

import java.math.BigDecimal;

public class Product extends AbstractEntity<Integer> {
	private static final long serialVersionUID = -5199443389597093532L;
	private String name;
	private String desc;
	private String imageLink;
	private BigDecimal price;
	private String category;
	private String producer;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getImageLink() {
		return imageLink;
	}
	public void setImageLink(String imageLink) {
		this.imageLink = imageLink;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getProducer() {
		return producer;
	}
	public void setProducer(String producer) {
		this.producer = producer;
	}
	@Override
	public String toString() {
		return String.format("Product [id=%s, name=%s, desc=%s, imageLink=%s, price=%s, category=%s, producer=%s]",
				getId(), name, desc, imageLink, price, category, producer);
	}
}
