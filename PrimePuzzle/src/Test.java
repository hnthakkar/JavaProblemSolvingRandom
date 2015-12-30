import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;


public class Test {
	private static Map allOptionMap = new HashMap();
	private static final String[] otherOpt = {"Boradcast All","Restart All"};

	public static void main(String[] arg){
		if (allOptionMap.isEmpty()) {
			List serviceCenters = new ArrayList();
			StringTokenizer st = new StringTokenizer("T6,T7", ",");
			for(int i = 0; i < 3; i++){	
				serviceCenters.clear();
				//SelectItem opt = null;
				String token;
				while (st.hasMoreTokens()) {
					token = st.nextToken();
					System.out.println(token);
					/*opt = new SelectItem();
					String token = st.nextToken();
					opt.setLabel(token);
					opt.setValue(token);
					serviceCenters.add(opt);*/
				}
				//Adding other option
				//SelectItem otherOptItem = null;
				if(i==0){
					/*otherOptItem = new SelectItem();
					otherOptItem.setLabel(otherOpt[i]);
					otherOptItem.setValue("All");*/
				}else if(i==1){
					/*otherOptItem = new SelectItem();
					otherOptItem.setLabel(otherOpt[i]);
					otherOptItem.setValue("All");*/
				}
				//serviceCenters.add(otherOptItem);
				allOptionMap.put(i, serviceCenters);
			}	
		}
	}
}
