package com.elega9t.tofu.source;

/**
 *
 * @author elega9t
 */
public interface Source {
    
    public void save(ProgressListener listener) throws Exception;

    public static interface EditListener {
        public void edited(Integer row, Integer col);
    }

    public static interface ProgressListener {

        public void update(String message);

    }
    
}
