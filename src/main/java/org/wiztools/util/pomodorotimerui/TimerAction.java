package org.wiztools.util.pomodorotimerui;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

/**
 *
 * @author subhash
 */
public class TimerAction extends AbstractAction {
    private final TimerLifecycle lifecycle;
    private int currentSecond;

    public TimerAction(TimerLifecycle lifecycle){
        this.lifecycle = lifecycle;
        this.currentSecond = lifecycle.getTimerMinute() * 60;
        lifecycle.start();
    }

    public void actionPerformed(ActionEvent e){
        if(currentSecond < 0){
            return;
        }
        lifecycle.progressInSecond(--currentSecond);
        if(currentSecond == 0){
            currentSecond--;
            lifecycle.end(TimerEnd.TIMEOUT);
        }
    }
}
