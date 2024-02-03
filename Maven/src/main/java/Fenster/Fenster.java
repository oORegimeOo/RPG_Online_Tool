package Fenster;

import Data.Data;

import javax.swing.*;
import java.awt.*;

abstract class Fenster {
    protected String frameTitle;
    protected JFrame mainFrame;

    public Fenster(String name) {
        this.frameTitle = name;
        this.mainFrame = new JFrame(this.frameTitle);
        this.mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    //Erstellt die Menüleiste für die einzelnen Fenster. Die Fenster individualisieren die Methode entsprechend ihr Menüleiste
    protected abstract JMenuBar getMenuBar();

    protected void setBackgroundImage(String image) {
        JLabel backgroundImage = new JLabel(new ImageIcon(Data.pathToImages+image));
        backgroundImage.setLayout(new GridBagLayout());
        this.mainFrame.setContentPane(backgroundImage);
    }

    protected void showFrame() {
        this.mainFrame.pack();
        this.mainFrame.setExtendedState(this.mainFrame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
        this.mainFrame.setVisible(true);
    }

    protected void setMenuBarInFrame(JMenuBar mb) {
        this.mainFrame.setJMenuBar(mb);
    }
}
