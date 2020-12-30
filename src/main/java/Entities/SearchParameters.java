package Entities;

//Class that stores the search parameters received from the client
public class SearchParameters {
    protected String PatientID;
    protected String Modality[];
    protected String BodyPart[];
    protected String Date[];

    //Getters and setters for the different variables
    public String getPatientID() {
        return PatientID;
    }

    public void setPatientID(String patientID) {
        PatientID = patientID;
    }

    public String[] getModality() {
        return Modality;
    }

    public void setModality(String[] modality) {
        Modality = modality;
    }

    public String[] getBodyPart() {
        return BodyPart;
    }

    public void setBodyPart(String[] bodyPart) {
        BodyPart = bodyPart;
    }

    public String[] getDate() {
        return Date;
    }

    public void setDate(String[] date) {
        Date = date;
    }

}

