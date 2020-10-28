package com.ms.WebPlatform.utility;



import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class spotifyKiller extends JFrame implements ActionListener{
	
  private static final long serialVersionUID = 1L;
  private JTextField minutesBox;

  public static void main(String[] args){
    new spotifyKiller().setVisible(true);
  }

  public static void execKill(long minutes) throws InterruptedException {
    Thread.sleep(minutes * 60L * 1000L);
    try {
      Runtime.getRuntime().exec("TASKKILL /F /IM spotify.exe");
      System.exit(0);
    }
    catch (IOException ioe) {
      ioe.printStackTrace();
    }
  }

  public spotifyKiller() {
    setTitle("Timed Program Killer");
    setSize(400, 300);
    setVisible(true);
    setDefaultCloseOperation(3);

    Container pane = getContentPane();
    pane.setLayout(new FlowLayout());

    JPanel box = new JPanel();
    box.setLayout(new FlowLayout());
    JButton startButton = new JButton("Start");
    startButton.addActionListener(this);
    box.add(startButton);
    box.add(new JLabel("Minutes Until Close"));
    box.add(this.minutesBox = new JTextField(20));

    pane.add(box);
    pack();
  }

  public void actionPerformed(ActionEvent e){
    String textNum = this.minutesBox.getText();
    long minuteNum = Long.parseLong(textNum);
    if (e.getActionCommand().equals("Start"))
      try {
        execKill(minuteNum);
      } catch (InterruptedException e1) {
        e1.printStackTrace();
      }
  }
}