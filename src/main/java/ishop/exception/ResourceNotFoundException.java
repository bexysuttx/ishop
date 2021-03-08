package ishop.exception;

import javax.servlet.http.HttpServletResponse;

public class ResourceNotFoundException extends AbstractApplicationException {
	private static final long serialVersionUID = -6214177122574254252L;
	
	public ResourceNotFoundException(String message) {
		super(message, HttpServletResponse.SC_NOT_FOUND);
	}

}
