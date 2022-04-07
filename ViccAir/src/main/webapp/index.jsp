<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Cancel Flight</title>
</head>
<body>

	<h1>Cancel Flight</h1>

	<form action="DeleteFlight">
		<table>
			<tr>
				<td>Flight to cancel:</td>
				<td><input type="text" name="flight"></td>
			</tr>
			<tr>
				<td colspan="2"><input type="submit" name="ok" value="Cancel"></td>
			</tr>
		</table>
	</form>

</body>
</html>
