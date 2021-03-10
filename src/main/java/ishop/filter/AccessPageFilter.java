package ishop.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ishop.Constants;
import ishop.utils.RoutingUtils;
import ishop.utils.SessionUtils;
import ishop.utils.WebUtils;

@WebFilter(filterName = "AccessPageFilter")
public class AccessPageFilter extends AbstractFilter {

	
	@Override
	public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		if (SessionUtils.isCurrentAccountCreated(request)) {
			chain.doFilter(request, response);
		} else {
			String requestUrl = WebUtils.getCurrentRequestUrl(request);
			if (ishop.utils.UrlUtils.isAjaxUrl(requestUrl)) {
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				response.getWriter().println("401");
			} else {
				request.getSession().setAttribute(Constants.SUCCESS_REDIRECT_URL_AFTER_SIGNIN, requestUrl);
				RoutingUtils.redirect("/sign-in", request, response);
			}
		}
	}

}
