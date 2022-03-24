<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<title>Bank Application</title>
</head>
<body>

	<h1>Test Page for BankOperator</h1>

	<h2>Search Client</h2>
	<form action="SearchClientServlet">
		<table>
			<tr>
				<td>Name:</td>
				<td><input type="text" name="name" /></td>
			</tr>
			<tr>
				<td>Address:</td>
				<td><input type="text" name="address" /></td>
			</tr>
			<tr>
				<td>Client id:</td>
				<td><input type="text" name="clientid" /></td>
			</tr>
			

			<tr>
				<td colspan="2"><input type="submit" value="OK" /></td>
			</tr>
		</table>
	</form>
	<table>
		<thead>
			<tr>
				<th>Client id</th>
				<th>Name</th>
				<th>Address</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="client" items="${clients}">
				<tr>
					<td>${client.clientid}</td>
					<td>${client.name}</td>
					<td>${client.address}</td>
				</tr>
			</c:forEach>
		
		</tbody>
	</table>



	<h2>Create Client</h2>
	<form action="CreateClientServlet">
		<table>
			<tr>
				<td>Name:</td>
				<td><input type="text" name="name" /></td>
			</tr>
			<tr>
				<td>Address:</td>
				<td><input type="text" name="address" /></td>
			</tr>
			<tr>
				<td>Zip code:</td>
				<td><input type="text" name="zipCode" /></td>
			</tr>
			

			<tr>
				<td colspan="2"><input type="submit" value="OK" /></td>
			</tr>
		</table>
	</form>
	${resultOfClientCreation}

	<h2>Create Account for Client</h2>
	<form action="CreateAccountServlet">
		<table>
			<tr>
				<td>ClientId:</td>
				<td><input type="text" name="clientId" /></td>
			</tr>
			<tr>
				<td>Balance:</td>
				<td><input type="text" name="balance" /></td>
			</tr>
			<tr>
				<td colspan="2"><input type="submit" value="OK" /></td>
			</tr>
		</table>
	</form>
	${resultOfAccountCreation}

	<h2>Transfer</h2>
	<form action="TransferServlet">
		<table>
			<tr>
				<td>From:</td>
				<td><input type="text" name="from" /></td>
			</tr>
			<tr>
				<td>To:</td>
				<td><input type="text" name="to" /></td>
			</tr>
			<tr>
				<td>Amount:</td>
				<td><input type="text" name="amount" /></td>
			</tr>
			<tr>
				<td colspan="2"><input type="submit" value="OK" /></td>
			</tr>
		</table>
	</form>
	${resultOfTransfer}

</body>
</html>