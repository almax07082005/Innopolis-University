package Lab7.Ex3;

public class Time {
    private int seconds;

    public Time(int hours) { this.seconds = hours * 3600; }
    public Time(int hours, int minutes) { this.seconds = hours * 3600 + minutes * 60; }
    public Time(int hours, int minutes, int seconds) { this.seconds = hours * 3600 + minutes * 60 + seconds; }
    public Time increment() {
        this.seconds++;
        return this;
    }
    public int getSeconds() { return this.seconds; }
    public String toString() { return this.seconds / 3600 + " hours " + this.seconds % 3600 / 60 + " minutes " + this.seconds % 3600 % 60 + " seconds"; }
}
