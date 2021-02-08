package com.elega9t.tofu.common;

import javax.swing.Icon;

/**
 *
 * @author elega9t
 */
public class IconNote {

    private Icon icon;
    private String text;

    public IconNote(Icon icon, String text) {
        this.icon = icon;
        this.text = text;
    }

    public Icon getIcon() {
        return icon;
    }

    public void setIcon(Icon icon) {
        this.icon = icon;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public IconNote getUserObject() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
