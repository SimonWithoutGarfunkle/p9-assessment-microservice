package medilabo.com.assessment.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Retrieve URL of other Medilabo microservices from application properties
 */
@Component
public class URLConfig {

    public static String URL_GATEWAY;
    public static String URL_PATIENT;
    public static String URL_DOCTOR;

    @Value("${url.gateway}")
    public void setUrlGateway(String urlGateway) {
        URL_GATEWAY = urlGateway;
    }

    @Value("${url.patient}")
    public void setUrlPatient(String urlPatient) {
        URL_PATIENT = urlPatient;
    }

    @Value("${url.doctor}")
    public void setUrlDoctor(String urlDoctor) {
        URL_DOCTOR = urlDoctor;
    }


}
