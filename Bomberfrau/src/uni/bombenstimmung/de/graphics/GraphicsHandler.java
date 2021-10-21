package uni.bombenstimmung.de.graphics;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class GraphicsHandler {

	public static final int WIDTH = 600, HEIGHT = 400;
	
	public static JFrame frame;
	public static JLabel label;
	
	public static void initGraphics() {
		
		frame = createFrame();
		label = new Label();
		System.out.println("Created Graphics");
		
	}
	
	private static JFrame createFrame() {
		
		JFrame tempFrame = new JFrame();
		tempFrame.setVisible(true);
		tempFrame.setLocationRelativeTo(null);
		tempFrame.setLocation(200, 150);
		tempFrame.setTitle("BomberFrau - Test");
		tempFrame.setResizable(false); 
		tempFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		tempFrame.addKeyListener(new InputHandler());
//		tempFrame.addMouseListener(new InputHandler());
//		tempFrame.addMouseMotionListener(new InputHandler());
//		tempFrame.addMouseWheelListener(new InputHandler());
//		tempFrame.addWindowListener(new InputHandler());
		
//		try { //TRY TO SET ICON
//			tempFrame.setIconImage(ImageIO.read(Bomberfrau_Main.class.getResourceAsStream("images/Icon.png")));
//		} catch (Exception error) {
//			System.out.println("The Window Icon couldn't be loaded!");
//		}
		
		tempFrame.setSize(WIDTH, HEIGHT);
		
		tempFrame.requestFocus();
		
		return tempFrame;
	}

	
}
