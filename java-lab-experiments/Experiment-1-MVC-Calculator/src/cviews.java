import javax.swing.*;



public class cviews extends JFrame {
    JLabel l1,l2,l3;
    JTextField t1,t2,t3;
    JButton add,sub,mul,div, percentage;

    cviews(){
        setSize(500,500);
        setLayout(null);
        setTitle("Calculator using MVC Architecture");

        l1 = new JLabel("First Number");
        l1.setBounds(50,50,100,30);
        add(l1);

        t1 = new JTextField();
        t1.setBounds(200,50,100,30);
        add(t1);

        add = new JButton("Add");
        add.setBounds(50,200,100,30);
        add(add);

        sub = new JButton("Subtract");
        sub.setBounds(200,200,100,30);  
        add(sub);

        mul = new JButton("Multiply");
        mul.setBounds(50,250,100,30);
        add(mul);

        div = new JButton("Divide");
        div.setBounds(200,250,100,30);  
        add(div);

        percentage = new JButton("Percentage");
        percentage.setBounds(50,350,100,30); 
        add(percentage);
        
        l2 = new JLabel("Second Number");
        l2.setBounds(50,100,100,30);
        add(l2);
        t2 = new JTextField();
        t2.setBounds(200,100,100,30);
        add(t2);

        l3 = new JLabel("Result");
        l3.setBounds(50,150,100,30);
        add(l3);
        t3 = new JTextField();
        t3.setBounds(200,150,100,30);
        t3.setEditable(false);
        add(t3);
        setVisible(true);
        
    }
}
