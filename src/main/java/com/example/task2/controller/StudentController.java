package com.example.task2.controller;

import com.example.task2.entity.Address;
import com.example.task2.entity.Group;
import com.example.task2.entity.Student;
import com.example.task2.entity.Subject;
import com.example.task2.payload.StudentDto;
import com.example.task2.repository.AddressRepository;
import com.example.task2.repository.GroupRepository;
import com.example.task2.repository.StudentRepository;
import com.example.task2.repository.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/student")
public class StudentController {
    @Autowired
    StudentRepository studentRepository;
    @Autowired
    GroupRepository groupRepository;
    @Autowired
    AddressRepository addressRepository;
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
    public Page<Student> getStudentListForUniversity(@PathVariable Integer universityId,
                                                     @RequestParam int page) {
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

    @GetMapping("/forDecanat/{facultyId}")
    public Page<Student> getStudentForDecanat(@PathVariable Integer facultyId,
                                              @RequestParam int page) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<Student> studentPage = studentRepository.findAllByGroup_FacultyId(pageable, facultyId);
        return studentPage;
    }

    //4. GROUP OWNER
    @GetMapping("/forOwner/{groupId}")
    public Page<Student> getAllSt(@PathVariable Integer groupId,
                                  @RequestParam int page) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<Student> students = studentRepository.findAllByGroup_FacultyId(pageable, groupId);
        return students;
    }

    @PostMapping("/add")
    public String addStudent(@RequestBody StudentDto studentDto) {

        Address address = addressRepository.findById(studentDto.getAddressId()).get();
        Group group = groupRepository.findById(studentDto.getGroupId()).get();
        List<Subject> subjects = subjectRepository.findAllById(studentDto.getSubjects());
        Student student = new Student(studentDto.getFirstName(),
                studentDto.getLastName(), address, group, subjects);
        return "added";
    }

    @DeleteMapping("delete/{id}")
    public String delete(@PathVariable Integer id) {
        studentRepository.deleteById(id);
        return "deleted";
    }

    @PutMapping("edite/{id}")
    public String edite(@PathVariable Integer id,
                        @RequestBody StudentDto studentDto) {
        Optional<Student> studentOpt = studentRepository.findById(id);
        if (studentOpt.isPresent()) {
            Student student = studentOpt.get();
            Address address = addressRepository.findById(studentDto.getAddressId()).get();
            Group group = groupRepository.findById(studentDto.getGroupId()).get();
            List<Subject> subjects = subjectRepository.findAllById(studentDto.getSubjects());
            student.setAddress(address);
            student.setGroup(group);
            student.setFirstName(studentDto.getFirstName());
            student.setLastName(studentDto.getLastName());
            student.setSubjects(subjects);
            studentRepository.save(student);
            return "Edited";
        }
        return "Not found";
    }
}
