package Views.MapEditor;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import Controllers.ButtonListener.ValidateButtonListener;
import Views.Utility.ImageLoader;
import Views.Utility.PixelMap;

public class ValidateAndMenuView extends JPanel {
	ButtonPanelView buttonPanel;
	
	public ValidateAndMenuView(ButtonPanelView buttonPanel) {
		this.buttonPanel = buttonPanel;
		setLayout(new GridLayout(0,2));
		
        setBounds((int)(PixelMap.SCREEN_WIDTH*17.7/20), PixelMap.SCREEN_HEIGHT/40, PixelMap.SCREEN_WIDTH/10, (int)(buttonPanel.getBounds().getY() - PixelMap.SCREEN_HEIGHT/40));
        setOpaque(false);
        
        JButton validateButton = new JButton(new ImageIcon(ImageLoader.getImage("SAD_CAT")));
        validateButton.setBackground(Color.red);
        validateButton.setOpaque(true);
        validateButton.setFocusable(false);
        validateButton.addActionListener(new ValidateButtonListener(validateButton));
        validateButton.setBorder(new LineBorder(Color.BLACK));
        
        JButton hamburgerButton = new JButton(new ImageIcon(ImageLoader.getImage("HAMBURGER")));
        hamburgerButton.setOpaque(true);
        hamburgerButton.setFocusable(false);
        hamburgerButton.addActionListener(e -> {
			buttonPanel.toggle();
		});
        hamburgerButton.setBorder(new LineBorder(Color.BLACK));
        
        this.add(validateButton);
        this.add(hamburgerButton);
	}
}