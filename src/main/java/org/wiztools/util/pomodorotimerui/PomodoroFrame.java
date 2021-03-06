package org.wiztools.util.pomodorotimerui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.*;
import org.simplericity.macify.eawt.Application;
import org.simplericity.macify.eawt.DefaultApplication;

/**
 *
 * @author subhash
 */
public class PomodoroFrame extends JFrame {

    private static final String TIMER_DEFAULT_TEXT = "00:00";

    private JButton jb_start = new JButton("Start");
    private JButton jb_stop = new JButton("Stop");

    private JTextField jtf_time_minutes = new JTextField(5);
    private JLabel jl_time_counter = new JLabel(TIMER_DEFAULT_TEXT, JLabel.CENTER);

    private TimeoutDialog jd_timeoutDialog;

    private TimerLifecycle lifecycle;
    private Timer timer;

    private final JFrame me = this;
    
    // Macify:
    private final Application application = new DefaultApplication();

    private void init(){
        // Configure Esc action
        KeyStroke escapeKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, false);
        Action escapeAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stopTimer();
            }
        };
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(escapeKeyStroke, "ESCAPE");
        getRootPane().getActionMap().put("ESCAPE", escapeAction);
        
        // Timeout dialog
        jd_timeoutDialog = new TimeoutDialog(me, "Stop Working :-)");

        jb_start.setMnemonic('s');
        getRootPane().setDefaultButton(jb_start);
        jb_stop.setEnabled(false);

        Font timerFont = new Font(Font.SANS_SERIF, Font.BOLD, 26);
        jl_time_counter.setFont(timerFont);

        jb_start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String minStr = jtf_time_minutes.getText();
                int minutes = -1;

                // Validation
                String errorMessage = null;
                try{
                    minutes = Integer.parseInt(minStr);
                    if(minutes < 1){
                        errorMessage = "Value cannot be less than 1.";
                    }
                    else if(minutes > (24*60)){ // Cannot set value above 24hrs
                        errorMessage ="Value cannot be more than 24 Hrs.";
                    }
                }
                catch(NumberFormatException ex){
                    errorMessage = "Value is not a number.";
                }

                if(errorMessage != null){
                    JOptionPane.showMessageDialog(me, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
                    jtf_time_minutes.requestFocus();
                    return;
                }

                lifecycle = new TimerLifecycleImpl(minutes);
                timer = new Timer(1000, new TimerAction(lifecycle));
                timer.start();
            }
        });

        jb_stop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stopTimer();
            }
        });
        
        // Macify:
        application.addAboutMenuItem();
    }
    
    private void stopTimer() {
        if(timer != null && timer.isRunning()) {
            timer.stop();
            lifecycle.end(TimerEnd.CANCELLED);
        }
    }

    class TimerLifecycleImpl implements TimerLifecycle{
        final int minutes;

        public TimerLifecycleImpl(int minutes) {
            this.minutes = minutes;
        }

        @Override
        public int getTimerMinute() {
            return minutes;
        }

        @Override
        public void start() {
            jb_start.setEnabled(false);
            jb_stop.setEnabled(true);
            jtf_time_minutes.setEnabled(false);
            jl_time_counter.setText(TimeUtil.getSecondsFormatted(minutes*60));
        }

        @Override
        public void progressInSecond(final int second) {
            jl_time_counter.setText(TimeUtil.getSecondsFormatted(second));
        }

        @Override
        public void end(final TimerEnd type) {
            jl_time_counter.setText(TIMER_DEFAULT_TEXT);
            jb_start.setEnabled(true);
            jb_stop.setEnabled(false);
            jtf_time_minutes.setEnabled(true);
            if(type == TimerEnd.TIMEOUT){
                timer.stop();
                jd_timeoutDialog.showMe();
            }
            jtf_time_minutes.requestFocus();
        }
    }

    public PomodoroFrame(final String title){
        super(title);

        init();

        Container c = this.getContentPane();
        c.setLayout(new BorderLayout());

        // North:
        JPanel jp_north = new JPanel();
        {
            jp_north.setLayout(new FlowLayout(FlowLayout.LEFT));
            JLabel jl = new JLabel("Timer (minutes): ");
            jl.setLabelFor(jtf_time_minutes);
            jl.setDisplayedMnemonic('t');
            jp_north.add(jl);
            jp_north.add(jtf_time_minutes);
        }
        c.add(jp_north, BorderLayout.NORTH);

        // Center:
        c.add(jl_time_counter, BorderLayout.CENTER);

        // South:
        JPanel jp_south = new JPanel();
        {
            jp_south.setLayout(new FlowLayout(FlowLayout.CENTER));
            jp_south.add(jb_start);
            jp_south.add(jb_stop);
        }
        c.add(jp_south, BorderLayout.SOUTH);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }
}
