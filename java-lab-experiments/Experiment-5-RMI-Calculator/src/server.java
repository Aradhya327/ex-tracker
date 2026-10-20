import java.rmi.Naming;
public class server {
    public static void main(String[] args) {
        try {
            calculatorimpl obj = new calculatorimpl();
            Naming.rebind("rmi://localhost/Calculator", obj);
            System.out.println("Calculator Service is running...");
        } catch (Exception e) {
            System.out.println(e);
            
        }
    }
}