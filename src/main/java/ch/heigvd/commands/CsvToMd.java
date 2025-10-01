package ch.heigvd.commands;

import com.sun.tools.javac.Main;
import picocli.CommandLine;
import picocli.CommandLine.Command;

import java.util.concurrent.Callable;

@Command(name = "foo", description = "Convert a CSV file to a MARKDOWN table")
public class CsvToMd implements Callable<Integer> {
    @CommandLine.ParentCommand protected Main parent;

    @Override
    public Integer call() throws Exception{
        System.out.println("Hello World!");
        return 0;
    }


    private void convertCSVtoMD(){

    }
}
