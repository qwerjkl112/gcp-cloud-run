<!DOCTYPE html>
<html>
<head>
    <title>Ranking</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="/style.css">
</head>
<body>
<h1> Scoreboard for most miles walked this week</h1>
<table border = "1px"> 
    <thead>
        <tr> 
            <th scope="col">Username</th>
            <th scope="col">Total Distance</th>
            <th scope="col">Timestamp</th>
        </tr>
    </thread>
    <tbody>
        <#list history as entry>
            <tr> 
                <th scope="row">${entry.username}</th>
                <td> ${entry.distance}</td>
                <td> ${entry.timestamp}</td>
            </tr>
        </#list>
    </tbody>
</table>
<a href="/">Back to the board</a>
</body>
</html>