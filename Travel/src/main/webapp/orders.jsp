<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Foglalások áttekintése</title>
</head>
<body>

	<h1>Foglalások áttekintése</h1>

	<a href="ViewOrders">Frissítés</a>&nbsp;&nbsp;
	<a href="index.jsp">Új rendelés</a>
	<br>
	<table>
		<thead>
			<tr>
				<th>Id</th>
				<th>Név</th>
				<th>Járat</th>
				<th>Indulás</th>
				<th>Helyek</th>
				<th>Státusz</th>
		  	</tr>
		</thead>
		
		<tbody>	
		<c:forEach var="row" items="${ordersList}">
			<tr>
				<td>${row.orderId}</td>
				<td>${row.customername}</td>
				<td>${row.flightId}</td>
				<td>${row.arrive}</td>
				<td>${row.seats}</td>
				<td>${row.status}</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>	
</body>
</html>
