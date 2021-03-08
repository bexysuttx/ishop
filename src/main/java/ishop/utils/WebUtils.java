package ishop.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public final class WebUtils {
	
	public static Cookie findCookie(HttpServletRequest req, String cookieName) {
		Cookie[] cookies = req.getCookies();
		if (cookies != null) {
			for (Cookie c:cookies) {
				if (c.getName().equals(cookieName)) {
					if (c.getValue()!=null && !"".equals(c.getValue())) {
						return c;
					}
				}
			}
		}
		return null;
	}
	
	public static void setCookie(String name, String value, int age, HttpServletResponse resp) {
		Cookie c = new Cookie(name, value);
		c.setMaxAge(age);
		c.setHttpOnly(true);
		c.setPath("/");
		resp.addCookie(c);
	}
	
	private WebUtils() {
	}

	public static String getCurrentRequestUrl(HttpServletRequest request) {
		String query = request.getQueryString();
		if (query == null) {
			return request.getRequestURI();
		} else {
			return request.getRequestURI() + "?" + query;
		}
	}

}
