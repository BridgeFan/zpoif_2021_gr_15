package beetle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Enumeration;

public class ChooseLevel extends JDialog {
    private JPanel panel;
    private JButton OKButton;
    private JButton returnButton;
    private JLabel label;
    private JList list;
    private ButtonGroup bg;

    public ChooseLevel() {
        list.removeAll();
        label.setText("src/levels");
        Boolean[] fileData = getFiles("src/levels");
        this.bg = new ButtonGroup();
        setContentPane(panel);
        for(int i=0;i<fileData.length;i++) {
            Font font = new Font("Serif", Font.PLAIN, 32);
            JRadioButton rb = new JRadioButton(Integer.toString(i+1));
            if(fileData[i])
                rb.setForeground(Color.GREEN);
            else
                rb.setForeground(Color.RED);
            rb.setFont(font);
            rb.setBounds(0,32*(i%16),120,32+120*(i/16));
            bg.add(rb);
            list.add(rb);
        }
        pack();
        setVisible(true);
        returnButton.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                dispose();
                MainMenu menu = new MainMenu();
            }
        } );
        OKButton.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                dispose();
                String [] args= new String[1];
                for (Enumeration<AbstractButton> buttons = bg.getElements(); buttons.hasMoreElements();) {
                    AbstractButton button = buttons.nextElement();

                    if (button.isSelected()) {
                        args[0]=button.getText();
                        break;
                    }
                }
                Application.main(args);
            }
        } );
    }
    public static void main(String[] args) {
        ChooseLevel dialog = new ChooseLevel();
    }

    private Boolean[] getFiles(String series) {
        ArrayList<Boolean> areFinished = new ArrayList<Boolean>();
        for(int i=1;i<999;i++) {
            if(!Files.exists( Path.of( series + "/" + String.format( "%03d", i ) + ".txt" ) ))
                break;
            areFinished.add(false);
        }
        return areFinished.toArray(new Boolean[0]);
    }
}
