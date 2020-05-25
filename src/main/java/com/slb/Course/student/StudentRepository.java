package com.slb.Course.student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class StudentRepository {

    @Autowired
    private final JdbcTemplate jdbcTemplate;

    public StudentRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    List<Student> selectAllStudents() {
        String sql = "SELECT * FROM student";
        return jdbcTemplate.query(sql, mapStudentFromDb());
    }

    private RowMapper<Student> mapStudentFromDb() {
        return (resultSet, i) -> {
            String studentIDStr = resultSet.getString(1);
            UUID studentID = UUID.fromString(studentIDStr);

            String firstName = resultSet.getString(2);
            String lastName = resultSet.getString(3);
            String email = resultSet.getString(4);

            String genderStr = resultSet.getString(5).toUpperCase();
            Student.Gender gender = Student.Gender.valueOf(genderStr);

            return new Student(studentID, firstName, lastName, email, gender);

        };
    }

    public int insertStudent(UUID newStudentId, Student student) {
        String sql = "INSERT INTO student values (?,?,?,?,?)";
        return jdbcTemplate.update(sql,
                newStudentId, student.getFirstName(), student.getLastName(),
                student.getEmail(), student.getGender().name().toUpperCase());
    }

    public List<StudentCourse> selectAllCoursesForStudent(UUID studentId) {
        String sql = "SELECT student.student_id, course.course_id, course.name, course.description, course.department," +
                " course.teacher_name, student_course.start_date, student_course.end_date, student_course.grade " +
                "  FROM student JOIN student_course USING (student_id) JOIN course USING (course_id) " +
                " WHERE student.student_id = ? ";

        return jdbcTemplate.query(sql, new Object[]{studentId}, mapStudentCourseFromDb());

    }

    private RowMapper<StudentCourse> mapStudentCourseFromDb() {
        return (resultSet, i) ->
                new StudentCourse(
                        UUID.fromString(resultSet.getString("student_id")),
                        UUID.fromString(resultSet.getString("course_id")),
                        resultSet.getString("name"),
                        resultSet.getString("description"),
                        resultSet.getString("department"),
                        resultSet.getString("teacher_name"),
                        resultSet.getDate("start_date").toLocalDate(),
                        resultSet.getDate("end_date").toLocalDate(),
                        Optional.ofNullable(resultSet.getString("grade")).map(Integer::parseInt).orElse(null)
                );

        }
}
