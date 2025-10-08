package ch.heigvd.commands;

import ch.heigvd.Main;
import picocli.CommandLine;
import picocli.CommandLine.Command;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.concurrent.Callable;

@Command(name = "csvtomd", description = "Convert a CSV file to a MARKDOWN table")
public class CsvToMd implements Callable<Integer> {
    @CommandLine.ParentCommand protected Main parent;

    @CommandLine.Option(
            names = {"-o", "--output"},
            paramLabel = "MD_FILE",
            description = "The output filename. By default, it will be the input filename."
    )
    protected String outputFilename;

    protected char CSV_SEPARATOR = ',';
    protected char MD_SEPARATOR = '|';

    @Override
    public Integer call() {
        importCSV(parent.getCSVFilename());
        convertCSVtoMD();
        exportMD(outputFilename == null ? parent.getCSVFilename() + ".md" : outputFilename);
        return 0;
    }

    private static final String END_OF_LINE = "\n";

    private final ArrayList<String> csvLines = new ArrayList<>();
    private final ArrayList<String> mdLines = new ArrayList<>();

    private void importCSV(String filename){

        try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filename), StandardCharsets.UTF_8))){
            String line;
            while ((line = br.readLine()) != null) {
                csvLines.add(line);
            }
        }catch(IOException e){
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void exportMD(String filename){

        try(BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename), StandardCharsets.UTF_8))){
            for(String line : mdLines){
                bw.write(line);
            }
        }catch(IOException e){
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void convertCSVtoMD(){
        int nbColumns = buildMdHeader();
        buildMdHeaderSeparator(nbColumns);
        buildMdBody(nbColumns);
    }

    private int buildMdHeader(){
        int nbColumns = 0;
        boolean inQuotes = false;
        String header = csvLines.getFirst();

        StringBuilder sb = new StringBuilder();
        sb.append(MD_SEPARATOR);

        for(int i = 0; i < header.length(); ++i){
            char c = header.charAt(i);

            if(c == '"') inQuotes = !inQuotes;
            if(c == CSV_SEPARATOR && !inQuotes){
                sb.append(MD_SEPARATOR);
                ++nbColumns;
            }else{
                sb.append(c);
            }
        }

        sb.append(MD_SEPARATOR).append(END_OF_LINE);
        mdLines.add(sb.toString());

        return nbColumns + 1;
    }

    private void buildMdHeaderSeparator(int nbColumns){
        String headerSeparator = "|---".repeat(nbColumns) + "|" + END_OF_LINE;
        mdLines.add(headerSeparator);
    }

    private void buildMdBody(int nbColumns){
        boolean inQuotes = false;

        for(int i = 1; i < csvLines.size(); ++i) {
            String line = csvLines.get(i);

            StringBuilder sb = new StringBuilder();
            sb.append(MD_SEPARATOR);

            int currentColumn = 1;

            for (int j = 0; j < line.length(); ++j) {
                char c = line.charAt(j);
                if(c == '"'){
                    inQuotes = !inQuotes;
                }

                if (c == CSV_SEPARATOR && !inQuotes) {
                    ++currentColumn;
                    if (currentColumn > nbColumns)
                        break;
                    sb.append(MD_SEPARATOR);
                }else{
                    sb.append(c);
                }
            }
            sb.append(MD_SEPARATOR).append(END_OF_LINE);
            mdLines.add(sb.toString());
        }
    }
}
