import lombok.Data;

@Data
public class Patient {
    private String userID;
    private String firstName;
    private String lastName;
    private int version;
    private String insuranceCompany;

    public Patient(String userID, String firstName, String lastName, int version, String insuranceCompany) {
        this.userID = userID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.version = version;
        this.insuranceCompany = insuranceCompany;
    }

    public String[] toArray() {
        String[] patientArray = new String[4];
        patientArray[0] = userID;
        patientArray[1] = firstName + " " + lastName;
        patientArray[2] = String.valueOf(version);
        patientArray[3] = insuranceCompany;
        return patientArray;
    }
}
