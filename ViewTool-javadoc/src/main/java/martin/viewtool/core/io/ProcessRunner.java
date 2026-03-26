/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package martin.viewtool.core.io;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.function.IntConsumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility for running external processes and parsing their output.
 * Progress percentages are extracted from stdout/stderr.
 *
 * @author cesar
 */
public class ProcessRunner {

    private static final Pattern PERCENTAGE = Pattern.compile("(\\d{1,3}(?:\\.\\d+)?)%");

    /**
     * Runs a command, streams its output to {@code out}, and reports progress.
     *
     * @param commands      command and its arguments
     * @param fileDirectory working directory for the process, or {@code null} for the current dir
     * @param out           appendable that receives each output line, or {@code null} to discard
     * @param onPercent     callback invoked with progress percentage (0–100), or {@code null} to ignore
     * @return process exit code
     * @throws IOException          if the process cannot be started or output cannot be read
     * @throws InterruptedException if the thread is interrupted while waiting for the process
     */
    public static int run(List<String> commands, File fileDirectory, Appendable out, IntConsumer onPercent)
            throws IOException, InterruptedException {

        ProcessBuilder pb = new ProcessBuilder(commands);
        if (fileDirectory != null) {
            pb.directory(fileDirectory);
        }
        pb.redirectErrorStream(true);

        Process p = pb.start();

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(p.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (out != null) {
                    try {
                        out.append(line).append('\n');
                    } catch (IOException ignore) {
                    }
                }
                if (onPercent != null) {
                    String t = line.trim();
                    if (t.endsWith("%") && t.length() <= 8) { 
                        parseAndSendPercent(t.substring(0, t.length() - 1).trim(), onPercent);
                    } else {
                        
                        Matcher m = PERCENTAGE.matcher(line);
                        if (m.find()) {
                            parseAndSendPercent(m.group(1), onPercent);
                        }
                    }
                }
            }
        }
        return p.waitFor();
    }

    private static void parseAndSendPercent(String s, IntConsumer onPercent) {
        try {
            double d = Double.parseDouble(s);
            int pct = (int) Math.round(d);
            if (pct < 0) {
                pct = 0;
            }
            if (pct > 100) {
                pct = 100;
            }
            onPercent.accept(pct);
        } catch (NumberFormatException ignore) {
        }
    }
}
