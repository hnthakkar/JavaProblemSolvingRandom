
public class Department implements Table{
	private String deptName;
	private String deptID;
	
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getDeptID() {
		return deptID;
	}
	public void setDeptID(String deptID) {
		this.deptID = deptID;
	}
	
	public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Department Table - ");
        sb.append("Column1" + getDeptName());
        sb.append(", ");
        sb.append("Column2:" + getDeptID());
        sb.append(".");
        return sb.toString();
	}
}
