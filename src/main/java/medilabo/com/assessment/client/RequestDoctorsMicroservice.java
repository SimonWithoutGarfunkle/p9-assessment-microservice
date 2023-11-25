package medilabo.com.assessment.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import static medilabo.com.assessment.configuration.Constants.DECLENCHEURS;
import static medilabo.com.assessment.configuration.URLConfig.URL_DOCTOR;

/**
 * Request doctors microservice to get the number of trigger words in the patient history
 */
@Service
public class RequestDoctorsMicroservice {

    @Autowired
    private RestTemplate restTemplate;

    public RequestDoctorsMicroservice(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
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
}
