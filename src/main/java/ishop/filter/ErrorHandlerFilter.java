package ishop.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ishop.exception.AbstractApplicationException;
import ishop.exception.AccessDeniedException;
import ishop.exception.InternalServerErrorException;
import ishop.exception.ResourceNotFoundException;
import ishop.exception.ValidationException;
import ishop.utils.RoutingUtils;
import ishop.utils.UrlUtils;

@WebFilter(filterName = "ErrorHandlerFilter")
public class ErrorHandlerFilter extends AbstractFilter {
	private static final Logger LOGGER = LoggerFactory.getLogger(ErrorHandlerFilter.class);
	private static final String INTERNAL_ERROR = "Internal error";

	@Override
	public void doFilter(HttpServletRequest req, HttpServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		try {
			chain.doFilter(req, new ThrowExceptionInstendOnSendErrorResponse(resp));
		} catch (Throwable e) {
			String requestUrl = req.getRequestURI();
			if (e instanceof ValidationException) {
				LOGGER.warn("Request is not valid: " + e.getMessage(), e);
			} else {
				LOGGER.error("Request" + requestUrl + "failed: " + e.getMessage(), e);
			}
			handleException(requestUrl, e, req, resp);
		}

	}

	private int getStatusCode(Throwable th) {
		if (th instanceof AbstractApplicationException) {
			return (((AbstractApplicationException) th).getCode());
		} else {
			return (HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}

	private void handleException(String requestUrl, Throwable th, HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		int statusCode = getStatusCode(th);
		resp.setStatus(statusCode);

		if (UrlUtils.isAjaxJsonUrl(requestUrl)) {
			JSONObject r = new JSONObject();
			r.put("message", th instanceof ValidationException ? th.getMessage() : INTERNAL_ERROR);
			RoutingUtils.sendJsonFragment(r, req, resp);
		} else if (UrlUtils.isAjaxHtmlUrl(requestUrl)) {
			RoutingUtils.sendHTMLFragment(INTERNAL_ERROR, req, resp);
		} else {
			req.setAttribute("statusCode", statusCode);
			RoutingUtils.forwardToPage("error.jsp", req, resp);
		}
	}

	private static class ThrowExceptionInstendOnSendErrorResponse extends HttpServletResponseWrapper {
		public ThrowExceptionInstendOnSendErrorResponse(HttpServletResponse response) {
			super(response);
		}

		@Override
		public void sendError(int sc) throws IOException {
			sendError(sc, INTERNAL_ERROR);
		}

		@Override
		public void sendError(int sc, String msg) throws IOException {
			switch (sc) {
			case 404: {
				throw new ResourceNotFoundException(msg);
			}
			case 403: {
				throw new AccessDeniedException(msg);
			}
			case 400: {
				throw new ValidationException(msg);
			}
			default:
				throw new InternalServerErrorException(msg);
			}
		}
	}

}
