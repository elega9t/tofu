package com.elega9t.tofu.persistance;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author elega9t
 */
public class IntJDBCTransform implements JDBCTransform<Integer> {

    private final String name;

    public IntJDBCTransform(String name) {
        this.name = name;
    }

    @Override
    public List<Integer> fromResultSet(ResultSet resultSet) throws SQLException {
            List<Integer> result = new ArrayList<>();
            while(resultSet.next()) {
                Integer v = resultSet.getInt(this.name);
                result.add(v);
            }
            return result;
    }

    @Override
    public PreparedStatement insertStatement(Integer t, Connection connection) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public PreparedStatement updateStatement(Integer t, Connection connection) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
