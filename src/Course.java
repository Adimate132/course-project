import java.util.ArrayList;
import java.util.List;

public class Course {
    private String courseID;
    private String courseName;
    private String instructor;
    private List<String> students = new ArrayList<>();

    public Course(String id, String cN, String i, ArrayList<String> s) {
        courseID = id;
        courseName = cN;
        instructor = i;
        students = s;
    }

    public Course(String id,String cN, String i){
        courseID = id;
        courseName = cN;
        instructor = i;
    }



    public String getCourseName() {
        return courseName;
    }

    public List<String> getStudents() {
        return students;
    }

    public String getCourseID() {
        return courseID;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public void setStudents(String s) {
        students.add(s);
    }

    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }

    public String getInstructor() {
        return instructor;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }


    @Override
    public String toString(){
        return "Course ID: " + courseID + " | Course Name: " + courseName + " | Instructor Name: " + instructor +" | Students: " + students;
    }
}
