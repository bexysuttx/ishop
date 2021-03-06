package ishop.entity;

public class OrderItem extends AbstractEntity<Long> {
	private static final long serialVersionUID = 41914245996292684L;
		private Long idOrder;
		private Product product;
		private int count;
		
		public OrderItem(Product product, int count) {
			super();
			this.product= product;
			this.count = count;
		}
		
		public OrderItem() {
			
		}

		public Long getIdOrder() {
			return idOrder;
		}
		public void setIdOrder(Long idOrder) {
			this.idOrder = idOrder;
		}
		public Product getProduct() {
			return product;
		}
		public void setProduct(Product product) {
			this.product = product;
		}
		public int getCount() {
			return count;
		}
		public void setCount(int count) {
			this.count = count;
		}

		@Override
		public String toString() {
			return String.format("id=%s, OrderItem [idOrder=%s, product=%s, count=%s]",getId(), idOrder, product, count);
		}
		
		
		
		
}
