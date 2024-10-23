package beetle;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;

public class Application extends JFrame {
    Board board;
    public Application() {
        initUI(1);
    }
    public Application(int level) {
        initUI(level);
    }

    private void initUI(int level) {
        setSize(800, 600);
        board = new Board(800,600);
        board.setManager(new LevelManager( "src/levels", board, this, level));
        add(board);

        addKeyListener(new Adapter(board));

        setTitle("FreeBeetle");
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        final int num = args.length>0 ? Integer.parseInt(args[0]) : 1;
        EventQueue.invokeLater(() -> {
            Application ex = new Application(num);
            ex.setVisible(true);
        });
    }

    private class Adapter extends KeyAdapter {
        Board board;
        Adapter(Board board) {
            this.board = board;
        }

        @Override
        public void keyReleased(KeyEvent e) {
            board.keyReleased(e);
        }

        @Override
        public void keyPressed(KeyEvent e) {
            board.keyPressed(e);
        }
    }
}