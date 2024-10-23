package beetle;

public class LevelManager {
    private final String series;
    private final Board board;
    private final Application app;
    private int level;

    public void setMoveToNextLevel(boolean moveToNextLevel) {
        this.moveToNextLevel = moveToNextLevel;
    }

    private boolean moveToNextLevel = true;
    LevelManager(String path, Board board, Application app, int level) {
        series=path;
        this.board=board;
        this.app = app;
        this.board.setManager( this );
        this.level=level;
        board.gameMap.loadFromFile( series + "/" + String.format("%03d", level) + ".txt");
    }
    void passFinishResult(boolean result) {
        int pts = board.player.getPoints();
        if(result) {
            NextLevelDialog.create(this, series, level, pts);
            level++;
        }
        if(!moveToNextLevel || !board.gameMap.loadFromFile( series + "/" + String.format("%03d", level) + ".txt")) {
            board.timer.stop();
            app.dispose();
            MainMenu menu = new MainMenu();
        }
    }
}
