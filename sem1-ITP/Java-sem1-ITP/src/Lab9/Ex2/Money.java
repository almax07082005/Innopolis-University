package Lab9.Ex2;

public enum Money {
    ONE(1),
    FIVE(5),
    TEN(10),
    FIFTY(50),
    HUNDRED(100),
    THOUSAND(1000);
    private final int denomination;
    Money(int denomination) {
        this.denomination = denomination;
    }
}
