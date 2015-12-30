import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class JSonObject {
	
	private int someInt;
    private String someString;
    private List<String> messages = null;

    public int getSomeInt() {
		return someInt;
	}

	public void setSomeInt(int someInt) {
		this.someInt = someInt;
	}

	public String getSomeString() {
		return someString;
	}

	public void setSomeString(String someString) {
		this.someString = someString;
	}

	public List<String> getMessages() {
		if(messages == null)
			return Collections.EMPTY_LIST;
		return messages;
	}

	public void setMessages(List<String> messages) {
		this.messages = messages;
	}

}
