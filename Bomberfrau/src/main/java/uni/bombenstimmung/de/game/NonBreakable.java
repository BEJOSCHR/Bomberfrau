package uni.bombenstimmung.de.game;

public class NonBreakable {

    private FieldContent type;
    
    public NonBreakable(FieldContent t) {
	type = t;
    }
    
    public FieldContent getFieldContent() {
	return type;
    }
    
    public void setFieldContent(FieldContent t) {
	type = t;
    }

}
