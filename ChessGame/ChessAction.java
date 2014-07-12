package chessGame;

public class ChessAction {
	public String actiontype; //shitty
	public ChessPiece chessPiece;
	
	public ChessAction(ChessPiece chessPiece, String action){
		this.actiontype = action;
		this.chessPiece = chessPiece;
	}
}
