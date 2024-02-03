package Assign3;

import java.util.*;

/**
 * This class represents the University Course Management System.<br>
 * It has some nested enums and main function.
 */
public class UniversityCourseManagementSystem {

    /**
     * <b>Enum representing the type of data.</b><br>
     * The usage of this enumeration is the following:<br>
     * To distinguish, for which type of Class (Student, Professor, etc.) the method should check the constraints.
     */
    public enum TypeOfData {
        COURSE,
        STUDENT,
        PROFESSOR,
        COURSE_LEVEL
    }

    /**
     * <b>Enum representing the type of mistake.</b><br>
     * This enum is used in the following way:<br>
     * Each time when any of this mistakes have appeared in any part of the program,
     * I add to the List<TypeOfMistake> this mistake and then, in the main function,
     * Where I call the method, I print the minimal mistake in the List. In this way
     * I can reach the priority of mistakes.
     */
    public enum TypeOfMistake {
        COURSE_EXISTS("Course exists"),
        STUDENT_ALREADY_ENROLLED("Student is already enrolled in this course"),
        STUDENT_MAX_LOAD("Maximum enrollment is reached for the student"),
        COURSE_FULL("Course is full"),
        STUDENT_NOT_ENROLLED("Student is not enrolled in this course"),
        PROFESSOR_MAX_LOAD("Professor's load is complete"),
        PROFESSOR_ALREADY_TEACHING("Professor is already teaching this course"),
        PROFESSOR_NOT_TEACHING("Professor is not teaching this course"),
        WRONG_INPUTS("Wrong inputs");

        private final String nameOfMistake;

        /**
         * Constructor for TypeOfMistake.
         * @param nameOfMistake The name of the mistake.
         */
        TypeOfMistake(String nameOfMistake) {
            this.nameOfMistake = nameOfMistake;
        }

        /**
         * Get the name of the mistake.
         * @return The name of the mistake.
         */
        public String getNameOfMistake() {
            return this.nameOfMistake;
        }
    }

    /**
     * List of courses.
     */
    static final List<Course> courses = new ArrayList<>();

    /**
     * List of students.
     */
    static final List<Student> students = new ArrayList<>();

    /**
     * List of professors.
     */
    static final List<Professor> professors = new ArrayList<>();

    /**
     * List of mistakes.
     */
    static final List<TypeOfMistake> mistakes = new ArrayList<>();

    /**
     * Scanner for user input.
     */
    private static final Scanner scanner = new Scanner(System.in);

    /**
     * Main method.
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        fillInitialData();
        String command;

        try {
            while (scanner.hasNextLine()) {
                command = scanner.nextLine();
                switch (command) {
                    case "course": {
                        if (!readCourse()) {
                            if (!mistakes.isEmpty()) System.out.println(Collections.min(mistakes).getNameOfMistake());
                            return;
                        }
                        System.out.println("Added successfully");
                        break;
                    }
                    case "student": {
                        if (!readStudent()) {
                            System.out.println(Collections.min(mistakes).getNameOfMistake());
                            return;
                        }
                        System.out.println("Added successfully");
                        break;
                    }
                    case "professor": {
                        if (!readProfessor()) {
                            System.out.println(Collections.min(mistakes).getNameOfMistake());
                            return;
                        }
                        System.out.println("Added successfully");
                        break;
                    }
                    case "enroll": {
                        if (!enrollToCourse()) {
                            System.out.println(Collections.min(mistakes).getNameOfMistake());
                            return;
                        }
                        System.out.println("Enrolled successfully");
                        break;
                    }
                    case "drop": {
                        if (!dropFromCourse()) {
                            System.out.println(Collections.min(mistakes).getNameOfMistake());
                            return;
                        }
                        System.out.println("Dropped successfully");
                        break;
                    }
                    case "teach": {
                        if (!assignToCourse()) {
                            System.out.println(Collections.min(mistakes).getNameOfMistake());
                            return;
                        }
                        System.out.println("Professor is successfully assigned to teach this course");
                        break;
                    }
                    case "exempt": {
                        if (!exemptFromCourse()) {
                            System.out.println(Collections.min(mistakes).getNameOfMistake());
                            return;
                        }
                        System.out.println("Professor is exempted");
                        break;
                    }
                }
            }
        }
        catch (Exception ignored) {
            System.out.println("Wrong inputs");
        }

        scanner.close();
    }

    /**
     * This method reads the data for the command "course" and add the course to the corresponding list.
     * @return true if all program did well without any mistakes, otherwise false.
     */
    public static boolean readCourse() {
        String courseName = scanner.nextLine(), courseLevel = scanner.nextLine();
        if (CheckMistakes.isCommand(courseName) || CheckMistakes.isCommand(courseLevel)) {
            mistakes.add(TypeOfMistake.WRONG_INPUTS);
            return false;
        }

        if (!(CheckMistakes.checkConstraints(courseName, TypeOfData.COURSE) && CheckMistakes.checkConstraints(courseLevel, TypeOfData.COURSE_LEVEL))) return false;
        courses.add(new Course(courseName, CourseLevel.setByName(courseLevel)));
        return true;
    }

    /**
     * This method exempt professor from course, which he teaches.
     * @return true if all program did well without any mistakes, otherwise false.
     */
    public static boolean exemptFromCourse() {
        String professorId = scanner.nextLine(), courseId = scanner.nextLine();
        if (CheckMistakes.isCommand(professorId) || CheckMistakes.isCommand(courseId)) {
            mistakes.add(TypeOfMistake.WRONG_INPUTS);
            return false;
        }

        int professorIndex, courseIndex;
        try {
            professorIndex = findIndex(Integer.parseInt(professorId), TypeOfData.PROFESSOR);
            courseIndex = Integer.parseInt(courseId) - 1;
            if (professorIndex == -1 || courseIndex < 0 || courseIndex >= courses.size()) throw new ArrayIndexOutOfBoundsException();
        }
        catch (Exception ignored) {
            mistakes.add(TypeOfMistake.WRONG_INPUTS);
            return false;
        }

        return professors.get(professorIndex).exempt(courses.get(courseIndex));
    }

    /**
     * This method assign professor for this course to teach.
     * @return true if all program did well without any mistakes, otherwise false.
     */
    public static boolean assignToCourse() {
        String professorId = scanner.nextLine(), courseId = scanner.nextLine();
        if (CheckMistakes.isCommand(professorId) || CheckMistakes.isCommand(courseId)) {
            mistakes.add(TypeOfMistake.WRONG_INPUTS);
            return false;
        }

        int professorIndex, courseIndex;
        try {
            professorIndex = findIndex(Integer.parseInt(professorId), TypeOfData.PROFESSOR);
            courseIndex = Integer.parseInt(courseId) - 1;
            if (professorIndex == -1 || courseIndex < 0 || courseIndex >= courses.size()) throw new ArrayIndexOutOfBoundsException();
        }
        catch (Exception ignored) {
            mistakes.add(TypeOfMistake.WRONG_INPUTS);
            return false;
        }

        return professors.get(professorIndex).teach(courses.get(courseIndex));
    }

    /**
     * This method drops exact student from exact course.
     * @return true if all program did well without any mistakes, otherwise false.
     */
    public static boolean dropFromCourse() {
        String studentId = scanner.nextLine(), courseId = scanner.nextLine();
        if (CheckMistakes.isCommand(studentId) || CheckMistakes.isCommand(courseId)) {
            mistakes.add(TypeOfMistake.WRONG_INPUTS);
            return false;
        }

        int studentIndex, courseIndex;
        try {
            studentIndex = findIndex(Integer.parseInt(studentId), TypeOfData.STUDENT);
            courseIndex = Integer.parseInt(courseId) - 1;
            if (studentIndex == -1 || courseIndex < 0 || courseIndex >= courses.size()) throw new ArrayIndexOutOfBoundsException();
        }
        catch (Exception ignored) {
            mistakes.add(TypeOfMistake.WRONG_INPUTS);
            return false;
        }

        return students.get(studentIndex).drop(courses.get(courseIndex));
    }

    /**
     * This method enrolls the student to the course.
     * @return true if all program did well without any mistakes, otherwise false.
     */
    public static boolean enrollToCourse() {
        String studentId = scanner.nextLine(), courseId = scanner.nextLine();
        if (CheckMistakes.isCommand(studentId) || CheckMistakes.isCommand(courseId)) {
            mistakes.add(TypeOfMistake.WRONG_INPUTS);
            return false;
        }

        int studentIndex, courseIndex;
        try {
            studentIndex = findIndex(Integer.parseInt(studentId), TypeOfData.STUDENT);
            courseIndex = Integer.parseInt(courseId) - 1;
            if (studentIndex == -1 || courseIndex < 0 || courseIndex >= courses.size()) throw new ArrayIndexOutOfBoundsException();
        }
        catch (Exception ignored) {
            mistakes.add(TypeOfMistake.WRONG_INPUTS);
            return false;
        }

        return students.get(studentIndex).enroll(courses.get(courseIndex));
    }

    /**
     * This method reads the data for the command "professor" and add the professor to the corresponding list.
     * @return true if all program did well without any mistakes, otherwise false.
     */
    public static boolean readProfessor() {
        String professorName = scanner.nextLine();
        if (CheckMistakes.isCommand(professorName)) {
            mistakes.add(TypeOfMistake.WRONG_INPUTS);
            return false;
        }
        if (!CheckMistakes.checkConstraints(professorName, TypeOfData.PROFESSOR)) return false;
        professors.add(new Professor(professorName));
        return true;
    }

    /**
     * This method reads the data for the command "student" and add the student to the corresponding list.
     * @return true if all program did well without any mistakes, otherwise false.
     */
    public static boolean readStudent() {
        String studentName = scanner.nextLine();
        if (CheckMistakes.isCommand(studentName)) {
            mistakes.add(TypeOfMistake.WRONG_INPUTS);
            return false;
        }
        if (!CheckMistakes.checkConstraints(studentName, TypeOfData.STUDENT)) return false;
        students.add(new Student(studentName));
        return true;
    }

    /**
     * This method manually fills the lists of courses, students, and professors with initial data.
     */
    public static void fillInitialData() {
        courses.add(new Course("java_beginner", CourseLevel.BACHELOR));
        courses.add(new Course("java_intermediate", CourseLevel.BACHELOR));
        courses.add(new Course("python_basics", CourseLevel.BACHELOR));
        courses.add(new Course("algorithms", CourseLevel.MASTER));
        courses.add(new Course("advanced_programming", CourseLevel.MASTER));
        courses.add(new Course("mathematical_analysis", CourseLevel.MASTER));
        courses.add(new Course("computer_vision", CourseLevel.MASTER));

        students.add(new Student("Alice"));
        students.get(students.size() - 1).enroll(courses.get(0));
        students.get(students.size() - 1).enroll(courses.get(1));
        students.get(students.size() - 1).enroll(courses.get(2));

        students.add(new Student("Bob"));
        students.get(students.size() - 1).enroll(courses.get(0));
        students.get(students.size() - 1).enroll(courses.get(3));

        students.add(new Student("Alex"));
        students.get(students.size() - 1).enroll(courses.get(4));

        professors.add(new Professor("Ali"));
        professors.get(professors.size() - 1).teach(courses.get(0));
        professors.get(professors.size() - 1).teach(courses.get(1));

        professors.add(new Professor("Ahmed"));
        professors.get(professors.size() - 1).teach(courses.get(2));
        professors.get(professors.size() - 1).teach(courses.get(4));

        professors.add(new Professor("Andrey"));
        professors.get(professors.size() - 1).teach(courses.get(5));
    }

    /**
     * This method finds index of students or professors by their memberId.
     * @param memberId the ID of student or professor.
     * @param type type of data (COURSE, STUDENT, PROFESSOR, or COURSE_LEVEL).
     * @return -1 if something went wrong, otherwise the index in the corresponding list of the member.
     */
    public static int findIndex(int memberId, TypeOfData type) {
        if (type == TypeOfData.STUDENT) {
            for (int i = 0; i < students.size(); i++) {
                if (students.get(i).getMemberId() == memberId) return i;
            }
            return -1;
        }
        if (type == TypeOfData.PROFESSOR) {
            for (int i = 0; i < professors.size(); i++) {
                if (professors.get(i).getMemberId() == memberId) return i;
            }
            return -1;
        }
        return -1;
    }
}

class CheckMistakes {
    /**
     * This method checks if field satisfies constraints, switching between type of fields.
     * @param field the string that has to be checked.
     * @param type type of data (COURSE, STUDENT, PROFESSOR, or COURSE_LEVEL).
     * @return true if no mistakes were encountered, otherwise false.
     */
    public static boolean checkConstraints(String field, UniversityCourseManagementSystem.TypeOfData type) {
        switch (type) {
            case COURSE: {
                if (CheckMistakes.incorrectLetters(field, true)) {
                    UniversityCourseManagementSystem.mistakes.add(UniversityCourseManagementSystem.TypeOfMistake.WRONG_INPUTS);
                    return false;
                }
                if (CheckMistakes.incorrectUnderscore(field)) {
                    UniversityCourseManagementSystem.mistakes.add(UniversityCourseManagementSystem.TypeOfMistake.WRONG_INPUTS);
                    return false;
                }

// Check if List of courses already contains current course (ignoring letter case)
                for (Course element : UniversityCourseManagementSystem.courses) {
                    if (element.getCourseName().equalsIgnoreCase(field)) {
                        UniversityCourseManagementSystem.mistakes.add(UniversityCourseManagementSystem.TypeOfMistake.COURSE_EXISTS);
                        return false;
                    }
                }
                break;
            }
            case STUDENT: {
                if (CheckMistakes.incorrectLetters(field, false)) {
                    UniversityCourseManagementSystem.mistakes.add(UniversityCourseManagementSystem.TypeOfMistake.WRONG_INPUTS);
                    return false;
                }

// Check if List of students already contains current course (ignoring letter case)
                for (Student element : UniversityCourseManagementSystem.students) {
                    if (element.getMemberName().equalsIgnoreCase(field)) {
                        UniversityCourseManagementSystem.mistakes.add(UniversityCourseManagementSystem.TypeOfMistake.WRONG_INPUTS);
                        return false;
                    }
                }
                break;
            }
            case PROFESSOR: {
                if (CheckMistakes.incorrectLetters(field, false)) {
                    UniversityCourseManagementSystem.mistakes.add(UniversityCourseManagementSystem.TypeOfMistake.WRONG_INPUTS);
                    return false;
                }

// Check if List of professors already contains current course (ignoring letter case)
                for (Professor element : UniversityCourseManagementSystem.professors) {
                    if (element.getMemberName().equalsIgnoreCase(field)) {
                        UniversityCourseManagementSystem.mistakes.add(UniversityCourseManagementSystem.TypeOfMistake.WRONG_INPUTS);
                        return false;
                    }
                }
                break;
            }
            case COURSE_LEVEL: {
// Check if course_level does not equal to specific names
                if (!(field.equals("bachelor") || field.equals("master"))) {
                    UniversityCourseManagementSystem.mistakes.add(UniversityCourseManagementSystem.TypeOfMistake.WRONG_INPUTS);
                    return false;
                }
                break;
            }
            default: return false;
        }
        return true;
    }

    /**
     * This method checks if symbols are only English letters or underscore.
     * @param field the string that has to be checked.
     * @param underscore shows if underscore can or cannot be in the string.
     * @return true if no mistakes were encountered, otherwise false.
     */
    public static boolean incorrectLetters(String field, boolean underscore) {
        for (char symbol : field.toCharArray()) {
            if (!((int) symbol >= 65 && (int) symbol <= 90 || (int) symbol >= 97 && (int) symbol <= 122 || underscore && (int) symbol == 95)) {
                UniversityCourseManagementSystem.mistakes.add(UniversityCourseManagementSystem.TypeOfMistake.WRONG_INPUTS);
                return true;
            }
        }
        return false;
    }

    /**
     * This method checks if both sides from underscore(s) has at least one English character.
     * @param field the string that has to be checked,
     * @return true if no mistakes were encountered, otherwise false.
     */
    public static boolean incorrectUnderscore(String field) {
        if (field.charAt(0) == '_' || field.charAt(field.length() - 1) == '_') {
            UniversityCourseManagementSystem.mistakes.add(UniversityCourseManagementSystem.TypeOfMistake.WRONG_INPUTS);
            return true;
        }
        for (int i = 1; i < field.length() - 1; i++) {
            if (field.charAt(i) == '_' && (field.charAt(i - 1) == '_' || field.charAt(i + 1) == '_')) {
                UniversityCourseManagementSystem.mistakes.add(UniversityCourseManagementSystem.TypeOfMistake.WRONG_INPUTS);
                return true;
            }
        }
        return false;
    }

    /**
     * This method checks if current line is command.
     * @param field the string that has to be checked.
     * @return true if no mistakes were encountered, otherwise false.
     */
    public static boolean isCommand(String field) {
        List<String> commands = Arrays.asList("course", "student", "professor", "enroll", "drop", "teach", "exempt");
        return commands.contains(field);
    }
}

/**
 * This class represents the course data.
 */
class Course {
    public static final int CAPACITY = 3;
    private static int numberOfCourses = 0;
    private final int courseId;
    private final String courseName;
    private final List<Student> enrolledStudents;
    private final CourseLevel courseLevel;

    /**
     * Constructor of this class, fills with all fields of it.
     * @param courseName the name of the course.
     * @param courseLevel the level of this course (bachelor or master).
     */
    public Course(String courseName, CourseLevel courseLevel) {
        this.enrolledStudents = new ArrayList<>();
        this.courseName = courseName;
        this.courseLevel = courseLevel;
        this.courseId = ++numberOfCourses;
    }

    /**
     * This method checks if the amount of enrolled students has reached the capacity.
     * @return true if yes, otherwise no.
     */
    public boolean isFull() {
        return this.enrolledStudents.size() == CAPACITY;
    }
    public String getCourseName() {
        return this.courseName;
    }
    public List<Student> getEnrolledStudents() {
        return this.enrolledStudents;
    }
}

/**
 * This enum represents the level of particular course.
 */
enum CourseLevel {
    BACHELOR,
    MASTER;

    /**
     * @param name the name of course level.
     * @return the enum type for this course level.
     */
    public static CourseLevel setByName(String name) {
        return (name.equals("bachelor") ? BACHELOR : MASTER);
    }
}

/**
 * An interface, which is implemented by Student.
 */
interface Enrollable {
    boolean drop(Course course);
    boolean enroll(Course course);
}

/**
 * The abstract class, which is inherited by Student and professor.
 */
abstract class UniversityMember {
    private static int numberOfMembers = 0;
    private final int memberId;
    private final String memberName;

    /**
     * This constructor fills fields with data.
     * @param memberName the name of member.
     */
    public UniversityMember(String memberName) {
        this.memberId = ++numberOfMembers;
        this.memberName = memberName;
    }
    public String getMemberName() {
        return this.memberName;
    }
    public int getMemberId() {
        return this.memberId;
    }
}

/**
 * The class stores data about professor.
 */
class Professor extends UniversityMember {
    public static final int MAX_LOAD = 2;
    private final List<Course> assignedCourses;

    public Professor(String memberName) {
        super(memberName);
        this.assignedCourses = new ArrayList<>();
    }

    /**
     * This method assign professor to teach particular course.
     * @param course the course has to be taught.
     * @return true if no mistakes, otherwise false.
     */
    public boolean teach(Course course) {
        boolean flag = true;
        if (this.assignedCourses.size() == MAX_LOAD) {
            UniversityCourseManagementSystem.mistakes.add(UniversityCourseManagementSystem.TypeOfMistake.PROFESSOR_MAX_LOAD);
            flag = false;
        }
        if (this.assignedCourses.contains(course)) {
            UniversityCourseManagementSystem.mistakes.add(UniversityCourseManagementSystem.TypeOfMistake.PROFESSOR_ALREADY_TEACHING);
            flag = false;
        }
        if (flag) this.assignedCourses.add(course);
        return flag;
    }

    /**
     * This method exempt professor from particular course.
     * @param course the course has to be untaught.
     * @return true if no mistakes, otherwise false.
     */
    public boolean exempt(Course course) {
        boolean flag = true;
        if (!this.assignedCourses.contains(course)) {
            UniversityCourseManagementSystem.mistakes.add(UniversityCourseManagementSystem.TypeOfMistake.PROFESSOR_NOT_TEACHING);
            flag = false;
        }
        if (flag) this.assignedCourses.remove(course);
        return flag;
    }
}

/**
 * This class stores data about student.
 */
class Student extends UniversityMember implements Enrollable {
    public static final int MAX_ENROLLMENT = 3;
    private final List<Course> enrolledCourses;

    /**
     * This constructor fills fields with data.
     * @param memberName the name of member.
     */
    public Student(String memberName) {
        super(memberName);
        this.enrolledCourses = new ArrayList<>();
    }

    /**
     * This method drops student from particular course.
     * @param course the particular course.
     * @return true if no mistakes, otherwise false.
     */
    @Override
    public boolean drop(Course course) {
        boolean flag = true;
        if (!this.enrolledCourses.contains(course) || !course.getEnrolledStudents().contains(this)) {
            UniversityCourseManagementSystem.mistakes.add(UniversityCourseManagementSystem.TypeOfMistake.STUDENT_NOT_ENROLLED);
            flag = false;
        }
        if (flag) {
            this.enrolledCourses.remove(course);
            course.getEnrolledStudents().remove(this);
        }
        return flag;
    }

    /**
     * This method enrolls student to particular course.
     * @param course the particular course.
     * @return true if no mistakes, otherwise false.
     */
    @Override
    public boolean enroll(Course course) {
        boolean flag = true;
        if (this.enrolledCourses.size() == MAX_ENROLLMENT) {
            UniversityCourseManagementSystem.mistakes.add(UniversityCourseManagementSystem.TypeOfMistake.STUDENT_MAX_LOAD);
            flag = false;
        }
        if (this.enrolledCourses.contains(course)) {
            UniversityCourseManagementSystem.mistakes.add(UniversityCourseManagementSystem.TypeOfMistake.STUDENT_ALREADY_ENROLLED);
            flag = false;
        }
        if (course.isFull()) {
            UniversityCourseManagementSystem.mistakes.add(UniversityCourseManagementSystem.TypeOfMistake.COURSE_FULL);
            flag = false;
        }
        if (flag) {
            this.enrolledCourses.add(course);
            course.getEnrolledStudents().add(this);
        }
        return flag;
    }
}
