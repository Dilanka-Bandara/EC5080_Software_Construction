// 2022E048
// Bandara H.G.T.D.
// Lab 01 - Student Management System in Java
// I am going to create all the parts in one java file

class Student {
    // Part 1:create the Attributes 
    int id;
    String name;
    int age;
    String grade;
    String regNum;
    String district;
    static int totalStudents; // Static variable to track the total number of students

    // Constructor to initialize attributes 
    Student(int id, String name, int age, String grade, String regNum, String district) {
        this.id = id;
        this.name = name;
        if (age < 0) {
            throw new IllegalArgumentException("Age cannot be negative");
        }
        this.age = age;
        this.grade = grade;
        this.regNum = regNum;
        this.district = district;
        totalStudents++;
    }

    // Default constructor for
    public Student() {
        this(0, "None", 0, "-", "-", "-");
    }

    // Display student details 
    void displayStudentDetails() {
        System.out.println("ID: " + id + ", Name: " + name + ", Age: " + age + ", Grade: " + grade + ", Reg No: " + regNum + ", District: " + district);
    }

    // Display total students
    static void displayTotalStudents() {
        System.out.println("Total Students: " + totalStudents);
    }

    // Update age with validation
    void updateAge(int newAge) {
        if (newAge < 0) {
            System.out.println("Invalid age. Age cannot be negative.");
            return;
        }
        this.age = newAge;
    }

    // Resolve name conflicts using 'this'
    void namespaceConflictExample(String name) {
        this.name = name;
    }

    // Override finalize() to observe garbage collection
    @Override
    protected void finalize() throws Throwable {
        System.out.println("Student object " + name + " is being garbage collected.");
        super.finalize();
    }
}

class StudentMod extends Student {
    // Constructor for StudentMod to initialize values
    StudentMod(int id, String name, int age, String grade, String regNum, String district) {
        super(id, name, age, grade, regNum, district);
    }
    
    // Categorize students using if-else
    void categorizeStudent() {
        if (grade.equals("A") || grade.equals("A+")) {
            System.out.println(name + " is Excellent");
        } else if (grade.equals("A-") || grade.equals("B+")) {
            System.out.println(name + " is Good");
        } else if (grade.equals("B") || grade.equals("B-")) {
            System.out.println(name + " is Average");
        } else {
            System.out.println(name + " Needs Improvement");
        }
    }

    // Categorize students using switch-case
    void categorizeStudentSwitch() {
        switch (grade) {
            case "A":
            case "A+":
                System.out.println(name + " is Excellent");
                break;
            case "A-":
            case "B+":
                System.out.println(name + " is Good");
                break;
            case "B":
            case "B-":
                System.out.println(name + " is Average");
                break;
            default:
                System.out.println(name + " Needs Improvement");
        }
    }
}

class StudentListProcessor {
    // Process students using loops
    void processStudentsUsingLoops(Student[] students) {
        System.out.println("Displaying all students:");
        for (Student student : students) {
            student.displayStudentDetails();
        }

        System.out.println("Students with grades A or A+:");
        int count = 0;
        while (count < students.length) {
            if (students[count].grade.equals("A") || students[count].grade.equals("A+")) {
                students[count].displayStudentDetails();
            }
            count++;
        }

        System.out.println("Displaying student details until age 25 is reached:");
        int i = 0;
        do {
            students[i].displayStudentDetails();
            i++;
        } while (i < students.length && students[i - 1].age < 25);
    }
}

public class Part01 {
    public static void main(String[] args) {
        // Part 1: Creating Student objects
        Student num1 = new Student(1234, "Bandara H.G.T.D.", 24, "A", "2022e048", "Nuwara Eliya");
        Student num2 = new Student(1235, "Wimalasooriye G.H.N.P.", 23, "A", "2022e039", "Matale");
        Student num3 = new Student(1236, "Kumarage H.R.", 22, "B+", "2022e050", "Colombo");
        Student num4 = new Student(1237, "Wijesinghe A.P.", 25, "B", "2022e051", "Kandy");

        // Display student details
        num1.displayStudentDetails();
        num2.displayStudentDetails();
        num3.displayStudentDetails();
        num4.displayStudentDetails();

        // Part 2: Categorizing students
        StudentMod mod1 = new StudentMod(1234, "Bandara H.G.T.D.", 24, "A", "2022e048", "Nuwara Eliya");
        mod1.categorizeStudent();
        mod1.categorizeStudentSwitch();

        StudentMod mod2 = new StudentMod(1235, "Wimalasooriye G.H.N.P.", 23, "A-", "2022e039", "Matale");
        mod2.categorizeStudent();
        mod2.categorizeStudentSwitch();

        // Part 3: Display total students
        Student.displayTotalStudents();

        // Update age and demonstrate variable scope
        num3.updateAge(23);
        num3.namespaceConflictExample("Wijesinghe A.P.");

        // Part 4: Simulate garbage collection
        num1 = null;
        num2 = null;
        num3 = null;
        num4 = null;

        // Request garbage collection
        System.gc();
    }
}
