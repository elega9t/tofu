package com.elega9t.tofu;

import com.google.gson.Gson;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 *
 * @author elega9t
 */
public class Config {
    
    private Db db;
    
    private Map<String, List<Column>> data;

    public Db getDb() {
        return db;
    }

    public void setDb(Db db) {
        this.db = db;
    }

    public Map<String, List<Column>> getData() {
        return data;
    }

    public List<Column> getData(String type) {
        return data.get(type);
    }

    public List<Column> getInvestigationData() {
        return this.getData("investigation");
    }
    
    public void setData(Map<String, List<Column>> data) {
        this.data = data;
    }

    public static class Db {
        private String driver;
        private String url;
        private String user;
        private String password;

        public String getDriver() {
            return driver;
        }

        public void setDriver(String driver) {
            this.driver = driver;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public Connection getConnection() throws SQLException {
            return DriverManager.getConnection(getUrl(), getUser(), getPassword());
        }

    }
    
    public static class Column {
        private String name;
        private String dbField;
        private String excel;
        private String match;
        private String type;
        private Boolean key;
        private Boolean required;
        private Boolean dataPoint;
        private Boolean editable;
        private String iconName;
        
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDbField() {
            return dbField;
        }

        public void setDbField(String field) {
            this.dbField = field;
        }

        public String getExcel() {
            return excel;
        }

        public void setExcel(String excel) {
            this.excel = excel;
        }

        public String getMatch() {
            return match;
        }

        public void setMatch(String match) {
            this.match = match;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public Boolean getKey() {
            return key != null? key : false;
        }

        public void setKey(Boolean key) {
            this.key = key;
        }

        public Boolean getRequired() {
            return required != null? required : false;
        }

        public void setRequired(Boolean required) {
            this.required = required;
        }

        public Boolean getDataPoint() {
            return dataPoint != null? dataPoint : false;
        }

        public void setDataPoint(Boolean dataPoint) {
            this.dataPoint = dataPoint;
        }

        public Boolean getEditable() {
            return editable != null? editable : false;
        }

        public void setEditable(Boolean editable) {
            this.editable = editable;
        }

        public String getIconName() {
            return iconName == null? "key" : iconName;
        }

        public void setIconName(String iconName) {
            this.iconName = iconName;
        }

        @Override
        public String toString() {
            return "Column{" + "name=" + name + ", dbField=" + dbField + ", excel=" + excel + ", match=" + match + ", type=" + type + ", key=" + key + ", required=" + required + ", dataPoint=" + dataPoint + ", iconName=" + iconName + '}';
        }
        
    }
    
    public static class Instance {
        private static Gson gson = new Gson();
        public static Config value = gson.fromJson(getConfigFileReader(), Config.class);
    }
    
    private static FileReader getConfigFileReader() {
        try {
            return new FileReader("./conf/config.json");
        } catch (FileNotFoundException ex) {
            throw new RuntimeException(ex);
        }
    }
    
}
