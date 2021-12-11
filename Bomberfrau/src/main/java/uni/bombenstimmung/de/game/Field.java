package uni.bombenstimmung.de.game;

public class Field {
    
    private int width;
    private int length;
    private FieldContent content;
    
    public Field(int w, int l, FieldContent c) {
	width = w;
	length = l;
	content = c;
    }
    
    public void setContent (FieldContent t) {
	content = t;
    }
    
    public FieldContent getContent() {
	return content;
    }

}
