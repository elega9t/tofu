package com.elega9t.tofu.component;

import com.elega9t.tofu.form.Main;
import com.elega9t.tofu.form.SourceEditorPanel;
import java.awt.*;
import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.plaf.metal.MetalIconFactory;

/**
 * @author 6dc
 *
 * A class which creates a JTabbedPane and auto sets a close button when you add a tab
 */
public class JTabbedPaneCloseButton extends JTabbedPane {

    private Main main;
    
    public JTabbedPaneCloseButton(Main main) {
        super();
        this.main = main;
    }

    /* Override Addtab in order to add the close Button everytime */
    public void addSourceEditorPanel(String title, Icon icon, SourceEditorPanel component) {
        super.addTab(title, icon, component);
        int count = this.getTabCount() - 1;
        setTabComponentAt(count, new CloseButtonTab(main, component, title, icon));
    }

    @Override
    public void addTab(String arg0, Icon arg1, Component arg2, String arg3) {
        super.addTab(arg0, arg1, arg2, arg3);
    }

    @Override
    public void addTab(String title, Icon icon, Component component) {
        addTab(title, icon, component, null);
    }

    @Override
    public void addTab(String title, Component component) {
        addTab(title, null, component);
    }

    /* addTabNoExit */
    public void addTabNoExit(String title, Icon icon, Component component, String tip) {
        super.addTab(title, icon, component, tip);
    }

    public void addTabNoExit(String title, Icon icon, Component component) {
        addTabNoExit(title, icon, component, null);
    }

    public void addTabNoExit(String title, Component component) {
        addTabNoExit(title, null, component);
    }

    /* Button */
    public class CloseButtonTab extends JPanel {
        private Main main;
        private SourceEditorPanel tab;

        public CloseButtonTab(Main main, final SourceEditorPanel tab, String title, Icon icon) {
            this.main = main;
            this.tab = tab;
            setOpaque(false);
            FlowLayout flowLayout = new FlowLayout(FlowLayout.CENTER, 3, 3);
            setLayout(flowLayout);
            JLabel jLabel = new JLabel(title + "  ");
            jLabel.setIcon(icon);
            add(jLabel);
            JButton button = new JButton(MetalIconFactory.getInternalFrameCloseIcon(16));
            button.setBorder(null);
            button.setFocusable(false);
            button.setMargin(new Insets(0, 0, 0, 0));
            button.addMouseListener(new CloseListener(main, tab));
            add(button);
        }
    }
    /* ClickListener */
    public class CloseListener implements MouseListener
    {
        private Main main;
        private SourceEditorPanel tab;

        public CloseListener(Main main, SourceEditorPanel tab){
            this.main = main;
            this.tab=tab;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if(e.getSource() instanceof JButton){
                this.main.closeTab(tab);
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {}

        @Override
        public void mouseReleased(MouseEvent e) {}

        @Override
        public void mouseEntered(MouseEvent e) {
            if(e.getSource() instanceof JButton){
                JButton clickedButton = (JButton) e.getSource();
             //   clickedButton.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY,3));
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            if(e.getSource() instanceof JButton){
                JButton clickedButton = (JButton) e.getSource();
             //   clickedButton.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY,3));
            }
        }
    }
}