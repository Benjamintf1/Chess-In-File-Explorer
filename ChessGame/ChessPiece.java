package chessGame;

public class ChessPiece {
	private static char[][] boardcolor = {{'b','w','b', 'w', 'b', 'w', 'b', 'w'},
		  {'w', 'b','w','b', 'w', 'b', 'w', 'b'},
		  {'b','w','b', 'w', 'b', 'w', 'b', 'w'},
		  {'w', 'b','w','b', 'w', 'b', 'w', 'b'},
		  {'b','w','b', 'w', 'b', 'w', 'b', 'w'},
		  {'w', 'b','w','b', 'w', 'b', 'w', 'b'},
		  {'b','w','b', 'w', 'b', 'w', 'b', 'w'},
		  {'w', 'b','w','b', 'w', 'b', 'w', 'b'}};
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
	
	
	public char piece;
	public int row;
	public int col;
	public boolean flag; //set if pawn has double-moved, or king/knight has not moved. 
	
	
	public ChessPiece(char piece,  boolean flag){
		this.piece = piece;
		this.flag = flag;

	}

	
	

	
	public String FileName(int col, int row){
		return ((char) ('a' + col)) + Integer.toString(row + 1) + "." + boardcolor[row][col] + piece;
		
	}
	
	
	
	public boolean isWhite(){
		if (piece <= WhitePawn && piece >= WhiteKing){
			return true;
		}
		return false;
	}
	
	public boolean isBlack(){
		if (piece <= BlackPawn && piece >= BlackKing){
			return true;
		}
		return false;
	}
	
	public char pieceColor(){
		if (isBlack()){
			return 'b';
		} else if (isWhite()){
			return 'w';
		} else {
			return 'e';
		}
		
	}
	
	public boolean isPawn(){
		return (piece == BlackPawn || piece == WhitePawn);
	}
	public boolean isKnight(){
		return (piece == BlackKnight || piece == WhiteKnight);
	
	}
	
	public boolean isBishop(){
		return (piece == BlackBishop || piece == WhiteBishop);
	}
	
	public boolean isRook(){
		return (piece == BlackRook || piece == WhiteRook);
	}
	
	public boolean isQueen(){
		return (piece == BlackQueen || piece == WhiteQueen);
	}
	
	public boolean isKing(){
		return (piece == BlackQueen || piece == WhiteQueen);
	}
	
}
