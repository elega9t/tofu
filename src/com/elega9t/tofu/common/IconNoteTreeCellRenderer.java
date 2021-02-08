package com.elega9t.tofu.common;

import javax.swing.JTree;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.tree.*;

/**
 *
 * @author elega9t
 */
public class IconNoteTreeCellRenderer implements TreeCellRenderer {
        
    private final JLabel label;

    public IconNoteTreeCellRenderer() {
        label = new JLabel();
    }

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded,
                                                  boolean leaf, int row, boolean hasFocus) {
        Object o = ((DefaultMutableTreeNode) value).getUserObject();
        if (o instanceof IconNote) {
            IconNote v = (IconNote) o;
            label.setIcon(v.getIcon());
            label.setText(v.getText());
        } else {
            label.setIcon(null);
            label.setText("" + value);
        }
        return label;
    }
}
