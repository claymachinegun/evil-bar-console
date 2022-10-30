package mvp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PuzzleFenCsvSource implements PuzzleFenSource {
    Iterator<String> wrapped;

    private String extractFen(String input) {
        
        int begin = input.indexOf(',', 0)+1;
        
        if(begin <= 0) {
            return null;
        }

        int end = input.indexOf(',',begin);

        if(end < 0 || end - begin <= 1) {
            return null;
        }

        return input.substring(begin,end-begin);
    }

    public PuzzleFenCsvSource() throws IOException {
        final List<String> source = new ArrayList<String>();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(PuzzleFenCsvSource.class.getClassLoader().getResourceAsStream("puzzles.csv")))) {
            
            reader.lines().forEach((csv)-> {
                String output = extractFen(csv);
                if(output != null) {
                    source.add(output);
                }
            });
            
        }

        wrapped = source.iterator();
    }

    @Override
    public Iterator<String> iterator() {
        return wrapped;
    }


    

}