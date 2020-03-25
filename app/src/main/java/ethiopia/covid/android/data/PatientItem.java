package ethiopia.covid.android.data;

import java.util.Date;

/**
 * Created by BrookMG on 3/25/2020 in ethiopia.covid.android.data
 * inside the project CoVidEt .
 */
public class PatientItem {
    private int id;
    private String patient_nationality; // "Japan",
    private String location; // "Addis Ababa",
    private String recent_travel_to; // "Burkina Faso",
    private String name; // "Not Available",
    private int age; // 48,
    private String gender; // "M"
    private String status; // "ST",
    private Date date_time_announced; // "2020-03-13T12:00:00Z"

    public PatientItem(int id, String patient_nationality, String location, String recent_travel_to, String name, int age, String gender, String status, Date date_time_announced) {
        this.id = id;
        this.patient_nationality = patient_nationality;
        this.location = location;
        this.recent_travel_to = recent_travel_to;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.status = status;
        this.date_time_announced = date_time_announced;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPatient_nationality() {
        return patient_nationality;
    }

    public void setPatient_nationality(String patient_nationality) {
        this.patient_nationality = patient_nationality;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getRecent_travel_to() {
        return recent_travel_to;
    }

    public void setRecent_travel_to(String recent_travel_to) {
        this.recent_travel_to = recent_travel_to;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getDate_time_announced() {
        return date_time_announced;
    }

    public void setDate_time_announced(Date date_time_announced) {
        this.date_time_announced = date_time_announced;
    }
}
