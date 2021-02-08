package com.elega9t.tofu.persistance;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

/**
 *
 * @author elega9t
 */
public interface JDBCTransform<T> {
    
    public List<T> fromResultSet(ResultSet resultSet) throws Exception;
    
    public PreparedStatement insertStatement(T t, Connection connection) throws Exception;
    
    public PreparedStatement updateStatement(T t, Connection connection) throws Exception;
    
}
