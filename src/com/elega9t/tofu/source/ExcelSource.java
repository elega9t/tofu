package com.elega9t.tofu.source;

import com.elega9t.tofu.Config;
import com.elega9t.tofu.Config.Column;
import com.elega9t.tofu.persistance.InvestigationRepository;
import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author elega9t
 */
public class ExcelSource implements Source {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");
    private final File file;

    public ExcelSource(File file) {
        this.file = file;
    }
    
    private Stream<Map<String, String>> load(ProgressListener listener) throws Exception {
        Config config = Config.Instance.value;
        List<Column> columns = config.getInvestigationData();

        FileInputStream excelFile = new FileInputStream(file);
        Workbook workbook = new XSSFWorkbook(excelFile);
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(workbook.sheetIterator(), Spliterator.ORDERED), false)
            .flatMap(sheet -> {
                listener.update("Loading sheet `" + sheet.getSheetName() + "`");
                Iterator<Row> rowIterator = sheet.iterator();
                List<String> headers = new ArrayList<>();
                if (rowIterator.hasNext()) {
                    Iterator<Cell> cellIterator = rowIterator.next().iterator();
                    while (cellIterator.hasNext()) {
                        Cell currentCell = cellIterator.next();
                        headers.add(currentCell.getStringCellValue().toLowerCase());
                    }
                }
                final AtomicInteger rowIndex = new AtomicInteger(1);
                return StreamSupport.stream(Spliterators.spliteratorUnknownSize(rowIterator, Spliterator.ORDERED), false)
                    .flatMap(row -> {
                        listener.update("Loading sheet `" + sheet.getSheetName() + "`, row: " + (rowIndex.get() + 1));
                        Map<String, String> i = new HashMap<>();
                        Iterator<Cell> cellIterator = row.iterator();
                        int columnIndex = 0;
                        while (cellIterator.hasNext()) {
                            Cell cell = cellIterator.next();
                            String value = null;
                            if (cell.getCellType() == CellType.STRING) {
                                value = cell.getStringCellValue();
                            }
                            else if (cell.getCellType() == CellType.NUMERIC) {
                                value = "" + cell.getNumericCellValue();
                            }
                            else if (cell.getCellType() == CellType.FORMULA && cell.getCachedFormulaResultType() == CellType.STRING) {
                                value = "" + cell.getRichStringCellValue();
                            } else if (cell.getCellType() != CellType.BLANK) {
                                throw new RuntimeException("Could not read data from sheet: " + sheet.getSheetName() + ", row: " + (rowIndex.get() + 1) + ", col: " + (columnIndex + 1) + ", cellType: " + cell.getCellType());
                            }
                            if (columnIndex < headers.size()) {
                                for (Column c: columns) {
                                    if (c.getMatch() != null && ((c.getMatch().equals("exact") && headers.get(columnIndex).equalsIgnoreCase(c.getExcel())) ||
                                            (c.getMatch().equals("contains") && headers.get(columnIndex).contains(c.getExcel())))) {
                                        if (c.getType() != null && c.getType().equalsIgnoreCase("date")) {
                                            try {
                                                Date d = DateUtil.getJavaDate(Double.parseDouble(value));
                                                value = DATE_FORMAT.format(d);
                                            } catch (Exception e) {}
                                        }
                                        i.put(c.getDbField(), value);
                                    }
                                }
                            }
                            columnIndex++;
                        }
                        boolean valid = true;
                        for (Column c: columns) {
                            if (c.getExcel() == null) {
                                i.put(c.getDbField(), file.getName());
                            }
                            if(c.getRequired()) {
                                Object v = i.get(c.getDbField());
                                valid = v != null;
                            }
                        }
                        rowIndex.incrementAndGet();
                        if (valid) {
                            return Arrays.asList(i).stream();
                        } else {
                            List<Map<String, String>> empty = Collections.emptyList();
                            return empty.stream();
                        }
                    });
            });
    }

    @Override
    public void save(ProgressListener listener) throws Exception {
        Config config = Config.Instance.value;
        try(Connection connection = config.getDb().getConnection()) {
            connection.setAutoCommit(false);
            InvestigationRepository repository = new InvestigationRepository();
            load(listener)
                .forEach(data -> {
                    try {
                        repository.insert(connection, data);
                    } catch (Exception ex) {
                        try {
                            connection.rollback();
                        } catch (SQLException e) { }
                        throw new RuntimeException(ex);
                    }
                });
            connection.commit();
            listener.update("All records saved.");
        }
    }

}
