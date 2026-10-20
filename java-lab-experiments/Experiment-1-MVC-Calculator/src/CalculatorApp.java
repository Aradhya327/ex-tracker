public class CalculatorApp {

    public static void main(String[] args) {

        // Model: holds the arithmetic logic
        cmodel cm = new cmodel();

        // View: builds the window
        cviews cv = new cviews();

        // Controller: connects the buttons to the model
        new ccontroller(cm, cv);
    }
}
