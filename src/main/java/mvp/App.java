package mvp;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class App {
    public static void main(String[] args) {
        PuzzleFenSource source = null;
        BoardConsolePrinter printer = new BoardConsolePrinter();
        try {
            source = new PuzzleFenCsvSource();
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        String s = source.iterator().next();

        Stockfish stockfish = new Stockfish("stockfish", 18);
        try {

            stockfish.init();

            for (String fen : source) {
                FenPosition meta = FenPosition.fromString(fen, stockfish);

                String position = meta.getSource().split(" ")[0];

                printer.print(position);

                System.out.println();
                
                System.out.println(String.format("%s to move ! (material:%f position:%f)",
                        meta.getMoveSide().toString(), meta.getMaterialEvaluation(), meta.getComplexEvaluation()));

                System.out.println();

            }
            stockfish.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
