package DTOs;


public class UserDTO {

    private String name;
    private String degree;

    public UserDTO(String Name, String Degree){
        this.name = Name;
        this.degree = Degree;
    }

    public String getName() {
        return name;
    }

    public String getDegree() {
        return degree;
    }
}