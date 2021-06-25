package alex.shepel.hdl_testbench.backend.filesWriter.codeGenerators;

import alex.shepel.hdl_testbench.backend.parsers.Parser;
import alex.shepel.hdl_testbench.backend.parsers.detectors.PortDescriptor;

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
public class Codegen extends ArrayList<String> {

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
        Parser parser = new Parser(filePath);
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
    @SuppressWarnings("SuspiciousListRemoveInLoop")
    public void setParameters(HashMap<String, String> parameters) {
        this.parameters = parameters;

        for (int index = 0; index < size(); index++) {
            final boolean isLocalParam = get(index).contains("localparam PARAMETER");
            final boolean isGlobalParam = get(index).contains("parameter PARAMETER");
            final boolean isInstIfaceParam = get(index).contains(".PARAMETER (iface.PARAMETER)");
            final boolean isInstClassParam = get(index).contains(".PARAMETER (PARAMETER)");

            if (isLocalParam || isGlobalParam || isInstIfaceParam || isInstClassParam) {
                remove(index);

                /* Sets local parameters in the local parameters declaration field. */
                if (isLocalParam)
                    for (String name : parameters.keySet())
                        add(index, "\tlocalparam " + name + " = " + parameters.get(name) + ";");

                /* Sets global parameters in the global parameters declaration field -- block #(). */
                if (isGlobalParam)
                    for (String name : parameters.keySet())
                        add(index, "\tparameter " + name + ",");

                /* Sets classes parameters when parameters received through the interface. */
                if (isInstIfaceParam)
                    for (String name : parameters.keySet())
                        add(index, "\t\t." + name + " (iface." + name + "),");

                /* Sets classes parameters when parameters received through the #()-block parameters. */
                if (isInstClassParam)
                    for (String name : parameters.keySet())
                        add(index, "\t\t." + name + " (" + name + "),");

                /* Removes last redundant "," in the parameters declaration block. */
                if (isGlobalParam || isInstIfaceParam || isInstClassParam) {
                    final int lastParameterIndex = index + parameters.keySet().size() - 1;
                    set(lastParameterIndex, get(lastParameterIndex).replace(",", ""));
                }
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
     * Gives packed or unpacked size of port in a format
     * of code (such as "[SOME_PARAMETER - 1 : 0]")
     * and translates it to the number format
     * (just a "42", for example).
     *
     * @param codedSize The unpacked size of port in a format of code.
     * @return The unpacked size of port in a number format.
     */
    public String decodeSizeReferencing(String codedSize) {
        if (codedSize.length() == 0)
            return "0";

        /* When size is described by parameter's name. */
        for (String name: getParameters().keySet())
            if (codedSize.contains(name))
                return name + " - 1";

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
            for (String name : getParameters().keySet())
                if (codedSize.contains(name))
                    return name;

            /* When size is described by a number. */
            int begIndex = codedSize.indexOf('[') + 1;
            int endIndex = codedSize.indexOf(':');
            int elderBit = Integer.parseInt(codedSize.substring(begIndex, endIndex));

            return String.valueOf(elderBit + 1);
        }
    }

    protected void definePackingAddPort(final boolean isSinglePort, final int index,
                                      final HashMap<String, PortDescriptor> ports,
                                      final String[] packedMacro, final String[] unpackedMacro) {

        for (final String name : ports.keySet()) {
            if (!name.toLowerCase().contains("clk") && !name.toLowerCase().contains("clock")) {
                final String unpackedSize = decodeSizeReferencing(ports.get(name).getUnpackedSize());
                final boolean isUnpacked = !unpackedSize.equals("0");

                addPortReplaceName(index, name, unpackedSize,
                        (isUnpacked) ? unpackedMacro : packedMacro);

                if (isSinglePort) break;

                add(index, "\t\t// Port: " + ports.get(name).toString());
                add(index, "");
            }
        }
    }

    protected void addPortReplaceName(final int index, final String name, final String size, final String[] macro) {
        for (int i = macro.length - 1; i >= 0; i--)
            add(index, macro[i].replace("port_name", name).replace("PARAMETER - 1", size));
    }
}
