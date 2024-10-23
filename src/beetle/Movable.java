package beetle;

abstract public class Movable {
    protected int x, y;
    protected int index;
    protected static int guidCounter=0;
    protected Direction direction=Direction.Left;
    Movable(int x, int y) {
        this.x=x;
        this.y=y;
        index=guidCounter;
        guidCounter++;
    }

    public boolean move(Direction direction) {
        if(direction==null)
            return false;
        int oldX=this.x;
        int oldY=this.y;
        switch(direction) {
            case Left:
                this.x--;
                break;
            case Right:
                this.x++;
                break;
            case Up:
                this.y--;
                break;
            case Down:
                this.y++;
                break;
        }
        this.direction=direction;
        if(!MoveChecker.canBeMovedTo(this, direction)) {
            this.x=oldX;
            this.y=oldY;
            return false;
        }
        return true;
    }

    int getX() {
        return x;
    }
    int getY() {
        return y;
    }
    int getIndex() {
        return index;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Movable that = (Movable) o;
        return index == that.index;
    }
}
