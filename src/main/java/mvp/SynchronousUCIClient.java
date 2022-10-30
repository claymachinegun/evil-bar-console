package mvp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.function.Consumer;

public class SynchronousUCIClient implements AutoCloseable {
    private Process process;
    private BufferedWriter writer;
    private BufferedReader reader;
    private String command;
    private Future<Void> processIO;

    public SynchronousUCIClient(String command) {
        this.command = command;
    }

    public void start() throws IOException {
        if(process != null) {
            throw new IOException("Process already started");
        }
        process = Runtime.getRuntime().exec(command);
        writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
        reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
    }

    public void sendCommand(String inputString) throws IOException {
        if(writer == null) {
            throw new IOException("Process does not started");
        }
        writer.write(inputString);
        writer.flush();
    }

    public String readLine() throws IOException {
        if(reader == null) {
            throw new IOException("Process does not started");
        }
        return reader.readLine();
    }

    @Override
    public void close() throws Exception {
        if (writer != null) {
            writer.close();
            writer = null;
        }
        if(reader != null) {
            reader.close();
            reader = null;
        }
        if (process != null) {
            process.destroy();
            process = null;
        }
    }
}

