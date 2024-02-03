package Lab7.Ex3;

public class Main {
    public static Time difference(Time time1, Time time2) { return new Time(0, 0, time1.getSeconds() - time2.getSeconds()); }
    public static void main(String[] args) {
        Time time1 = new Time(2, 34), time2 = new Time(1);
        time1.increment().increment().increment();
        System.out.println(time1);
        System.out.println(difference(time1, time2));
    }
}
