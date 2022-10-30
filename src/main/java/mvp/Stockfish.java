package mvp;

import java.awt.List;
import java.io.IOException;

public class Stockfish implements AutoCloseable {
    private SynchronousUCIClient uciClient;
    private int depth;

    public Stockfish(String stockfishPath, int depth) {
        this.uciClient = new SynchronousUCIClient(stockfishPath);
        this.depth = depth;
    }

    private String readUntilAndGetPrevious(String mark) throws IOException {
        String previous = "";
        String output = "";
        do {
            previous = output;
            output = this.uciClient.readLine();
        } while (!output.startsWith(mark));
        return previous;
    }

    public int getEvaluation(String fenPosition) throws IOException {
        String output = "";
        this.uciClient.sendCommand("ucinewgame\n");
        do {
            this.uciClient.sendCommand("isready\n");
            output = this.uciClient.readLine();
        } while (!output.startsWith("readyok"));

        this.uciClient.sendCommand(String.format("position fen %s\n", fenPosition));
            
        this.uciClient.sendCommand(String.format("go depth %d\n", this.depth));
        
        String result = this.readUntilAndGetPrevious("bestmove");
        System.out.println(result);

        return 5;
    }


    public void init() throws IOException {
        this.uciClient.start();
        this.uciClient.sendCommand("uci\n");
        readUntilAndGetPrevious("uciok");
        
    }

    @Override
    public void close() throws Exception {
        uciClient.close();
    }

    
    

}