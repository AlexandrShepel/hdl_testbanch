<<<<<<< Updated upstream:src/alex.shepel/main/java/backend/parsers/detectors/ParametersDetector.java
package alex.shepel.hdl_testbench.backend.parsers.detectors;
=======
package backend.parsers.detectors;
>>>>>>> Stashed changes:src/main/java/alex/shepel/hdl_testbench/backend/parser/detectors/ParametersDetector.java

import java.util.ArrayList;
import java.util.HashMap;

/*
 * File: ParametersDetector.java
 * -----------------------------------------------
 * Looks for parameters in the parsed module.
 */
public class ParametersDetector {

    /* List of found parameters. */
    private final HashMap<String, String> parameters = new HashMap<>();

    /**
     * The class constructor.
     *
     * @param parsedFile The ArrayList object
     *                   that contains all code lines
     *                   of parsed file.
     */
    public ParametersDetector(ArrayList<String> parsedFile) {
        createParametersHashMap(parsedFile);
    }

    /**
     * Creates a HashMap from a found parameters descriptions.
     *
     * @param parsedFile The ArrayList object
     *                   that contains all code lines
     *                   of parsed file.
     */
    private void createParametersHashMap(ArrayList<String> parsedFile) {
        boolean isParametersDeclaration = false;

        for (String codeLine : parsedFile) {
            /* Looks for beginning of parameters declaration.
            Parameters declaration begins with "#(" or "parameter" strings. */
            if (codeLine.contains("#(") || codeLine.contains("parameter"))
                isParametersDeclaration = true;

            /* Parse parameters and looks for ending of parameters declaration.
            Parameters declaration must contain "=". Declaration block ends with symbol ")". */
            if (isParametersDeclaration) {
                if (codeLine.contains(")"))
                    isParametersDeclaration = false;

                if (codeLine.contains("="))
                    getParameter(codeLine);
            }
        }
    }

    /**
     * Looks for a parameter that is written in the ongoing line of code.
     *
     * @param codeLine The String value that contains
     *                 a separate line of code.
     */
    private void getParameter(String codeLine) {
        codeLine = removeShellSymbols(codeLine);
        parameters.put(
                codeLine.substring(0, codeLine.indexOf("=")),
                codeLine.substring(codeLine.indexOf("=") + 1)
        );
    }

    /**
     * Removes all uninformative symbols from a specified line of code.
     *
     * @param codeLine The String value that contains
     *                 a separate line of code.
     * @return The modified line of code without uninformative symbols.
     */
    private String removeShellSymbols(String codeLine) {
        /* Symbols that must be removed from line. */
        String[] regularSymbols = {" ", "\t", "\n", ",", ";", ")"};
        String[] specificSymbols = {"#(", "parameter"};

        if (codeLine.contains("//"))
            codeLine = codeLine.substring(0, codeLine.indexOf("//"));

        /* Removes regular symbols. */
        for (String symbol: regularSymbols) {
            while (codeLine.contains(symbol)) {
                codeLine = codeLine.replace(symbol, "");
            }
        }

        /* Removes specific symbols. */
        for (String symbol: specificSymbols) {
            if (codeLine.contains(symbol)) {
                codeLine = codeLine.replace(
                    codeLine.substring(0, codeLine.indexOf(symbol) + symbol.length()),
                    "");
            }
        }

        /* Returns resulting line. */
        return codeLine;
    }

    /**
     * Returns all parameters that was found.
     *
     * @return The HashMap object.
     *         Key contains a name of a found parameter.
     *         Map's value stores an parameter's value.
     */
    public HashMap<String, String> getParameters() {
        return parameters;
    }

}
