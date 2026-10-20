import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ccontroller implements ActionListener {
    cmodel cm;
    cviews cv;

    ccontroller(cmodel cm , cviews cv){
        this.cm=cm;
        this.cv = cv;

        cv.add.addActionListener(this);
        cv.sub.addActionListener(this);
        cv.mul.addActionListener(this);
        cv.div.addActionListener(this);
        cv.percentage.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        double num1 = Double.parseDouble(cv.t1.getText());
        double num2 = Double.parseDouble(cv.t2.getText());
        double result = 0;

        if(e.getSource() == cv.add){
            result = cm.add(num1,num2);
        }
        else if(e.getSource() == cv.sub){
            result = cm.subtract(num1,num2);
        }
        else if(e.getSource() == cv.mul){
            result = cm.multiply(num1,num2);
        }
        else if(e.getSource() == cv.div){
            result = cm.divide(num1,num2);
        }
        else if(e.getSource() == cv.percentage){
            result = cm.percentage(num1,num2);
        }
        cv.t3.setText(String.valueOf(result));
    }
    }
