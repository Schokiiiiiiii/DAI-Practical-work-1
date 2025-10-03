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

    @Override
    public Integer call() {
        importCSV(parent.getCSVFilename());
        convertCSVtoMD();
        exportMD(/*parent.getOutputFilename*/"/home/samuel/DAI/DAI-Practical-work-1/data/dataToMd.md");
        return 0;
    }

    private static final String END_OF_LINE = "\n";
    private final ArrayList<String> csvLines = new ArrayList<>();
    private final ArrayList<String> mdLines = new ArrayList<>();

    private void importCSV(String filename){

        try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filename), StandardCharsets.UTF_8))){
            String line;
            while ((line = br.readLine()) != null) {
                csvLines.add(line + END_OF_LINE);
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
        buildMdHeader();
        int nbColumns = csvLines.getFirst().split(",").length;
        buildMdHeaderSeparator(nbColumns);
        buildMdBody();
    }

    private void buildMdHeader(){
        String header = csvLines.getFirst();
        for(int i = 0; i < header.length(); ++i){
            if(header.charAt(i) == ','){
                header = header.substring(0, i) + " | " + header.substring(i+1);
            }
        }
        mdLines.add(header);
    }

    private void buildMdHeaderSeparator(int nbColumns){
        String headerSeparator = "|---".repeat(nbColumns) + "|" + END_OF_LINE;
        mdLines.add(headerSeparator);
    }

    private void buildMdBody(){
        for(int i = 1; i < csvLines.size(); ++i) {
            String line = csvLines.get(i);
            for (int j = 0; j < line.length(); ++j) {
                if (line.charAt(j) == ',') {
                    line = line.substring(0, j) + " | " + line.substring(j + 1);
                }
            }
            mdLines.add(line);
        }
    }
}
