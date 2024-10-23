import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.List;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

// custom packages
import utilities.*;

public class Main {
    public static void main(String[] args) {
        List<Student> students = new ArrayList<>();
        Scanner sc = new Scanner(System.in);
        int input = -1;
        String givenFile = "./data/students_data.txt";
        String currentFile = "current_students.txt";

        readFile(givenFile, students); // read file and populate student object

        while(input != 5) {
            try {
                // display options
                System.out.print(
                    "Select an option:\n" +
                    "1. Add Student\n" +
                    "2. Update Student File\n" +
                    "3. Sort Students by GPA\n" + 
                    "4. Search Student by ID\n" + 
                    "5. Exit\n\nEnter an option: "
                );
                input = sc.nextInt();

                if (input == 1) {
                    addStudent(students);
                } else if (input == 2) {
                    System.out.println("\nUpdating file...");
                    System.out.println("Complete! Check \"current_students.txt\" file for new version.\n");
                    updateFile(currentFile, students);
                } else if (input == 3) {
                    // Sort students by GPA
                    students = sortStudentsByGPA(students);
                    System.out.println("\nStudents sorted by GPA. Update file to see results.\n");
                } else if (input == 4) {
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
}




