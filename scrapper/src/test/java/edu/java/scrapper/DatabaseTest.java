package edu.java.scrapper;

import java.sql.Connection;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DatabaseTest extends IntegrationTest {
    private final Connection connection;

    public DatabaseTest() throws SQLException {
        connection = POSTGRES.createConnection("");
    }

    @Test
    void creatingChatsTableCheck() throws SQLException {
        String query = "Select * from chats";

        ResultSetMetaData result = connection.createStatement().executeQuery(query).getMetaData();

        assertEquals(result.getColumnName(1), "id");
        assertEquals(result.getColumnName(2), "created_at");
    }

    @Test
    void creatingLinksTableCheck() throws SQLException {
        String query = "Select * from links";

        ResultSetMetaData result = connection.createStatement().executeQuery(query).getMetaData();

        assertEquals(result.getColumnName(1), "id");
        assertEquals(result.getColumnName(2), "url");
        assertEquals(result.getColumnName(3), "last_update");
    }

    @Test
    void creatingChatsLinksTableCheck() throws SQLException {
        String query = "Select * from chats_links";

        ResultSetMetaData result = connection.createStatement().executeQuery(query).getMetaData();

        assertEquals(result.getColumnName(1), "chat_id");
        assertEquals(result.getColumnName(2), "link_id");
    }
}
