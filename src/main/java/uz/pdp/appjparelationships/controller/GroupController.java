package uz.pdp.appjparelationships.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import uz.pdp.appjparelationships.entity.Faculty;
import uz.pdp.appjparelationships.entity.Group;
import uz.pdp.appjparelationships.payload.GroupDto;
import uz.pdp.appjparelationships.repository.FacultyRepository;
import uz.pdp.appjparelationships.repository.GroupRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/group")
public class GroupController {

    @Autowired
    GroupRepository groupRepository;
    @Autowired
    FacultyRepository facultyRepository;

    //VAZIRLIK UCHUN
    //READ
    @GetMapping
    public Page<Group> getGroups(@RequestParam int page) {

        Pageable pageable = PageRequest.of(page, 10);
        Page<Group> groups = groupRepository.findAllBy(pageable);
        return groups;
    }

    //UNIVERSITET MAS'UL XODIMI UCHUN
    @GetMapping("/byUniversityId/{universityId}")
    public Page<Group> getGroupsByUniversityId(@PathVariable Integer universityId,@RequestParam int page) {

        Pageable pageable = PageRequest.of(page, 10);
        Page<Group> groupPage = groupRepository.findAllByFaculty_University_Id(universityId, pageable);
        return groupPage;
    }

    //Fakultet hodimi uchun
    @GetMapping("/byUniversityId/{facultyId}")
    public Page<Group> getGroupByFacultyId(@PathVariable Integer facultyId, @RequestParam int page){

        Pageable pageable = PageRequest.of(page, 10);
        Page<Group> groupPage = groupRepository.findAllByFaculty_Id(facultyId, pageable);
        return groupPage;
    }

    //CREATE
    @PostMapping
    public String addGroup(@RequestBody GroupDto groupDto) {

        Group group = new Group();
        group.setName(groupDto.getName());

        Optional<Faculty> optionalFaculty = facultyRepository.findById(groupDto.getFacultyId());
        if (!optionalFaculty.isPresent()) {
            return "Such faculty not found";
        }

        group.setFaculty(optionalFaculty.get());

        groupRepository.save(group);
        return "Group added";
    }

    //UPDATE
    @PutMapping(value = "/{id}")
    public String editGroup(@PathVariable Integer id, @RequestBody GroupDto groupDto){
        if (groupRepository.existsById(id)){
            if (!groupRepository.existsByNameAndFacultyId(groupDto.getName(),groupDto.getFacultyId())){
                Optional<Group> optionalGroup = groupRepository.findById(id);
                Optional<Faculty> optionalFaculty = facultyRepository.findById(groupDto.getFacultyId());
                Group group = optionalGroup.get();
                group.setFaculty(optionalFaculty.get());
                group.setName(groupDto.getName());
                groupRepository.save(group);
                return "Group edited";

            }else
                return "This group name already exists at faculty";
        }else
            return "Not found group";


    }

    //DELETE
    @DeleteMapping(value = "/{id}")
    public String deleteGroup(@PathVariable Integer id){
        if (groupRepository.existsById(id)){
            groupRepository.deleteById(id);
            return "Group deleted";
        }else
            return "Not found group";
    }

}
