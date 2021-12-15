package uz.pdp.appjparelationships.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.appjparelationships.entity.Address;
import uz.pdp.appjparelationships.entity.Subject;

import java.util.List;

public interface SubjectRepository extends JpaRepository<Subject, Integer> {

    List<Subject> findAllByKursNumberAndFacultyId(Integer kursNumber, Integer facultyId);

    Page<Subject> findAllByKursNumberAndFacultyId_Pageble(Integer kursNumber, Integer facultyId, Pageable pageable);

    Page<Subject> findAllBy(Pageable pageable);

    Page<Subject> findAllByFacultyId(Integer facultyId, Pageable pageable);

    Page<Subject> findAllByKursNumber(Integer kursNumber, Pageable pageable);

    boolean existsByNameAndFacultyIdAndKursNumber(String name, Integer facultyId, Integer kursNumber);

}
