import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InsuranceCompany {
    private String name;
    private Map<String, Patient> patientList;

    public InsuranceCompany(String name) {
        this.name = name;
        patientList = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public void addPatient(Patient newPatient) {
        Patient oldPatient = patientList.get(newPatient.getUserID());

        if (oldPatient != null && oldPatient.getVersion() > newPatient.getVersion()) {
            return;
        }

        patientList.put(newPatient.getUserID(), newPatient);
    }

    public List<Patient> getPatientList() {
        return new ArrayList<>(patientList.values());
    }

}
