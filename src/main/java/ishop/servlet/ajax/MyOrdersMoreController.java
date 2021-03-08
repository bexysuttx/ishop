package ishop.servlet.ajax;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ishop.Constants;
import ishop.entity.Order;
import ishop.servlet.AbstractController;
import ishop.utils.RoutingUtils;
import ishop.utils.SessionUtils;

@WebServlet("/ajax/html/more/my-orders")
public class MyOrdersMoreController extends AbstractController {
	private static final long serialVersionUID = 6248481852574086184L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		List<Order> orders = getOrderService().listMyOrders(SessionUtils.getCurrentAccount(req), getPage(req), Constants.ORDER_PER_PAGE);
		req.setAttribute("orders", orders);
		RoutingUtils.forwardToFragment("my-order-tbody.jsp", req, resp);
	}

}
