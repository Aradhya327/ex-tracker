# Experiment 5 - Calculator using RMI

A calculator whose arithmetic runs in a separate JVM. The client calls methods
on an object that lives in the server process; RMI handles the networking.

## Aim

Build a remote calculator using Java RMI with addition, subtraction,
multiplication and division.

## Concepts

- `Remote` marker interface and `RemoteException`
- `UnicastRemoteObject` for exporting an object so it can be called remotely
- `Naming.rebind` to register an object, `Naming.lookup` to find it
- `rmiregistry` as the naming service
- The client only knows the **interface** - it never sees the implementation class

## Files

| File | Role |
|------|------|
| `src/Calculator.java` | Remote interface - the contract both sides agree on |
| `src/calculatorimpl.java` | Server-side implementation of that interface |
| `src/server.java` | Creates the implementation and registers it |
| `src/client.java` | Looks the object up and calls its methods |

## How the call travels

```
client.java                rmiregistry            server.java
    |                          |                      |
    |                           |  rebind("Calculator")|
    |                           | <--------------------|
    |  lookup("Calculator")     |                      |
    | ------------------------> |                      |
    |  <--- stub -------------- |                      |
    |                                                  |
    |  stub.add(5, 3) --------- network -------------> |
    |                                  calculatorimpl.add()
    |  <---------------- result ---------------------  |
```

## Run

The registry must be running before the server registers itself, and the server
must be running before the client looks anything up.

```powershell
javac -d out src/*.java
```

Terminal 1 - leave this running:

```powershell
rmiregistry
```

Terminal 2:

```powershell
java -cp out server
```

Expected: `Calculator Service is running...`

Terminal 3:

```powershell
java -cp out client
```

Expected:

```
Addition: 8
Subtraction: 2
Multiplication: 15
Division: 1.6666666666666667
```

## The remote interface

```java
public interface Calculator extends Remote {
    int add(int a, int b) throws RemoteException;
    int subtract(int a, int b) throws RemoteException;
    int multiply(int a, int b) throws RemoteException;
    double divide(int a, int b) throws RemoteException;
}
```

Every remote method must declare `throws RemoteException`, because a network
failure can happen on any call. The calls look local in the client source -
that transparency is what RMI provides.

## What to observe

- `Naming.rebind("rmi://localhost/Calculator", obj)` uses a capital `C`. The
  name is case-sensitive, so the client's `lookup` must match exactly or you get
  `NotBoundException`.
- Stop the server after registering, then run the client. The lookup still
  succeeds because the registry is separate, but the first method call fails -
  the registry only holds a reference, not the object.
- `divide` throws `ArithmeticException` on a zero divisor. That is an unchecked
  exception, so it travels back to the client without being declared.
- No `rmic` step is needed. Modern Java generates the stub dynamically at
  runtime, so `javac` and `rmiregistry` are all you need.

## Troubleshooting

| Error | Cause |
|-------|-------|
| `Connection refused to host: localhost` | `rmiregistry` is not running |
| `NotBoundException: Calculator` | Server not started, or the name case differs |
| `ClassNotFoundException` | `-cp out` missing from the `java` command |
