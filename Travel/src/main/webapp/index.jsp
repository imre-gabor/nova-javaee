<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Repülőjegy foglalás</title>
</head>
<body>

	<h1>Repülőjegy foglalás</h1>
	<form action="NewOrder" method="post">
		<table>
			<tr>
				<td>Megrendelő neve:</td>
				<td><input type="text" name="customername" /></td>
			</tr>
			<tr>
				<td>Járat azonosítója:</td>
				<td><input type="text" name="flightId" /></td>
			</tr>
			<tr>
				<td>Indulás (yyyy.mm.dd):</td>
				<td><input type="text" name="depart" /></td>
			</tr>
			<tr>
				<td>Érkezés (yyyy.mm.dd):</td>
				<td><input type="text" name="arrive" /></td>
			</tr>
			<tr>
				<td>Helyek száma:</td>
				<td><input type="text" name="seat" /></td>
			</tr>
			<tr>
				<td colspan="2" align="center"><input type="submit" value="OK" />
				</td>
			</tr>
		</table>
	</form>
	<a href="ViewOrders">Foglalások áttekintése</a>
</body>
</html>
