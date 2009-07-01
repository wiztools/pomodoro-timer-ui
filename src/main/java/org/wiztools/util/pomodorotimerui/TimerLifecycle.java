package org.wiztools.util.pomodorotimerui;

/**
 *
 * @author subhash
 */
public interface TimerLifecycle {
    public int getTimerMinute();
    public void start();
    public void progressInSecond(int second);
    public void end(TimerEnd type);
}
