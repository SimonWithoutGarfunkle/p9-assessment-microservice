package medilabo.com.assessment.controller;

import medilabo.com.assessment.client.RequestDoctorsMicroservice;
import medilabo.com.assessment.model.PatientDTO;
import medilabo.com.assessment.service.AssessmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import static medilabo.com.assessment.configuration.URLConfig.URL_PATIENT;

/**
 * Rest Controller for the Assessment Microservice
 * Request data from Backends Patient and Doctor to assess the risk for the patient
 */
@RestController
@RequestMapping("/api/assessment")
public class AssessmentController {

    @Autowired
    private AssessmentService assessmentService;

    @Autowired
    private RequestDoctorsMicroservice requestDoctorsMicroservice;

    @Autowired
    private RestTemplate restTemplate;

    private static Logger logger = LoggerFactory.getLogger(AssessmentController.class);


    public AssessmentController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     *Get info of the patient from back end Patient
     * @param id of the patient
     * @return patient
     */
    public PatientDTO getPatientDTO(Integer id) {
        ResponseEntity<PatientDTO> response = restTemplate.exchange(URL_PATIENT+id, HttpMethod.GET, null, PatientDTO.class);
        return response.getBody();
    }


    /**
     * Assess the risk level of the patient
     * @param id of the patient to assess
     * @return String risk level
     */
    @GetMapping("/{id}")
    public String assessPatient(@PathVariable(value = "id") Integer id) {
        logger.debug("Assessing patient n°"+id);
        PatientDTO patient = getPatientDTO(id);
        long score = requestDoctorsMicroservice.getAnalyse(id);
        logger.info("score received = "+score);
        return assessmentService.riskAssessmentPatientDTO(patient, score);
    }
}
