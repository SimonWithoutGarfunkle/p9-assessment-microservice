package medilabo.com.assessment.service;

import medilabo.com.assessment.model.PatientAssessmentDTO;
import medilabo.com.assessment.model.PatientDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;

@Service
public class AssessmentService {

    private static Logger logger = LoggerFactory.getLogger(AssessmentService.class);


    /**
     * Convert PatientDTO to PatientAssessmentDTO and assess him with riskAssessment method
     * @param patient to assess
     * @param score number of DECLENCHEURS (trigger words)
     * @return String risk level
     */
    public String riskAssessmentPatientDTO(PatientDTO patient, long score) {
        logger.info("assessing patient n°"+patient.getIdPatient()+ " with score : "+score);
        return riskAssessment(convertPatientToPatientAssessment(patient, score));
    }

    /**
     * Performs a risk assessment based on the provided patient's assessment data.
     *
     * @param patient The patient's assessment data
     * @return String indicating the risk level
     */
    public String riskAssessment(PatientAssessmentDTO patient) {
        if (apparitionPrecoce(patient)) {
            return "Early onset";
        } else if (enDanger(patient)) {
            return "In Danger";
        } else if (risqueLimite(patient)) {
            return "Borderline";
        } else {
            //le cahier des charges ne couvre pas le cas "1 seul déclencheur"
            //ce cas n'étant pas dans "risque limité", il sera considéré comme "aucun risque"
            return "None";
        }
    }

    /**
     * Checks if the patient's assessment falls within the limited risk range.
     *
     * @param patient The patient's assessment data
     * @return true if the assessment falls within the limited risk range, otherwise false
     */
    private boolean risqueLimite(PatientAssessmentDTO patient) {
        long triggerCount = patient.getAssessmentScore();
        int age = calculateAge(patient);
        return triggerCount >= 2 && triggerCount <= 5 && age > 30;
    }

    /**
     * Checks if the patient's assessment indicates they are in danger.
     *
     * @param patient The patient's assessment data
     * @return true if the assessment indicates danger, otherwise false
     */
    private boolean enDanger(PatientAssessmentDTO patient) {
        long triggerCount = patient.getAssessmentScore();
        int age = calculateAge(patient);
        boolean isMale = patient.getGenre().equalsIgnoreCase("HOMME");

        return (isMale && age < 30 && triggerCount >= 3) ||
                (!isMale && age < 30 && triggerCount >= 4) ||
                (age >= 30 && triggerCount >= 6);
    }

    /**
     * Checks if the patient's assessment indicates an early onset of risk.
     *
     * @param patient The patient's assessment data
     * @return true if the assessment indicates early onset, otherwise false
     */
    private boolean apparitionPrecoce(PatientAssessmentDTO patient) {
        long triggerCount = patient.getAssessmentScore();
        int age = calculateAge(patient);
        boolean isMale = patient.getGenre().equalsIgnoreCase("HOMME");

        return (isMale && age < 30 && triggerCount >= 5) ||
                (!isMale && age < 30 && triggerCount >= 7) ||
                (age >= 30 && triggerCount >= 8);
    }

    /**
     * Calculates the patient's age based on their date of birth.
     *
     * @param patient The patient's assessment data
     * @return The calculated age of the patient
     */
    private int calculateAge(PatientAssessmentDTO patient) {
        LocalDate today = LocalDate.now();
        LocalDate birthDate = patient.getDateNaissance().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return Period.between(birthDate, today).getYears();

    }

    /**
     * Converts the patient's general information to an assessment-specific DTO.
     *
     * @param patientDTO The patient's general information
     * @param score The assessment score
     * @return PatientAssessmentDTO containing assessment-specific data
     */
    public PatientAssessmentDTO convertPatientToPatientAssessment (PatientDTO patientDTO, long score) {
        Date date;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            date = dateFormat.parse(patientDTO.getDateNaissance());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        PatientAssessmentDTO patient = new PatientAssessmentDTO(
                patientDTO.getIdPatient(), patientDTO.getGenre(), score, date);

        return patient;
    }
}
