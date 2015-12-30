
public class Student implements Table{

	private String firstName;
	private String lastName;
	private String rollNumber;
	
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getRollNumber() {
		return rollNumber;
	}
	public void setRollNumber(String rollNumber) {
		this.rollNumber = rollNumber;
	}
	
	public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Student Table - ");
        sb.append("Column1" + getFirstName());
        sb.append(", ");
        sb.append("Column2:" + getLastName());
        sb.append(", ");
        sb.append("Column3:" + getRollNumber());
        sb.append(".");
        return sb.toString();
	}
}
