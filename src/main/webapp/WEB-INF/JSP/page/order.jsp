<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="ishop" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div id="order">
	<c:if test="${CURRENT_MESSAGE != null }">
		<div class="alert alert-success hidden-print" role="alert">${CURRENT_MESSAGE }</div>
	</c:if>
	<h4 class="text-center">Order # ${order.id }</h4>
	<hr />
	<table class="table table-bordered">
		<thead>
			<tr>
				<th>Product</th>
				<th>Price</th>
				<th>Count</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach var="item" items="${order.items }">
			<tr id="product${item.product.id }" class="item">
				<td class="text-center"><img class="small" src="${item.product.imageLink}" alt="${item.product.name}"><br>${item.product.name}</td>
				<td class="price">$ ${item.product.price}</td>
				<td class="count">${item.count}</td>
			</tr>
			</c:forEach>
			  <tr>
                 <td colspan="2" class="total"> <strong>Total:</strong></td>
                 <td colspan="2" class="total-cost">$ ${order.totalCost }</td>
               </tr>
		</tbody>
	</table>
	
	<div class="row hidden-print">
	<div class="col-md-4 col-md-offset-4 col-lg-2 col-lg-offset-5">
	
	<a href="/my-orders" class = "post-request btn btn-primary btn-block" >My orders</a>
	</div>
</div>
</div>
