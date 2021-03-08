package ishop.servlet.page;

import java.io.IOException;
import java.net.URLDecoder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ishop.Constants;
import ishop.model.CurrentAccount;
import ishop.model.SocialAccount;
import ishop.servlet.AbstractController;
import ishop.utils.RoutingUtils;
import ishop.utils.SessionUtils;
@WebServlet("/from-social")
public class FromSocialController extends AbstractController {
	private static final long serialVersionUID = 2167233972512926391L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String code = req.getParameter("code");
		if (code != null) {
			SocialAccount socialAccount = getSocialService().getSocialAccount(code);
			CurrentAccount currentAccount = getOrderService().authentificate(socialAccount);
			SessionUtils.setCurrentAccount(req, currentAccount);
			redirectToSuccesPage(req,resp);
		} else {
			LOGGER.warn("Parameter code not found");
			if (req.getSession().getAttribute(Constants.SUCCESS_REDIRECT_URL_AFTER_SIGNIN) != null) {
				req.getSession().removeAttribute(Constants.SUCCESS_REDIRECT_URL_AFTER_SIGNIN);
			}
			RoutingUtils.redirect("/sign-in", req, resp);
		}
	}

	private void redirectToSuccesPage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String targetUrl = (String) req.getSession().getAttribute(Constants.CURRENT_REQUEST_URL);
		if (targetUrl != null ) {
			req.getSession().removeAttribute(Constants.SUCCESS_REDIRECT_URL_AFTER_SIGNIN);
			RoutingUtils.forwardToPage(URLDecoder.decode(targetUrl,"UTF-8"), req, resp);
		} else {
			RoutingUtils.redirect("/my-orders", req, resp);
		}
		
	}

}
