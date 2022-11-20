package form;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class Form2 {
    private JButton okButton;
    private JTextArea textArea1;
    private JPanel panel1;
    public Form2()
    {
        JFrame frame = new JFrame();
        frame.setContentPane(panel1);
        frame.setVisible(true);
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String path = textArea1.getText();
                File file = new File(path);
                if(file.exists())
                {
                    Form1 form1 = new Form1(path);
                    frame.setVisible(false);
                    frame.dispose();
                }
                else {
                    JOptionPane.showMessageDialog(frame, "File is missing");
                }
            }
        });
    }
}
