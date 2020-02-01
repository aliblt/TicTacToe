package Controller;

public class Game {

    int turn = 0;
    char[] board = new char[9];
    boolean isGameStarted = false;
    Player p1,p2;

    public Game() {
        p1 = null;
        p2 = null;
        refreshBoard();
    }

    public void refreshBoard() {
        turn = 0;
        for ( int i=0 ; i<9 ; i++ )
            board[i] = '\u0000';
    }

    public int checkWin() {
        if( firstPlayerWin() )
            return 1;
        else if( secondPlayerWin() )
            return -1;

        return 0;
    }

    public boolean checkTie( ) {
        boolean fl = true;

        for( int i=0 ; i<9 ; i++ )
            if( board[i] == '\u0000' ) {
                fl = false;
                break;
            }

        return fl;
    }

    public boolean hasGameEnded() {
        return (checkWin()!=0 || checkTie() );
    }

    public boolean checkChar( char ch ) {

        if( board[0] == ch && board[4] == ch && board[8] == ch )
            return true;
        if( board[2] == ch && board[4] == ch && board[6] == ch )
            return true;

        for( int i=0 ; i<3 ; i++ ){
            if( board[3*i] == ch && board[3*i+1] == ch && board[3*i+2] == ch )
                return true;
            if( board[i] == ch && board[i+3] == ch && board[i+3] == ch )
                return true;
        }

        return false;
    }

    public boolean secondPlayerWin() {
        return checkChar('O');
    }

    public boolean firstPlayerWin() {
        return checkChar('X');
    }

    public boolean makeMove(int place, char ch) {

        if( place<0 || place >8 )
            return false;

        if( board[place] != '\u0000' )
            return false;

        board[place] = ch;
        turn = 1-turn;

        return true;
    }

    public char[] getBoard() {
        return board;
    }

    public boolean isGameStarted() {
        return isGameStarted;
    }

    public int getTurn() {
        return turn;
    }

    public void setP1(Player p1) {
        this.p1 = p1;
    }

    public void setP2(Player p2) {
        this.p2 = p2;
    }

    public Player getP1() {
        return p1;
    }

    public Player getP2() {
        return p2;
    }

    public void startGame(){ isGameStarted = true; }

    public void finishGame(){
        p1 = null;
        p2 = null;
        isGameStarted = false;
    }

}
