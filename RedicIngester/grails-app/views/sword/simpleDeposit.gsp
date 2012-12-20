<%@ page import="org.swordapp.client.DepositReceipt" %>
<!doctype html>
<html>
	<head>
		<title>Simple Deposit Result</title>
	</head>
	<body>

<ul>
<li>Status Code: ${receipt.getStatusCode()}</li>
<li>Location: ${receipt.getLocation()}</li>
</ul>


	
	</body>
</html>
