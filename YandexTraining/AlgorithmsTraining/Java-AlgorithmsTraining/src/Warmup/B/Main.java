package Warmup.B;

import java.util.Scanner;

public class Main {
    public static class Fraction {
    
        private int enumerator, denominator;
    
        public Fraction() {
            this.enumerator = 0;
            this.denominator = 1;
        }
    
        public Fraction(int enumerator, int denominator) {
            if (denominator == 0) {
                System.out.println("Denominator cannot be equal to 0.");
                return;
            }
    
            this.enumerator = enumerator;
            this.denominator = denominator;
            this.reduce();
        }
    
        public void addFractions(Fraction fraction1, Fraction fraction2) {
            this.enumerator = fraction1.enumerator * fraction2.denominator + fraction2.enumerator * fraction1.denominator;
            this.denominator = fraction1.denominator * fraction2.denominator;
            this.reduce();
        }
    
        private void reduce() {
            for (int i = (this.enumerator < this.denominator ? this.enumerator : this.denominator); i >= 2; --i) {
                if (this.enumerator % i == 0 && this.denominator % i == 0) {
                    this.enumerator /= i;
                    this.denominator /= i;
                }
            }
        }
    
        public String toString() {
            return this.enumerator + " " + this.denominator;
        }
    }

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        Fraction fraction1 = new Fraction(scanner.nextInt(), scanner.nextInt()), fraction2 = new Fraction(scanner.nextInt(), scanner.nextInt());

        Fraction resultFraction = new Fraction();
        resultFraction.addFractions(fraction1, fraction2);
        System.out.println(resultFraction);
        
        scanner.close();
    }
}
