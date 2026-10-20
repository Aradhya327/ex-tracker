import java.rmi.Naming;

public class client {
    public static void main(String[] args) {
        try {
            Calculator calc = (Calculator) Naming.lookup("rmi://localhost/Calculator");
            System.out.println("Addition: " + calc.add(5, 3));
            System.out.println("Subtraction: " + calc.subtract(5, 3));
            System.out.println("Multiplication: " + calc.multiply(5, 3));
            System.out.println("Division: " + calc.divide(5, 3));
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}