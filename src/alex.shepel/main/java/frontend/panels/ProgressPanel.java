package frontend.panels;

import frontend.FrontendParameters;

import javax.swing.*;
import java.awt.*;

/*
 * File: ProgressPanel.java
 * -------------------------------------
 * Monitors and displays an ongoing step
 * of configuration setting.
 */
public class ProgressPanel extends JPanel implements FrontendParameters {

    /* The ongoing configuration step. */
    private int step = 0;

    /**
     * The class constructor.
     */
    public ProgressPanel() {
        setPreferredSize(new Dimension(APP_WIDTH, PP_HEIGHT));
    }

    /**
     * -- Sets a panel background.
     * -- Paints all graphical components on the panel.
     *
     * @param g The Graphics object that contains all
     *          graphical components of this panel.
     */
    @Override
    public void paintComponent(Graphics g) {
        setBackground(g);
        drawPanel(g);
    }

    /**
     * Draws all graphical components on the panel.
     *
     * @param g The Graphics object that contains all
     *          graphical components of this panel.
     */
    private void drawPanel(Graphics g) {
        double displayArea = getWidth() * 0.75;
        double displayCenter = getWidth() >> 1;
        double startDisplaying = displayCenter - displayArea / 2;
        double spacing = (displayArea - PROGRESS_OVAL_SIZE * NUM_OF_STEPS) / (NUM_OF_STEPS - 1);

        drawLines(g, startDisplaying, spacing);
        drawOvals(g, startDisplaying, spacing);
    }

    /**
     * Draws connecting lines.
     *
     * @param g The Graphics object that contains all
     *          graphical components of this panel.
     * @param startDisplaying The x-coordinate of place
     *                        where we start drawing.
     * @param lineLength The length of each connecting line.
     */
    private void drawLines(Graphics g, double startDisplaying, double lineLength) {
        for (int i = 0; i < NUM_OF_STEPS - 1; i++) {
            setDrawColor(g, i);
            g.drawLine(
                    (int) (startDisplaying + (PROGRESS_OVAL_SIZE + lineLength) * i) + PROGRESS_OVAL_SIZE,
                    (int) (getHeight() - PROGRESS_OVAL_SIZE * 1.5),
                    (int) (startDisplaying + (PROGRESS_OVAL_SIZE + lineLength) * (i  + 1)),
                    (int) (getHeight() - PROGRESS_OVAL_SIZE * 1.5)
            );
        }
    }

    /**
     * Draws ovals that represents completed steps
     * of test environment configuration.
     *
     * @param g The Graphics object that contains all
     *          graphical components of this panel.
     * @param startDisplaying The x-coordinate of place
     *                        where we start drawing.
     * @param spacing The spacing between two ovals.
     */
    private void drawOvals(Graphics g, double startDisplaying, double spacing) {
        for (int i = 0; i < NUM_OF_STEPS; i++) {
            setDrawColor(g, i);
            g.fillOval(
                    (int) (startDisplaying + (PROGRESS_OVAL_SIZE + spacing) * i),
                    getHeight() - PROGRESS_OVAL_SIZE * 2,
                    PROGRESS_OVAL_SIZE,
                    PROGRESS_OVAL_SIZE
            );
        }
    }

    /**
     * Sets the drawing color.
     * It can be green ("READY" color)
     * or grey ("NOT_READY" color).
     *
     * @param g The Graphics object that contains all
     *          graphical components of this panel.
     * @param drawingElementNumber The number of ongoing drawing element.
     *                             It compares with number of completed steps
     *                             and is used to define which color to draw with.
     */
    private void setDrawColor(Graphics g, int drawingElementNumber) {
        if (drawingElementNumber < step)
            g.setColor(READY);
        else
            g.setColor(NOT_READY);
        if (step == NUM_OF_STEPS - 1)
            g.setColor(READY);
    }

    /**
     * Sets a panel background.
     *
     * @param g The Graphics object that contains all
     *          graphical components of this panel.
     */
    private void setBackground(Graphics g) {
        g.setColor(BACKGROUND_COLOR);
        g.fillRect(0, 0, getWidth(), getHeight());
    }

    /**
     * Sets next configuration step.
     * It also means that previous configuration step is done.
     */
    public void nextStep() {
        if (getStep() < NUM_OF_STEPS - 1) {
            step++;
            repaint();
        }
    }

    /**
     * Sets previous configuration step.
     * It also means that user want to change
     * configuration that was set on a previous step.
     */
    public void previousStep() {
        if (getStep() > 0) {
            step--;
            repaint();
        }
    }

    /**
     * Returns an ongoing configuration step.
     *
     * @return The ongoing configuration step.
     */
    public int getStep() {
        return step;
    }

}
