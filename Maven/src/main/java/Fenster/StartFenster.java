package Fenster;

import javax.swing.*;
import javax.swing.plaf.basic.BasicBorders;
import java.awt.*;
import java.awt.event.ActionEvent;

class StartFenster extends Fenster{
    public static void main(String[] args) {
        new StartFenster();
    }

    public StartFenster() {
        //Erstellt das Fenster
        super("RPG Online Tool");

        setBackgroundImage("BackgroundStart.jpeg");

        setMenuBarInFrame(getMenuBar());

        showFrame();
    }

    //Erstellt die Menüleiste fürs Startfenster. Sie wird "Load", "Save" und "Close" enthalten. Sie wird die Möglichkeiten
    //enthalten, ein Fenster für einen Charakter aus den möglichen Systemen zu öffnen und das Programm zu beenden.
    protected JMenuBar getMenuBar(){
        JMenuBar mb = new JMenuBar();
        mb.add(new JMenu(""));
        JButton dSA5 = new JButton(new AbstractAction("DSA5") {
            @Override
            public void actionPerformed(ActionEvent e) {
                new DSA5Fenster();
            }
        });
        dSA5.setBackground(Color.LIGHT_GRAY);
        dSA5.setBorder(new BasicBorders.ButtonBorder(Color.GRAY,Color.GRAY,Color.GRAY,Color.GRAY));
        mb.add(dSA5);
        mb.add(new JMenu(""));
        JButton sR5 = new JButton(new AbstractAction("SR5") {
            @Override
            public void actionPerformed(ActionEvent e) {
                new SR5Fenster();
            }
        });
        sR5.setBackground(Color.LIGHT_GRAY);
        sR5.setBorder(new BasicBorders.ButtonBorder(Color.GRAY,Color.GRAY,Color.GRAY,Color.GRAY));
        mb.add(sR5);
        mb.add(new JMenu(""));
        JButton quit = new JButton(new AbstractAction("Quit") {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        quit.setBackground(Color.LIGHT_GRAY);
        quit.setBorder(new BasicBorders.ButtonBorder(Color.GRAY,Color.GRAY,Color.GRAY,Color.GRAY));
        mb.add(quit);
        return mb;
    }
}
