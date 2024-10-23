package beetle;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.*;
import java.util.Locale;
import java.util.Objects;

public class NextLevelDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private final LevelManager manager;


    public NextLevelDialog(LevelManager manager) {
        this.manager = manager;

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        // add your code here
        manager.setMoveToNextLevel(true);
        dispose(); //go to next level
    }

    private void onCancel() {
        // add your code here if necessary
        manager.setMoveToNextLevel(false);
        dispose(); //go to main menu
    }

    public static void create(LevelManager manager, String pack, int level, int pts) {
        NextLevelDialog dialog = new NextLevelDialog( manager );
        var components = dialog.contentPane.getComponents();
        for (Component c : components) {
            if (Objects.equals(c.getName(), "TopPanel")) {
                var inComponents = ((Container) c).getComponents();
                for (Component ic : inComponents) {
                    if (Objects.equals(ic.getName(), "TopLabel")) {
                        ((JLabel) ic).setText(pack + ": level " + level + " passed. " + pts + "points");
                    }
                }
                break;
            }
        }
        dialog.pack();
        dialog.setVisible(true);
    }

}
