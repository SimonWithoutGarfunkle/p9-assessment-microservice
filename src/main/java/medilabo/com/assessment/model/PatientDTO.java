package medilabo.com.assessment.model;


import lombok.Getter;

@Getter
public class PatientDTO {

    private Integer idPatient;

    private String nom;

    private String prenom;

    private String genre;

    private String rue;

    private String dateNaissance;

    private String codePostal;

    private String ville;

    private String telephone;
}
