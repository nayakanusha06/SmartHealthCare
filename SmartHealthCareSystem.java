import java.util.*;
import java.util.stream.Collectors;
import java.util.Base64;

// Simulated encryption for sensitive data storage (simplified for demonstration)
class SecureStorage {
    private static final String KEY = "encryptionKey";

    public static String encrypt(String data) {
        // Basic encryption simulation (not secure in real-life applications)
        return Base64.getEncoder().encodeToString((data + KEY).getBytes());
    }

    public static String decrypt(String data) {
        return new String(Base64.getDecoder().decode(data)).replace(KEY, "");
    }
}

// Patient record class to hold patient details
class PatientRecord {
    String name;
    String diagnosis;
    String prescription;
    List<String> symptoms;
    List<String> history;
    List<String> medications;
    Map<String, String> deviceData;  // Simulated wearable device data

    public PatientRecord(String name, String diagnosis, String prescription, List<String> symptoms, List<String> history, List<String> medications) {
        this.name = name;
        this.diagnosis = diagnosis;
        this.prescription = prescription;
        this.symptoms = symptoms;
        this.history = history;
        this.medications = medications;
        this.deviceData = new HashMap<>();
    }

    public void addDeviceData(String deviceName, String data) {
        this.deviceData.put(deviceName, SecureStorage.encrypt(data)); // Store data securely
    }

    public String getDeviceData(String deviceName) {
        return SecureStorage.decrypt(this.deviceData.get(deviceName));
    }
}

// Smart health care system
class SmartHealthCareSystem {
    private static List<PatientRecord> patientDatabase = new ArrayList<>();
    private static final List<String> SUGGESTIONS = Arrays.asList(
        "Healthy diet",
        "Drink more water",
        "Limit alcohol intake",
        "Proper sleep",
        "Avoid stress",
        "Exercise",
        "Smoking cessation",
        "Eat nourishing food"
    );

    // Method to add a new patient record to the database
    public static void addPatientRecord(PatientRecord record) {
        patientDatabase.add(record);
    }

    // Method to find patient records based on symptoms and suggest treatments
    public static void findMatchingPatients(List<String> symptoms) {
        System.out.println("Searching for patients with matching symptoms...");
        for (PatientRecord record : patientDatabase) {
            if (record.symptoms.containsAll(symptoms)) {
                System.out.println("Match found for patient: " + record.name);
                System.out.println("Diagnosis: " + record.diagnosis);
                System.out.println("Prescription: " + record.prescription);

                // Randomly select a few suggestions for the patient
                Collections.shuffle(SUGGESTIONS); // Shuffle the list to randomize suggestions
                List<String> selectedSuggestions = SUGGESTIONS.subList(0, Math.min(3, SUGGESTIONS.size())); // Select up to 3 suggestions

                System.out.println("Suggested Treatment: " + String.join(", ", selectedSuggestions) + "\n");
            }
        }
    }

    // Method to print all records of a specific patient, including hereditary issues
    public static void displayPatientHistory(String patientName) {
        System.out.println("Patient history for " + patientName + ":");
        for (PatientRecord record : patientDatabase) {
            if (record.name.equalsIgnoreCase(patientName)) {
                System.out.println("Diagnosis: " + record.diagnosis);
                System.out.println("Prescription: " + record.prescription);
                System.out.println("Symptoms: " + String.join(", ", record.symptoms));
                System.out.println("Past Health Issues: " + String.join(", ", record.history));
                System.out.println("Medications: " + String.join(", ", record.medications));
                System.out.println("Wearable Device Data: " + record.deviceData.entrySet().stream()
                        .map(entry -> entry.getKey() + ": " + SecureStorage.decrypt(entry.getValue()))
                        .collect(Collectors.joining(", ")));
                System.out.println("------");
            }
        }
    }

    // Mock remote consultation (telemedicine)
    public static void remoteConsultation(String patientName) {
        System.out.println("Starting remote consultation for " + patientName + "...");
        // Simulate the consultation process (this could be expanded with more details)
        System.out.println("Doctor is reviewing patient history...");
        displayPatientHistory(patientName);
        System.out.println("Consultation complete. Recommendations have been updated in the system.");
    }

    public static void main(String[] args) {
        // Creating some patient records for the organized database
        List<String> aliceSymptoms1 = Arrays.asList("fever", "cough", "sore throat");
        List<String> aliceHistory = Arrays.asList("Flu", "Allergy");
        List<String> aliceMedications = Arrays.asList("Paracetamol", "Cetrizine");
        PatientRecord aliceRecord1 = new PatientRecord("Alice", "Flu", "Bed rest, paracetamol", aliceSymptoms1, aliceHistory, aliceMedications);

        // Simulating wearable device data for Alice
        aliceRecord1.addDeviceData("Heart Rate Monitor", "75 bpm");
        aliceRecord1.addDeviceData("Blood Sugar Monitor", "110 mg/dL");

        List<String> bobSymptoms = Arrays.asList("thirst", "frequent urination");
        List<String> bobHistory = Arrays.asList("Diabetes");
        List<String> bobMedications = Arrays.asList("Insulin", "Metformin");
        PatientRecord bobRecord = new PatientRecord("Bob", "Diabetes", "Insulin, metformin", bobSymptoms, bobHistory, bobMedications);

        addPatientRecord(aliceRecord1);
        addPatientRecord(bobRecord);

        // Display patient history for Alice
        displayPatientHistory("Alice");

        // Check for matching symptoms and suggest treatment
        List<String> newPatientSymptoms = Arrays.asList("fever", "cough", "sore throat");
        findMatchingPatients(newPatientSymptoms);

        // Remote consultation mock-up
        remoteConsultation("Bob");

        // Adding a new patient and checking if their symptoms match an existing record
        List<String> newPatientSymptoms2 = Arrays.asList("thirst", "frequent urination");
        List<String> newPatientHistory = new ArrayList<>();
        List<String> newPatientMedications = new ArrayList<>();
        PatientRecord newPatientRecord = new PatientRecord("Charlie", "Undiagnosed", "Pending", newPatientSymptoms2, newPatientHistory, newPatientMedications);

        addPatientRecord(newPatientRecord);
        findMatchingPatients(newPatientSymptoms2);

        // Ask for patient name and record status
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the name of the patient to display their record: ");
        String patientName = scanner.nextLine();
        displayPatientHistory(patientName);

        // Ask if the patient is new or to update an existing record
        System.out.print("Is this patient new or to update an existing record? (new/update): ");
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
            // Update existing record
            boolean recordFound = false;
            for (PatientRecord record : patientDatabase) {
                if (record.name.equalsIgnoreCase(patientName)) {
                    recordFound = true;
                    System.out.println("Updating record for " + patientName);

                    System.out.print("Enter new diagnosis (leave blank to keep current): ");
                    String diagnosis = scanner.nextLine();
                    if (!diagnosis.isEmpty()) record.diagnosis = diagnosis;

                    System.out.print("Enter new prescription (leave blank to keep current): ");
                    String prescription = scanner.nextLine();
                    if (!prescription.isEmpty()) record.prescription = prescription;

                    System.out.print("Enter new past health issues (comma-separated, leave blank to keep current): ");
                    List<String> pastHealthIssues = scanner.nextLine().isEmpty() ? record.history : Arrays.asList(scanner.nextLine().split("\\s*,\\s*"));
                    record.history = pastHealthIssues;

                    System.out.print("Enter new medications (comma-separated, leave blank to keep current): ");
                    List<String> medications = scanner.nextLine().isEmpty() ? record.medications : Arrays.asList(scanner.nextLine().split("\\s*,\\s*"));
                    record.medications = medications;

                    System.out.print("Enter new symptoms (comma-separated, leave blank to keep current): ");
                    List<String> symptoms = scanner.nextLine().isEmpty() ? record.symptoms : Arrays.asList(scanner.nextLine().split("\\s*,\\s*"));
                    record.symptoms = symptoms;

                    System.out.println("Record updated.");
                    break;
                }
            }
            if (!recordFound) {
                System.out.println("Patient record not found.");
            }
        } else {
            System.out.println("Invalid option.");
        }

        // Final search for matching symptoms
        System.out.println("Searching for patients with matching symptoms...");
        findMatchingPatients(Arrays.asList("fever", "cough", "sore throat")); // Example symptoms, adjust as needed
    }
}
