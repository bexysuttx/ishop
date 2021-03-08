<%@ tag pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>


<%@ attribute name="classes" required="false" type="java.lang.String"%>

<form action = "/sign-in" method = "post">
<input type="hidden" name="target" value="${encodedUrl }">
	<button type = "submit" class="btn btn-primary ${classes }">
		<i class="fa fa-facebook-official" aria-hidden="true"></i> Sign in
	</button>
</form>