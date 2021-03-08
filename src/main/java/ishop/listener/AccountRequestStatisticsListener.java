package ishop.listener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpServletRequest;

import ishop.Constants;

@WebListener
public class AccountRequestStatisticsListener implements ServletRequestListener {

	@Override
	public void requestDestroyed(ServletRequestEvent sre) {	
	}

	@Override
	public void requestInitialized(ServletRequestEvent sre) {
		HttpServletRequest req= (HttpServletRequest) sre.getServletRequest();
		@SuppressWarnings("unchecked")
		List<String> actions = (List<String>) req.getSession().getAttribute(Constants.ACCOUNT_ACTION_HYSTIRY);
		if (actions == null) {
			actions = new ArrayList<String>();
			req.getSession().setAttribute(Constants.ACCOUNT_ACTION_HYSTIRY, actions);
		}
		actions.add(getCurrentAction(req));	
	}

	private String getCurrentAction(HttpServletRequest req) {
		StringBuilder s= new StringBuilder();
		s.append(req.getMethod()).append(" ").append(req.getRequestURI());
		Map<String,String[]> map = req.getParameterMap();
		if (map != null) {
			boolean first= true;
			for (Map.Entry<String, String[]> entry : map.entrySet()) {
				if (first) {
					s.append('?');
					first = false;
				} else {
					s.append('&');
				}
				for (String value : entry.getValue()) {
					s.append(entry.getKey()).append("=").append(value).append("&");
				}
				s.deleteCharAt(s.length()-1);
			}
		}
		return s.toString();
	}

}
