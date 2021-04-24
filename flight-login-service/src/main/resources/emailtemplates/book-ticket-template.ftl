<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>Sending Email with Freemarker HTML Template Example</title>

    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>

    <link href='http://fonts.googleapis.com/css?family=Roboto' rel='stylesheet' type='text/css'>

    <!-- use the font -->
    <style>
        body {
            font-family: 'Roboto', sans-serif;
            font-size: 48px;
        }
    </style>
</head>
<body style="margin: 0; padding: 0;">

    <table align="center" border="0" cellpadding="0" cellspacing="0" width="600" style="border-collapse: collapse;">
        <tr>
            <td align="center" bgcolor="#78ab46" style="padding: 40px 0 30px 0;">
                <img src="cid:logo.png" alt="http://localhost:7101/login/logUser" style="display: block;" />
            </td>
        </tr>
        <tr>
            <td bgcolor="#eaeaea" style="padding: 40px 30px 40px 30px;">
                <p>Dear ${fullName},</p>
                <p>You have booked a flight from ${sourceAirportName} to ${destinationAirportName} in ${flightName}.</p>
                <p>The price of the ticket is Rs. ${bookTicketPrice}.</p>
                <p>Duration of the flight is ${duration} hours.</p>
                <p>Please carry your passport, and your passport number is ${passport}.</p>
                <p>If there is any changes then you will be notified in ${mobileNo} as well as in your email ${email}</p>
                <p>Have a safe journey.</p>
                <p>Thanks</p>
            </td>
        </tr>
        <tr>
            <td bgcolor="#777777" style="padding: 30px 30px 30px 30px;">
                <p>${signature}</p>
            </td>
        </tr>
    </table>

</body>
</html>