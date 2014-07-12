package chessGame;

public class ChessAction {
	public String action; //shitty
	public ChessPiece chessPiece;
	
	public ChessAction(ChessPiece chessPiece, String action){
		this.action = action;
		this.chessPiece = chessPiece;
	}
}
