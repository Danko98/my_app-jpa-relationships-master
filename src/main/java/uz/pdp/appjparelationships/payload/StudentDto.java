package uz.pdp.appjparelationships.payload;

import lombok.Data;

@Data
public class StudentDto {
    private String firstName;
    private String lastName;
    private Integer groupId;
    private Integer addressId;
    private Integer kursNumber;  // 1 >= kursNumber <= 4
    private Integer facultyId;

}
