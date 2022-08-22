import java.util.Comparator;

public class LastFirstNameComparator implements Comparator<Patient> {

    @Override
    public int compare(Patient p1, Patient p2) {
        int n;

        n = p1.getLastName().compareTo(p2.getLastName());
        if (n != 0) return n;

        return p1.getFirstName().compareTo(p2.getFirstName());
    }
}
