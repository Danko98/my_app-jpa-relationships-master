package uz.pdp.appjparelationships.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import uz.pdp.appjparelationships.entity.Subject;
import uz.pdp.appjparelationships.repository.SubjectRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/subject")
public class SubjectController {
    @Autowired
    SubjectRepository subjectRepository;

    //CREATE
    @RequestMapping(method = RequestMethod.POST)
    public String addSubject(@RequestBody Subject subject) {
        if (!subjectRepository.existsByNameAndFacultyIdAndKursNumber(subject.getName(),subject.getFacultyId(),subject.getKursNumber())){

            Subject subject1 = new Subject();
            subject1.setName(subject.getName());
            subject1.setKursNumber(subject.getKursNumber());
            subject1.setFacultyId(subject.getFacultyId());

            subjectRepository.save(subject1);
            return "Subject added";
        }else
            return "This subject already exist";
    }

    //READ BY FACULTY ID
    @GetMapping(value = "/byfaculty/{facultyId}")
    public Page<Subject> getSubjectsByFacultyId(@PathVariable Integer facultyId, @RequestParam int page){

        Pageable pageable = PageRequest.of(page, 10);
        Page<Subject> subjectPage = subjectRepository.findAllByFacultyId(facultyId, pageable);
        return subjectPage;
    }

    //READ BY KURS ID
    @GetMapping(value = "/byKursNumber/{kursNumber}")
    public Page<Subject> getSubjectsByKursNumber(@PathVariable Integer kursNumber, @RequestParam int page){

        Pageable pageable = PageRequest.of(page, 10);
        Page<Subject> subjectPage = subjectRepository.findAllByKursNumber(kursNumber, pageable);
        return subjectPage;
    }

    @GetMapping(value = "/byKursNumberAndFacultyId/{kursNumber}/{facultyId}")
    public Page<Subject> getSubKursFacultyId(@PathVariable Integer kursNumber,@PathVariable Integer facultyId, @RequestParam int page){

        Pageable pageable = PageRequest.of(page, 10);
        Page<Subject> subjectPage = subjectRepository.findAllByKursNumberAndFacultyId_Pageble(kursNumber, facultyId, pageable);
        return subjectPage;
    }

    //READ ALL
    @GetMapping
    public Page<Subject> getSubjects(@RequestParam int page) {

        Pageable pageable = PageRequest.of(page, 10);
        Page<Subject> subjectList = subjectRepository.findAllBy(pageable);
        return subjectList;
    }

    //READ
    @GetMapping(value = "/{id}")
    public Subject getSubjectById(@PathVariable Integer id){
        if (subjectRepository.existsById(id)){
            Optional<Subject> optionalSubject = subjectRepository.findById(id);
            return optionalSubject.get();
        }else
            return new Subject();
    }

    //UPDATE
    @PutMapping(value = "/{id}")
    public String editSubject(@PathVariable Integer id, @RequestBody Subject subjectDto){
        if (subjectRepository.existsById(id)){
            if (subjectRepository.existsByNameAndFacultyIdAndKursNumber(subjectDto.getName(),subjectDto.getFacultyId(),subjectDto.getKursNumber())){

                Optional<Subject> optionalSubject = subjectRepository.findById(id);
                Subject subject = optionalSubject.get();
                subject.setFacultyId(subjectDto.getFacultyId());
                subject.setName(subjectDto.getName());
                subject.setKursNumber(subjectDto.getKursNumber());
                subjectRepository.save(subject);

                return "Subject edited";
            }else
                return "This subject already exists";
        }else
            return "Not found subject";
    }

    //DELETE
    @DeleteMapping(value = "/{id}")
    public String deleteSubject(@PathVariable Integer id){
        if (subjectRepository.existsById(id)){
            subjectRepository.deleteById(id);
            return "Subject delted";
        }else
            return "Not found subject";
    }



}
