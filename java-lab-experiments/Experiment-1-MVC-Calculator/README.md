# Experiment 1 - Calculator using MVC Architecture

A desktop calculator built with Swing, split into the three layers of the
model-view-controller pattern so that no single class does two jobs.

## Aim

Build a calculator that performs addition, subtraction, multiplication,
division and percentage, using the MVC design pattern.

## Concepts

- **Model** - holds the arithmetic and knows nothing about the screen.
- **View** - builds the window and exposes its widgets as fields.
- **Controller** - listens for button clicks and decides what to do.
- `ActionListener` and `actionPerformed`
- Splitting a Swing application so the logic can be tested without a GUI

## Files

| File | Role | Responsibility |
|------|------|----------------|
| `cmodel.java` | Model | `add`, `subtract`, `multiply`, `divide`, `percentage` |
| `cviews.java` | View | Frame, labels, text fields, five buttons |
| `ccontroller.java` | Controller | Registers itself on the buttons, reads the inputs, updates the result |
| `CalculatorApp.java` | Entry point | Creates the model and view, wires them to the controller |

## How the pieces connect

```
CalculatorApp
   ├── new cmodel()          the logic
   ├── new cviews()          the window
   └── new ccontroller(m, v) registers an ActionListener on every button
```

When a button is pressed, the controller asks the model for the answer and
writes it into the read-only result field. The model never touches Swing, and
the view never does arithmetic - that separation is the whole point of the
experiment.

## Run

```powershell
javac -d out src/*.java
java -cp out CalculatorApp
```

## What to observe

- All five buttons share one `actionPerformed` method; `e.getSource()` decides
  which operation runs.
- The result field is set to `setEditable(false)` so it can only be written by
  the controller.
- `divide` returns `0` when the second number is zero rather than throwing, so
  the window never crashes on bad input.
