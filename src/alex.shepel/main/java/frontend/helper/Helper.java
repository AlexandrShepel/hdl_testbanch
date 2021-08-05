package frontend.helper;

import javax.swing.*;

/*
 * File: Helper.java
 * -------------------------------------
 * Shows error messages on the app's window.
 * Gets error information from occurring Exception object.
 */
public class Helper extends JOptionPane {

    /**
     * The class constructor.
     */
    public static void showExceptionMessage(Exception exception) {
        showMessageDialog(null, exception.getMessage(), "Helper", JOptionPane.WARNING_MESSAGE);
    }

}
