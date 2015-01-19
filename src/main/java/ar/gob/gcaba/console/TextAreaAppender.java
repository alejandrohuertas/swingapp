package ar.gob.gcaba.console;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextArea;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;

public class TextAreaAppender extends AppenderSkeleton implements ActionListener {

    private JTextArea consoleLog;
    public TextAreaAppender(JTextArea consoleLog){
        this.consoleLog = consoleLog;
    }
    
    @Override
    public void close() {
        
    }

    @Override
    public boolean requiresLayout() {
        return false;
    }

    @Override
    protected void append(final LoggingEvent arg0) {
        consoleLog.append(arg0.getMessage()+"\n");
    }

    public JTextArea getConsoleLog() {
        return consoleLog;
    }

    void setConsoleLog(JTextArea consoleLog) {
        this.consoleLog = consoleLog;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        consoleLog.revalidate();
    }


}
