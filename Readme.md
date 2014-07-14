CHESS IN FILE EXPLORE
---------------------

THIS PROJECT IS STILL UNDER DEVELOPMENT. 

Needs instilation of file associations and file icons. And the Move Piece module needs to be re-read and tested throughly.


Purpose
--------------

A long time ago, while coding in the lab for a class, I jokingly mentioned coding a game in git ("the trick is to trick git into making the gui).
Now obviously this doesn't make sense and is kinda dumb, but though that I had the idea of making tic-tac-toe only using the file system. This was
completed quickly and rather easily. I showed it off to some people and discussed some things, and eventually conceded that I could do chess using
the file system, albeit with less restrictions. 

	Can run code to achieve the task of playing Chess. 
	Must be portable. Unlike tic tac toe, it must be able to run on all operating systems
	Must be accessible. No downloading of software or command line knowledge necessary, or knowledge of the registry be required to play. Must be able to play solely by clicking Files

	
Components
-----------

	ResetBoard 
	----------
	This shall reset the board, nothing special just look for all the files that conform to the piece specification, delete them, and then create files that conform to the start of the game. 
	This should also reset any logs, the problems file, and .LastMove file. 
	
	MovePiece
	-------------
	This should take either a Algebraic notation move and a folder (via command line) (this is for pros), or should be associated with all the pieces, and take a piece file. 
	After the arguments have been processed it should either store the argument in .LastMove(if the move cannot be completed as specified), or Validate the move and edit the board files as
	needed. It should also store the algabraic notation move into a log file. If the move is not valid, store it in the Problems file. 
	
	This will be compiled as both a jar file, and a .exe using Launch4j(so file associations work correctly) and released. 
	
	
	
Specifications
-----------------------

Pieces files shall be their algebraic location, followed by a dot(for file extension), and the extension shall be the color of the space followed by the unicode chess piece(or e for empty). There will also be created a z.lastclick and z.whosemove file. 

Any other folders or files to be created by this program must start with a letter after h, so as not to mess up the board. 

