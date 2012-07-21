package org.wiztools.util.pomodorotimerui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.*;
import java.security.SecureRandom;
import java.util.Random;
import javax.swing.*;

/**
 *
 * @author subhash
 */
public class TimeoutDialog extends JDialog {

    private static final String[] smilies = new String[]{":-)", ":-(", ":-D", ":-O", ":-*", ":-/", ":P", "8-)", ":-[", ":'(", ":-X", ":-$", ":-!"};

    private static final int DIALOG_WIDTH = 400;
    private static final int DIALOG_HEIGHT = 300;

    private JLabel jl = new JLabel("Stop working :-)", JLabel.CENTER);
    private JButton jb = new JButton("Ok");

    private Timer timer;

    public TimeoutDialog(Frame owner, String title) {
        super(owner, title);
        setLocationRelativeTo(null);
        setSize(DIALOG_WIDTH, DIALOG_HEIGHT);
        setResizable(false);
        setModal(true);
        
        getRootPane().setDefaultButton(jb);

        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                closeDialog();
            }
            
        });
        
        // Configure Esc action
        KeyStroke escapeKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, false);
        Action escapeAction = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                closeDialog();
            }
        };
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(escapeKeyStroke, "ESCAPE");
        getRootPane().getActionMap().put("ESCAPE", escapeAction);

        Container c = getContentPane();
        c.setLayout(new BorderLayout());

        c.add(jl, BorderLayout.CENTER);

        JPanel jp_south = new JPanel();
        jp_south.setLayout(new FlowLayout(FlowLayout.CENTER));
        jb.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                closeDialog();
            }
        });
        jp_south.add(jb);
        c.add(jp_south, BorderLayout.SOUTH);
    }

    private void closeDialog(){
        if(timer != null && timer.isRunning()) {
            timer.stop();
        }
        setVisible(false);
    }

    public void showMe(){
        ActionListener l = new BeepAction();
        timer = new Timer(1000, l);
        timer.start();
        setVisible(true);
    }

    class BeepAction extends AbstractAction{
        @Override
        public void actionPerformed(ActionEvent e){
            Random r = new SecureRandom();
            Toolkit.getDefaultToolkit().beep();
            int i = r.nextInt(smilies.length);
            final String msg = "Stop working " + smilies[i];
            jl.setText(msg);
        }
    }

}
