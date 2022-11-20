package form;

import org.example.DbAccess.DbConnection;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Form1 {
    private JTable table1;
    private JPanel panel1;
    private JButton searchButton;
    private JTextField textField1;
    private JLabel Label2;
    private JButton allButton;
    public Form1(String dbPath) {
        JFrame frame = new JFrame();
        frame.setContentPane(panel1);
        frame.setVisible(true);
        try {
            DbConnection ex = new DbConnection(dbPath);
            ex.createTablesExample();
            table1.setModel(ex.executeQuery("SELECT * FROM group1"));

        } catch (Exception e) {
            e.printStackTrace();
        }
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(isDigit(textField1.getText()))
                {
                    DbConnection ex = new DbConnection(dbPath);
                    String sql = String.format("SELECT * FROM group1 Where MONTH(birth_date) = %s", textField1.getText());
                    try {
                        table1.setModel(ex.executeQuery(sql));

                    } catch (Exception exc) {
                        exc.printStackTrace();
                    }
                }
                else {
                    JOptionPane.showMessageDialog(frame, "Input data is not a number");
                }

            }
        });
        allButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    DbConnection ex = new DbConnection(dbPath);
                    table1.setModel(ex.executeQuery("SELECT * FROM group1"));

                } catch (Exception exc) {
                    exc.printStackTrace();
                }
            }
        });
    }

    private static boolean isDigit(String s) throws NumberFormatException {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
