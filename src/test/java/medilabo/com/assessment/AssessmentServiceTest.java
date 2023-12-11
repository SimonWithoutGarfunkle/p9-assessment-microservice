package medilabo.com.assessment;

import medilabo.com.assessment.model.PatientAssessmentDTO;
import medilabo.com.assessment.service.AssessmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class AssessmentServiceTest {


    @InjectMocks
    private AssessmentService assessmentService;

    private Date dateJeune = new Date();
    private Date dateVieux = new Date();

    @BeforeEach
    private void setUp() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2000, Calendar.JANUARY, 1);
        dateJeune = calendar.getTime();
        calendar.set(1970, Calendar.JANUARY, 1);
        dateVieux = calendar.getTime();

    }

    @Test
    void testRiskAssessmentPatientDTO_None() {
        // Arrange
        PatientAssessmentDTO testPatient = new PatientAssessmentDTO(1, "HOMME", 1L, dateVieux);

        // Act
        String riskLevel = assessmentService.riskAssessment(testPatient);

        // Assert
        assertEquals("None", riskLevel);
    }


    @Test
    void testRiskAssessmentPatientDTO_Borderline() {
        // Arrange
        PatientAssessmentDTO testPatient = new PatientAssessmentDTO(1, "HOMME", 3L, dateVieux);

        // Act
        String riskLevel = assessmentService.riskAssessment(testPatient);

        // Assert
        assertEquals("Borderline", riskLevel);
    }

    @Test
    void testRiskAssessment_InDanger() {
        // Arrange
        PatientAssessmentDTO testPatient = new PatientAssessmentDTO(1, "HOMME", 4L, dateJeune);

        // Act
        String riskLevel = assessmentService.riskAssessment(testPatient);

        // Assert
        assertEquals("In Danger", riskLevel);
    }

    @Test
    void testRiskAssessment_EarlyOnset() {
        // Arrange
        PatientAssessmentDTO testPatient = new PatientAssessmentDTO(1, "FEMME", 8L, dateJeune);

        // Act
        String riskLevel = assessmentService.riskAssessment(testPatient);

        // Assert
        assertEquals("Early onset", riskLevel);
    }

}