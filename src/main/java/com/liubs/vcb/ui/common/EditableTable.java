package com.liubs.vcb.ui.common;

import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.table.JBTable;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 * @author Liubsyy
 * @date 2024/11/7
 */
public class EditableTable {
    private DefaultTableModel tableModel;
    private JBTable table;
    private ToolbarDecorator toolbarDecorator;

    public EditableTable(DefaultTableModel tableModel) {
        this.tableModel = tableModel;
        this.table = new JBTable(tableModel);
        createDecorator();
    }

    public void createDecorator(){
        toolbarDecorator = ToolbarDecorator.createDecorator(table);
        toolbarDecorator.setAddAction(event -> {
            Object[] objects = new Object[tableModel.getColumnCount()];
            tableModel.addRow(objects);
        });
        toolbarDecorator.setRemoveAction(event -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                tableModel.removeRow(selectedRow);
            }
        });
    }

    public JPanel getPanel(){
        return toolbarDecorator.createPanel();
    }


}
