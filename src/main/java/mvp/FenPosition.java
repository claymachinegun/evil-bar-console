package mvp;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FenPosition {
    public enum MoveSide {WHITE, BLACK}

    private final static String fenRegEx = "^([pPnNbBqQkKrR0-9]+\\/[pPnNbBqQkKrR0-9]+\\/[pPnNbBqQkKrR0-9]+\\/[pPnNbBqQkKrR0-9]+\\/[pPnNbBqQkKrR0-9]+\\/[pPnNbBqQkKrR0-9]+\\/[pPnNbBqQkKrR0-9]+\\/[pPnNbBqQkKrR0-9]+) ([wb])(.+)$";
    private final static Pattern fenRegExPattern = Pattern.compile(fenRegEx);

    private final static Map<Character, Integer> pieceValues = Map.ofEntries(
        Map.entry('p', -1),
        Map.entry('P',  1),
        Map.entry('n', -3),
        Map.entry('N',  3), 
        Map.entry('b', -3),
        Map.entry('B',  3),
        Map.entry('k', -4),
        Map.entry('K',  4),
        Map.entry('r', -5),
        Map.entry('R',  5),
        Map.entry('q', -9),
        Map.entry('Q',  9)
    );

    public static FenPosition fromString(String fen, Stockfish stockfish) throws Exception {
        FenPosition result = new FenPosition();
        result.setSource(fen);
        
        Matcher matcher = fenRegExPattern.matcher(fen);
        matcher.find();
        if(matcher.groupCount() != 3) {
            throw new Exception("FEN parsing Error");
        }

        String pieces = matcher.group(1);
        String move = matcher.group(2);

        if(move.equals("w") || move.equals("W")) {
            result.setMoveSide(MoveSide.WHITE);
        } else {
            result.setMoveSide(MoveSide.BLACK);
        }

        int materialEvaluation = 0;
        for(int i = 0 ; i < pieces.length(); i++) {
            if(pieceValues.containsKey(pieces.charAt(i))) {
                materialEvaluation += pieceValues.get(pieces.charAt(i));
            }
        }

        result.setMaterialEvaluation(materialEvaluation);

        int complexEvaluation = stockfish.getEvaluation(fen);

        if(result.getMoveSide() == MoveSide.BLACK) {
            complexEvaluation = complexEvaluation * -1;
        }

        result.setComplexEvaluation(complexEvaluation);

        return result;
    }


    private String source;
    private float materialEvaluation;
    private float complexEvaluation;
    private MoveSide moveSide;


    private FenPosition() {};


    public String getSource() {
        return source;
    }

    private void setSource(String source) {
        this.source = source;
    }

    public float getMaterialEvaluation() {
        return materialEvaluation;
    }

    private void setMaterialEvaluation(float materialEvaluation) {
        this.materialEvaluation = materialEvaluation;
    }

    public float getComplexEvaluation() {
        return complexEvaluation;
    }

    private void setComplexEvaluation(float complexEvaluation) {
        this.complexEvaluation = complexEvaluation;
    }

    public MoveSide getMoveSide() {
        return moveSide;
    }

    public void setMoveSide(MoveSide moveSide) {
        this.moveSide = moveSide;
    }





}
