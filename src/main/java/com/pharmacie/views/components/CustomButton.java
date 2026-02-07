package com.pharmacie.views.components;
import javax.swing.*;
import java.awt.*;

public class CustomButton extends JButton{
	 public enum ButtonType {
	        PRIMARY,    // Bleu - Actions principales
	        SUCCESS,    // Vert - Validation, ajout
	        DANGER,     // Rouge - Suppression, annulation
	        WARNING,    // Orange - Avertissement
	        INFO,       // Cyan - Information
	        SECONDARY   // Gris - Actions secondaires
	    }
	    
	    public CustomButton(String text, ButtonType type) {
	        super(text);
	        setupButton(type);
	    }
	    
	    public CustomButton(String text, Icon icon, ButtonType type) {
	        super(text, icon);
	        setupButton(type);
	    }
	    
	    private void setupButton(ButtonType type) {
	        setFont(new Font("Segoe UI", Font.BOLD, 13));
	        setFocusPainted(false);
	        setBorderPainted(false);
	        setCursor(new Cursor(Cursor.HAND_CURSOR));
	        setPreferredSize(new Dimension(120, 35));
	        
	        // Couleurs selon le type
	        switch (type) {
	            case PRIMARY:
	                setBackground(new Color(0, 123, 255));
	                setForeground(Color.WHITE);
	                break;
	            case SUCCESS:
	                setBackground(new Color(40, 167, 69));
	                setForeground(Color.WHITE);
	                break;
	            case DANGER:
	                setBackground(new Color(220, 53, 69));
	                setForeground(Color.WHITE);
	                break;
	            case WARNING:
	                setBackground(new Color(255, 193, 7));
	                setForeground(Color.BLACK);
	                break;
	            case INFO:
	                setBackground(new Color(23, 162, 184));
	                setForeground(Color.WHITE);
	                break;
	            case SECONDARY:
	                setBackground(new Color(108, 117, 125));
	                setForeground(Color.WHITE);
	                break;
	        }
	        
	        // Effet hover
	        addMouseListener(new java.awt.event.MouseAdapter() {
	            public void mouseEntered(java.awt.event.MouseEvent evt) {
	                setBackground(getBackground().darker());
	            }
	            public void mouseExited(java.awt.event.MouseEvent evt) {
	                setupButton(type);
	            }
	        });
	    }

}
