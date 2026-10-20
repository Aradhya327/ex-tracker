import java.awt.event.*;
import javax.swing.*;
import java.sql.*;

public class StudentRegistration extends JFrame implements ActionListener {

    private static final String DB_URL =
            System.getenv().getOrDefault("DB_URL", "jdbc:mysql://localhost:3306/college");
    private static final String DB_USER =
            System.getenv().getOrDefault("DB_USER", "root");
    private static final String DB_PASSWORD =
            System.getenv().getOrDefault("DB_PASSWORD", "luffy");

    JLabel lblName, lblRoll, lblGender, lblBranch;
    JTextField txtName, txtRoll, txtBranch;
    JRadioButton male, female;
    ButtonGroup genderGroup;
    JCheckBox terms;
    JButton submit, reset;

    public StudentRegistration() {
        setTitle("Student Registration Form");
        setSize(450, 420);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        lblName = new JLabel("Student Name");
        lblName.setBounds(30, 30, 120, 30);
        add(lblName);

        txtName = new JTextField();
        txtName.setBounds(170, 30, 180, 30);
        add(txtName);

        lblRoll = new JLabel("Roll Number");
        lblRoll.setBounds(30, 80, 120, 30);
        add(lblRoll);

        txtRoll = new JTextField();
        txtRoll.setBounds(170, 80, 180, 30);
        add(txtRoll);

        lblGender = new JLabel("Gender");
        lblGender.setBounds(30, 130, 120, 30);
        add(lblGender);

        male = new JRadioButton("Male");
        male.setBounds(170, 130, 70, 30);
        add(male);

        female = new JRadioButton("Female");
        female.setBounds(250, 130, 90, 30);
        add(female);

        genderGroup = new ButtonGroup();
        genderGroup.add(male);
        genderGroup.add(female);

        lblBranch = new JLabel("Branch");
        lblBranch.setBounds(30, 180, 120, 30);
        add(lblBranch);

        txtBranch = new JTextField();
        txtBranch.setBounds(170, 180, 180, 30);
        add(txtBranch);

        terms = new JCheckBox("I accept Terms & Conditions");
        terms.setBounds(30, 230, 250, 30);
        add(terms);

        submit = new JButton("Submit");
        submit.setBounds(80, 290, 100, 35);
        submit.addActionListener(this);
        add(submit);

        reset = new JButton("Reset");
        reset.setBounds(220, 290, 100, 35);
        reset.addActionListener(this);
        add(reset);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == reset) {
            txtName.setText("");
            txtRoll.setText("");
            txtBranch.setText("");

            genderGroup.clearSelection();
            terms.setSelected(false);

            return;
        }

        if (txtName.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please Enter Student Name");
            return;
        }

        if (txtRoll.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please Enter Roll Number");
            return;
        }

        if (!male.isSelected() && !female.isSelected()) {
            JOptionPane.showMessageDialog(this, "Please Select Gender");
            return;
        }

        if (txtBranch.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please Enter Branch");
            return;
        }

        if (!terms.isSelected()) {
            JOptionPane.showMessageDialog(this, "Please Accept Terms & Conditions");
            return;
        }

        String gender;

        if (male.isSelected())
            gender = "Male";
        else
            gender = "Female";

        boolean saved = saveToDatabase(txtName.getText().trim(),
                                       txtRoll.getText().trim(),
                                       gender,
                                       txtBranch.getText().trim());

        // Only claim success once the row is actually in the table.
        if (saved) {
            JOptionPane.showMessageDialog(this,
                    "Registration Successful\n\n"
                            + "Student Name : " + txtName.getText()
                            + "\nRoll Number : " + txtRoll.getText()
                            + "\nGender : " + gender
                            + "\nBranch : " + txtBranch.getText());
        }
    }

    private boolean saveToDatabase(String name, String rollNumber, String gender, String branch) {

        String sql = "INSERT INTO student (name, roll_number, branch, gender) VALUES (?, ?, ?, ?)";

        try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setString(2, rollNumber);
            ps.setString(3, branch);
            ps.setString(4, gender);

            return ps.executeUpdate() == 1;

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage());
            return false;
        }
    }

    public static void main(String[] args) {
        new StudentRegistration();
    }
}
