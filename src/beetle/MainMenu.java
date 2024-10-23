package beetle;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

public class MainMenu extends JDialog {
    private JButton exitButton;
    private JPanel panel1;
    private JButton settingsButton;
    private JButton newGameButton;

    public MainMenu() {
        setContentPane(panel1);
        //setModal(true);
        pack();
        setVisible(true);
        newGameButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                String[] args = new String[0];
                dispose();
                ChooseLevel level = new ChooseLevel();
            }
        });
        exitButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                dispose();
            }
        });
    }

    public static void main(String[] args) {
        MainMenu dialog = new MainMenu();
    }


}
