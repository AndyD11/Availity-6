import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import lombok.Data;

import java.io.*;
import java.util.*;

@Data
public class Processor {

    private String input;
    private String output;
    private Comparator<Patient> comparator;

    public Processor(String input, String output) {
        this.input = input;
        this.output = output;
        this.comparator = new LastFirstNameComparator();
    }

    public void processFile() {
        try {
            List<String[]> csvData = getData();

            Map<String, InsuranceCompany> insuranceCompanies = separateByInsuranceCompany(csvData);

            String[] header = {"User ID", "Name", "Version", "Insurance Company"};
            for (InsuranceCompany insuranceCompany : insuranceCompanies.values()) {
                String outputFile = output + "/" + insuranceCompany.getName() + ".csv";

                List<Patient> patientList = insuranceCompany.getPatientList();
                patientList.sort(comparator);
                List<String[]> mappedPatientList = patientList.stream().map(Patient::toArray).toList();

                writeData(outputFile, header, mappedPatientList);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected Map<String, InsuranceCompany> separateByInsuranceCompany(List<String[]> patientList) {
        Map<String, InsuranceCompany> insuranceCompanies = new HashMap<>();
        for (String[] patientArray : patientList) {
            try {
                Patient newPatient = createPatient(patientArray);
                String companyName = newPatient.getInsuranceCompany();

                if (!insuranceCompanies.containsKey(companyName)) {
                    InsuranceCompany newInsuranceCompany = new InsuranceCompany(companyName);
                    insuranceCompanies.put(companyName, newInsuranceCompany);
                }

                insuranceCompanies.get(companyName).addPatient(newPatient);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return insuranceCompanies;
    }

    protected Patient createPatient(String[] patientArray) {
        String userID = patientArray[0];
        String firstName = patientArray[1].split(" ")[0];
        String lastName = patientArray[1].split(" ")[1];
        int version = Integer.parseInt(patientArray[2]);
        String insuranceCompany = patientArray[3];

        return new Patient(userID, firstName, lastName, version, insuranceCompany);
    }

    protected List<String[]> getData() throws IOException, CsvException {
        FileReader fileReader = new FileReader(input);
        CSVReader csvReader = new CSVReaderBuilder(fileReader).withSkipLines(1).build();

        List<String[]> csvData = csvReader.readAll();

        csvReader.close();

        return  csvData;
    }

    protected void writeData(String fileName, String[] header, List<String[]> data) throws IOException {
        File file = new File(fileName);
        FileWriter outputFile = new FileWriter(file);

        CSVWriter writer = new CSVWriter(outputFile,
                CSVWriter.DEFAULT_SEPARATOR,
                CSVWriter.NO_QUOTE_CHARACTER,
                CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                CSVWriter.RFC4180_LINE_END);

        writer.writeNext(header);
        writer.writeAll(data);

        writer.close();
    }

}
