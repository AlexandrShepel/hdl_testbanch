package alex.shepel.hdl_testbench.backend.parser.detectors;

import java.util.ArrayList;
import java.util.HashMap;

/*
 * File: PortsDetector.java
 * -----------------------------------------------
 * Looks for ports in the parsed module.
 */
public class PortsDetector {

    /* List of found ports (inputs and outputs). */
    private final HashMap<String, PortDescriptor> inputs = new HashMap<>();
    private final HashMap<String, PortDescriptor> outputs = new HashMap<>();

    /**
     * The class constructor.
     *
     * @param parsedFile The ArrayList object
     *                   that contains all code lines
     *                   of parsed file.
     */
    public PortsDetector(ArrayList<String> parsedFile) {
        createPortsHashMap(parsedFile);
    }

    /**
     * Creates a HashMap from a found ports descriptions.
     *
     * @param parsedFile The ArrayList object
     *                   that contains all code lines
     *                   of parsed file.
     */
    private void createPortsHashMap(ArrayList<String> parsedFile) {
        for (String codeLine: parsedFile) {
            if (isInputDeclaration(codeLine)) {
                codeLine = codeLine.replace("input", "");
                inputs.putAll(getPorts(codeLine));
            }

            else if (isOutputDeclaration(codeLine)) {
                codeLine = codeLine.replace("output", "");
                outputs.putAll(getPorts(codeLine));
            }
        }
    }

    /**
     * Checks if ongoing line of code contains input declaration.
     *
     * @param codeLine The String value that contains
     *                 a separate line of code.
     * @return The boolean "true" value, if ongoing code line
     *         contains an input declaration. Else "false".
     */
    private boolean isInputDeclaration(String codeLine) {
        boolean isInputFound = codeLine.contains("input");
        boolean isTypeFound = codeLine.contains("logic") || codeLine.contains("wire") || codeLine.contains("reg");
        return isInputFound && isTypeFound;
    }

    /**
     * Checks if ongoing line of code contains output declaration.
     *
     * @param codeLine The String value that contains
     *                 a separate line of code.
     * @return The boolean "true" value, if ongoing code line
     *         contains an output declaration. Else "false".
     */
    private boolean isOutputDeclaration(String codeLine) {
        boolean isOutputFound = codeLine.contains("output");
        boolean isTypeFound = codeLine.contains("logic") || codeLine.contains("wire") || codeLine.contains("reg");
        return isOutputFound && isTypeFound;
    }

    /**
     * Returns all ports that are written in the ongoing line of code.
     *
     * @param codeLine The String value that contains
     *                 a separate line of code.
     * @return The HashMap object.
     *         Key contains a name of a found port.
     *         Value stores a full information about port
     *         as PortDescriptor object.
     */
    private HashMap<String, PortDescriptor> getPorts(String codeLine) {
        PortDescriptor descriptor = new PortDescriptor();
        HashMap<String, PortDescriptor> ports = new HashMap<>();

        codeLine = deleteSpacings(codeLine);

        descriptor.setType(getPortType(codeLine));
        codeLine = codeLine.replace(descriptor.getType(), "");

        descriptor.setSigned(getSigned(codeLine));
        codeLine = codeLine.replace(descriptor.getSigned(), "");

        descriptor.setPackedSize(getPackedSize(codeLine));
        codeLine = codeLine.replace(descriptor.getPackedSize(), "");

        descriptor.setUnpackedSize(getUnpackedSize(codeLine));
        codeLine = codeLine.replace(descriptor.getUnpackedSize(), "");

        for (String name: codeLine.split(",")) {
            PortDescriptor finalDescriptor = new PortDescriptor();
            finalDescriptor.copyDescription(descriptor);
            finalDescriptor.setName(name);
            ports.put(finalDescriptor.getName(), finalDescriptor);
        }

        return ports;
    }

    /**
     * Deletes all spacings from a specified line of code
     * for further processing.
     *
     * @param codeLine The String value that contains
     *                 a separate line of code.
     * @return The modified line of code without spacings.
     */
    private String deleteSpacings(String codeLine) {
        String[] spacingSymbols = {" ", "\t", "\n"};

        for (String symbol: spacingSymbols) {
            while (codeLine.contains(symbol)) {
                codeLine = codeLine.replace(symbol, "");
            }
        }

        return codeLine;
    }

    /**
     * Returns a type of a specified port.
     *
     * @param codeLine The String value that contains
     *                 a separate line of code.
     * @return The string value of a port's type.
     *         If port does not have any type,
     *         returns a "logic" type that is used by default.
     */
    private String getPortType(String codeLine) {
        String[] portTypes = {"logic", "wire", "reg"};

        for (String portType: portTypes) {
            if (codeLine.contains(portType)) {
                return portType;
            }
        }

        return "logic";
    }

    /**
     * Returns a "signed" word if specified port is signed.
     *
     * @param codeLine The String value that contains
     *                 a separate line of code.
     * @return "signed" word if specified port is signed.
     *         If port is not signed, returns empty string.
     */
    private String getSigned(String codeLine) {
        if (codeLine.contains("signed")) {
            return "signed";
        }

        return "";
    }

    /**
     * Returns a packed size of a specified port.
     *
     * @param codeLine The String value that contains
     *                 a separate line of code.
     * @return The string value of packed size of a port.
     *         If port is not packed, returns empty string.
     */
    private String getPackedSize(String codeLine) {
        if (codeLine.indexOf('[') == 0) {
            return codeLine.substring(codeLine.indexOf('['), codeLine.indexOf(']') + 1);
        }

        return "";
    }

    /**
     * Returns an unpacked size of a specified port.
     *
     * @param codeLine The String value that contains
     *                 a separate line of code.
     * @return The string value of unpacked size of a port.
     *         If port is not unpacked, returns empty string.
     */
    private String getUnpackedSize(String codeLine) {
        if ((codeLine.indexOf("]") == codeLine.length() - 1) || (codeLine.indexOf("]") == codeLine.length() - 2)) {
            return codeLine.substring(codeLine.indexOf('['), codeLine.indexOf(']') + 1);
        }

        return "";
    }

    /**
     * Returns all input ports that was found.
     *
     * @return The HashMap object.
     *         Key contains a name of a found port.
     *         Value stores a full information about port
     *         as PortDescriptor object.
     */
    public HashMap<String, PortDescriptor> getInputPorts() {
        return inputs;
    }

    /**
     * Returns all input ports that was found.
     *
     * @return The HashMap object.
     *         Key contains a name of a found port.
     *         Value stores a full information about port
     *         as PortDescriptor object.
     */
    public HashMap<String, PortDescriptor> getOutputPorts() {
        return outputs;
    }

}
