package com.elega9t.tofu.persistance;

import com.elega9t.tofu.Config.Column;
import java.util.List;
import java.util.Map;

/**
 *
 * @author elega9t
 */
public interface DataRepository {
    
    public String getWhereClause();
    
    public List<Map<String, String>> getData() throws Exception;
    
    public List<Column> getColumns();
    
    public List<String> getDataPoints(String field) throws Exception;

    public DataRepository filteredInstance(String field, String value);
    
}
