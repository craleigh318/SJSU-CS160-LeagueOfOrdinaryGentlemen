package login_handling;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Associates a user to a name and a password.
 *
 * @author Christopher Raleigh
 * @author Benjamin Ngo
 * @author Jeremy Wong
 * @author David-Eric Thorpe
 */
public class User implements Serializable {

    private int id;

    /**
     * Creates a User Java object from an existing user, using the ID value.
     *
     * @param id the ID of the user in the SQL database
     */
    User(int id) {
        this.id = id;
    }

    /**
     * Registers a new user into the system.
     *
     * @param username the user's name
     * @param password the user's password
     * @throws SQLException
     */
    static User createUser(String username, String password) throws
            SQLException {
        setSQLUpdate(username, password);
        int newID = getUserID(username);
        User newUser = new User(newID);
        return newUser;
    }

    /**
     * Gets an ID number from a certain username.
     *
     * @param username the username to search
     * @return the next ID to use for a new user
     * @throws SQLException
     */
    private static int getUserID(String username) throws SQLException {
        String query = "SELECT `user_id` FROM `users` WHERE `username` = `?`";
        PreparedStatement ps = server_connections.ConnectionManager.
                getConnection().prepareStatement(query);
        ps.setString(1, username);
        ResultSet results = ps.executeQuery(query);
        int newID = results.getInt(1);
        return newID;
    }

    /**
     * Returns results from an SQL query.
     *
     * @param value the value to get from the row
     * @return results from the query
     * @throws SQLException
     */
    private ResultSet getSQLQuery(String value)
            throws SQLException {
        String query = "SELECT `?` FROM `users` WHERE `user_id` = ?";
        PreparedStatement ps = server_connections.ConnectionManager.
                getConnection().prepareStatement(query);
        ps.setString(1, value);
        ps.setInt(2, id);
        ResultSet results = ps.executeQuery(query);
        return results;
    }

    /**
     * Adds a row via an SQL update.
     *
     * @param username the user's name
     * @param password the user's password
     * @return a row count
     * @throws SQLException
     */
    private static int setSQLUpdate(String username, String password)
            throws SQLException {
        String update = "INSERT INTO `users` (`username`, "
                + "`password`) VALUES (`?`, `?`)";
        PreparedStatement ps = server_connections.ConnectionManager.
                getConnection().prepareStatement(update);
        ps.setString(1, username);
        ps.setString(2, password);
        int rowCount = ps.executeUpdate(update);
        return rowCount;
    }

    /**
     *
     * @return this user's name
     */
    String getUsername() throws SQLException {
        ResultSet results = getSQLQuery("username");
        String username = results.getString(1);
        return username;
    }

    /**
     *
     * @return this user's password
     * @throws SQLException
     */
    private String getPassword() throws SQLException {
        ResultSet results = getSQLQuery("password");
        String password = results.getString(1);
        return password;
    }

    /**
     * Compares a user-entered password to the real password.
     *
     * @param enteredPassword the password entered by the user
     * @return 0 if the passwords match
     * @throws SQLException
     */
    int comparePassword(String enteredPassword) throws SQLException {
        return getPassword().compareTo(enteredPassword);
    }
}