package org.wiztools.util.pomodorotimerui;

import javax.swing.SwingUtilities;

/**
 *
 * @author subhash
 */
public class Main {

    private static final String TITLE = "Timer";

    public static void main(String[] arg){
        System.out.println("Starting WizTools.org Pomodoro Timer...");
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new PomodoroFrame(TITLE);
            }
        });
    }
}
