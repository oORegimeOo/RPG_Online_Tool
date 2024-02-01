package Fenster;

import CharacterSheets.CharacterSheet;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class QuestionAtCloseToSaveFenster extends Fenster{

    CharacterSheet cS;
    public QuestionAtCloseToSaveFenster(String name, CharacterSheet cS) {
        super(name);
        this.cS = cS;
        this.mainFrame.setJMenuBar(getMenuBar());
        this.mainFrame.setLocationRelativeTo(null);
        this.mainFrame.pack();
        this.mainFrame.setVisible(true);
    }

    @Override
    protected JMenuBar getMenuBar(){
        JMenuBar mb = new JMenuBar();
        mb.add(new JMenu(""));
        JLabel text = new JLabel("Soll der Charakter gespeichert werden?");
        mb.add(text);
        mb.add(new JMenu(""));
        JButton ja = new JButton(new AbstractAction("Ja") {
            @Override
            public void actionPerformed(ActionEvent e) {
                cS.saveCharakter();
                mainFrame.setVisible(false);
            }
        });
        ja.setBackground(Color.LIGHT_GRAY);
        mb.add(ja);
        mb.add(new JMenu(""));
        JButton nein = new JButton(new AbstractAction("Nein") {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.setVisible(false);
            }
        });
        nein.setBackground(Color.LIGHT_GRAY);
        mb.add(nein);
        mb.add(new JMenu(""));
        return mb;
    }
}
