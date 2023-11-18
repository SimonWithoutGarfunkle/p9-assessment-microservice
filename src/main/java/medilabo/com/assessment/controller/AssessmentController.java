package medilabo.com.assessment.controller;

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
import org.springframework.web.util.UriComponentsBuilder;

import static medilabo.com.assessment.configuration.Constants.DECLENCHEURS;
import static medilabo.com.assessment.configuration.URLConfig.URL_DOCTOR;
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
     * Ask Doctor Backend the number of DECLENCHEURS (trigger words) in the history of the patient
     * @param id of the patient
     * @return long number of DECLENCHEURS in the history of the patient
     */
    public long getAnalyse(Integer id) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        //Add the list of DECLENCHEURS to the request
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(URL_DOCTOR + id + "/analyse");
        for (String mot : DECLENCHEURS) {
            builder.queryParam("mots", mot);
        }

        HttpEntity<?> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<Long> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, requestEntity, Long.class);

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
        long score = getAnalyse(id);
        logger.info("score received = "+score);
        return assessmentService.riskAssessmentPatientDTO(patient, score);
    }
}
