package mvp;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class App 
{
    public static void main( String[] args )
    {
        PuzzleFenSource source = null;
        BoardConsolePrinter printer = new BoardConsolePrinter();
        try {
            source = new PuzzleFenCsvSource();
        } catch(IOException exception) {
            exception.printStackTrace();
        }

        String s = source.iterator().next();
        try {
            try(UCIClient client = new UCIClient("stockfish", System.out::println)) {
                client.start(Executors.newFixedThreadPool(1));
                Thread.sleep(1000);
                client.sendCommand("uci\n");
                Thread.sleep(1000);
                client.sendCommand("position fen " + s + "\n");
                client.sendCommand("go depth 18\n");
                Thread.sleep(10 * 1000);
            }
        } catch(Exception exception) {
            exception.printStackTrace();
        }


        /*Stockfish stockfish = new Stockfish();
        stockfish.setOnStringRecieved(System.out::println);
        try {
            stockfish.start();
            stockfish.sendCommand("uci\n");
            Thread.sleep(1000);
            stockfish.sendCommand("position fen " + s + "\n");
            Thread.sleep(1000);
            stockfish.sendCommand("go depth 18\n");
            Thread.sleep(10000000);
        } catch(Exception e) {
            e.printStackTrace();
        }*/
        for(String fen : source) {
            //pass line into parser
            String position = fen.split(" ")[0];
            
            printer.print(position);
            System.out.println();

        }


    }
}
