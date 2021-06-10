package cn.erning.aabinstaller.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.TextEvent;
import java.awt.event.TextListener;

/**
 * @author erning
 * @date 2021-06-08 17:19
 * des:
 */
public class JTextFieldHintListener implements FocusListener {
    private final String mHintText;
    private final JTextField mTextField;

    public JTextFieldHintListener(String hintText, JTextField textField) {
        this.mHintText = hintText;
        this.mTextField = textField;
        textField.setForeground(Color.GRAY);
        textField.setText(hintText);
    }

    @Override
    public void focusGained(FocusEvent e) {
        String temp = mTextField.getText();
        if(temp.equals(mHintText)){
            mTextField.setText("");
            mTextField.setForeground(Color.BLACK);
        }
    }
    @Override
    public void focusLost(FocusEvent e) {
        String temp = mTextField.getText();
        if(temp.equals("")){
            mTextField.setForeground(Color.GRAY);
            mTextField.setText(mHintText);
        }
    }

}
