package com.elega9t.tofu.source;

import com.elega9t.tofu.Config.Column;

/**
 *
 * @author elega9t
 */
public class RequiredData {

    private Column column;
    private String value;

    public RequiredData(Column column, String value) {
        this.column = column;
        this.value = value;
    }

    public Column getColumn() {
        return column;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "RequiredData{" + "column=" + column + ", value=" + value + '}';
    }
    
}
