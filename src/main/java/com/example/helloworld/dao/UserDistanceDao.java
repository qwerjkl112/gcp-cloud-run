package com.example.helloworld.dao;

import com.example.helloworld.model.LeaderboardEntry;
import com.example.helloworld.model.User;
import com.example.helloworld.model.UserDistanceEntry;
import com.google.api.gax.longrunning.OperationFuture;
import com.google.cloud.spanner.Database;
import com.google.cloud.spanner.DatabaseAdminClient;
import com.google.cloud.spanner.DatabaseClient;
import com.google.cloud.spanner.DatabaseId;
import com.google.cloud.spanner.ResultSet;
import com.google.cloud.spanner.Spanner;
import com.google.cloud.spanner.SpannerException;
import com.google.cloud.spanner.SpannerExceptionFactory;
import com.google.cloud.spanner.SpannerOptions;
import com.google.cloud.spanner.Statement;
import com.google.spanner.admin.database.v1.CreateDatabaseMetadata;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class UserDistanceDao {

    private DatabaseClient dbClient;
    private DatabaseAdminClient dbAdminClient;
    private DatabaseId dbId;

    public UserDistanceDao() {
        SpannerOptions spannerOptions = SpannerOptions.newBuilder().build();
        this.dbId = DatabaseId.of(spannerOptions.getProjectId(), "cloudspanner-frankguo", "cloudspanner-frankguo");
        Spanner spanner = spannerOptions.getService();
        dbAdminClient = spanner.getDatabaseAdminClient();
        dbClient = spanner.getDatabaseClient(dbId);
        try {
            create();
        } catch (Exception e) {
            System.out.println("Skipping initializing Database as it already exists\n" + e);

        }
    }

    private void create() {
        OperationFuture<Database, CreateDatabaseMetadata> op = 
            dbAdminClient.createDatabase(
                dbId.getInstanceId().getInstance(),
                dbId.getDatabase(),
            Arrays.asList(
                "CREATE TABLE Users(\n"
                 + " UserId INT64 NOT NULL,\n"
                 + " Username String(2048) NOT NULL\n"
                 + ") PRIMARY KEY(UserId)",
                "CREATE TABLE Distances(\n" 
                 + " UserId INT64 NOT NULL,\n"
                 + " Distance FLOAT64 NOT NULL,\n"
                 + " Timestamp TIMESTAMP NOT NULL\n"
                 + " OPTIONS (allow_commit_timestamp=true)\n"
                 + ") PRIMARY KEY(UserId, Timestamp),\n"
                 + "INTERLEAVE IN PARENT Users ON DELETE NO ACTION"));
            try {
                // Initiate the request which returns an OperationFuture.
                Database dbOperation = op.get();
                System.out.println("Created database [" + dbOperation.getId() + "]");
            } catch (ExecutionException e) {
                // If the operation failed during execution, expose the cause.
                throw (SpannerException) e.getCause();
            } catch (InterruptedException e) {
                // Throw when a thread is waiting, sleeping, or otherwise occupied,
                // and the thread is interrupted, either before or during the activity.
                throw SpannerExceptionFactory.propagateInterrupt(e);
            } catch (Exception e) {
                System.out.println("caught this error\n + " + e);
            }
    }

    public User retrieveUser(String username) throws Exception {
        if (!validateUsername(username)) {
            throw new Exception("User does not exist");
        }

        String sql = "SELECT UserId FROM Users WHERE username = '" + username + "'";
        ResultSet resultSet = dbClient.singleUse().executeQuery(Statement.of(sql));
        if (resultSet.next()) {
            Long userId = resultSet.getLong("UserId");
            if (userId != null) {
                User user = new User();
                user.setUserId(userId);
                user.setUsername(username);
                return user;
            }
        }
        return null;
    }

    public User retrieveUser(Long userId) throws Exception {
        if (!validateUserId(userId)) {
            throw new Exception("User does not exist");
        }

        String sql = "SELECT UserId FROM Users WHERE userId = " + userId;
        ResultSet resultSet = dbClient.singleUse().executeQuery(Statement.of(sql));
        if (resultSet.next()) {
            String username = resultSet.getString("Username");
            if (username != null && username.equals("")) {
                User user = new User();
                user.setUserId(userId);
                user.setUsername(username);
                return user;
            }
        }
        return null;
    }

    private boolean validateUsername(String username) {
        // check to make sure username does not exist
        String sql = "SELECT Count(UserId) as UserCount FROM Users WHERE Username = '" + username + "'";
        ResultSet resultSet = dbClient.singleUse().executeQuery(Statement.of(sql));
        if (resultSet.next()) {
            if (resultSet.getLong("UserCount") != 0) {
                System.out.println("Username exists");
                return true;
            }
        }
        return false;
    }

    private boolean validateUserId(Long userId) {
        // check to make sure userId exists
        String sql = "SELECT Count(UserId) as UserCount FROM Users WHERE userId = " + userId;
        ResultSet resultSet = dbClient.singleUse().executeQuery(Statement.of(sql));
        if (resultSet.next()) {
            if (resultSet.getLong("UserCount") != 0) {
                System.out.println("Username exists");
                return true;
            }
        }
        return false;
    }

    public User insertUser(String username) throws Exception {
        // check to make sure username does not exist
        if (validateUsername(username)) {
            throw new Exception("Username has been taken");
        }
        long randomId = (long) Math.floor(Math.random() * 9_000_000_000L) + 1_000_000_000L;
        dbClient
            .readWriteTransaction()
            .run(transaction -> {
                String s = "INSERT INTO Users (UserId, Username)" 
                + String.format(" VALUES (%s, '%s')", randomId, username);
                long rowCount = transaction.executeUpdate(Statement.of(s));
                if (rowCount == 0) {
                    System.out.println("Failed to insert new user" + username);
                    throw new Exception("Failed to insert new user");
                }
                System.out.println("Successfully updated " + rowCount + " row(s)");
                return null;
            });
        
        User newUser = new User();
        newUser.setUserId(randomId);
        newUser.setUsername(username);
        return newUser;
    }

    public Boolean insertDistance(Long userId, Float distance) throws Exception {
        // check userId exist
        if (!validateUserId(userId)) {
            throw new Exception("User does not exist");
        }

        dbClient
            .readWriteTransaction()
            .run(transaction -> {
                String s = "INSERT INTO Distances (UserId, Distance, Timestamp)" 
                + String.format(" VALUES (%s, %s, CURRENT_TIMESTAMP())", userId, distance);
                long rowCount = transaction.executeUpdate(Statement.of(s));
                if (rowCount == 0) {
                    System.out.println("Failed to insert new distance" + userId);
                    throw new Exception("Failed to insert new distance");
                }
                System.out.println("Successfully updated " + rowCount + " row(s)");
                return null;
            });

        return true;
    }

    public List<UserDistanceEntry> getUserDistanceEntries(User user) throws Exception {
        List<UserDistanceEntry> entries = new ArrayList<>();
        Statement statement = Statement.newBuilder(
            "SELECT u.username, u.userId, d.distance, d.timestamp "
            + " FROM Users u "
            + " JOIN Distances d ON u.UserId = d.UserId "
            + " WHERE u.userId = @userId")
            .bind("userId")
            .to(user.getUserId())
            .build();

        ResultSet rs = dbClient.singleUse().executeQuery(statement);
        while (rs.next()) {
            entries.add(new UserDistanceEntry(rs.getLong("userId"), rs.getString("username"),
             (float) rs.getDouble("distance"), rs.getTimestamp("timestamp").toString()));
        }
        return entries;
    }

    public List<LeaderboardEntry> getWeeklyScoreBoard() {
        List<LeaderboardEntry> leaderboard = new ArrayList<>();
        Statement statement = Statement.newBuilder(
            "SELECT Users.username, SUM(Distance) as Total_Distance"
            + " FROM Distances JOIN Users ON Users.UserId = Distances.UserId"
            + " WHERE Distances.TIMESTAMP > TIMESTAMP_SUB(CURRENT_TIMESTAMP, INTERVAL 7 DAY)"
            + " GROUP BY Distances.UserId, Users.username ORDER BY Total_Distance DESC"
        )
        .build();
        ResultSet rs = dbClient.singleUse().executeQuery(statement);
        while (rs.next()) {
            leaderboard.add(new LeaderboardEntry(rs.getString("username"), (float) rs.getDouble("Total_Distance")));
        }
        return leaderboard;
    }

}