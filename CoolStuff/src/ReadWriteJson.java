import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;

public class ReadWriteJson {

	public static void main(String[] str) {
		JSonObject obj = new JSonObject();
		obj.setSomeInt(6);
		obj.setSomeString("ok..this is good");
		List<String> testList = new ArrayList<String>();
		testList.add("A");
		testList.add("B");
		testList.add("C");
		testList.add("D");
		obj.setMessages(testList);

		JSonObject json = new JSonObject();
		ObjectMapper mapper = new ObjectMapper();
		try {
			// mapper.writeValue(new File("C:\\jsonfile\\"), json);
			JSonObject obj2 = mapper.readValue(new File("C:\\jsonfile\\testjson.json"), JSonObject.class);
			System.out.println(obj2.getSomeInt());
			System.out.println(obj2.getSomeString());
			System.out.println(obj2.getMessages());
			// System.out.println(obj2);
		} catch (Exception e) {
			System.out.println("OK...some problem");
		}
	}
}
