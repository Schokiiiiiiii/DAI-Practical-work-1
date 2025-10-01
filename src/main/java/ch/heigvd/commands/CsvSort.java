package ch.heigvd.commands;

import ch.heigvd.Main;
import picocli.CommandLine;

import java.util.concurrent.Callable;

@CommandLine.Command(name = "CsvSort", description = "Sorts a CSV file based on a column")
public class CsvSort implements Callable<Integer> {

    @CommandLine.ParentCommand protected Main parent;

    @CommandLine.Option(
            names = {"-c", "--column"},
            description = "The header of the column to sort.",
            defaultValue = "column1")
    protected int column;

    @Override
    public Integer call() {
        System.out.println("Here is the column: " + column);
        return 0;
    }
}
