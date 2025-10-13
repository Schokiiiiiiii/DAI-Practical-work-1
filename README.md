# DAI-Practical-work-1

Authors : Fabien Léger and Samuel Dos Santos <br>
Idea : Utilitary for CSV formatted data files <br>
Repository : https://github.com/Schokiiiiiiii/DAI-Practical-work-1 

## Context

This is the repository for the first practical work in the DAI course. The goal of the project is to create a small CSV utilitary tool that can sort the data inside a CSV file and convert CSV files to Markdown tables. The project uses [picocli](https://picocli.info/) to manage the different command line options.

## Download the project

1. Clone the repo locally
    - `git clone https://github.com/Schokiiiiiiii/DAI-Practical-work-1.git`
2. In IntelliJ IDEA, create a new project from existing sources and selecte your local repository.
    - Select `Import project from external model then select Maven` 

## Use

### Compile the application using the maven wrapper

#### IntelliJ IDEA

On the top right corner, run the `Package Application as .jar file` configuration.
This will package the application into a single .jar file inside of the `target/` forlder.

In case of an error, try running the `mvn install` command.

#### Compile with the terminal

Run the following command : `./mvnw dependency:go-offline clean compile package`.

### Run the command

#### csvsort


#### csvtomd

Simply convert a CSV file to a Markdown table :

```
java -jar target/DAI-Practical-work-1-1.0-SNAPSHOT.jar -i=PATH_TO_CSV_FILE csvtomd
```

Specify an output file name : 

```
java -jar target/DAI-Practical-work-1-1.0-SNAPSHOT.jar -i=PATH_TO_CSV_FILE.csv csvtomd -o=PATH_TO_OUTPUT_FILE.md
```

Specify a CSV separator (',', ';', '\t', '|', ...) :

```
java -jar target/DAI-Practical-work-1-1.0-SNAPSHOT.jar -i=PATH_TO_CSV_FILE -s='SEPARATOR'
```

## Contribute to the project

We only merge signed commits. [GitHub signed commits tutorial](https://docs.github.com/en/authentication/managing-commit-signature-verification/signing-commits)
