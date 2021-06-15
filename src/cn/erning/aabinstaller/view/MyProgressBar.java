package cn.erning.aabinstaller.view;

import javax.swing.*;
import java.awt.*;

/**
 * @author erning
 * @date 2021-06-15 16:43
 * des:
 */
public class MyProgressBar {
    private final JDialog splash;

    public MyProgressBar(Frame owner, String title, String message){
        JOptionPane pane = new JOptionPane(message, JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, new String[0]);
        splash = pane.createDialog(owner, title);
        splash.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        Dimension dlgSize = splash.getSize();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        splash.pack();
        splash.setLocation((screenSize.width - dlgSize.width) / 2, (screenSize.height - dlgSize.height) / 2);
        splash.setResizable(false);
    }

    public void show(){
        try {
            int priority = Thread.currentThread().getPriority();
            synchronized (splash) {
                (new Thread(() -> {
                    synchronized (splash) {
                        splash.notifyAll();
                    }
                    Thread.currentThread().setPriority(1);
                    splash.setModal(true);
                    splash.setVisible(true);
                })).start();
                Thread.currentThread().setPriority(1);
                splash.wait();
            }
            if (splash.getGraphics() != null)
                splash.paintAll(splash.getGraphics());
            Thread.currentThread().setPriority(priority);
        } catch (InterruptedException ie) {
            splash.setVisible(false);
        }
    }

    public void dispose(){
        if(splash != null){
            splash.dispose();
        }
    }
}
