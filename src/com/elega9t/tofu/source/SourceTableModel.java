package com.elega9t.tofu.source;

import java.io.File;
import java.util.List;
import javax.swing.table.TableModel;

/**
 *
 * @author elega9t
 */
public interface SourceTableModel extends Source, TableModel {
    
    public void export(File file, ProgressListener listener) throws Exception;

    public List<RequiredData> getKeyValues(int row) throws Exception;    

}
