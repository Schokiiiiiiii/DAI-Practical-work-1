package ch.heigvd.commands;

import ch.heigvd.Main;
import picocli.CommandLine;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.concurrent.Callable;

@CommandLine.Command(name = "csvsort", description = "Sorts a CSV file based on a column")
public class CsvSort implements Callable<Integer> {

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

    private char separator;

    /**
     * sorts a csv file based on a column
     * @return 0 if it went well, 1 otherwise
     */
    @Override
    public Integer call() {
        separator = parent.getCSVSeparator();

        // if no output filename -> same as input filename
        if (outputFilename == null) outputFilename = parent.getCSVFilename();

        // search for column name's index
        int idx = findColumnIndex();
        if (idx == -1) {
            System.err.println("Error: Column not found!\n");
            return 1;
        }

        // initialize data 2d array and retrieve the csv data
        ArrayList<ArrayList<String>> data = new ArrayList<>();
        retrieveData(data);

        // sort data using custom comparator to chose index
        data.subList(1, data.size())
                .sort(Comparator.comparing(o -> o.get(idx)));

        // write data to output file
        writeToCSV(data);

        return 0;
    }

    /**
     * finds the index for a given string column
     * @return  index found or -1 if not found
     */
    // Yes, we could first turn the CSV into a 2D array and then look for column index, but it's both better to train
    // IOs that way and also eliminates huge file storage when column name is wrong
    private int findColumnIndex() {

        // read from file
        try (Reader reader = new FileReader(parent.getCSVFilename());
             BufferedReader br = new BufferedReader(reader)) {

            // initialize index to find
            int idx = 0;

            // read the header line
            String line = br.readLine();

            // loop over each separator
            int sep;
            while ((sep = line.indexOf(separator)) != -1) {

                // if there is a '"', fix the separator in case it was wrong
                if (line.charAt(0) == '"') {
                    sep = line.indexOf('"', 1);
                    ++sep;
                }

                // take the word and check if it's the right one
                String word = line.substring(0, sep);
                if (word.equals(columnName))
                    return idx;

                // start the line after the new separator
                line = line.substring(sep + 1);
                ++idx;
            }

            // check last word
            if (line.equals(columnName)) return idx;

        } catch (IOException e) {
            System.out.println("Error with the csv input file: " + e.getMessage());
        }

        return 0;
    }

    /**
     * puts the data inside a 2d ArrayList given in parameter
     * @param data ArrayList inside which to put the data
     */
    private void retrieveData(ArrayList<ArrayList<String>> data) {

        // read from file
        try (Reader reader = new FileReader(parent.getCSVFilename());
             BufferedReader br = new BufferedReader(reader)) {

            // loop over each line
            String line;
            while ((line = br.readLine()) != null) {

                // add a new empty line to our data
                data.add(new ArrayList<>());

                // loop over each separator
                int sep;
                while ((sep = line.indexOf(separator)) != -1) {

                    // if there is a '"', fix the separator in case it was wrong
                    if (line.charAt(0) == '"') {
                        sep = line.indexOf('"', 1);
                        ++sep;
                    }

                    // get the word and add it to our data
                    String word = line.substring(0, sep);
                    data.getLast().add(word);

                    // start the line after the separator
                    line = line.substring(sep + 1);
                }

                // add last word
                data.getLast().add(line);
            }

        } catch (IOException e) {
            System.out.println("Error with the csv input file: " + e.getMessage());
        }
    }

    /**
     * writes a 2d ArrayList inside a CSV file
     * @param data 2d ArrayList to write
     */
    private void writeToCSV(ArrayList<ArrayList<String>> data) {

        // open file to write
        try (Writer writer = new FileWriter(outputFilename);
             BufferedWriter bw = new BufferedWriter(writer)) {

            // write line by line, word by word
            for (ArrayList<String> datum : data) {
                for (int i = 0; i < datum.size(); i++) {
                    if (i != 0) bw.write(separator);
                    bw.write(datum.get(i));
                }
                bw.write('\n');
            }

            // flush what is left inside the buffer
            bw.flush();

        } catch (IOException e) {
            System.out.println("Error with the csv output file: " + e.getMessage());
        }
    }
}
