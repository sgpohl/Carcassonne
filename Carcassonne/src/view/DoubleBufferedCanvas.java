package view;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

public class DoubleBufferedCanvas extends Canvas {
	private static final long serialVersionUID = 5950549071438180567L;
	
	private Image buffer;
	private Dimension bufferDim;
	
	@Override
	public void update(Graphics g) {
		if(bufferDim == null || !bufferDim.equals(this.getSize()))
			recreateBuffer();
		
		buffer.getGraphics().clearRect(0, 0, bufferDim.width, bufferDim.height);
		this.paint(buffer.getGraphics());
		
		g.drawImage(buffer, 0, 0, bufferDim.width, bufferDim.height, null);
	}
	
	@Override
	public void paint(Graphics g) {
		
	}
	
	private void recreateBuffer() {
		Dimension dim = this.getSize();
		
		if(buffer != null) {
			buffer.flush();
			buffer = null;
		}
		
		buffer = createImage(dim.width, dim.height);
		bufferDim = (Dimension)dim.clone();
	}
	
}
