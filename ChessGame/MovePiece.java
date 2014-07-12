

//please forgive me for this code...it's bad. 
package chessGame;

import java.io.BufferedReader;
import java.io.File;
//import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Pattern;


//col = abcdefgh
//row = 1-8

//can't keep rows and colomns straight. seriously, this sucks. 



public class MovePiece {
	private static ChessPiece[][] board;
	
	
	
	
	
	private static char whoseturn;
	private static File workingfolder;
	//private static File problemsFile;
	private static ChessPiece emptyPiece = new ChessPiece('e', false);
	
	
	public static void main(String[] args) {

		
		
		
		
		
		if (args.length == 1){
			Pattern validargument = Pattern.compile("([a-h][1-8])|(promote)\\.[wb][e" + ChessPiece.WhiteKing + "-" + ChessPiece.BlackPawn +"]");
			File argument = new File(args[0]);
			if (!argument.exists()){
				System.out.println("I don't know what to do here, like, read up on the readme or something, because the argument you passed through there isn't a file");
				System.exit(1); //peace out
			}
			
			workingfolder = argument.getParentFile();
			
			
			File lastclickfile = new File(workingfolder, "z.lastclick" );
			
			File whosemovefile = new File(workingfolder, "z.whoseturn");
			try {
				BufferedReader br = new BufferedReader(new FileReader(whosemovefile));
				String line = br.readLine();
				whoseturn = line.charAt(0);
				if (whoseturn == 'w' | whoseturn == 'b'){
					System.exit(1);
				}
				br.close();
				
			} catch (Exception e) {
				
				e.printStackTrace();
				System.exit(1);
			}
			if (!whosemovefile.exists()){
				System.out.println("");
			}
			
			if (!validargument.matcher(argument.getName()).matches()){
				System.out.println("I don't know what to do here, like, read up on the readme or something, because the argument you passed through there isn't a chessPiecefile");
				System.exit(1); //peace out
			}
			if (lastclickfile.exists()){
				//this is what happens if we want to actually process a move. 
				//it's happening. 
				board = new ChessPiece[8][8];
				
				String lastpieceFile = null; //again, must be set
				ChessPiece theKing = null; //this should be set unless the user deleted their king piece or something.
				ChessPiece lastClick = null; // This is to quash the error. If it wasn't initialized, then the program should have already exited. 
				try {
					BufferedReader br = new BufferedReader(new FileReader(lastclickfile));
					lastpieceFile = br.readLine();
					br.close();
					lastClick = new ChessPiece(lastpieceFile, new File(workingfolder, lastpieceFile).length() > 0);
					
					Pattern validpiece = Pattern.compile("[a-h][1-8]\\.[wb][e" + ChessPiece.WhiteKing + "-" + ChessPiece.BlackPawn +"]");
					
					for(File file: workingfolder.listFiles()){
						if (validpiece.matcher(file.getName()).matches()) {
							boolean flag = (file.length() > 0);
							ChessPiece currentpiece = new ChessPiece(file.getName(), flag);
							board[currentpiece.col][currentpiece.row] = currentpiece;
							if(currentpiece.isKing() && currentpiece.pieceColor() == whoseturn){
								theKing = currentpiece;
							}
						}
					}
					
					
				} catch (Exception e) {
					System.out.println("Something happened wrong with reading from the \"last click\" file. " );
					e.printStackTrace();
					System.exit(1);
				}
				Pattern isPromotion = Pattern.compile("promote\\.[wb][" + ChessPiece.WhiteKing + "-" + ChessPiece.BlackPawn +"]");
				
				if( isPromotion.matcher(argument.getName()).matches()){
					File newPiece = new File(workingfolder, ((char) ('a' + lastClick.col)) + Integer.toString(lastClick.row + 1)+argument.getName().substring(7)) ;
					new File(workingfolder, lastpieceFile).delete();
					try {
						newPiece.createNewFile();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						System.exit(1);
					}
				}
				else{
					ChessPiece nextClick = new ChessPiece(argument.getName(), argument.length() > 0);
					ArrayList<ChessAction> thisMove = ValidateMove( lastClick.col ,  lastClick.row,  nextClick.col,  nextClick.row);
					for( ChessAction action : thisMove){
						switch(action.actiontype){
						case "remove":
							board[action.chessPiece.col][action.chessPiece.row] = emptyPiece.MovedPiece(action.chessPiece.col, action.chessPiece.row);
							break;
						case "create":
							board[action.chessPiece.col][action.chessPiece.row] = action.chessPiece;
							break;
						case "flag":
							board[action.chessPiece.col][action.chessPiece.row].flag = true;
							break;
						case "promote":
							break;
						default:
							System.out.println("Something happened wrong while parsing the action list");
							System.exit(1);
						}
						
					}
					for(ChessPiece[] array : board){
						for (ChessPiece piece : array){
							if( piece.pieceColor() != 'e' && piece.pieceColor() != whoseturn){
								if(ValidateMove( piece.col ,  piece.row,  theKing.col,  theKing.row) != null){
									System.out.println("This move would put the king in check, it is not valid.");
									System.exit(1);
								}
							}
						}
					}
					
					lastclickfile.delete();
					
					
					
					
					
					try {
						
						boolean changewhoseturn = true;
						for( ChessAction action : thisMove){
							switch(action.actiontype){
							case "remove":
								new File(workingfolder, action.chessPiece.FileName()).delete();
								break;
							case "create":
								new File(workingfolder, action.chessPiece.FileName()).createNewFile();
								break;
							case "flag":
								FileWriter flagger = new FileWriter(new File(workingfolder, action.chessPiece.FileName()));
								flagger.write("This file is a pawn that has moved twice, it can be en passanted");
								flagger.close();
								break;
							case "promote":
								char[] promotePieces;
								if (action.chessPiece.pieceColor() == 'w'){
									promotePieces = ChessPiece.whitepromote;
								} else {
									promotePieces = ChessPiece.blackpromote;
								}
								
								for( char pieceUnicode : promotePieces){
									new File(workingfolder, "promote" + '.' + action.chessPiece.BoardColor() + pieceUnicode).createNewFile();
								}
								
								lastclickfile.createNewFile();
								FileWriter writelastclick = new FileWriter(lastclickfile);
								writelastclick.write(action.chessPiece.FileName());
								writelastclick.close();
								
								changewhoseturn = false;
								break;
							default:
								System.out.println("Something happened wrong while parsing the action list");
								System.exit(1);
							}
							
						}
						
						if (changewhoseturn){
							whosemovefile.delete();
							whosemovefile.createNewFile();
							FileWriter fw = new FileWriter(whosemovefile);
							if(whoseturn == 'w'){
								fw.write("b");
							} else {
								fw.write("w");
							}
							fw.close();
						}
						
					} catch (IOException e) {
						System.exit(1);
						e.printStackTrace();
					}
					
				}
				
			} else {
				try {
					
					lastclickfile.createNewFile();
					
					ChessPiece currentPiece = new ChessPiece(argument.getName(), false);
					if (currentPiece.pieceColor() != whoseturn){
						System.out.println("This piece is not valid, you can only control your own pieces. ");
						System.exit(1);
					}
					
					
					FileWriter fw = new FileWriter(lastclickfile);
					fw.write(argument.getName());
					fw.close();
				} catch (IOException e) {
					System.out.println("I couldn't create a file, whoops.");
					e.printStackTrace();
					System.exit(1); //peace out					
				}
				
				
				
			}
			
			
			
			
			
		} else {
			System.out.println("This program only supports one argument at the moment(the file you are clicking) sorry.");
		}
	}
	
	
	
	private static ArrayList<ChessAction> ValidateMove(int col1, int row1, int col2, int row2){
		if( board[col1][row1].isPawn()){
			return ValidatePawn(col1, row1, col2, row2);
		}
		else if( board[col1][row1].isRook()){
			return ValidateRook(col1, row1, col2, row2);
		}
		else if ( board[col1][row1].isKnight()){
			return ValidateKnight(col1, row1, col2, row2);
		}
		else if ( board[col1][row1].isBishop()){
			return ValidateBishop(col1, row1, col2, row2);
		}
		else if ( board[col1][row1].isQueen()){
			return ValidateQueen(col1, row1, col2, row2);
		}
		else if ( board[col1][row1].isKing()){
			return ValidateKing(col1, row1, col2, row2);
		}
		else {
			System.out.println("Something went wrong...The piece you(or the program) is attempting to move is not a piece.");
			System.exit(1);
			return null;
		}
		
	}
	
	private static ArrayList<ChessAction> ValidatePawn(int col1, int row1, int col2, int row2){
		
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
							ArrayList<ChessAction> list = new ArrayList<ChessAction>();
							
							list.add(new ChessAction(firstsquare,"remove"));
							list.add(new ChessAction(secondsquare,"remove"));
							list.add(new ChessAction(secondPiece,"remove"));
							
							
							list.add(new ChessAction(emptyPiece.MovedPiece(col1, row1),"create"));
							list.add(new ChessAction(emptyPiece.MovedPiece(col2, row2-unitVectorCol),"create"));
							list.add(new ChessAction(firstsquare.MovedPiece( col2, row2),"create"));
							
							
							return list;
						}
					}
				}
				return null;
			}
			
			if ( firstsquare.pieceColor() == secondsquare.pieceColor()){
				return null;
			}
			//capture
			ArrayList<ChessAction> list = new ArrayList<ChessAction>();
			
			list.add(new ChessAction(firstsquare,"remove"));
			list.add(new ChessAction(secondsquare,"remove"));
			
			list.add(new ChessAction(emptyPiece.MovedPiece( col1, row1),"create"));
			list.add(new ChessAction(firstsquare.MovedPiece( col2, row2),"create"));
			if (row2 == 7 || row2 == 0){
				list.add(new ChessAction(firstsquare.MovedPiece( col2, row2),"promote"));
			}
			
			
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
			
			ArrayList<ChessAction> list = new ArrayList<ChessAction>();
			
			list.add(new ChessAction(firstsquare,"remove"));
			list.add(new ChessAction(secondsquare,"remove"));
			
			list.add(new ChessAction(emptyPiece.MovedPiece( col1, row1),"create"));
			list.add(new ChessAction(firstsquare.MovedPiece( col2, row2),"create"));
			list.add(new ChessAction(firstsquare.MovedPiece( col2, row2),"flag"));
			
			return list;
			
			
			
		} else if ( (Math.abs(row2 - row1) == 1) ){ // single move forward
			if ( (row2 - row1) != unitVectorCol ) {
				return null;
			}
			
			if ( board[col1][row2].pieceColor() != 'e'){
				return null;
			}
			
			ArrayList<ChessAction> list = new ArrayList<ChessAction>();
			
			list.add(new ChessAction(firstsquare,"remove"));
			list.add(new ChessAction(secondsquare,"remove"));
			
			list.add(new ChessAction(emptyPiece.MovedPiece( col1, row1),"create"));
			list.add(new ChessAction(firstsquare.MovedPiece( col2, row2),"create"));
			
			if (row2 == 7 || row2 == 0){
				list.add(new ChessAction(firstsquare.MovedPiece( col2, row2),"promote"));
			}
			
			
			return list;
		}
		
		
		return null;
	}
	
	private static ArrayList<ChessAction> ValidateRook(int col1, int row1, int col2, int row2){
		
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
		
		ArrayList<ChessAction> list = new ArrayList<ChessAction>();
		
		list.add(new ChessAction(firstsquare,"remove"));
		list.add(new ChessAction(secondsquare,"remove"));
		
		list.add(new ChessAction(emptyPiece.MovedPiece( col1, row1),"create"));
		list.add(new ChessAction(firstsquare.MovedPiece( col2, row2),"create"));
		
		
		return list;
	}
	
	private static ArrayList<ChessAction> ValidateKnight(int col1, int row1, int col2, int row2){
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
		
		ArrayList<ChessAction> list = new ArrayList<ChessAction>();
		
		list.add(new ChessAction(firstsquare,"remove"));
		list.add(new ChessAction(secondsquare,"remove"));
		
		list.add(new ChessAction(emptyPiece.MovedPiece( col1, row1),"create"));
		list.add(new ChessAction(firstsquare.MovedPiece( col2, row2),"create"));
		
		
		return list;
		
	}
	
	private static ArrayList<ChessAction> ValidateBishop(int col1, int row1, int col2, int row2){
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
		
		ArrayList<ChessAction> list = new ArrayList<ChessAction>();
		
		list.add(new ChessAction(firstsquare,"remove"));
		list.add(new ChessAction(secondsquare,"remove"));
		
		list.add(new ChessAction(emptyPiece.MovedPiece( col1, row1),"create"));
		list.add(new ChessAction(firstsquare.MovedPiece( col2, row2),"create"));
		
		
		return list;
	}
	
	private static ArrayList<ChessAction> ValidateQueen(int col1, int row1, int col2, int row2){
		
		ArrayList<ChessAction> bishopmove = ValidateBishop(col1, row1, col2, row2);
		
		if (bishopmove == null){
			return ValidateRook(col1, row1, col2, row2);
		}
		return bishopmove; //The code just keeps getting worse, this hasn't happened in a project before...I still feel like a horrible person.
	}
	
	private static ArrayList<ChessAction> ValidateKing(int col1, int row1, int col2, int row2){
		
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
					ArrayList<ChessAction> list = new ArrayList<ChessAction>();
					
					list.add(new ChessAction(firstsquare,"remove"));
					list.add(new ChessAction(secondsquare,"remove"));
					list.add(new ChessAction(board[rookcol1][row1],"remove"));
					list.add(new ChessAction(emptyPiece.MovedPiece( rookcol2, row1),"remove"));
					
					list.add(new ChessAction(emptyPiece.MovedPiece( col1, row1),"create"));
					list.add(new ChessAction(emptyPiece.MovedPiece( rookcol1, row1),"create"));
					list.add(new ChessAction(firstsquare.MovedPiece( col2, row2),"create"));
					list.add(new ChessAction(board[rookcol1][row1].MovedPiece( rookcol2, row1),"create"));
					
					return list;
					
				}
				
				
			}
			return null;
		}
		
		if ( firstsquare.pieceColor() == secondsquare.pieceColor()){
			return null;
		}
		
		
		
		ArrayList<ChessAction> list = new ArrayList<ChessAction>();
		
		list.add(new ChessAction(firstsquare,"remove"));
		list.add(new ChessAction(secondsquare,"remove"));
		
		list.add(new ChessAction(emptyPiece.MovedPiece( col1, row1),"create"));
		list.add(new ChessAction(firstsquare.MovedPiece( col2, row2),"create"));
		
		
		return list;
	}
	

}
