package chessGame;

public class MovePiece {
	private static char[][] board;
	private static char[][] boardcolor = {{'b','w','b', 'w', 'b', 'w', 'b', 'w'},
										  {'w', 'b','w','b', 'w', 'b', 'w', 'b'},
										  {'b','w','b', 'w', 'b', 'w', 'b', 'w'},
										  {'w', 'b','w','b', 'w', 'b', 'w', 'b'},
										  {'b','w','b', 'w', 'b', 'w', 'b', 'w'},
										  {'w', 'b','w','b', 'w', 'b', 'w', 'b'},
										  {'b','w','b', 'w', 'b', 'w', 'b', 'w'},
										  {'w', 'b','w','b', 'w', 'b', 'w', 'b'}};
	private static char whoseturn;
	public static void main(String[] args) {
		board = new char[8][8];
		for (String s: args){
			System.out.println(s);
		}
	}
	
	
	private boolean ValidatePawn(int col1, int row1, int col2, int row2){
		return false;
	}
	
	private boolean ValidateRook(int col1, int row1, int col2, int row2){
		int lengthCol = col1 - col2;
		int lengthRow = row1 - row2;
		if ( !(lengthCol != 0 && lengthRow == 0 || lengthRow != 0 && lengthCol == 0)) {
			return false;
		}
		int unitVectorCol = lengthCol / Math.abs(lengthCol);
		int unitVectorRow = lengthRow / Math.abs(lengthRow);
		boolean Blocking = false;
		for (int i = 1; col2 != col1 + unitVectorCol*i && row2 != row1 + unitVectorRow*i; ++i){
			if (board[col1 + unitVectorCol*i][row1 + unitVectorRow*i] != 'e'){
				Blocking = true;
			}
		}
		
		if (Blocking) {
			return false;
		}
		
		return true;
	}
	
	private boolean ValidateKnight(int col1, int row1, int col2, int row2){
		int length1 = Math.abs(col1 - col2);
		int length2 = Math.abs(row1 - row2);
		if ( !(length1 == 2 && length2 == 1 || length2 == 2 && length1 == 1)) {
			return false;
		} 
		if ( !(ChessPiece.PieceColor(board[col1][row1]) != ChessPiece.PieceColor(board[col2][row2]))){
			return false;
		}
		return true;
		
	}
	
	private boolean ValidateBishop(int col1, int row1, int col2, int row2){
		int lengthCol = col1 - col2;
		int lengthRow = row1 - row2;
		if ( !( Math.abs(lengthCol) == Math.abs(lengthRow) )) {
			return false;
		}
		int unitVectorCol = lengthCol / Math.abs(lengthCol);
		int unitVectorRow = lengthRow / Math.abs(lengthRow);
		boolean Blocking = false;
		for (int i = 1; col2 != col1 + unitVectorCol*i && row2 != row1 + unitVectorRow*i; ++i){
			if (board[col1 + unitVectorCol*i][row1 + unitVectorRow*i] != 'e'){
				Blocking = true;
			}
		}
		
		if (Blocking) {
			return false;
		}
		
		if ( !(ChessPiece.PieceColor(board[col1][row1]) != ChessPiece.PieceColor(board[col2][row2]))){
			return false;
		}
		
		return true;
	}
	
	private boolean ValidateQueen(int col1, int row1, int col2, int row2){
		return ValidateBishop(col1, row1, col2, row2) || ValidateRook(col1, row1, col2, row2); //Oh my god i'm a horrible person. 
	}
	
	private boolean ValidateKing(int col1, int row1, int col2, int row2){
		return false;
	}

}
