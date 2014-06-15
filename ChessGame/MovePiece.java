package chessGame;

import java.io.File;

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
	private static File workingfolder;
	private static File problemsFile;
	
	
	public static void main(String[] args) {
		board = new char[8][8];
		for (String s: args){
			System.out.println(s);
		}
	}
	
	
	private boolean ValidatePawn(int col1, int row1, int col2, int row2){
		int unitVectorCol;
		if (ChessPiece.PieceColor(board[col1][row1]) == 'w') {
			unitVectorCol = 1;
		} else {
			unitVectorCol = -1;
		}
		
		
		if (col1 != col2) {
			if ( (row2 - row1) != unitVectorCol ) {
				return false;
			}
			
			if (Math.abs(col1 - col2) != 1){
				return false;
			}
			
			if ( board[row2][col2] == 'e'){
				return false;
			}
			
			if ( ChessPiece.PieceColor(board[col1][row1]) == ChessPiece.PieceColor(board[col2][row2])){
				return false;
			}
			
			return true;
			
			
			
			
		} else if ( (Math.abs(row2 - row1) == 2) ) {
			
			//double first move
			if (!( row1 == 6 || row1 == 1)){
				return false; //must be pawns first move
			}
			
			if (board[col1][row1+unitVectorCol] != 'e'){
				return false;
			}
			
			if ( board[col1][row2] != 'e'){
				return false;
			}
			
			return true;
			
		} else if ( (Math.abs(row2 - row1) == 1) ){ // single move forward
			if ( (row2 - row1) != unitVectorCol ) {
				return false;
			}
			
			if ( board[col1][row2] != 'e'){
				return false;
			}
			
			return true;
		}
		
		
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
		int lengthCol = Math.abs(col1 - col2);
		int lengthRow = Math.abs(row1 - row2);
		
		
		if ( !(lengthCol == 2 && lengthRow == 1 || lengthRow == 2 && lengthCol == 1)) {
			return false;
		} 
		if ( ChessPiece.PieceColor(board[col1][row1]) == ChessPiece.PieceColor(board[col2][row2])){
			return false;
		}
		return true;
		
	}
	
	private boolean ValidateBishop(int col1, int row1, int col2, int row2){
		int directionCol = col1 - col2;
		int directionRow = row1 - row2;

		
		if ( !( Math.abs(directionCol) == Math.abs(directionRow) )) {
			return false;
		}
		int unitVectorCol = directionCol / Math.abs(directionCol);
		int unitVectorRow = directionRow / Math.abs(directionRow);
		boolean Blocking = false;
		for (int i = 1; col2 != col1 + unitVectorCol*i && row2 != row1 + unitVectorRow*i; ++i){
			if (board[col1 + unitVectorCol*i][row1 + unitVectorRow*i] != 'e'){
				Blocking = true;
			}
		}
		
		if (Blocking) {
			return false;
		}
		
		if ( ChessPiece.PieceColor(board[col1][row1]) == ChessPiece.PieceColor(board[col2][row2])){
			return false;
		}
		
		return true;
	}
	
	private boolean ValidateQueen(int col1, int row1, int col2, int row2){
		return ValidateBishop(col1, row1, col2, row2) || ValidateRook(col1, row1, col2, row2); //Oh my god i'm a horrible person. 
	}
	
	private boolean ValidateKing(int col1, int row1, int col2, int row2){
		int lengthRow = Math.abs(col1 - col2);
		int lengthCol = Math.abs(row1 - row2);
		
		
		if (lengthRow > 1 || lengthCol > 1) {
			return false;
		}
		
		if ( ChessPiece.PieceColor(board[col1][row1]) == ChessPiece.PieceColor(board[col2][row2])){
			return false;
		}
		
		
		
		return true;
	}
	
	private boolean isEmpty(int col, int row){
		File pieceFile = new File(workingfolder, getFileName(col, row));
		if ( !pieceFile.exists() ) {
			System.exit(1);  //board is invalid, there's nothing I can do except peace out. 
		}
		if ( pieceFile.length() == 0 ) {
			return false;
		}
		return true;
	}
	
	private String getFileName(int col, int row){
		return ((char) ('a' + col)) + Integer.toString(row + 1) + "." + boardcolor[row][col] + board[row][col];
		
	}

}
