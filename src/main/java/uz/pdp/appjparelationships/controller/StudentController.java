package uz.pdp.appjparelationships.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import uz.pdp.appjparelationships.entity.Address;
import uz.pdp.appjparelationships.entity.Group;
import uz.pdp.appjparelationships.entity.Student;
import uz.pdp.appjparelationships.entity.Subject;
import uz.pdp.appjparelationships.payload.StudentDto;
import uz.pdp.appjparelationships.repository.AddressRepository;
import uz.pdp.appjparelationships.repository.GroupRepository;
import uz.pdp.appjparelationships.repository.StudentRepository;
import uz.pdp.appjparelationships.repository.SubjectRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/student")
public class StudentController {
    @Autowired
    StudentRepository studentRepository;

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    GroupRepository groupRepository;

    @Autowired
    SubjectRepository subjectRepository;

    //1. VAZIRLIK
    @GetMapping("/forMinistry")
    public Page<Student> getStudentListForMinistry(@RequestParam int page) {
        //1-1=0     2-1=1    3-1=2    4-1=3
        //select * from student limit 10 offset (0*10)
        //select * from student limit 10 offset (1*10)
        //select * from student limit 10 offset (2*10)
        //select * from student limit 10 offset (3*10)
        Pageable pageable = PageRequest.of(page, 10);
        Page<Student> studentPage = studentRepository.findAll(pageable);
        return studentPage;
    }

    //2. UNIVERSITY
    @GetMapping("/forUniversity/{universityId}")
    public Page<Student> getStudentListForUniversity(@PathVariable Integer universityId, @RequestParam int page) {
        //1-1=0     2-1=1    3-1=2    4-1=3
        //select * from student limit 10 offset (0*10)
        //select * from student limit 10 offset (1*10)
        //select * from student limit 10 offset (2*10)
        //select * from student limit 10 offset (3*10)
        Pageable pageable = PageRequest.of(page, 10);
        Page<Student> studentPage = studentRepository.findAllByGroup_Faculty_UniversityId(universityId, pageable);
        return studentPage;
    }

    //3. FACULTY DEKANAT
    @GetMapping(value = "/forFaculty/{facultyId}")
    public Page<Student> getStudntListForFaculty(@PathVariable Integer facultyId,@RequestParam int page){

        Pageable pageable = PageRequest.of(page, 10);
        Page<Student> studentPage = studentRepository.findAllByGroup_Faculty_Id(facultyId, pageable);
        return studentPage;
    }
    //4. GROUP OWNER

    @GetMapping(value = "/forGroup/{groupId}")
    public Page<Student> getStudentListForGroup(@PathVariable Integer groupId, @RequestParam int page){

        Pageable pageable = PageRequest.of(page,10);
        Page<Student> studentPage = studentRepository.findAllByGroup_Id(groupId,pageable);

        return studentPage;
    }

    @PostMapping
    public String addStudent(@RequestBody StudentDto studentDto){
        if (studentRepository.existsByFirstNameAndLastNameAndAddressId(studentDto.getFirstName(),studentDto.getLastName(),studentDto.getAddressId())){
            if (addressRepository.existsById(studentDto.getAddressId())){
                if (groupRepository.existsById(studentDto.getGroupId())){

                    List<Subject> subjectList = subjectRepository.findAllByKursNumberAndFacultyId(studentDto.getKursNumber(),studentDto.getFacultyId());
                    Optional<Group> optionalGroup = groupRepository.findById(studentDto.getGroupId());
                    Optional<Address> optionalAddress = addressRepository.findById(studentDto.getAddressId());
                    Student student = new Student();
                    student.setGroup(optionalGroup.get());
                    student.setAddress(optionalAddress.get());
                    student.setFirstName(studentDto.getFirstName());
                    student.setLastName(studentDto.getLastName());
                    student.setSubjects(subjectList);
                    studentRepository.save(student);

                    return "Student saved";
                }else
                    return "Not found group";
            }else
                return "Not found address";
        }else
            return "Not found student";

    }

    @PutMapping(value = "/{id}")
    public String editStudent(@RequestBody StudentDto studentDto,     @PathVariable Integer id){
        if (studentRepository.existsById(id)){
            if (addressRepository.existsById(studentDto.getAddressId())){
                if (groupRepository.existsById(studentDto.getGroupId())){
                    if (studentRepository.existsByFirstNameAndLastNameAndAddressId(studentDto.getFirstName(),studentDto.getLastName(),studentDto.getAddressId())){

                        List<Subject> subjectList = subjectRepository.findAllByKursNumberAndFacultyId(studentDto.getKursNumber(), studentDto.getFacultyId());
                        Optional<Address> optionalAddress = addressRepository.findById(studentDto.getAddressId());
                        Optional<Group> optionalGroup = groupRepository.findById(studentDto.getGroupId());
                        Optional<Student> optionalStudent = studentRepository.findById(id);

                        Student student = optionalStudent.get();
                        student.setFirstName(studentDto.getFirstName());
                        student.setLastName(studentDto.getLastName());
                        student.setAddress(optionalAddress.get());
                        student.setGroup(optionalGroup.get());
                        student.setSubjects(subjectList);

                        studentRepository.save(student);

                        return "Student edited";

                    }else
                        return "This student already exists";
                }else
                    return "Not found group";
            }else
                return "Not found address";
        }else
            return "Not found student";
    }

    @DeleteMapping(value = "/{id}")
    public String deleteStudent(@PathVariable Integer id){
        if (studentRepository.existsById(id)){
            studentRepository.deleteById(id);
            return "Student deleted";
        }else
            return "Not found student";
    }


}
