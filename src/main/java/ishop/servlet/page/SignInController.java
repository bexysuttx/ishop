package ishop.servlet.page;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ishop.Constants;
import ishop.servlet.AbstractController;
import ishop.utils.RoutingUtils;
import ishop.utils.SessionUtils;

@WebServlet("/sign-in")
public class SignInController extends AbstractController {
	private static final long serialVersionUID = 6461933447832185815L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		if (SessionUtils.isCurrentAccountCreated(req)) {
			RoutingUtils.redirect("/my-orders", req, resp);
		} else {
			RoutingUtils.forwardToPage("sign-in.jsp", req, resp);
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		if (SessionUtils.isCurrentAccountCreated(req)) {
			RoutingUtils.redirect("/my-orders", req, resp);
		} else {
			String targetUrl = req.getParameter("target");
			if (targetUrl != null) {
				req.getSession().setAttribute(Constants.SUCCESS_REDIRECT_URL_AFTER_SIGNIN, targetUrl);
			}
			RoutingUtils.redirect(getSocialService().getAuthorizeUrl(), req, resp);
		}
	}

}
