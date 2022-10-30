package mvp;

import java.awt.im.InputContext;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.function.Consumer;

public class Stockfish {
    private Process process;
    private Consumer<String> onStringRecieved;
    private Future processIO;    
    private ExecutorService executorService;
    private ConcurrentLinkedQueue<String> writerQueue;
    private BufferedWriter writer;

    public void setOnStringRecieved(Consumer<String> consumer) {
        this.onStringRecieved = consumer;
        this.writerQueue = new ConcurrentLinkedQueue<>();
    }

    public Stockfish() {
        executorService = Executors.newFixedThreadPool(1);
    }

    public void start() throws IOException {
        process = Runtime.getRuntime().exec("stockfish");
        writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
        processIO = executorService.submit(()-> {
            
                try(BufferedReader processInput = new BufferedReader(new InputStreamReader(process.getInputStream()))){
                    while(true) {
                            String s = processInput.readLine();
                            if(onStringRecieved != null && s != null) {
                                onStringRecieved.accept(s);
                            }
                    }
            }
            
        });
    }

    public void sendCommand(String inputString) throws IOException {
            System.out.println("got this -> " + inputString);
            writer.write(inputString);
            writer.flush();
    } 

    public void stop() {
        processIO.cancel(true);
    }


}