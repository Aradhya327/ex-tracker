# Experiment 4 - TCP Client-Server Communication

Two programs that talk to each other over a TCP socket on port 5000. The
client sends one line, the server prints it and replies, then both close.

## Aim

Write a client and a server program that exchange messages over a socket
connection.

## Concepts

- `ServerSocket` for listening, `Socket` for connecting
- Blocking calls: `accept()` waits for a client, `readLine()` waits for data
- `PrintWriter` with `autoFlush` for writing lines
- `BufferedReader` over an `InputStreamReader` for reading lines
- `try-with-resources` closing sockets in the right order

## Files

| File | Role |
|------|------|
| `src/server.java` | Listens on 5000, accepts one client, prints its message, replies `Hello client` |
| `src/client.java` | Connects to `localhost:5000`, sends `Hello Server`, prints the reply |

## The exchange

```
client                            server
  |                                 |
  |  connect -> localhost:5000      |  accept()
  |                                 |
  |  "Hello Server"  -------------> |  input.readLine()
  |                                 |
  |  <------------- "Hello client"  |  output.println()
  |                                 |
  |  close                          |  close
```

## Run

Two terminals. The server must start first - the client has nothing to connect
to otherwise.

```powershell
javac -d out src/*.java
```

Terminal 1:

```powershell
java -cp out server
```

Terminal 2:

```powershell
java -cp out client
```

Expected server output:

```
Server started on port 5000
Waiting for the client...
Client connected: /127.0.0.1:52143
Client says: Hello Server
Connection closed
```

Expected client output:

```
Connected to server localhost:5000
Server says: Hello client
```

## What to observe

- Start the client first and you get `Connection refused` - `accept()` is what
  makes the server wait, and nothing waits unless the server is running.
- The server handles exactly one client and then exits. Serving many clients
  needs a loop around `accept()` plus a thread per connection, which is the
  next step beyond this experiment.
- Both sides read and write on **different** streams: the client writes to
  `getOutputStream()` and reads from `getInputStream()`, and the server does
  the reverse. Mixing them up compiles but deadlocks.
- The `true` in `new PrintWriter(out, true)` is auto-flush. Without it the
  message sits in the buffer and the other side waits forever.
