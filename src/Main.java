import java.io.*;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.List;

// custom packages
import utilities.*;

public class Main {
    public static void main(String[] args) {
        List<Student> students = new ArrayList<>();
        List<Course> courses = new ArrayList<>();
        Scanner sc = new Scanner(System.in);
        int input = -1;
        String givenFile = "src/data/students_data.txt";
        String currentFile = "current_students.txt";
        readFile(givenFile, students); // read file and populate student object
        String givenCoursesFile = "src/data/courses_data.txt";
        String currentCoursesFile = "current_courses.txt";
        readCoursesFile(givenCoursesFile, courses); //read file and populate courses object

        while(input != 8) {
            try {
                // display options
                System.out.print(
                        "Select an option:\n" +
                                "1. Add Student\n" +
                                "2. Update Student File\n" +
                                "3. Update Course File\n" +
                                "4. Sort Students by GPA\n" +
                                "5. Search Student by ID\n" +
                                "6. Add Course\n" +
                                "7. Enroll Student in Course\n" +
                                "8. Exit\n\nEnter an option: "
                );
                input = sc.nextInt();

                if (input == 1) {
                    addStudent(students);
                } else if (input == 2) {
                    System.out.println("\nUpdating file...");
                    System.out.println("Complete! Check \"current_students.txt\" file for new version.\n");
                    updateFile(currentFile, students);
                }else if (input == 3) {
                    System.out.println("\nUpdating file...");
                    System.out.println("Complete! Check \"current_courses.txt\" file for new version.\n");
                    updateCoursesFile(currentCoursesFile, courses);
                }else if (input == 4) {
                    // Sort students by GPA
                    students = sortStudentsByGPA(students);
                    System.out.println("\nStudents sorted by GPA. Update file to see results.\n");
                } else if (input == 5) {
                    // Search student by ID
                    System.out.print("Enter Student ID to search: ");
                    sc.nextLine(); // consume newline
                    String studentID = sc.nextLine();
                    boolean studentFound = false;
                    
                    // traverse students list
                    for (int i = 0; i < students.size(); i++) {
                        Student currentStudent = students.get(i); // store current student

                        // if current student's ID == entered ID
                        if (currentStudent.getStudentID().equals(studentID)) {
                            studentFound = true;
                            System.out.println("\nStudent found: " + currentStudent.toString() + "\n");
                        }
                    }

                    if (!studentFound) { // student not found
                        System.out.println("\nStudent not found.\n");
                    }

                }
                else if (input == 6) {
                    System.out.print("Enter Course ID: ");
                    sc.nextLine();
                    String courseID = sc.nextLine();
                    System.out.print("Enter Course Name: ");
                    String courseName = sc.nextLine();
                    System.out.print("Enter Instructor Name: ");
                    String instructorName = sc.nextLine();
                    Course newCourse = new Course(courseID, courseName, instructorName);
                    courses.add(newCourse);
                    updateCoursesFile(currentCoursesFile, courses);
                    System.out.println();

                }
                else if (input == 7) {
                    System.out.println("Great let's enroll a Student.");
                    System.out.print("Enter the Student's Student ID that you wish to enroll: ");
                    sc.nextLine();
                    String studentID = sc.nextLine();
                    boolean studentFound = false;
                    boolean courseFound = false;
                    System.out.print("Enter the Course ID of the Course that you wish to enroll in: ");
                    String courseID = sc.nextLine();
                    enrollStudent(courses, students, studentID, courseID);
                    updateCoursesFile(currentCoursesFile, courses);
                }
            } catch (InputMismatchException e) {
                System.out.println("\nInvalid input. Please try again.\n");
            }
        }
        sc.close();
    }

    // Method to add student
    public static void addStudent(List<Student> students) {
        Scanner scnr = new Scanner(System.in);
        ArrayList<Double> allGrades = new ArrayList<>();
        String name, studentID = "", major;
        double GPA = 0.0;
        int age = 0;
        boolean studentExists = false;
 
        System.out.print("Enter student ID: ");
        studentID = scnr.nextLine();
        
        // traverse students list
        for (int i = 0; i < students.size(); i++) {
            Student currentStudent = students.get(i); // store current student

            // if current student's ID == entered ID
            if (currentStudent.getStudentID().equals(studentID)) {
                System.out.println("\nStudent with ID " + studentID + " already exists. Please try again with a new ID.\n");
                studentExists = true;
            }
        }

        if (studentExists) { // return to main selection if student exists
            scnr.close();
            return;
        }
   
        System.out.print("Enter student name: ");
        name = scnr.nextLine();
        System.out.print("Enter student age: ");
        age = scnr.nextInt();
        System.out.print("Enter student Major: ");
        scnr.nextLine(); // ignore
        major = scnr.nextLine();
        
        // add grades ------
        Double grade = 0.0;
        boolean addedGrades = false;
        do {
            try {
                addedGrades = true;
                System.out.print("Enter Grade (-1 to stop): ");
                grade = scnr.nextDouble();
                if (grade != -1) {
                    allGrades.add(grade);
                }
            }
            catch (InputMismatchException e) {
                System.err.println(e.getMessage());
            }
        } while (grade != -1);
        // ------------------

        // if grades not added
        if (!addedGrades) {
            Student newStudent = new Student(studentID, name, age, major); // create student
            students.add(newStudent); // append to list of all students
        }
        else { // grades added
            if (allGrades.size() > 0) { // if at least 1 grade added
                GPA = calculateGPA(allGrades);
                Student newStudent = new Student(studentID, name, age, major, allGrades, GPA); // create student w/ gpa
                students.add(newStudent); // append to list of all students
            }
            else { // less than 1 grade added
                Student newStudent = new Student(studentID, name, age, major, allGrades); // create student
                students.add(newStudent); // append to list of all students
            }

            // display newly added grades & gpa to user
            System.out.println("\nAdded grades: " + allGrades);
            System.out.println("Student GPA: " + GPA + "\n");
        }
            
        scnr.close();
    }

    // method to read from file of 1k students and create student objects
    public static void readFile(String fileName, List<Student> students) {
        FileInputStream infile = null;
        Scanner in = null;
        try { // attempt opening file
            infile = new FileInputStream(fileName);
            in = new Scanner(infile);

            // read & parse file
            while(in.hasNextLine()) { // note 4 segments: id,name,age,[grades]
                String curLine = in.nextLine(); // store current line
                String[] fields = curLine.split(","); // split into array of strings w/ comma as separator

                // store fields
                String id = fields[0];
                String name = fields[1];
                int age = Integer.parseInt(fields[2]);
                String major = fields[3];
                ArrayList<Double> grades = parseGrades(fields[4]);
                final double GPA = calculateGPA(grades);  

                // create new student
                Student newStudent = new Student(id, name, age, major, grades, GPA);
                students.add(newStudent);
                
                // System.out.println(newStudent.toString()); // DEBUG view students created
            }

            in.close();
        }
        catch(IOException e) {
            System.err.println(e.getMessage());
        }
    }

    //method to read from file of courses and create course objects
    public static void readCoursesFile(String fileName, List<Course> courses) {
        FileInputStream infile = null;
        Scanner in = null;
        try { // attempt opening file
            infile = new FileInputStream(fileName);
            in = new Scanner(infile);

            // read & parse file
            while(in.hasNextLine()) { // note 4 segments: id,name,instructor,[students]
                String curLine = in.nextLine(); // store current line
                String[] fields = curLine.split(","); // split into array of strings w/ comma as separator

                // store fields
                String id = fields[0];
                String name = fields[1];
                String instructor = fields[2];

                ArrayList<String> studentsInCourse = parseStudents(fields[3]);


                // create new student
                Course newCourse = new Course(id, name, instructor, studentsInCourse);
                courses.add(newCourse);

                // System.out.println(newStudent.toString()); // DEBUG view students created
            }

            in.close();
        }
        catch(IOException e) {
            System.err.println(e.getMessage());
        }
    }

    // method to parse grades from given file
    public static ArrayList<Double> parseGrades(String grades) {
        ArrayList<Double> parsedGrades = new ArrayList<>();
        String[] splitGrades = grades.split(";"); // split into array of grades with ';' as separator
        
        // traverse grades array
        for (int i = 0; i < splitGrades.length; i++) {
            double grade = Double.parseDouble(splitGrades[i]);
            parsedGrades.add(grade);
        }

        return parsedGrades;
    }

    //method to parse Students from given file for courses
    public static ArrayList<String> parseStudents(String students) {
        ArrayList<String> parsedStudents = new ArrayList<>();
        String[] splitStudents = students.split(";");

        //traverse students array
        for (int i = 0; i < splitStudents.length; i++) {
            parsedStudents.add(splitStudents[i]);
        }
        return parsedStudents;
    }
    
    // method to create new students file
    public static void updateFile(String fileName, List<Student> students) {
        FileOutputStream outfile = null;
        PrintWriter out = null;

        try { // attempt file open / creation
            outfile = new FileOutputStream(fileName, false);
            out = new PrintWriter(outfile);

            // print student to file
            for (int i = 0; i < students.size(); i++) {
                out.println(students.get(i).toString());
            }
        }
        catch(IOException e) {
            System.err.println(e.getMessage());
        }

        out.flush(); // flush buffer
    }

    //method to create new courses file
    public static void updateCoursesFile(String fileName, List<Course> courses) {
        FileOutputStream outfile = null;
        PrintWriter out = null;

        try { // attempt file open / creation
            outfile = new FileOutputStream(fileName, false);
            out = new PrintWriter(outfile);

            // print student to file
            for (int i = 0; i < courses.size(); i++) {
                out.println(courses.get(i).toString());
            }
        }
        catch(IOException e) {
            System.err.println(e.getMessage());
        }

        out.flush(); // flush buffer
    }
    
    // Method to sort students by GPA
    public static List<Student> sortStudentsByGPA(List<Student> students) {
        return SortUtils.mergeSort(students);
    }

    // Method to search student by ID
    public static int findStudentById(List<Student> students, Student targetStudent) {
        return SearchUtils.binarySearch(students, targetStudent);
    }

    // method to calculate gpa
    public static double calculateGPA(ArrayList<Double> grades) {
        // call recursive helper to sum grades
        ArrayList<Double> copy = new ArrayList<>(grades); // create copy of original to avoid rewriting values
        final int SIZE = copy.size();
        final double SUM = sumGrades(copy);
        final double GPA = SUM / SIZE;
        return Math.round(GPA * 100.0) / 100.0;
    }

    // helper for calculateGPA to recursively sum grades
    private static double sumGrades(ArrayList<Double> grades) {
        // base case: num grades in list == 0
        if (grades.size() == 0) {
            return 0.0;
        }
        double curGrade = grades.get(0); // store first item in list
        grades.remove(0); // remove current item from list
        return curGrade + sumGrades(grades); // return sum (will return entire sum once calls slingshot back)
    }

    //method to enroll student in course
    public static void enrollStudent(List<Course> courses, List<Student> students, String studentID, String courseID){
        boolean courseFound = false;
        boolean studentFound = false;
        boolean studentInCourse = false;
        for (int i = 0; i < courses.size(); i++) {
            Course currentCourse = courses.get(i);
            if (courseID.equals(currentCourse.getCourseID())) {
                courseFound = true;
                // traverse students list
                for (int j = 0; j < students.size(); j++) {
                    Student currentStudent = students.get(j); // store current student

                    // if current student's ID == entered ID
                    if (currentStudent.getStudentID().equals(studentID)) {
                        studentFound = true;
                        for (int k = 0; k < currentCourse.getStudents().size(); k++){
                            if (studentID.equals(currentCourse.getStudents().get(k))){
                                studentInCourse = true; //leave
                            }
                        }
                        courses.get(i).setStudents(currentStudent.getStudentID());
                        if (studentInCourse){
                            System.out.println("\nStudent already in course.\n");
                        }
                    }
                }

                if (!studentFound) { // student not found
                    System.out.println("\nStudent not found.\n");
                }
            }
        }
        if (!courseFound) {
            System.out.println("\nCourse not found.\n");
        }
        System.out.println();
    }
}




