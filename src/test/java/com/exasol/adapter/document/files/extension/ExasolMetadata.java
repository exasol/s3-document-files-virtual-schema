package com.exasol.adapter.document.files.extension;

import static org.hamcrest.MatcherAssert.assertThat;

import java.sql.*;

import org.hamcrest.Matcher;

import com.exasol.matcher.ResultSetStructureMatcher;

public class ExasolMetadata {

    private final Connection connection;
    private final String extensionSchemaName;

    ExasolMetadata(final Connection connection, final String extensionSchemaName) {
        this.connection = connection;
        this.extensionSchemaName = extensionSchemaName;
    }

    public void assertScript(final Matcher<ResultSet> matcher) {
        try {
            final PreparedStatement statement = connection.prepareStatement(
                    "SELECT SCRIPT_NAME, SCRIPT_TYPE, SCRIPT_INPUT_TYPE, SCRIPT_RESULT_TYPE, SCRIPT_TEXT, SCRIPT_COMMENT "
                            + " FROM SYS.EXA_ALL_SCRIPTS " //
                            + " WHERE SCRIPT_SCHEMA=?" //
                            + " ORDER BY SCRIPT_NAME");
            statement.setString(1, extensionSchemaName);
            assertThat(statement.executeQuery(), matcher);
        } catch (final SQLException exception) {
            throw new IllegalStateException(exception);
        }
    }

    public void assertNoScripts() {
        assertScript(ResultSetStructureMatcher.table("VARCHAR", "VARCHAR", "VARCHAR", "VARCHAR", "VARCHAR", "VARCHAR")
                .matches());
    }

    public void assertConnection(final Matcher<ResultSet> matcher) {
        assertResult("SELECT CONNECTION_NAME, CONNECTION_COMMENT FROM EXA_ALL_CONNECTIONS ORDER BY CONNECTION_NAME ASC",
                matcher);
    }

    public void assertNoConnections() {
        assertConnection(ResultSetStructureMatcher.table("VARCHAR", "VARCHAR").matches());
    }

    public void assertVirtualSchema(final Matcher<ResultSet> matcher) {
        assertResult(
                "SELECT SCHEMA_NAME, SCHEMA_OWNER, ADAPTER_SCRIPT, ADAPTER_NOTES FROM SYS.EXA_ALL_VIRTUAL_SCHEMAS ORDER BY SCHEMA_NAME ASC",
                matcher);
    }

    public void assertNoVirtualSchema() {
        assertVirtualSchema(ResultSetStructureMatcher.table("VARCHAR", "VARCHAR", "VARCHAR", "VARCHAR").matches());
    }

    private void assertResult(final String sql, final Matcher<ResultSet> matcher) {
        try {
            final PreparedStatement statement = connection.prepareStatement(sql);
            assertThat(statement.executeQuery(), matcher);
        } catch (final SQLException exception) {
            throw new IllegalStateException(exception);
        }
    }
}
