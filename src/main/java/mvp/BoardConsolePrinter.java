package mvp;

public class BoardConsolePrinter {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
    public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
    public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
    public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
    public static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
    public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";


    private String blackSquare;
    private String whiteSquare;
    private String blackPiece;
    private String whitePiece;

    public BoardConsolePrinter() {
        this.blackSquare = ANSI_GREEN_BACKGROUND;
        this.blackPiece = ANSI_BLACK;
        this.whiteSquare = ANSI_WHITE_BACKGROUND;
        this.whitePiece = ANSI_RED;
    }



    public BoardConsolePrinter(String blackSquare, String whiteSquare, String blackPiece, String whitePiece) {
        this.blackSquare = blackSquare;
        this.blackPiece = blackPiece;
        this.whiteSquare = whiteSquare;
        this.whitePiece = whitePiece;
        
    }

    private void setCellBackground(int cell) {
        System.out.print(cell % 2 == 1 ?  blackSquare : whiteSquare);
    }

    public void print(String fen) {
        int cell = 0;
        for(int i = 0; i < fen.length(); i ++) {
            char c = fen.charAt(i);
            if(Character.isLetter(c)) {
                setCellBackground(cell);
                System.out.print(String.format(" %s ", (Character.isLowerCase(c) ? blackPiece : whitePiece) + c));
                cell++;
            }
            if(Character.isDigit(c)) {
                int empty = c-'0';
                for(int k = 0; k < empty; k++) {
                    setCellBackground(cell);
                    System.out.print("   ");
                    cell++;
                }
            }
            if(c == '/' || i == fen.length()-1) {
                System.out.println(ANSI_BLACK_BACKGROUND + ANSI_WHITE + " " + String.valueOf(9 - (cell / 8)));
                cell++;
            }
        }

        for(int i = 0; i < 8; i ++) {
            System.out.print(" " + (char)('a' + i) + " ");
        }
        System.out.println("");
    }
}