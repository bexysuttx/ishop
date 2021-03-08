<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"
	trimDirectiveWhitespaces="true"%>
<!DOCTYPE HTML>
<html lang="en">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>IShop devices</title>
<link rel="stylesheet" href="/static/css/bootstrap-theme.css">
<link rel="stylesheet" href="/static/css/font-awesome.css">
<link rel="stylesheet" href="/static/css/bootstrap.css">
<link rel="stylesheet" href="/static/css/app.css">
</head>
<body>
	<header>
		<jsp:include page="fragment/header.jsp"></jsp:include>
	</header>
	<div class="container-fluid">
		<div class="row">
			<aside class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
				<jsp:include page="fragment/aside.jsp"></jsp:include>
			</aside>
			<main class="col-xs-12 col-sm-8 col-md-9 col-lg-10">
				<jsp:include page="${currentPage }"></jsp:include>
			</main>
		</div>
	</div>
	<footer>
		<jsp:include page="fragment/footer.jsp"></jsp:include>
	</footer>
	<script src="/static/js/jquery.js"></script>
	<script src="/static/js/bootstrap.js"></script>
	<script src="/static/js/app.js"></script>
</body>
</html>