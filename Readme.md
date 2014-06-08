CHESS IN FILE EXPLORE
---------------------

This project is still under development and details in this doccument are not how the project is, but rather as it is envisioned to be. 


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
	
	This should be compiled as both a jar file, and a .exe using jsmooth(so file associations work correctly)
	
	Install
	---------------
	This is likely to be written in C#. There needs to be a component for windows users which correctly associates all the file types to the MovePiece executable, as well as correctly setting
	up the icons to be used by the pieces
	
	Icons
	------------------
	This should be a folder containing all the icons for window users and png files for non-windows users. 
	
	
	NOTE: When this is completed, I expect to be able to set up associations and icons correctly for windows, however, as a warning to linux, osx, and whatever users, I do not anticipate
	making this work for these systems as well. While the core functionality should work, you will have to set the file icons yourself as well as the file associations if you wish to play
	using the mouse only.
	
	
Specifications
-----------------------

Pieces files shall be their algebraic location, followed by a dot(for file extension), and the extension shall be the color of the space followed by the unicode chess piece(or e for empty)

Any folders or files to be created by this program must start with a letter after h, so as not to mess up the board. 

