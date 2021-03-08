package ishop.exception;

import javax.servlet.http.HttpServletResponse;

public class AccessDeniedException extends AbstractApplicationException {
	private static final long serialVersionUID = -2352634595291351633L;
	
	
	public AccessDeniedException(String s) {
		super(s, HttpServletResponse.SC_FORBIDDEN);
	}




}
