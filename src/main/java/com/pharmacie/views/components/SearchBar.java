package com.pharmacie.views.components;
import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class SearchBar extends JPanel{
	 private JTextField searchField;
	    private JButton searchButton;
	    private String placeholder;
	    
	    public SearchBar(String placeholder) {
	        this.placeholder = placeholder;
	        setupSearchBar();
	    }
	    
	    private void setupSearchBar() {
	        setLayout(new BorderLayout(5, 0));
	        setBackground(Color.WHITE);
	        
	        // Champ de recherche
	        searchField = new JTextField();
	        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
	        searchField.setPreferredSize(new Dimension(300, 35));
	        searchField.setBorder(BorderFactory.createCompoundBorder(
	            BorderFactory.createLineBorder(new Color(206, 212, 218)),
	            BorderFactory.createEmptyBorder(5, 10, 5, 10)
	        ));
	        
	        // Placeholder
	        searchField.setForeground(Color.GRAY);
	        searchField.setText(placeholder);
	        searchField.addFocusListener(new FocusAdapter() {
	            @Override
	            public void focusGained(FocusEvent e) {
	                if (searchField.getText().equals(placeholder)) {
	                    searchField.setText("");
	                    searchField.setForeground(Color.BLACK);
	                }
	            }
	            
	            @Override
	            public void focusLost(FocusEvent e) {
	                if (searchField.getText().isEmpty()) {
	                    searchField.setForeground(Color.GRAY);
	                    searchField.setText(placeholder);
	                }
	            }
	        });
	        
	        // Bouton de recherche
	        searchButton = new CustomButton("Rechercher", CustomButton.ButtonType.PRIMARY);
	        
	        add(searchField, BorderLayout.CENTER);
	        add(searchButton, BorderLayout.EAST);
	    }
	    
	    public JTextField getSearchField() {
	        return searchField;
	    }
	    
	    public JButton getSearchButton() {
	        return searchButton;
	    }
	    
	    public String getSearchText() {
	        String text = searchField.getText();
	        return text.equals(placeholder) ? "" : text;
	    }
	    
	    public void clearSearch() {
	        searchField.setText("");
	        searchField.setForeground(Color.GRAY);
	        searchField.setText(placeholder);
	    }

}
