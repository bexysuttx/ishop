package ishop.exception;

import javax.servlet.http.HttpServletResponse;

public class ValidationException extends AbstractApplicationException {
	private static final long serialVersionUID = 7495765350758523634L;

	public ValidationException(String msg) {
		super(msg, HttpServletResponse.SC_BAD_REQUEST);
	}
}
