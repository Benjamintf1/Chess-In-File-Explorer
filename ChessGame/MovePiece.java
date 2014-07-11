

//please forgive me for this code...it's bad. 
package chessGame;

import java.io.File;
import java.util.ArrayList;


//col = abcdefgh
//row = 1-8

//can't keep rows and colomns straight. seriously, this sucks. 



public class MovePiece {
	private static ChessPiece[][] board;

	private static char whoseturn;
	private static File workingfolder;
	private static File problemsFile;
	private static ChessPiece emptyPiece = new ChessPiece('e', false);
	
	
	public static void main(String[] args) {
		board = new ChessPiece[8][8];
		
		if (args.length == 1){
			
		}
	}
	
	
	private ArrayList<FileAction> ValidatePawn(int col1, int row1, int col2, int row2){
		
		ChessPiece firstsquare = board[col1][row1];
		ChessPiece secondsquare = board[col2][row2];
		
		int unitVectorCol;
		if (firstsquare.pieceColor() == 'w') {
			unitVectorCol = 1;
		} else {
			unitVectorCol = -1;
		}
		
		
		if (col1 != col2) {
			if ( (row2 - row1) != unitVectorCol ) {
				return null;
			}
			
			if (Math.abs(col1 - col2) != 1){
				return null;
			}
			
			if ( secondsquare.pieceColor() != 'e' ){
				//en passant
				ChessPiece secondPiece = board[col2][row2-unitVectorCol];
				if (secondPiece.pieceColor() != firstsquare.pieceColor()) {
					if (secondPiece.isPawn()) {
						if (secondPiece.flag) {
							ArrayList<FileAction> list = new ArrayList<FileAction>();
							
							list.add(new FileAction(firstsquare.FileName(col1, row1),"remove"));
							list.add(new FileAction(secondsquare.FileName(col2, row2),"remove"));
							list.add(new FileAction(secondPiece.FileName(col2, row2-unitVectorCol),"remove"));
							
							
							list.add(new FileAction(emptyPiece.FileName( col1, row1),"create"));
							list.add(new FileAction(emptyPiece.FileName(col2, row2-unitVectorCol),"create"));
							list.add(new FileAction(firstsquare.FileName( col2, row2),"create"));
							
							
							return list;
						}
					}
				}
				return null;
			}
			
			if ( firstsquare.pieceColor() == secondsquare.pieceColor()){
				return null;
			}
			
			ArrayList<FileAction> list = new ArrayList<FileAction>();
			
			list.add(new FileAction(firstsquare.FileName(col1, row1),"remove"));
			list.add(new FileAction(secondsquare.FileName( col2, row2),"remove"));
			
			list.add(new FileAction(emptyPiece.FileName( col1, row1),"create"));
			list.add(new FileAction(firstsquare.FileName( col2, row2),"create"));
			
			
			return list;
			
			
			
			
		} else if ( (Math.abs(row2 - row1) == 2) ) {
			
			//double first move
			if (!( row1 == 6 || row1 == 1)){
				return null; //must be pawns first move
			}
			
			if (board[col1][row1+unitVectorCol].pieceColor() != 'e'){
				return null;
			}
			
			if ( board[col1][row2].pieceColor() != 'e'){
				return null;
			}
			
			ArrayList<FileAction> list = new ArrayList<FileAction>();
			
			list.add(new FileAction(firstsquare.FileName( col1, row1),"remove"));
			list.add(new FileAction(secondsquare.FileName( col2, row2),"remove"));
			
			list.add(new FileAction(emptyPiece.FileName( col1, row1),"create"));
			list.add(new FileAction(firstsquare.FileName( col2, row2),"create"));
			
			list.add(new FileAction(firstsquare.FileName( col2, row2),"write"));
			
			
			return list;
			
		} else if ( (Math.abs(row2 - row1) == 1) ){ // single move forward
			if ( (row2 - row1) != unitVectorCol ) {
				return null;
			}
			
			if ( board[col1][row2].pieceColor() != 'e'){
				return null;
			}
			
			ArrayList<FileAction> list = new ArrayList<FileAction>();
			
			list.add(new FileAction(firstsquare.FileName( col1, row1),"remove"));
			list.add(new FileAction(secondsquare.FileName(col2, row2),"remove"));
			
			list.add(new FileAction(emptyPiece.FileName(col1, row1),"create"));
			list.add(new FileAction(firstsquare.FileName( col2, row2),"create"));
			
			
			return list;
		}
		
		
		return null;
	}
	
	private ArrayList<FileAction> ValidateRook(int col1, int row1, int col2, int row2){
		
		ChessPiece firstsquare = board[col1][row1];
		ChessPiece secondsquare = board[col2][row2];
		
		
		int lengthCol = col1 - col2;
		int lengthRow = row1 - row2;
		if ( !(lengthCol != 0 && lengthRow == 0 || lengthRow != 0 && lengthCol == 0)) {
			return null;
		}
		int unitVectorCol = lengthCol / Math.abs(lengthCol);
		int unitVectorRow = lengthRow / Math.abs(lengthRow);


		for (int i = 1; col2 != col1 + unitVectorCol*i && row2 != row1 + unitVectorRow*i; ++i){
			if (board[col1 + unitVectorCol*i][row1 + unitVectorRow*i].pieceColor() != 'e'){
				return null;
			}
		}
		
	
		if ( firstsquare.pieceColor() == secondsquare.pieceColor()){
			return null;
		}
		
		ArrayList<FileAction> list = new ArrayList<FileAction>();
		
		list.add(new FileAction(firstsquare.FileName( col1, row1),"remove"));
		list.add(new FileAction(secondsquare.FileName( col2, row2),"remove"));
		
		list.add(new FileAction(emptyPiece.FileName( col1, row1),"create"));
		list.add(new FileAction(firstsquare.FileName(col2, row2),"create"));
		
		
		return list;
	}
	
	private ArrayList<FileAction> ValidateKnight(int col1, int row1, int col2, int row2){
		ChessPiece firstsquare = board[col1][row1];
		ChessPiece secondsquare = board[col2][row2];
		
		int lengthCol = Math.abs(col1 - col2);
		int lengthRow = Math.abs(row1 - row2);
		
		
		if ( !(lengthCol == 2 && lengthRow == 1 || lengthRow == 2 && lengthCol == 1)) {
			return null;
		} 
		if ( firstsquare.pieceColor() == secondsquare.pieceColor() ){
			return null;
		}
		
		ArrayList<FileAction> list = new ArrayList<FileAction>();
		
		list.add(new FileAction(firstsquare.FileName( col1, row1),"remove"));
		list.add(new FileAction(secondsquare.FileName(col2,row2),"remove"));
		
		list.add(new FileAction(emptyPiece.FileName( col1, row1),"create"));
		list.add(new FileAction(firstsquare.FileName( col2, row2),"create"));
		
		
		return list;
		
	}
	
	private ArrayList<FileAction> ValidateBishop(int col1, int row1, int col2, int row2){
		ChessPiece firstsquare = board[col1][row1];
		ChessPiece secondsquare = board[col2][row2];
		
		
		
		int directionCol = col1 - col2;
		int directionRow = row1 - row2;

		
		if ( !( Math.abs(directionCol) == Math.abs(directionRow) )) {
			return null;
		}
		int unitVectorCol = directionCol / Math.abs(directionCol);
		int unitVectorRow = directionRow / Math.abs(directionRow);


		for (int i = 1; col2 != col1 + unitVectorCol*i && row2 != row1 + unitVectorRow*i; ++i){
			if (board[col1 + unitVectorCol*i][row1 + unitVectorRow*i].pieceColor() != 'e'){
				return null;
			}
		}
		
		
		
		if ( firstsquare.pieceColor() == secondsquare.pieceColor()){
			return null;
		}
		
		ArrayList<FileAction> list = new ArrayList<FileAction>();
		
		list.add(new FileAction(firstsquare.FileName(col1, row1),"remove"));
		list.add(new FileAction(secondsquare.FileName( col2, row2),"remove"));
		
		list.add(new FileAction(emptyPiece.FileName(col1, row1),"create"));
		list.add(new FileAction(firstsquare.FileName( col2, row2),"create"));
		
		
		return list;
	}
	
	private ArrayList<FileAction> ValidateQueen(int col1, int row1, int col2, int row2){
		
		ArrayList<FileAction> bishopmove = ValidateBishop(col1, row1, col2, row2);
		
		if (bishopmove == null){
			return ValidateRook(col1, row1, col2, row2);
		}
		return bishopmove; //The code just keeps getting worse, this hasn't happened in a project before...I still feel like a horrible person.
	}
	
	private ArrayList<FileAction> ValidateKing(int col1, int row1, int col2, int row2){
		
		ChessPiece firstsquare = board[col1][row1];
		ChessPiece secondsquare = board[col2][row2];
		
		
		int lengthRow = Math.abs(col1 - col2);
		int lengthCol = Math.abs(row1 - row2);
		
		
		if (lengthRow > 1 || lengthCol > 1) {
			if (lengthCol == 2 && lengthRow == 0){
				//castle
				int rookcol1;
				int rookcol2;
				int iterator;
				if (col1 == 2){
					iterator = -1;
					rookcol1 = 0;
					rookcol2 = 3;
				} else {
					iterator = 1;
					rookcol1 = 7;
					rookcol2 = 5;
				}
				
				if(firstsquare.flag && board[rookcol1][row1].flag){
					//make sure spaces bet
					
					for ( int i = iterator; i + col1 != rookcol1; i = i + iterator){
						if (board[col1 + i][row1].pieceColor() != 'e'){
							return null;
						}
					}
					ArrayList<FileAction> list = new ArrayList<FileAction>();
					
					list.add(new FileAction(firstsquare.FileName( col1, row1),"remove"));
					list.add(new FileAction(secondsquare.FileName( col2, row2),"remove"));
					list.add(new FileAction(board[rookcol1][row1].FileName( rookcol1, row1),"remove"));
					list.add(new FileAction(emptyPiece.FileName( rookcol2, row1),"remove"));
					
					list.add(new FileAction(emptyPiece.FileName( col1, row1),"create"));
					list.add(new FileAction(emptyPiece.FileName( rookcol1, row1),"create"));
					list.add(new FileAction(firstsquare.FileName( col2, row2),"create"));
					list.add(new FileAction(board[rookcol1][row1].FileName( rookcol2, row1),"create"));
					
					return list;
					
				}
				
				
			}
			return null;
		}
		
		if ( firstsquare.pieceColor() == secondsquare.pieceColor()){
			return null;
		}
		
		
		
		ArrayList<FileAction> list = new ArrayList<FileAction>();
		
		list.add(new FileAction(firstsquare.FileName( col1, row1),"remove"));
		list.add(new FileAction(secondsquare.FileName( col2, row2),"remove"));
		
		list.add(new FileAction(emptyPiece.FileName( col1, row1),"create"));
		list.add(new FileAction(firstsquare.FileName( col2, row2),"create"));
		
		
		return list;
	}
	

}
