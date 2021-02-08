package com.elega9t.tofu.persistance;

import com.elega9t.tofu.Config;
import com.elega9t.tofu.Config.Column;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author elega9t
 */
public class InvestigationRepository implements DataRepository {

    private static Config config = Config.Instance.value;

    private String whereClause;
    
    public InvestigationRepository() {
        this("");
    }

    public InvestigationRepository(String whereClause) {
        this.whereClause = whereClause;
    }

    public String getWhereClause() {
        return whereClause;
    }

    private String getWhereClauseSql() {
        return this.whereClause.length() == 0? "": " WHERE " + this.whereClause;
    }

    @Override
    public List<Column> getColumns() {
        return config.getInvestigationData();
    }

    @Override
    public List<Map<String, String>> getData() throws Exception {
        String query = "SELECT (SELECT COUNT(*) FROM `investigation` WHERE `case_id`=i.`case_id`) `count`, i.* FROM `investigation` i" + this.getWhereClauseSql();
        try (Connection connection = config.getDb().getConnection()) {
            ResultSet rs = connection.createStatement().executeQuery(query);
            return transform.fromResultSet(rs);
        }
    }

    @Override
    public DataRepository filteredInstance(String field, String value) {
        String newWhereClause = this.whereClause;
        if (newWhereClause.length() == 0) {
            newWhereClause = "`" + field + "`='"+ value +"'";
        } else {
            newWhereClause += " AND `" + field + "`='"+ value +"'";            
        }
        return new InvestigationRepository(newWhereClause);
    }
    
    @Override
    public List<String> getDataPoints(String field) throws Exception {
        String query = "SELECT DISTINCT `" + field + "` FROM `investigation`" + this.getWhereClauseSql() + " ORDER BY `" + field +"` DESC";
        try (Connection connection = config.getDb().getConnection()) {
            ResultSet rs = connection.createStatement().executeQuery(query);
            return new StringJDBCTransform(field).fromResultSet(rs);
        }
    }
    
    public void insert(Connection connection, Map<String, String> v) throws Exception {
        PreparedStatement ps = transform.insertStatement(v, connection);
        ps.executeUpdate();
    }
    
    public void update(Connection connection, Map<String, String> v) throws Exception {
        PreparedStatement ps = transform.updateStatement(v, connection);
        ps.executeUpdate();
    }
    
    public static void createTable() throws SQLException {
        try (Connection connection = config.getDb().getConnection()) {
            String createTableQuery = "CREATE TABLE `investigation`("
                + "`id` INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY";
            for (Column column: config.getInvestigationData()) {
                switch (column.getType()) {
                    case "text":
                        createTableQuery += ", `" + column.getDbField() + "` VARCHAR(256)";
                        break;
                    case "longtext":
                        createTableQuery += ", `" + column.getDbField() + "` VARCHAR(512)";
                        break;
                    case "clob":
                        createTableQuery += ", `" + column.getDbField() + "` CLOB";
                        break;
                    case "date":
                        createTableQuery += ", `" + column.getDbField() + "` VARCHAR(32)";
                        break;
                }
            }
            createTableQuery += ")";
            connection.createStatement().executeUpdate(createTableQuery);
        }
    }
    
    private static JDBCTransform<Map<String, String>> transform = new JDBCTransform<Map<String, String>>() {
        @Override
        public List<Map<String, String>> fromResultSet(ResultSet resultSet) throws Exception {
            Config config = Config.Instance.value;
            List<Map<String, String>> result = new ArrayList<>();
            while(resultSet.next()) {
                Map<String, String> v = new HashMap<>();
                v.put("id", "" + resultSet.getLong("id"));
                v.put("count", "" + resultSet.getLong("count"));
                for (Column c: config.getInvestigationData()) {
                    v.put(c.getDbField(), resultSet.getString(c.getDbField()));
                }
                result.add(v);
            }
            return result;
        }

        @Override
        public PreparedStatement insertStatement(Map<String, String> data, Connection connection) throws Exception {
            Config config = Config.Instance.value;
            List<String> fields = new ArrayList<>();
            for (Column column: config.getInvestigationData()) {
                fields.add("`" + column.getDbField() + "`");
            }
            String query = "INSERT INTO `investigation`(" + String.join(", ", fields) + 
                    ") VALUES (" + String.join(", ", Collections.nCopies(fields.size(), "?")) + ")";
            
            PreparedStatement p = connection.prepareStatement(query);
            int index = 1;
            for (Column column: config.getInvestigationData()) {
                String value = data.get(column.getDbField());
                p.setString(index, value);
                index ++;
            }
            return p;
        }

        @Override
        public PreparedStatement updateStatement(Map<String, String> data, Connection connection) throws Exception {
            Config config = Config.Instance.value;
            String query = "UPDATE `investigation` SET ";
            List<String> fields = new ArrayList<>();
            for (Column column: config.getInvestigationData()) {
                fields.add("`" + column.getDbField() + "` = ?");
            }
            
            query += String.join(", ", fields) + " WHERE `id` = ?";
                    
            PreparedStatement p = connection.prepareStatement(query);
            int index = 1;
            for (Column column: config.getInvestigationData()) {
                String value = data.get(column.getDbField());
                p.setString(index, value);
                index ++;
            }
            p.setLong(index, Long.parseLong(data.get("id")));
            return p;
        }
    };
    
}
