package TEST;

public class AC {
	public enum ACtype {passenger, cargo}
	public enum ACsize {large, small}
    protected String id;
    protected ACtype type;
    protected ACsize size;
    
    protected AC(String id, ACtype type, ACsize size) {
    		this.id = id;
    		this.type = type;
    		this.size = size;
    }
}
