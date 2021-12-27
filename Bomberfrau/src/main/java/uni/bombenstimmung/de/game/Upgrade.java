/*	Diese Klasse ist von Dennis
 * 	Sie dient dazu, die Upgrades im Spiel zu verwalten.
 */

package uni.bombenstimmung.de.game;

public class Upgrade {
    
    private FieldContent type = FieldContent.UPGRADE_ITEM_SHOE;
    private int value;
    private int lifetime;
    private int upgradetype;
    
    public Upgrade() {
	// TODO Auto-generated constructor stub
    }
    
    public void upgradeItem(int v, int ut) {
	//Soll entscheiden was Upgegraded wird und dann Upgraden
    }
    
    public FieldContent getFieldContent() {
	return type;
    }
    

    public void setValue(int v) {
	value = v;
    }

    public int getValue() {	
	return value;
    }
    
    public void disappear() {
	
    }

}
