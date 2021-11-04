/**
 * 
 */
package uni.bombenstimmung.de.main;

import uni.bombenstimmung.de.graphics.GraphicsHandler;

/**
 * @author BEJOSCH
 *
 */
public class BomberfrauMain {

	/**
	 * The start of everything (in this program)
	 * @param args The arguments which are added to the start argument
	 */
	public static void main(String[] args) {
		
		System.out.println("Starting Bomberfrau, juhu!");
		
		//Test edit von Tim
		System.out.println("Laeuft");

		//Test edit von Dennis
		System.out.println("Juhu MF");
		
		//Test Jos
		
		//Test edit von Tim auf dem Mac
		System.out.println("Laeuft 2");
		
		//Test von Jos

		System.out.println();
		System.out.println("Test: ");
		for(int i = 99 ; i > 0 ; i--) {
			if(i%9 == 0 && i != 0 && i != 99) {
				System.out.println();
			}
			System.out.print(doubleWriteNumber(i)+" ");
		}
		System.out.println();
		
		//Sart Graphics
		GraphicsHandler.initGraphics();
		
	}

	//Beispiel wie ich mir eine Methode mit Documentation etc vorstellen wuerde (Ob deutsch oder english muessen wir uns noch einigen):
	
	/**
	 * Stellt jede nummer mit min. 2 Stellen dar
	 * @param number
	 * @return Die nummer aber immer in min 2 Stellen
	 */
	private static String doubleWriteNumber(int number) {
		if(number >= 10) {
			return ""+number;
		}else {
			return "0"+number;
		}
	}
	
}
