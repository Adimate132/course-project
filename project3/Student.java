
import java.util.ArrayList;

public class Student implements Comparable<Student> {
    private String studentID;
    private String name;
    private int age;
    private String major;
    private ArrayList<Double> grades;
    private double gpa;

    // default constructor
    public Student(){
        studentID = "";
        name = "";
        age = 0;
        major = "";
        grades = new ArrayList<Double>();
    }
    // overload 1 (id, name, age, major)
    public Student(String id, String name, int age, String major)  {
        studentID = id;
        this.name = name;
        this.age = age;
        this.major = major;
        grades = new ArrayList<Double>();
    }
    // overload 2 (id, name, age, major, grades)
    public Student(String id, String name, int age, String major, ArrayList<Double> grades)  {
        studentID = id;
        this.name = name;
        this.age = age;
        this.major = major;
        this.grades = grades;
    }
    // overload 3 (id, name, age, major, grades, gpa)
    public Student(String id, String name, int age, String major, ArrayList<Double> grades, Double gpa)  {
        studentID = id;
        this.name = name;
        this.age = age;
        this.major = major;
        this.grades = grades;
        this.gpa = gpa;
    }

    // setters
    public void setAge(int a) {
        age = a;
    }
    public int getAge() {
        return age;
    }
    public void setStudentID(String s) {
        studentID = s;
    }
    public String getStudentID() {
        return studentID;
    }
    public void setName(String n) {
        name = n;
    }
    public void setMajor(String m) {
        major = m;
    }
    public void setGpa(double gpa) {
        this.gpa = gpa;
    }
    
    // getters 
    public String getName() {
        return name;
    }
    public String getMajor() {
        return major;
    }
    public ArrayList<Double> getGrades(){
        return grades;
    }
    public double getGpa() {
        return gpa;
    }
    
    // methods
    public void addGrade(Double g){
        grades.add(g);
    }
    public void addGrade(double g){
        grades.add(g);
    }



    @Override
    public String toString(){
        return "Student ID: " + studentID + ", Name: " + name + ", Age: " + age + ", Major: " + major + ", Grades: " + grades +  ", GPA: " + gpa;
    }

    @Override
    public int compareTo(Student other) {
        return Double.compare(other.gpa, this.gpa); // compare gpa (decending)
    }

    
}
