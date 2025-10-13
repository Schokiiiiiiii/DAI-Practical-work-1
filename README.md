# DAI-Practical-work-1

## Table of contents

- [Authors](#authors)
- [Overview](#overview)
- [Features](#features)
- [Download the project](#download-the-project)
- [Usage](#usage)
    - [csvsort](#csvsort)
    - [csvtomd](#csvtomd)
- [Contribute](#contribute)

## Authors

- [Fabien Léger](https://github.com/Schokiiiiiiii)
- [Samuel Dos Santos](https://github.com/Samurai-05)

## Overview

This is the repository for the first practical work in the DAI course. The goal of the project is to create a small CSV utilitary for working with CSV files. It allows users to sort CSV data and convert CSV files into Markdown tables.

## Features

- Sort CSV data by columns.
- Convert CSV files to Markdown tables.

## Download the project

1. Clone the repo locally
    - `git clone https://github.com/Schokiiiiiiii/DAI-Practical-work-1.git`
2. In IntelliJ IDEA, create a new project from existing sources and selecte your local repository.
    - Select `Import project from external model then select Maven` 

## Usage

### Compile the application using the maven wrapper

#### IntelliJ IDEA

On the top right corner, run the `Package Application as .jar file` configuration.
This will package the application into a single `.jar` file inside of the `target/` forlder.

In case of an error, try running the `mvn install` command.

#### Compile with the terminal

Run the following command : `./mvnw dependency:go-offline clean compile package`.

### Run the command

*Example files are provided inside the `data/` folder.*

**Example**
```
Name,Age
Alice,30
Bob,25
```

#### csvsort

Sort a CSV file : 

```bash
java -jar target/DAI-Practical-work-1-1.0-SNAPSHOT.jar -i=PATH_TO_CSV_FILE csvsort -c=COLUMN_TO_SORT 
```

Specify an output file :

```bash
java -jar target/DAI-Practical-work-1-1.0-SNAPSHOT.jar -i=PATH_TO_CSV_FILE csvsort -c=COLUMN_TO_SORT  o=PATH_TO_OUTPUT_FILE.csv
```

Specify a CSV separator ('`,`', '`;`', '`\t`', '`|`', ...) :

```bash
java -jar target/DAI-Practical-work-1-1.0-SNAPSHOT.jar -i=PATH_TO_CSV_FILE csvsort -c=COLUMN_TO_SORT  -s='SEPARATOR'
```

**Example output for column Age**

```
Name,Age
Bob,25
Alice,30
```

#### csvtomd

Simply convert a CSV file to a Markdown table :

```bash
java -jar target/DAI-Practical-work-1-1.0-SNAPSHOT.jar -i=PATH_TO_CSV_FILE csvtomd
```

Specify an output file : 

```bash
java -jar target/DAI-Practical-work-1-1.0-SNAPSHOT.jar -i=PATH_TO_CSV_FILE.csv csvtomd -o=PATH_TO_OUTPUT_FILE.md
```

Specify a CSV separator (',', ';', '\t', '|', ...) :

```bash
java -jar target/DAI-Practical-work-1-1.0-SNAPSHOT.jar -i=PATH_TO_CSV_FILE -s='SEPARATOR'
```

**Output Markdown**

```
| Name   | Age |
|--------|-----|
| Patrik | 30  |
| Alicia | 25  |
```

## Contribute

We only merge signed commits. [GitHub signed commits tutorial](https://docs.github.com/en/authentication/managing-commit-signature-verification/signing-commits)

(GitHub Copilot was used for layout suggestions)
