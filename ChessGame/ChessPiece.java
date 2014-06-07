package chessGame;

public class ChessPiece {
	public final static char BlackPawn = '\u265F';
	public final static char BlackKnight = '\u265E';
	public final static char BlackBishop = '\u265D';
	public final static char BlackRook = '\u265C';
	public final static char BlackQueen = '\u265B';
	public final static char BlackKing = '\u265A';
	public final static char WhitePawn = '\u2659';
	public final static char WhiteKnight = '\u2658';
	public final static char WhiteBishop = '\u2657';
	public final static char WhiteRook = '\u2656';
	public final static char WhiteQueen = '\u2655';
	public final static char WhiteKing = '\u2654';
	
	public static boolean isWhite(char Piece){
		if (Piece <= WhitePawn && Piece >= WhiteKing){
			return true;
		}
		return false;
	}
	
	public static boolean isBlack(char Piece){
		if (Piece <= BlackPawn && Piece >= BlackKing){
			return true;
		}
		return false;
	}
	
	public static boolean isPawn(char Piece){
		return (Piece == BlackPawn || Piece == WhitePawn);
	}
	public static boolean isKnight(char Piece){
		return (Piece == BlackKnight || Piece == WhiteKnight);
	
	}
	
	public static boolean isBishop(char Piece){
		return (Piece == BlackBishop || Piece == WhiteBishop);
	}
	
	public static boolean isRook(char Piece){
		return (Piece == BlackRook || Piece == WhiteRook);
	}
	
	public static boolean isQueen(char Piece){
		return (Piece == BlackQueen || Piece == WhiteQueen);
	}
	
	public static boolean isKing(char Piece){
		return (Piece == BlackQueen || Piece == WhiteQueen);
	}
	
}
