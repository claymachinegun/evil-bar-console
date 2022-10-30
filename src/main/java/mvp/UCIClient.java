package mvp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.function.Consumer;

public class UCIClient implements AutoCloseable {
    private Process process;
    private Consumer<String> onStringRecieved;
    private BufferedWriter writer;
    private String command;
    private Future<Void> processIO;

    public UCIClient(String command, Consumer<String> onStringRecieved) {
        this.command = command;
        this.onStringRecieved = onStringRecieved;
    }

    public void start(ExecutorService executorService) throws IOException {
        process = Runtime.getRuntime().exec(command);
        writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
        processIO = executorService.submit(() -> {
            try (BufferedReader processInput = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                while (true) {
                    String s = processInput.readLine();
                    if (onStringRecieved != null && s != null) {
                        onStringRecieved.accept(s);
                    }
                }
            }

        });
    }

    public void sendCommand(String inputString) throws IOException {
        writer.write(inputString);
        writer.flush();
    }

    @Override
    public void close() throws Exception {
        if (processIO != null) {
            processIO.cancel(true);
            processIO = null;
        }
        if (writer != null) {
            writer.close();
            writer = null;
        }
        if (process != null) {
            process.destroy();
            process = null;
        }
    }

}