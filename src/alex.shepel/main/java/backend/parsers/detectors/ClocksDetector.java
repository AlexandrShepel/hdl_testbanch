<<<<<<< Updated upstream:src/alex.shepel/main/java/backend/parsers/detectors/ClocksDetector.java
package alex.shepel.hdl_testbench.backend.parsers.detectors;
=======
package backend.parsers.detectors;
>>>>>>> Stashed changes:src/main/java/alex/shepel/hdl_testbench/backend/parser/detectors/ClocksDetector.java

import java.util.ArrayList;
import java.util.Set;

/*
 * File: ClocksDetector.java
 * -----------------------------------------------
 * Looks for clocks ports in the parsed module.
 */
public class ClocksDetector {

    /* List of found clocks ports. */
    private final ArrayList<String> clkNames = new ArrayList<>();

    /**
     * The class constructor.
     *
     * @param ports The Set object that contains
     *              all ports of parsed file.
     */
    public ClocksDetector(Set<String> ports) {
        createClocksList(ports);
    }

    /**
     * Creates an ArrayList object from a found clocks.
     *
     * @param ports The Set object that contains
     *              all ports of parsed file.
     */
    private void createClocksList(Set<String> ports) {
        for (String port: ports) {
            if (isClockPort(port)) {
                clkNames.add(port);
            }
        }
    }

    /**
     * Checks if specified port is a clock port
     * by finding a "clk" or "clock" key words
     * in the port's name.
     *
     * @param port The String value that contains
     *             a name of port.
     * @return The boolean "true" value, if ongoing port
     *         is a clock port. Else "false".
     */
    private boolean isClockPort(String port) {
        String[] clockKeyWords = {"clock", "clk"};
        boolean clkFound = false;

        for (String keyWord: clockKeyWords) {
            clkFound = port.toLowerCase().contains(keyWord);
        }

        return clkFound;
    }

    /**
     * Returns all clock ports that was found.
     *
     * @return The ArrayList object that
     *         contains names of found clock port.
     */
    public ArrayList<String> getClocks() {
        return clkNames;
    }

}