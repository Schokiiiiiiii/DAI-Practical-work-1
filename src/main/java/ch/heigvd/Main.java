/*********************************************************************************************************************
 * @filename    : Main.java
 * @authors     : Fabien Léger and Samuel Dos Santos
 * @version     : 0.1.0
 * @updated on  : 24.09.2025
 * @description : offers a CLI to interact with a CSV file and transform it.
 *********************************************************************************************************************/

package ch.heigvd;

import ch.heigvd.commands.CsvSort;
import ch.heigvd.commands.CsvToMd;
import picocli.CommandLine;

import java.io.File;

@CommandLine.Command(
        description = "A small program to modify and transform CSV files.",
        version = "0.1.0",
        showDefaultValues = true,
        subcommands = {
                CsvSort.class,
                CsvToMd.class
        },
        scope = CommandLine.ScopeType.INHERIT,
        mixinStandardHelpOptions = true)
public class Main {

    /**
     * main that starts the right command following arguments given
     * @param args parameters for picocli
     */
    public static void main(String[] args) {

        // get filename
        String jarFilename =
                new File(Main.class.getProtectionDomain().getCodeSource().getLocation().getPath())
                        .getName();

        // do the command
        int exitCode = new CommandLine(new Main()).setCommandName(jarFilename).execute(args);

        // exit the program with exit code from command
        System.exit(exitCode);
    }
}