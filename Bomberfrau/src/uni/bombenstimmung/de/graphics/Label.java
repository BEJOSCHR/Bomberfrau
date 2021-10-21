package uni.bombenstimmung.de.graphics;

import java.awt.*;

import javax.swing.JLabel;

@SuppressWarnings("serial")
public class Label extends JLabel {

	//FPS
	public static int displayedFPS = 0;
	long nextSecond = System.currentTimeMillis() + 1000;
	int FramesInCurrentSecond = 0;
	int FramesInLastSecond = 0;
	
	static long nextRepaintDelay = 0;
	static int maxFPS = 100;
	
//==========================================================================================================
	/**
	 * The first settings of the new label - Constructor
	 */
	public Label() {
		
		this.setBounds(0, 0, GraphicsHandler.WIDTH, GraphicsHandler.HEIGHT);
		this.setVisible(true);
		GraphicsHandler.frame.add(this, BorderLayout.CENTER);
		
	}
	
//==========================================================================================================
	/**
	 * The methode, called by the JLabel for the display parts
	 */
	protected void paintComponent(Graphics g) {
		
		//MAX FPS GRENZE SCHAFFEN
		long now = System.currentTimeMillis();
		try {
		   if (nextRepaintDelay > now) {
			   Thread.sleep(nextRepaintDelay - now);
		   }
		   nextRepaintDelay = now + 1000/(maxFPS-20);
		} catch (InterruptedException e) { }
		
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		//DRAW BACKGROUND
		draw_Background(g);
		
		draw_Title(g);
		
		//DRAW FPS
		g.setColor(Color.WHITE);
		g.setFont(new Font("Arial", Font.BOLD, 12));
		g.drawString(""+getCurrentFPSValue(), 0+5, GraphicsHandler.HEIGHT-15);
		
		//CALCULATE FPS
		calculateFPS();
		
		repaint();
		
	}
	
	
	private void draw_Title(Graphics g) {
		
		g.setColor(Color.WHITE);
		g.setFont(new Font("Arial", Font.BOLD, 22));
		String text = "Willkommen bei Bomberfrau - PewPew Booooom";
		int width = g.getFontMetrics().stringWidth(text);
		g.drawString(text, GraphicsHandler.WIDTH/2-width/2, GraphicsHandler.HEIGHT/2-100);

		
	}

	private void draw_Background(Graphics g) {
		
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(0, 0, GraphicsHandler.WIDTH, GraphicsHandler.HEIGHT);
		
	}
	
	
//==========================================================================================================
	/**
	 * Updates the FPS
	 */
	private void calculateFPS() {
		long currentTime = System.currentTimeMillis();
		if(currentTime > nextSecond) {
			nextSecond += 1000;
			FramesInLastSecond = FramesInCurrentSecond;
			FramesInCurrentSecond = 0;
		}
		FramesInCurrentSecond++;
		displayedFPS = FramesInLastSecond;
	}
	
//==========================================================================================================
	/**
	 * Get the currentFPSValue
	 * @return int - The current FPS to display
	 */
	public static int getCurrentFPSValue() {
		return displayedFPS;
	}
	
}
