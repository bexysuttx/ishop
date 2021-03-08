package ishop;

public final class Constants {

	public final static String CURRENT_SHOPPING_CART = "CURRENT_SHOPPING_CART";

	public final static int MAX_PRODUCTS_IN_CART = 20;

	public final static int MAX_COUNT_PRODUCTS_CART = 10;
	
	public static final int ORDER_PER_PAGE = 5;

	public final static String ACCOUNT_ACTION_HYSTIRY = "ACCOUNT_ACTION_HYSTIRY";

	public final static int MAX_PRODUCTS_PER_HTML = 12;

	public final static String CATEGORY_LIST = "CATEGORY_LIST";

	public final static String PRODUCER_LIST = "PRODUCER_LIST";
	
	public final static String CURRENT_ACCOUNT = "CURRENT_ACCOUNT";
	
	public static final String SUCCESS_REDIRECT_URL_AFTER_SIGNIN = "SUCCESS_REDIRECT_URL_AFTER_SIGNIN";
	
	public static final String CURRENT_REQUEST_URL = "CURRENT_REQUEST_URL";

	public enum Cookie {

		SHOPPING_CART("ISCC", 60 * 60 * 24 * 365);

		private final String name;
		private final int ttl;

		private Cookie(String name, int ttl) {
			this.name = name;
			this.ttl = ttl;
		}

		public String getName() {
			return name;
		}

		public int getTtl() {
			return ttl;
		}
	}
}
