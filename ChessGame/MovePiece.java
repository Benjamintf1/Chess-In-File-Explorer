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
										  {'w', 'b','w','b', 'w', 'b', 'w', 'b'}     };
	public static void main(String[] args) {
		board = new char[8][8];
		String hello = "hello";
		for (String s: args){
			System.out.println(s);
		}
		System.out.println(hello.charAt(1));

	}

}
