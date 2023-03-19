# Evil bar
Evil bar is a console chess puzzle game in which you need to guess the stockfish evaluation of given position.

Game itself needs stockfish or (theoretically) any other UCI-supporting chess engine to evaluate positions
Positions in FEN format provided by [Lichess](https://database.lichess.org/#puzzles)

## Build and run
- Make sure you have stockfish installed
- Change stockfish [location](https://github.com/claymachinegun/evil-bar-console/blob/0483a92aedadcb4d482f95334f5717ba1753fd45/src/main/java/mvp/App.java#L16) if needed
- `./mvnw clean package`
- `java -jar ./target/mvp-1.0-SNAPSHOT.jar`
