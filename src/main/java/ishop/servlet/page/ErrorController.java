package ishop.servlet.page;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ishop.servlet.AbstractController;
import ishop.utils.RoutingUtils;

@WebServlet("/error")
public class ErrorController  extends AbstractController{
	private static final long serialVersionUID = -5548854906297808611L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setAttribute("statusCode", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		RoutingUtils.forwardToPage("error.jsp", req, resp);
	}

}
