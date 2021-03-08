package ishop.listener;

import java.util.List;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import ishop.Constants;

@WebListener
public class AccountSessionStatisticsListener implements HttpSessionListener {

	@Override
	public void sessionCreated(HttpSessionEvent se) {
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent se) {
		@SuppressWarnings("unchecked")
		List<String> actions = (List<String>) se.getSession().getAttribute(Constants.ACCOUNT_ACTION_HYSTIRY);
		if (actions != null) {
			logCurrentActionHistory(se.getSession().getId(), actions);
		}
	}

	private void logCurrentActionHistory(String id, List<String> actions) {
		System.out.println(id + " ->\n\t" + String.join("\n\t", actions));

	}

}
