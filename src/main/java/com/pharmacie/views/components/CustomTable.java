package com.pharmacie.views.components;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class CustomTable extends JTable{
	public CustomTable(DefaultTableModel model) {
        super(model);
        setupTable();
    }
    
    private void setupTable() {
        // Style général
        setFont(new Font("Segoe UI", Font.PLAIN, 12));
        setRowHeight(30);
        setSelectionBackground(new Color(184, 207, 229));
        setSelectionForeground(Color.BLACK);
        setGridColor(new Color(230, 230, 230));
        setShowGrid(true);
        setIntercellSpacing(new Dimension(1, 1));
        
        // Header
        JTableHeader header = getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(new Color(52, 58, 64));
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(header.getWidth(), 40));
        
        // Centrer le contenu des cellules
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        setDefaultRenderer(Object.class, centerRenderer);
        
        // Sélection d'une seule ligne
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }
    
    @Override
    public boolean isCellEditable(int row, int column) {
        return false; // Tableau non éditable par défaut
    }

}
