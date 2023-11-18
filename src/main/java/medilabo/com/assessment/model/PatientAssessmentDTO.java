package medilabo.com.assessment.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
 * Clean Patient model with only usefull data for the assessment
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PatientAssessmentDTO {

    private Integer idPatient;

    private String genre;

    private long assessmentScore;

    private Date dateNaissance;

}
