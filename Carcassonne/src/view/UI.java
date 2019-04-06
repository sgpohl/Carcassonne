package view;
import logic.*;

import javax.swing.*;

public class UI {
	
	JFrame frame;
	
	public UI() {
		frame = new JFrame();
		frame.setSize(500, 500);
		frame.setVisible(true);
	}
	
	
	public void draw(Position pos, model.Tile tile) {
		
	}
	
	public void highlight(Position pos, boolean isEmpty) {
		
	}
	
	public static void main(String[] args) {
		new UI();
	}
}
