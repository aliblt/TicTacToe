Detailed Description of the implementation

Player CLASS
Basic class for keeping clients as an object. It contains id and socket of the client.

Game CLASS
This class is responsible at operations on a game board. It also includes the game board.
It keeps players, boards, who is turn currently and game status.
Also it checks current game situation if it is win, lose or tie.
Board kept as a char array from 0 to 8
0 | 1 | 2
3 | 4 | 5
6 | 7 | 8
Those are the corresponding values for game board.

Client CLASS
This class initially takes server ip and port number as a parameter accordingly.
Then it starts a connection between user and server. After connection established,
It waits for user inputs from keyboard. The commands are "LIST", "LOGOUT", and "PLAY".
If the user enters PLAY as an input, it sends message to a server and server adds it to play queue.
When opponent found by server, the game starts. Server sends information about who is turn.
If player moves, it also gets updated situation of the game. For move input, player only needs to
enter one number which should be between 0 and 8 (See table of Game Class).

Server CLASS
This class takes server port number as a parameter and initialize a ServerSocket for TCP connection.
It starts to listen connection and if it receives a connection, it crates player object and thread for
communication between server and client. This class also keeps the Player lists and queue for players who
wants to play a game.

ServerThread CLASS
This class is responsible for communication management between server and client. It receives a command and
reply according to message type. If a player sends a PLAY command, this class call addPlayer() function to
add the player to queue. After that it starts to check if there is an opponent (checkPlayerNumbers() method).
If an opponent founds and there is no on going game, the new game starts. After every movement of player,
it sends information to both sides about who is turn currently and what is the latest situation for game board
(playGame() method). If the game ends, it also sends if there is winner or it is tie.
If user wants, they can send LOGOUT command for logging out.

How To Compile
javac Server.java ServerThread.java Player.java Game.java Client.java

How to Run
To run server: java Server <port Number>
To run a client: java Client <server ip addr> <server port Number>

How to use
First start server code. After that start clients. Clients gives inputs ("LIST", "LOGOUT", "PLAY").
If they want to play the game, They wait for match. After match, if it is their turn they enter a number,
which indicates board tile number for move.

What does not work:
-UDP streaming