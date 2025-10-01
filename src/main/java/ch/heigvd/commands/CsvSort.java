package ch.heigvd.commands;

import ch.heigvd.Main;
import picocli.CommandLine;

import java.util.concurrent.Callable;

@CommandLine.Command(name = "csvsort", description = "Sorts a CSV file based on a column")
public class CsvSort implements Callable<Integer> {

    @CommandLine.ParentCommand protected Main parent;

    @CommandLine.Option(
            names = {"-c", "--column"},
            description = "The header of the column to sort.")
    protected String columnName;

    @Override
    public Integer call() {
        System.out.println("Here is the column: " + columnName);
        return 0;
    }
}
