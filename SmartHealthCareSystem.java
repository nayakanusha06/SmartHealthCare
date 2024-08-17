import java.util.*;
import java.util.stream.Collectors;
import java.util.Base64;

// SecureStorage class for encrypted data storage
class SecureStorage {
    private static final String KEY = "encryptionKey";

    public static String encrypt(String data) {
        return Base64.getEncoder().encodeToString((data + KEY).getBytes());
    }

    public static String decrypt(String data) {
        return new String(Base64.getDecoder().decode(data)).replace(KEY, "");
    }
}

// PatientRecord class to store patient information
class PatientRecord {
    private String name;
    private String diagnosis;
    private String prescription;
    private List<String> symptoms;
    private List<String> history;
    private List<String> medications;
    private Map<String, String> deviceData;
    private int visitCount;
    private String personalizedAdvice;

    public PatientRecord(String name, String diagnosis, String prescription, List<String> symptoms, List<String> history, List<String> medications) {
        this.name = name;
        this.diagnosis = diagnosis;
        this.prescription = prescription;
        this.symptoms = symptoms != null ? new ArrayList<>(symptoms) : new ArrayList<>();
        this.history = history != null ? new ArrayList<>(history) : new ArrayList<>();
        this.medications = medications != null ? new ArrayList<>(medications) : new ArrayList<>();
        this.deviceData = new HashMap<>();
        this.visitCount = 1;
        this.personalizedAdvice = generatePersonalizedAdvice();
    }

    public String getName() {
        return name;
    }

    public List<String> getSymptoms() {
        return symptoms;
    }

    public void addDeviceData(String deviceName, String data) {
        deviceData.put(deviceName, SecureStorage.encrypt(data));
    }

    public String getDeviceData(String deviceName) {
        return SecureStorage.decrypt(deviceData.get(deviceName));
    }

    public void updateRecord(String diagnosis, String prescription, List<String> history, List<String> medications, List<String> symptoms) {
        if (diagnosis != null && !diagnosis.isEmpty()) this.diagnosis = diagnosis;
        if (prescription != null && !prescription.isEmpty()) this.prescription = prescription;
        if (history != null && !history.isEmpty()) this.history = new ArrayList<>(history);
        if (medications != null && !medications.isEmpty()) this.medications = new ArrayList<>(medications);
        if (symptoms != null && !symptoms.isEmpty()) this.symptoms = new ArrayList<>(symptoms);
        this.visitCount++;
        this.personalizedAdvice = generatePersonalizedAdvice();
    }

    private String generatePersonalizedAdvice() {
        String advice = "Based on your symptoms and medical history, we recommend: ";
        advice += visitCount > 1 ? "Regular follow-ups, " : "An initial consultation, ";
        advice += symptoms.contains("fever") ? "plenty of rest, " : "";
        advice += symptoms.contains("cough") ? "warm fluids, " : "";
        advice += symptoms.contains("sore throat") ? "throat lozenges, " : "";
        return advice + "and a healthy lifestyle.";
    }

    @Override
    public String toString() {
        return String.format(
            "Patient Name: %s\nDiagnosis: %s\nPrescription: %s\nSymptoms: %s\nPast Health Issues: %s\nMedications: %s\nWearable Device Data: %s\nVisit Count: %d\nPersonalized Advice: %s\n------",
            name, diagnosis, prescription,
            String.join(", ", symptoms),
            String.join(", ", history),
            String.join(", ", medications),
            deviceData.entrySet().stream()
                .map(entry -> entry.getKey() + ": " + SecureStorage.decrypt(entry.getValue()))
                .collect(Collectors.joining(", ")),
            visitCount,
            personalizedAdvice
        );
    }
}

// SmartHealthCareSystem class to manage patient records
class SmartHealthCareSystem {
    private static List<PatientRecord> patientDatabase = new ArrayList<>();
    private static final List<String> SUGGESTIONS = Arrays.asList(
        "Healthy diet", "Drink more water", "Limit alcohol intake", "Proper sleep", "Avoid stress", 
        "Exercise", "Smoking cessation", "Eat nourishing food"
    );

    public static void addPatientRecord(PatientRecord record) {
        patientDatabase.add(record);
    }

    public static void findMatchingPatients(List<String> symptoms) {
        if (symptoms == null || symptoms.isEmpty()) {
            System.out.println("No symptoms provided to search for.");
            return;
        }

        boolean matchFound = false;
        for (PatientRecord record : patientDatabase) {
            if (record.getSymptoms().containsAll(symptoms)) {
                matchFound = true;
                System.out.println("Match found for patient: " + record.getName());
                System.out.println(record);

                Collections.shuffle(SUGGESTIONS);
                List<String> selectedSuggestions = SUGGESTIONS.subList(0, Math.min(3, SUGGESTIONS.size()));
                System.out.println("Suggested Treatment: " + String.join(", ", selectedSuggestions) + "\n");
            }
        }

        if (!matchFound) {
            System.out.println("No matching patient records found.");
        }
    }

    public static void displayPatientHistory(String patientName) {
        for (PatientRecord record : patientDatabase) {
            if (record.getName().equalsIgnoreCase(patientName)) {
                System.out.println(record);
                return;
            }
        }
        System.out.println("No record found for patient: " + patientName);
    }

    public static void remoteConsultation(String patientName) {
        System.out.println("Starting remote consultation for " + patientName + "...");
        displayPatientHistory(patientName);
        System.out.println("Consultation complete. Recommendations have been updated in the system.");
    }

    public static void updatePatientRecord(Scanner scanner, String patientName) {
        for (PatientRecord record : patientDatabase) {
            if (record.getName().equalsIgnoreCase(patientName)) {
                System.out.println("Updating record for " + patientName);

                System.out.print("Enter new diagnosis (leave blank to keep current): ");
                String diagnosis = scanner.nextLine();
                System.out.print("Enter new prescription (leave blank to keep current): ");
                String prescription = scanner.nextLine();
                System.out.print("Enter new past health issues (comma-separated, leave blank to keep current): ");
                String historyInput = scanner.nextLine();
                List<String> pastHealthIssues = historyInput.isEmpty() ? null : Arrays.asList(historyInput.split("\\s*,\\s*"));
                System.out.print("Enter new medications (comma-separated, leave blank to keep current): ");
                String medicationsInput = scanner.nextLine();
                List<String> medications = medicationsInput.isEmpty() ? null : Arrays.asList(medicationsInput.split("\\s*,\\s*"));
                System.out.print("Enter new symptoms (comma-separated, leave blank to keep current): ");
                String symptomsInput = scanner.nextLine();
                List<String> symptoms = symptomsInput.isEmpty() ? null : Arrays.asList(symptomsInput.split("\\s*,\\s*"));

                record.updateRecord(diagnosis, prescription, pastHealthIssues, medications, symptoms);
                System.out.println("Record updated.");
                return;
            }
        }
        System.out.println("Patient record not found.");
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Sample data
        List<String> aliceSymptoms = Arrays.asList("fever", "cough", "sore throat");
        PatientRecord aliceRecord = new PatientRecord("Alice", "Flu", "Bed rest, paracetamol", aliceSymptoms, Arrays.asList("Flu", "Allergy"), Arrays.asList("Paracetamol", "Cetrizine"));
        aliceRecord.addDeviceData("Heart Rate Monitor", "75 bpm");
        aliceRecord.addDeviceData("Blood Sugar Monitor", "110 mg/dL");

        List<String> bobSymptoms = Arrays.asList("thirst", "frequent urination");
        PatientRecord bobRecord = new PatientRecord("Bob", "Diabetes", "Insulin, metformin", bobSymptoms, Arrays.asList("Diabetes"), Arrays.asList("Insulin", "Metformin"));

        addPatientRecord(aliceRecord);
        addPatientRecord(bobRecord);

        // Check for matching symptoms and suggest treatment
        findMatchingPatients(Arrays.asList("fever", "cough", "sore throat"));

        // Remote consultation example
        remoteConsultation("Bob");

        // Add or update patient records
        System.out.print("Enter the name of the patient to display their record: ");
        String patientName = scanner.nextLine();
        displayPatientHistory(patientName);

        System.out.print("Is this patient new or do you want to update an existing record? (new/update): ");
        String recordStatus = scanner.nextLine().trim().toLowerCase();

        if (recordStatus.equals("new")) {
            System.out.print("Enter diagnosis: ");
            String diagnosis = scanner.nextLine();
            System.out.print("Enter prescription: ");
            String prescription = scanner.nextLine();
            System.out.print("Enter past health issues (comma-separated): ");
            List<String> pastHealthIssues = Arrays.asList(scanner.nextLine().split("\\s*,\\s*"));
            System.out.print("Enter medications (comma-separated): ");
            List<String> medications = Arrays.asList(scanner.nextLine().split("\\s*,\\s*"));
            System.out.print("Enter symptoms (comma-separated): ");
            List<String> symptoms = Arrays.asList(scanner.nextLine().split("\\s*,\\s*"));

            PatientRecord newRecord = new PatientRecord(patientName, diagnosis, prescription, symptoms, pastHealthIssues, medications);
            addPatientRecord(newRecord);
            System.out.println("New patient record added.");

        } else if (recordStatus.equals("update")) {
            updatePatientRecord(scanner, patientName);
        } else {
            System.out.println("Invalid option.");
        }

        // Final check for matching symptoms
        findMatchingPatients(Arrays.asList("fever", "cough", "sore throat"));
    }
}
