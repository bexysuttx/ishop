package ishop.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ishop.Constants;
import ishop.utils.WebUtils;

@WebFilter(filterName = "SetCurrentRequestUrlFilter")
public class SetCurrentRequestUrlFilter extends AbstractFilter {

	@Override
	public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		request.setAttribute(Constants.CURRENT_REQUEST_URL, WebUtils.getCurrentRequestUrl(request));
		chain.doFilter(request, response);
		
	}

	

}
