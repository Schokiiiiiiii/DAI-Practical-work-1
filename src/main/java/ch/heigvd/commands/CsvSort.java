package ch.heigvd.commands;

import ch.heigvd.Main;
import picocli.CommandLine;

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

    @Override
    public Integer call() {

        if (outputFilename == null) outputFilename = parent.getCSVFilename();

        System.out.println("Here is the column: " + columnName);

        return 0;
    }
}
