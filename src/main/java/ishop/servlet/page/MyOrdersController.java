package ishop.servlet.page;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ishop.Constants;
import ishop.entity.Order;
import ishop.model.CurrentAccount;
import ishop.servlet.AbstractController;
import ishop.utils.RoutingUtils;
import ishop.utils.SessionUtils;

@WebServlet("/my-orders")
public class MyOrdersController extends AbstractController {
	private static final long serialVersionUID = 4888080364952023341L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		CurrentAccount currentAccount = SessionUtils.getCurrentAccount(req);
		List<Order>  orders = getOrderService().listMyOrders(currentAccount, 1, Constants.ORDER_PER_PAGE);
		req.setAttribute("orders", orders);
		int orderCount = getOrderService().countMyOrders(currentAccount);
		req.setAttribute("pageCount", getPageCount(orderCount, Constants.ORDER_PER_PAGE));
		RoutingUtils.forwardToPage("my-orders.jsp", req, resp);
	}

}
