package ku.cs.models.collections;

import ku.cs.models.persons.Student;
import ku.cs.models.persons.User;

import java.util.ArrayList;
import java.util.List;

public class StudentList {
    private List<Student> students;

    public StudentList() {
        this.students = new ArrayList<>();
    }

    public void addStudents(Student student) {
        if (!this.students.contains(student)) {
            this.students.add(student);
        }
    }

    public void addStudent(String id, String email, String firstName, String lastName, String advisor, User adder) {
        if (findStudentById(id) == null){
            students.add(new Student(id, email, firstName, lastName, advisor, adder));
        }
    }

    public void addStudent(String id, String email, String firstName, String lastName, User adder) {
        if (findStudentById(id) == null){
            students.add(new Student(id, email, firstName, lastName, adder));
        }
    }

    public Student findStudentById(String id) {
        for (Student student : this.students) {
            if (student.getId().equals(id)) {
                return student;
            }
        }
        return null;
    }

    public List<Student> getStudents() {
        return students;
    }
}
