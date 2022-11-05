package mvp;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
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
            Scanner input = new Scanner(System.in);
            
            for (String fen : source) {
                FenPosition meta = FenPosition.fromString(fen, stockfish);

                String position = meta.getSource().split(" ")[0];

                printer.print(position);

                System.out.println();
                String choice = null;
                while(choice == null) {
                    System.out.print(String.format("%s to move! Who is better?(b/w/d/q):", meta.getMoveSide()));
                    choice = input.nextLine();
                }
                boolean quit = false;
                int estimate = 0;
                switch(choice.charAt(0)) {
                    case 'b':
                        estimate = -1;
                        break;
                    case 'w':
                        estimate = 1;
                        break;
                    case 'd':
                        estimate = 0;
                        break;
                    case 'q':
                        quit = true;
                    break;
                }

                if(quit) {
                    break;
                }
                
                if(Integer.signum(estimate) == (int)Math.signum(meta.getComplexEvaluation())) {
                    System.out.print("CORRECT!");
                } else if(estimate == 0) {
                    if(Math.abs(meta.getComplexEvaluation()) < 100) {
                        System.out.print("CORRECT!");
                    } else {
                        System.out.print("WRONG!");
                    }
                } else {
                    System.out.print("WRONG!");
                }

                System.out.println(String.format("%s (material:%f position:%f)",
                        fen, meta.getMaterialEvaluation(), meta.getComplexEvaluation()));

                System.out.println();

            }
            input.close();
            stockfish.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
