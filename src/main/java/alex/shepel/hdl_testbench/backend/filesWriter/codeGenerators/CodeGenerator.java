package alex.shepel.hdl_testbench.backend.filesWriter.codeGenerators;

import alex.shepel.hdl_testbench.backend.parser.Parser;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/*
 * File: CodeGenerator.java
 * -----------------------------------------------
 * Parses specified file. Uses Parser object.
 * Saves it as list of code lines.
 * Overwrites existing code lines if needed.
 */
public class CodeGenerator extends ArrayList<String> {

    /* Properties of a file that can be overwritten. */
    private HashMap<String, String> parameters;
    private String fileName;

    /**
     * -- Parses specified file.
     * -- Saves result as list of lines of code.
     *
     * @param filePath The path to file that must be parsed.
     * @throws IOException Error when reading a specified file.
     */
    public void parseFile(String filePath) throws IOException {
        ArrayList<String> pathElements = new ArrayList<>(Arrays.asList(filePath.split("/")));
        fileName = pathElements.get(pathElements.size() - 1);
        Parser parser = new Parser(filePath, Parser.INTERNAL_RESOURCE);
        addAll(parser.fileToArrayList());
    }

    /**
     * Adds today's date to the module description.
     */
    public void setDate() {
        for (int index = 0; index < size(); index++) {
            if (get(index).contains("Start design")) {
                /* gets ongoing date and formats it */
                String day = LocalDate.now().getDayOfMonth() + "";
                String month = LocalDate.now().getMonthValue()+ "";
                String year = LocalDate.now().getYear() + "";
                String data = day + "." + month + "." + year;

                /* replaces date in the parsed file */
                int dateSubstringIndex = get(index).indexOf("00.00.0000");
                String editedLine =
                        get(index).substring(0, dateSubstringIndex) +
                                data +
                                get(index).substring(dateSubstringIndex + "00.00.0000".length());
                set(index, editedLine);

                break;
            }
        }
    }

    /**
     * Sets DUT's parameters to the test environment files.
     *
     * @param parameters The parameters of the parsed DUT file.
     */
    public void setParameters(HashMap<String, String> parameters) {
        this.parameters = parameters;

        /* Sets parameters in the parameters declaration field. */
        for (int index = 0; index < size(); index++) {
            if (get(index).contains("localparam PARAMETER")) {
                remove(index);

                for (String name : parameters.keySet()) {
                    add(index, "\tlocalparam " + name + " = " + parameters.get(name) + ";");
                }

                break;
            }
        }

        /* Sets parameters for the connected to the DUT modules. */
        for (int index = 0; index < size(); index++) {
            if (get(index).contains(".PARAMETER")) {
                remove(index);

                for (String name: parameters.keySet()) {
                    add(index, "\t\t." + name + " (" + name + "),");
                }

                int lastParameterIndex = index + parameters.keySet().size() - 1;
                String lastParameterLine = get(lastParameterIndex);
                set(lastParameterIndex, lastParameterLine.substring(0, lastParameterLine.indexOf(",")));

                break;
            }
        }
    }

    /**
     * Returns parameters that was found in the parsed file.
     *
     * @return The HashMap object that contains parameters names
     *         and their values.
     */
    public HashMap<String, String> getParameters() {
        return parameters;
    }

    /**
     * Returns a parsed file as ArrayList object
     * that contains all code lines of the file.
     *
     * @return The ArrayList object
     *         that contains parsed code.
     */
    public ArrayList<String> getParsedFile() {
        return this;
    }

    /**
     * Returns a name of the parsed file.
     *
     * @return The String value of parsed file name.
     */
    public String getName() {
        return fileName;
    }

    /**
     * Prints parsed file to the console.
     * Used for debugging.
     */
    public void printFile() {
        for (String line: this) {
            System.out.println(line);
        }
    }

    /**
     * Gives packed or unpacked size of port in a format
     * of code (such as "[SOME_PARAMETER - 1 : 0]")
     * and translates it to the number format
     * (just a "42", for example).
     *
     * @param codedSize The unpacked size of port in a format of code.
     * @return The unpacked size of port in a number format.
     */
    public String decodeSizeReferencing(String codedSize) {
        /* When size is described by parameter's name. */
        for (String name: getParameters().keySet()) {
            if (codedSize.contains(name)) {
                return name + " - 1";
            }
        }

        /* When size is described by a number. */
        int begIndex = codedSize.indexOf('[') + 1;
        int endIndex = codedSize.indexOf(':');

        return codedSize.substring(begIndex, endIndex);
    }

    /**
     * Gives packed or unpacked size of port in a format
     * of code (such as "[SOME_PARAMETER - 1 : 0]").
     * This method is different from decodeSizeDeclaration() method,
     * because result will be more per unit.
     * (result will be "43" when decodeSizeDeclaration() method
     * will return "42").
     *
     * See context of using this methods to understand why it needed.
     * Also see resulting files. There will be difference between
     * Generators objects declarations and sizes of ports that
     * corresponds to that Generators. This difference equals 1.
     *
     * @param codedSize The unpacked size of port in a format of code.
     * @return The unpacked size of port in a number format.
     */
    public String decodeSizeDeclaration(String codedSize) {
        /* Port's width is 1 bit. */
        if (codedSize.equals(""))
            return "1";

            /* Port's width larger then 1 bit. */
        else {
            /* When size is described by parameter's name. */
            for (String name : getParameters().keySet()) {
                if (codedSize.contains(name)) {
                    return name;
                }
            }

            /* When size is described by a number. */
            int begIndex = codedSize.indexOf('[') + 1;
            int endIndex = codedSize.indexOf(':');
            int elderBit = Integer.parseInt(codedSize.substring(begIndex, endIndex));

            return String.valueOf(elderBit + 1);
        }
    }
}
