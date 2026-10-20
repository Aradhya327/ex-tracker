import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class calculatorimpl extends UnicastRemoteObject implements Calculator {
     calculatorimpl() throws RemoteException {
        super();
    }
    
    public int add(int a, int b) {
        return a + b;
    }

    public int subtract(int a, int b) {
        return a - b;
    }

    public int multiply(int a, int b) {
        return a * b;
    }

    public double divide(int a, int b) {
        if (b == 0) {
            throw new ArithmeticException("Division by zero");
        }
        return (double) a / b;
    }
}