<!DOCTYPE html>
<html>
<head>
    <title>Ranking</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="/style.css">
</head>
<body>
<h1> Scoreboard of the most miles walked for Googlers since last 7 days </h1>
<table border = "1px"> 
    <thead>
        <tr> 
            <th scope="col">Username</th>
            <th scope="col">Total Distance</th>
        </tr>
    </thread>
    <tbody>
        <#list rankings as ranking>
            <tr> 
                <th scope="row">${ranking.username}</th>
                <td> ${ranking.totalDistance}</td>
            </tr>
        </#list>
    </tbody>
</table>
<h3>Look up history by username</h3>
<form action="/getHistoryByUsername" method="post">
    <div>Username:</div>
    <input type="text" name="username" required="required" pattern="[A-Za-z0-9]{1,20}"/>
    <input type="submit" value="Lookup"/>
</form>


<h3>Log your walk history!</h3>
<form action="/createDistance" method="post">
    <div>Username: </div>
    <input type="text" name="username" required="required" pattern="[A-Za-z0-9]{1,20}"/>
    <br/><br/>
    <div>Distance: </div>
    <input type="number" step="0.01" required="required" name="distance" min="0.01"/>
    <input type="submit" value="Submit"/>
</form>

</body>
</html>