package ChessGame;
import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

public class ResetBoard {
	final private static String[] board = {"a1.bwr", "a2.wwp", "a3.be", "a4.we", "a5.be", "a6.we", "a7.bbp","a8.wbr", "b1.wwk","b2.bwp", "b3.we",
		"b4.be", "b5.we", "b6.be", "b7.wbp", "b8.bbk", "c1.bwb", "c2.wwp", "c3.be", "c4.we", "c5.be", "c6.we", "c7.bbp", "c8.wbb", "d1.wwq", "d2.bwp",
		"d3.we", "d4.be", "d5.we", "d6.be", "d7.wbp", "d8.bbq", "e1.bwk", "e2.wwp", "e3.be", "e4.we", "e5.be", "e6.we", "e7.bbp", "e8.wbk", "f1.wwb",
		"f2.bwp", "f3.we", "f4.be", "f5.we", "f6.be", "f7.wbp", "f8.bbb", "g1.bwk", "g2.wwp", "g3.be", "g4.we", "g5.be", "g6.we", "g7.bbp", "g8.wbk",
		"h1.wwr", "h2.bwp", "h3.we", "h4.be", "h5.we", "h6.be", "h7.wbp", "h8.bbr"};
	
	/**
	 * This should reset the "game board" and create any files as needed. 
	 * @param args
	 * This should not have anything in it, Arguments will be ignored
	 */
	public static void main(String[] args) {
		File folder = new File(".");
		Pattern p = Pattern.compile("[a-h][1-8]\\.[wb][wb]?[kqrbkpe]");
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
