package com.elega9t.tofu.source;

import com.elega9t.tofu.Config;
import com.elega9t.tofu.Config.Column;
import com.elega9t.tofu.persistance.DataRepository;
import com.elega9t.tofu.persistance.InvestigationRepository;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.table.AbstractTableModel;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author elega9t
 */
public class DataRepositorySource extends AbstractTableModel implements SourceTableModel {

    protected List<Map<String, String>> data;
    protected List<Column> columns;
    protected Set<Integer> edited;
    private final EditListener editListener;
    private final String title;

    public DataRepositorySource(DataRepository repository, ProgressListener listener) throws Exception {
        this(repository, listener, new EditListener() {
            @Override
            public void edited(Integer row, Integer col) {
            }
        });
    }

    public DataRepositorySource(DataRepository repository, ProgressListener listener, EditListener editListener) throws Exception {
        this.title = repository.getWhereClause();
        this.editListener = editListener;
        this.load(repository, listener);
    }
    
    @Override
    public int getRowCount() {
        return this.data.size();
    }

    @Override
    public int getColumnCount() {
        return this.columns.size();
    }

    @Override
    public String getColumnName(int arg0) {
        return this.columns.get(arg0).getName();
    }

    @Override
    public Object getValueAt(int row, int col) {
        try {
            return this.data.get(row).get(this.columns.get(col).getDbField());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setValueAt(Object value, int row, int col) {
        this.edited.add(row);
        this.data.get(row).put(this.columns.get(col).getDbField(), "" + value);
        this.editListener.edited(row, col);
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        return this.columns.get(col).getEditable();
    }

    @Override
    public List<RequiredData> getKeyValues(int row) throws Exception {
        List<RequiredData> result = new ArrayList<>();
        for(Column c: this.columns) {
            if (c.getKey()) {
                RequiredData v = new RequiredData(c, this.data.get(row).get(c.getDbField()));
                result.add(v);
            }
        }
        return result;
    }

    private void load(DataRepository repository, ProgressListener listener) throws Exception {
        this.edited = new HashSet<>();
        this.data = repository.getData();
        Column count = new Column();
        count.setName("Count");
        count.setDbField("count");
        this.columns = new ArrayList<>(repository.getColumns());
        this.columns.add(0, count);
    }

    @Override
    public void save(ProgressListener listener) throws Exception {
        Config config = Config.Instance.value;
        InvestigationRepository repository = new InvestigationRepository();
        try(Connection connection = config.getDb().getConnection()) {
            connection.setAutoCommit(false);
            this.edited.stream()
                .forEach(row -> {
                    try {
                        repository.update(connection, this.data.get(row));
                    } catch (Exception ex) {
                        try {
                            connection.rollback();
                        } catch (SQLException e) { }
                        throw new RuntimeException(ex);
                    }
                });
                connection.commit();
                edited.clear();
        }
    }

    @Override
    public void export(File file, ProgressListener listener) throws Exception {
        if (this.edited.size() > 0) {
            throw new RuntimeException("There are unsaved changes. Please save them first.");
        } else {
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet();
            Row row = sheet.createRow(0);
            int columnCount = 0;
            for(Column column: columns) {
                Cell cell = row.createCell(columnCount++);
                cell.setCellValue(column.getName());
            }

            int rowCount = 1;
            for (Map<String, String> rowData: data) {
                row = sheet.createRow(rowCount++);
                columnCount = 0;
                for(Column column: columns) {
                    Cell cell = row.createCell(columnCount++);
                    cell.setCellValue(rowData.get(column.getDbField()));
                }
                listener.update("Exported row: " + (rowCount - 1));
            }
            try (FileOutputStream outputStream = new FileOutputStream(file)) {
                workbook.write(outputStream);
            }
        }
    }
    
}
