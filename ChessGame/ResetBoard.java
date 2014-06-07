package chessGame;
import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

public class ResetBoard {
	final private static String[] board = {"a1.b"+ChessPiece.WhiteRook, "a2.w"+ChessPiece.WhitePawn, "a3.be", "a4.we", "a5.be", "a6.we", "a7.b"+ChessPiece.BlackPawn,"a8.w"+ChessPiece.BlackRook, 
		"b1.w"+ChessPiece.WhiteKnight,"b2.b"+ChessPiece.WhitePawn, "b3.we","b4.be", "b5.we", "b6.be", "b7.w"+ChessPiece.BlackPawn, "b8.b"+ChessPiece.BlackKnight, 
		"c1.b"+ChessPiece.WhiteBishop, "c2.w"+ChessPiece.WhitePawn, "c3.be", "c4.we", "c5.be", "c6.we", "c7.b"+ChessPiece.BlackPawn, "c8.w"+ChessPiece.BlackBishop,
		"d1.w"+ChessPiece.WhiteQueen, "d2.b"+ChessPiece.WhitePawn,	"d3.we", "d4.be", "d5.we", "d6.be", "d7.w"+ChessPiece.BlackPawn, "d8.b"+ChessPiece.BlackQueen, 
		"e1.b"+ChessPiece.WhiteKing, "e2.w"+ChessPiece.WhitePawn, "e3.be", "e4.we", "e5.be", "e6.we", "e7.b"+ChessPiece.BlackPawn, "e8.w"+ChessPiece.BlackKing,
		"f1.w"+ChessPiece.WhiteBishop,"f2.b"+ChessPiece.WhitePawn, "f3.we", "f4.be", "f5.we", "f6.be", "f7.w"+ChessPiece.BlackPawn, "f8.b"+ChessPiece.BlackBishop,
		"g1.b"+ChessPiece.WhiteKnight, "g2.w"+ChessPiece.WhitePawn, "g3.be", "g4.we", "g5.be", "g6.we", "g7.b"+ChessPiece.BlackPawn, "g8.w"+ChessPiece.BlackKnight,
		"h1.w"+ChessPiece.WhiteRook, "h2.b"+ChessPiece.WhitePawn, "h3.we", "h4.be", "h5.we", "h6.be", "h7.w"+ChessPiece.BlackPawn, "h8.b"+ChessPiece.BlackRook};
	
	/**
	 * This should reset the "game board" and create any files as needed. 
	 * @param args
	 * This should not have anything in it, Arguments will be ignored
	 */
	public static void main(String[] args) {
		
		
		File folder = new File(".");
		Pattern p = Pattern.compile("[a-h][1-8]\\.[wb][e" + ChessPiece.WhiteKing + "-" + ChessPiece.BlackPawn +"]");
		for(String file: folder.list()){
			if (p.matcher(file).matches()) {
				File filetodelete = new File(file);
				filetodelete.delete();
				System.out.print("deleted ");
			}
			System.out.println(file);
		}
		
		for (String file: board){
			File filetocreate = new File(file);
			try {
				filetocreate.createNewFile();
			} catch (IOException e) {
				System.out.println("Failed to create file " + file);
				e.printStackTrace();
			}
		}
		
		
		
	}

}
