package ch.heigvd.commands;

import ch.heigvd.Main;
import picocli.CommandLine;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.concurrent.Callable;

@CommandLine.Command(name = "csvsort", description = "Sorts a CSV file based on a column")
public class CsvSort implements Callable<Integer> {

    private char SEPARATOR = ',';

    @CommandLine.ParentCommand protected Main parent;

    @CommandLine.Option(
            names = {"-c", "--column"},
            description = "The header of the column to sort.",
            required = true)
    protected String columnName;

    @CommandLine.Option(
            names = {"-o", "--output"},
            description = "The output filename. By default, it will be the input filename.")
    protected String outputFilename;

    /**
     * sorts a csv file based on a column
     * @return 0 if it went well, 1 otherwise
     */
    @Override
    public Integer call() {

        if (outputFilename == null) outputFilename = parent.getCSVFilename();

        int idx = findColumnIndex();
        if (idx == -1) {
            System.err.println("Error: Column not found!\n");
            return 1;
        }

        ArrayList<ArrayList<String>> data = new ArrayList<>();

        retrieveData(data);
        for (ArrayList<String> datum : data) {
            for (String s : datum) {
                System.out.print(s);
            }
            System.out.println();
        }

        System.out.println("Here is the column : " + columnName);
        System.out.println("Here is the index  : " + idx);

        return 0;
    }

    /**
     * finds the index for a given string column
     * @return  index found or -1 if not found
     */
    // Yes, we could first turn the CSV into a 2D array and then look for column index, but it's both better to train
    // IOs that way and also eliminates huge file storage when column name is wrong
    private int findColumnIndex() {

        try (Reader reader = new FileReader(parent.getCSVFilename());
             BufferedReader br = new BufferedReader(reader)) {

            int idx = 0;

            String line = br.readLine();

            int sep;
            while ((sep = line.indexOf(SEPARATOR)) != -1) {

                String word = line.substring(0, sep);
                if (word.equals(columnName))
                    return idx;
                ++idx;
                line = line.substring(sep + 1);
            }


            if (line.equals(columnName)) return idx; // check last word

        } catch (IOException e) {
            System.out.println("Error with the csv input file: " + e.getMessage());
        }

        return 0;
    }

    private void retrieveData(ArrayList<ArrayList<String>> data) {

        try (Reader reader = new FileReader(parent.getCSVFilename());
             BufferedReader br = new BufferedReader(reader)) {

            String line;
            while ((line = br.readLine()) != null) {
                data.add(new ArrayList<>());

                int sep;
                while ((sep = line.indexOf(SEPARATOR)) != -1) {

                    String word = line.substring(0, sep);
                    data.getLast().add(word);
                    line = line.substring(sep + 1);
                }
                data.getLast().add(line); // add last word
            }

        } catch (IOException e) {
            System.out.println("Error with the csv input file: " + e.getMessage());
        }
    }
}
