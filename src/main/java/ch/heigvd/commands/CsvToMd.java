/*********************************************************************************************************************
 * @filename    : CsvToMd.java
 * @authors     : Fabien Léger and Samuel Dos Santos
 * @version     : 1.0.0
 * @updated     : 13.10.2025
 * @description : picocli subcommand to transform a CSV file into a MD table inside a MD file
 *********************************************************************************************************************/

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
    @CommandLine.ParentCommand private Main parent;

    @CommandLine.Option(
            names = {"-o", "--output"},
            paramLabel = "MD_FILE",
            description = "The output filename. By default, it will be the input filename."
    )
    private String outputFilename;

    private char csvSeparator;

    @Override
    public Integer call() {
        csvSeparator = parent.getCsvSeparator();

        importCSV(parent.getInputFilename());
        convertCSVtoMD();
        exportMD(outputFilename == null ? parent.getInputFilename() + ".md" : outputFilename);
        return 0;
    }

    private static final char END_OF_LINE = '\n';
    private final char MD_SEPARATOR       = '|';


    private final ArrayList<String> csvLines = new ArrayList<>();
    private final ArrayList<String> mdLines  = new ArrayList<>();

    /**
     * Import a file by reading it and storing the data in an ArrayList.
     * @param filename Path to the input file
     */
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

    /**
     * Export a .md file
     * @param filename Path to the location of the destination file.
     */
    private void exportMD(String filename){

        try(BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename), StandardCharsets.UTF_8))){
            for(String line : mdLines){
                bw.write(line);
            }
        }catch(IOException e){
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Convert the imported CSV lines in a Markdown table, build a header, a separator and the body of the file.
     */
    private void convertCSVtoMD(){
        int nbColumns = buildMdHeader();
        buildMdHeaderSeparator(nbColumns);
        buildMdBody(nbColumns);
    }

    /**
     * Build the header of the file from the first line of the CSV data.
     * @return The number of columns.
     */
    private int buildMdHeader(){
        int nbColumns = 0;
        boolean inQuotes = false;
        String header = csvLines.getFirst();

        StringBuilder sb = new StringBuilder();
        sb.append(MD_SEPARATOR);

        for(int i = 0; i < header.length(); ++i){
            char c = header.charAt(i);

            // Ignore CSV separators inside quotes
            if(c == '"') inQuotes = !inQuotes;
            if(c == csvSeparator && !inQuotes){
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

    /**
     * Build the line that separates the metadata line from the body.
     * @param nbColumns Number of columns to consider when building the separator.
     */
    private void buildMdHeaderSeparator(int nbColumns){
        String headerSeparator = "|---".repeat(nbColumns) + "|" + END_OF_LINE;
        mdLines.add(headerSeparator);
    }

    /**
     * Builds the body of the table.
     * @param nbColumns The number of columns to consider in the table.
     */
    private void buildMdBody(int nbColumns){
        boolean inQuotes = false;

        for(int i = 1; i < csvLines.size(); ++i) {
            String line = csvLines.get(i);

            StringBuilder sb = new StringBuilder();
            sb.append(MD_SEPARATOR);

            int currentColumn = 1;

            for (int j = 0; j < line.length(); ++j) {
                char c = line.charAt(j);

                // Ignore CSV separators inside quotes
                if(c == '"'){
                    inQuotes = !inQuotes;
                }

                if (c == csvSeparator && !inQuotes) {
                    ++currentColumn;
                    // Avoid adding more data to the table if there are more columns in the current line
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
