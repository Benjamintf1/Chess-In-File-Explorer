

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
	private static ChessPiece emptyPiece = new ChessPiece('e', false);//this allows for easy placement of empty pieces. 
	
	
	public static void main(String[] args) {

		
		
		
		
		
		if (args.length == 1){
			
			
			Pattern validargument = Pattern.compile("([a-h][1-8]|promote)\\.[wb][e" + ChessPiece.WhiteKing + "-" + ChessPiece.BlackPawn +"]");
			File argument = new File(args[0]);
			
			if (! argument.exists()){
				System.out.println("I don't know what to do here, like, read up on the readme or something, because the argument you passed through there isn't a file");
				System.exit(1); //TODO
			}
			
			workingfolder = argument.getParentFile();
			
			
			File lastclickfile = new File(workingfolder, "z.lastclick" );
			
			File whosemovefile = new File(workingfolder, "z.whoseturn");
			try {
				BufferedReader br = new BufferedReader(new FileReader(whosemovefile));
				String line = br.readLine();
				whoseturn = line.charAt(0);
				if (!(whoseturn == 'w' | whoseturn == 'b')){
					
					System.exit(1);//TODO
				}
				br.close();
				
			} catch (Exception e) {
				
				e.printStackTrace();
				System.exit(1); //TODO
			}
			if (!whosemovefile.exists()){
				System.out.println("no whose move exits.");
				System.exit(1);//todo
			}
			if (!validargument.matcher(argument.getName()).matches()){
				System.out.println("This is not a valid piece. ");
				System.exit(1); //TODO
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
								System.out.println("The king is " + currentpiece.toString());
								theKing = currentpiece;
							}
						}
					}
					
					
				} catch (Exception e) {
					System.out.println("Something happened wrong with reading from the \"last click\" file. " );
					e.printStackTrace();
					lastclickfile.delete();
					System.exit(1); //TODO
				}
				Pattern isPromotion = Pattern.compile("promote\\.[wb][" + ChessPiece.WhiteKing + "-" + ChessPiece.BlackPawn +"]");
				
				if( isPromotion.matcher(argument.getName()).matches()){
					File newPiece = new File(workingfolder, ((char) ('a' + lastClick.col)) + Integer.toString(lastClick.row + 1)+argument.getName().substring(7)) ;
					new File(workingfolder, lastpieceFile).delete();
					try {
						newPiece.createNewFile();
						lastclickfile.delete();
						whosemovefile.delete();
						whosemovefile.createNewFile();
						FileWriter fw = new FileWriter(whosemovefile);
						if(whoseturn == 'w'){
							fw.write("b");
						} else {
							fw.write("w");
						}
						fw.close();
						
					} catch (IOException e) {
						
						e.printStackTrace();
						System.exit(1);//TODO
					}
					
					
				}
				else{
					ChessPiece nextClick = new ChessPiece(argument.getName(), argument.length() > 0);
					ArrayList<ChessAction> thisMove = ValidateMove( lastClick.col ,  lastClick.row,  nextClick.col,  nextClick.row);
					if (thisMove == null) {
						System.out.println("invalid move");
						lastclickfile.delete();
						System.exit(1);
					}
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
							System.exit(1); //TODO
						}
						
					}
					for(ChessPiece[] array : board){
						for (ChessPiece piece : array){
							if( piece.pieceColor() != 'e' && piece.pieceColor() != whoseturn){
								if(ValidateMove( piece.col ,  piece.row,  theKing.col,  theKing.row) != null){
									
									System.out.println("This move would put the king in check, it is not valid., the piece is " + piece.toString() + theKing.toString());
									lastclickfile.delete();
									System.exit(1);//TODO
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
								System.exit(1); //TODO
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
						System.exit(1); //TODO
						e.printStackTrace();
					}
					
				}
				
			} else { //
				try {
					
					lastclickfile.createNewFile();
					
					ChessPiece currentPiece = new ChessPiece(argument.getName(), false);
					if (currentPiece.pieceColor() != whoseturn){
						System.out.println("This piece is not valid, you can only control your own pieces.");
						System.exit(1); //TODO
					}
					
					
					FileWriter fw = new FileWriter(lastclickfile);
					fw.write(argument.getName());
					fw.close();
				} catch (IOException e) {
					System.out.println("I couldn't create a file, whoops.");
					e.printStackTrace();
					System.exit(1); //TODO				
				}
				
				
				
			}
			
			
			
			
			
		} else {
			System.out.println("This program only supports one argument at the moment(the file you are clicking) sorry."); // wrong arguments supplied.
		}
	}
	
	
	
	private static ArrayList<ChessAction> ValidateMove(int col1, int row1, int col2, int row2){ //run respective Validate move, if wrong, exit. 
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
			System.exit(1); //TODO
			return null;
		}
		
	}
	
	private static ArrayList<ChessAction> ValidatePawn(int col1, int row1, int col2, int row2){ //ugly
		
		ChessPiece firstsquare = board[col1][row1]; //easy access.
		ChessPiece secondsquare = board[col2][row2];
		
		int unitVectorCol; //set direction. 
		if (firstsquare.pieceColor() == 'w') {
			unitVectorCol = 1; 
		} else {
			unitVectorCol = -1;
		}
		
		
		if (col1 != col2) { //must be a capture
			if ( (row2 - row1) != unitVectorCol ) { //not moving forward. 
				return null;
			}
			
			if (Math.abs(col1 - col2) != 1){ // only by one
				return null;
			}
			
			if ( secondsquare.pieceColor() != 'e' ){
				//en passant
				ChessPiece secondPiece = board[col2][row2-unitVectorCol];
				
				if (secondPiece.pieceColor() != firstsquare.pieceColor()) { // can't capture own piece. 
					if (secondPiece.isPawn()) {//must be a pawn. 
						if (secondPiece.flag) {//that has moved twice
							ArrayList<ChessAction> list = new ArrayList<ChessAction>();//complex move
							
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
			
			if ( firstsquare.pieceColor() == secondsquare.pieceColor()){ //can't capture own piece. 
				return null;
			}
			//capture
			ArrayList<ChessAction> list = new ArrayList<ChessAction>(); //standard move
			
			list.add(new ChessAction(firstsquare,"remove"));
			list.add(new ChessAction(secondsquare,"remove"));
			
			list.add(new ChessAction(emptyPiece.MovedPiece( col1, row1),"create"));
			list.add(new ChessAction(firstsquare.MovedPiece( col2, row2),"create"));
			if (row2 == 7 || row2 == 0){
				list.add(new ChessAction(firstsquare.MovedPiece( col2, row2),"promote"));//promote if on last row. 
			}
			
			
			return list;
			
			
			
			
		} else if ( (Math.abs(row2 - row1) == 2) ) {
			
			//double first move
			if (!( row1 == 6 || row1 == 1)){
				return null; //must be pawns first move
			}
			
			if (board[col1][row1+unitVectorCol].pieceColor() != 'e'){
				return null; // intermediary must be empty. 
			}
			
			if ( board[col1][row2].pieceColor() != 'e'){
				return null; //second space must also be empty. 
			}
			
			ArrayList<ChessAction> list = new ArrayList<ChessAction>();//standard move-ish
			
			list.add(new ChessAction(firstsquare,"remove"));
			list.add(new ChessAction(secondsquare,"remove"));
			
			list.add(new ChessAction(emptyPiece.MovedPiece( col1, row1),"create"));
			list.add(new ChessAction(firstsquare.MovedPiece( col2, row2),"create"));
			list.add(new ChessAction(firstsquare.MovedPiece( col2, row2),"flag")); //allow for passant. 
			
			return list;
			
			
			
		} else if ( (Math.abs(row2 - row1) == 1) ){ // single move forward
			if ( (row2 - row1) != unitVectorCol ) { //right direction.
				return null;
			}
			
			if ( board[col1][row2].pieceColor() != 'e'){ //dest is empty.
				return null;
			}
			
			ArrayList<ChessAction> list = new ArrayList<ChessAction>(); // standard move.
			
			list.add(new ChessAction(firstsquare,"remove"));
			list.add(new ChessAction(secondsquare,"remove"));
			
			list.add(new ChessAction(emptyPiece.MovedPiece( col1, row1),"create"));
			list.add(new ChessAction(firstsquare.MovedPiece( col2, row2),"create"));
			
			if (row2 == 7 || row2 == 0){
				list.add(new ChessAction(firstsquare.MovedPiece( col2, row2),"promote"));//if on the last row, promote
			}
			
			
			return list;
		}
		
		
		return null;//if not any of those cases, it's not valid. 
	}
	
	private static ArrayList<ChessAction> ValidateRook(int col1, int row1, int col2, int row2){
		
		ChessPiece firstsquare = board[col1][row1]; //easy access 
		ChessPiece secondsquare = board[col2][row2];
		
		
		int lengthCol = col2-col1;
		int lengthRow =  row2- row1;
		
		
		if ( !((lengthCol != 0 && lengthRow == 0) || (lengthRow != 0 && lengthCol == 0))) { //make sure in straight line.
			return null;
		}
		int unitVectorCol;
		
		if (lengthCol != 0){//prevent devision by 0. 
			unitVectorCol = lengthCol / Math.abs(lengthCol); //get unit vector for both. 
		} else {
			unitVectorCol = 0;
		}
		int unitVectorRow;
		if (lengthRow != 0){
			unitVectorRow = lengthRow / Math.abs(lengthRow);
		} else {
			unitVectorRow = 0;
		}

		for (int i = 1; col2 != col1 + unitVectorCol*i && row2 != row1 + unitVectorRow*i; ++i){//make sure there is no pieces blocking.
			if (board[col1 + unitVectorCol*i][row1 + unitVectorRow*i].pieceColor() != 'e'){
				return null;
			}
		}
		
	
		if ( firstsquare.pieceColor() == secondsquare.pieceColor()){ //can't capture same colored piece. 
			return null;
		}
		
		ArrayList<ChessAction> list = new ArrayList<ChessAction>(); //standard move.
		
		list.add(new ChessAction(firstsquare,"remove"));
		list.add(new ChessAction(secondsquare,"remove"));
		
		list.add(new ChessAction(emptyPiece.MovedPiece( col1, row1),"create"));
		list.add(new ChessAction(firstsquare.MovedPiece( col2, row2),"create"));
		
		
		return list;
	}
	
	private static ArrayList<ChessAction> ValidateKnight(int col1, int row1, int col2, int row2){
		ChessPiece firstsquare = board[col1][row1];
		ChessPiece secondsquare = board[col2][row2];
		
		int lengthCol = Math.abs(col1 - col2); //used to make sure shape is correct. 
		int lengthRow = Math.abs(row1 - row2);
		
		
		if ( !(lengthCol == 2 && lengthRow == 1 || lengthRow == 2 && lengthCol == 1)) { //check for valid shape. 
			return null;
		} 
		if ( firstsquare.pieceColor() == secondsquare.pieceColor() ){ //can't capture same colored piece. 
			return null;
		}
		
		ArrayList<ChessAction> list = new ArrayList<ChessAction>();
		
		list.add(new ChessAction(firstsquare,"remove")); //standard move. 
		list.add(new ChessAction(secondsquare,"remove"));
		
		list.add(new ChessAction(emptyPiece.MovedPiece( col1, row1),"create"));
		list.add(new ChessAction(firstsquare.MovedPiece( col2, row2),"create"));
		
		
		return list;
		
	}
	
	private static ArrayList<ChessAction> ValidateBishop(int col1, int row1, int col2, int row2){
		ChessPiece firstsquare = board[col1][row1]; //easy accessers
		ChessPiece secondsquare = board[col2][row2];
		
		
		
		int directionCol = col2- col1 ; //used to determine which direction we are going
		int directionRow =  row2- row1;

		
		if ( !( Math.abs(directionCol) == Math.abs(directionRow) )) { //bishop must be diagonal. 
			return null;
		}
		int unitVectorCol = directionCol / Math.abs(directionCol); // get the unit vector so we can use it as an iterator.
		int unitVectorRow = directionRow / Math.abs(directionRow);


		for (int i = 1; col2 != col1 + unitVectorCol*i && row2 != row1 + unitVectorRow*i; ++i){
			//until we reach the end, keep checking
			if (board[col1 + unitVectorCol*i][row1 + unitVectorRow*i].pieceColor() != 'e'){ // if intermediary square is not empty, then it is an invalid move. 
				return null;
			} else {
				System.out.println(board[col1 + unitVectorCol*i][row1 + unitVectorRow*i].pieceColor());
			}
		}
		
		
		
		if ( firstsquare.pieceColor() == secondsquare.pieceColor()){ //cannot capture same color piece. 
			return null;
		}
		
		ArrayList<ChessAction> list = new ArrayList<ChessAction>();
		
		list.add(new ChessAction(firstsquare,"remove")); //standard move. 
		list.add(new ChessAction(secondsquare,"remove"));
		
		list.add(new ChessAction(emptyPiece.MovedPiece( col1, row1),"create"));
		list.add(new ChessAction(firstsquare.MovedPiece( col2, row2),"create"));
		
		
		return list;
	}
	
	private static ArrayList<ChessAction> ValidateQueen(int col1, int row1, int col2, int row2){
		
		ArrayList<ChessAction> bishopmove = ValidateBishop(col1, row1, col2, row2);
		
		if (bishopmove == null){ //if it isn't a valid bishop move, then see if it'd be a valid rook move. 
			System.out.println("problem in rook");
			return ValidateRook(col1, row1, col2, row2);
		}
		System.out.println("problem in bishop");
		return bishopmove; //The code just keeps getting worse, this hasn't happened in a project before...I still feel like a horrible person.
	}
	
	private static ArrayList<ChessAction> ValidateKing(int col1, int row1, int col2, int row2){
		
		ChessPiece firstsquare = board[col1][row1]; //easy access for endpoints
		ChessPiece secondsquare = board[col2][row2];
		
		
		int lengthRow = Math.abs(row1 - row2);
		int lengthCol = Math.abs(col1 - col2);
		
		
		if (lengthRow > 1 || lengthCol > 1) { //it's either invalid, or castle
			if (lengthCol == 2 && lengthRow == 0){ // it's a castle
				int rookcol1; //where the rook is
				int rookcol2; //where it's going
				int iterator; 
				if (col1 == 2){ //queen's side castle
					iterator = -1;
					rookcol1 = 0;
					rookcol2 = 3;
				} else { //king's side
					iterator = 1;
					rookcol1 = 7;
					rookcol2 = 5;
				}
				
				if(firstsquare.flag && board[rookcol1][row1].flag){ //make sure the king and rook havn't move
					
					for ( int i = iterator; i + col1 != rookcol1; i = i + iterator){//check to see if the intermediary spaces are empty
						if (board[col1 + i][row1].pieceColor() != 'e'){
							return null; // if they are not, return a invalid move
						}
					}
					ArrayList<ChessAction> list = new ArrayList<ChessAction>(); 
					
					list.add(new ChessAction(firstsquare,"remove")); //remove king
					list.add(new ChessAction(secondsquare,"remove")); //remove square he's moving to
					list.add(new ChessAction(board[rookcol1][row1],"remove")); //remove rook
					list.add(new ChessAction(emptyPiece.MovedPiece( rookcol2, row1),"remove")); //remove piece rook is moving to
					
					list.add(new ChessAction(emptyPiece.MovedPiece( col1, row1),"create")); //make empty square where king used to be
					list.add(new ChessAction(emptyPiece.MovedPiece( rookcol1, row1),"create")); //same for rook
					list.add(new ChessAction(firstsquare.MovedPiece( col2, row2),"create")); //place king
					list.add(new ChessAction(board[rookcol1][row1].MovedPiece( rookcol2, row1),"create")); //place rook
					
					return list;
					
				}
				
				
			}
			return null; //not a castle, but just invalid move
		}
		
		if ( firstsquare.pieceColor() == secondsquare.pieceColor()){
			return null; //can't capture the same color piece.
		}
		
		
		
		ArrayList<ChessAction> list = new ArrayList<ChessAction>();
		
		list.add(new ChessAction(firstsquare,"remove")); //standard move, remove entry and exit, make new pieces. 
		list.add(new ChessAction(secondsquare,"remove"));
		
		list.add(new ChessAction(emptyPiece.MovedPiece( col1, row1),"create"));
		list.add(new ChessAction(firstsquare.MovedPiece( col2, row2),"create"));
		
		
		return list;
	}
	

}
