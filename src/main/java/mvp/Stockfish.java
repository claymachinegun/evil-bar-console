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
            try {
                Thread.sleep(1000);
            } catch(InterruptedException ignored) {}
        } while (!output.startsWith("readyok"));

        this.uciClient.sendCommand(String.format("position fen %s\n", fenPosition));
            
        this.uciClient.sendCommand(String.format("go depth %d\n", this.depth));
        
        String result = this.readUntilAndGetPrevious("bestmove");

        int searchFrom = result.indexOf("score", 0);
        if(searchFrom < 0) {
            throw new IOException("no `score` appears");
        }

        searchFrom+=6;
        int searchTo = result.indexOf(' ', searchFrom);
        if(searchTo < 0) {
            throw new IOException("invalid `score` string");
        }
        searchTo = result.indexOf(' ', searchTo+1);
        if(searchTo < 0) {
            throw new IOException("invalid `score` string");
        }

        String[] score = result.substring(searchFrom, searchTo).split(" ");

        if(score.length != 2) {
            throw new IOException("cannot split `score` parts");
        }
        int resultingScore = -9999;
        try {
            resultingScore = Integer.parseInt(score[1]);
        } catch (NumberFormatException exception) {
            throw new IOException("error parsing score");
        }

        if(score[0].equals("mate")) {
            resultingScore = resultingScore * 4300;
        }

        return resultingScore;
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