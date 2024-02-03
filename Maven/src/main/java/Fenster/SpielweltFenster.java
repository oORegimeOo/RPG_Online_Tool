package Fenster;

import CharacterSheets.CharacterSheet;
import Data.Data;

import javax.swing.*;
import javax.swing.plaf.basic.BasicBorders;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

abstract class SpielweltFenster extends Fenster {
    protected CharacterSheet characterSheet;
    protected String systemAttribute;
    protected JLabel[] attribute;
    @SuppressWarnings("rawtypes")
    protected JComboBox[] attributeValues;
    protected JLabel[] characterDetails;
    protected JTextField[] characterDetailsInput;
    protected JButton checkButton;

    public SpielweltFenster(String name, CharacterSheet cS, String sysAtt) {
        //Erstellt das Fenster
        super(name);
        //Attribute des entsprechenden Systems
        this.systemAttribute = sysAtt;

        //Standardcharakterbogen des entsprechenden Systems
        this.characterSheet = cS;

        erstelleInhalt();
        setBackgroundImage("Background"+cS.getSystemName()+".jpg");
        erstelleMainFrameMitAllgemeinenInhalten();

    }

    //Erstellt alle allgemeinen items für den Frame
    protected void erstelleInhalt() {
        //Erstellt den Inhalt fürs Fenster
        erstelleCharacterDetails();
        erstelleCharacterAttribute();
    }
    protected void erstelleCharacterDetails() {
        this.characterDetails = new JLabel[this.characterSheet.getAllCharacterDetailNames().size()];
        this.characterDetailsInput = new JTextField[this.characterSheet.getAllCharacterDetailNames().size()];
        int idx = 0;
        for (String detail : Data.characterDetailsDefault.split(",")) {
            JLabel detailLabel = new JLabel(detail+":");
            detailLabel.setOpaque(false);
            detailLabel.setName(detail);
            this.characterDetails[idx] = detailLabel;
            this.characterDetailsInput[idx] = getDetailInput(detail, idx);
            idx++;
        }
        this.checkButton = new JButton("Übernehmen");
        this.checkButton.setBorder(new BasicBorders.ButtonBorder(Color.BLACK,Color.BLACK,Color.BLACK,Color.BLACK));
        this.checkButton.addActionListener(e -> {
            int pos = 0;
            for (String detail : Data.characterDetailsDefault.split(",")) {
                this.characterSheet.setCharakterDetail(detail, this.characterDetailsInput[pos].getText());
                pos++;
            }
        });
    }

    private JTextField getDetailInput(String detail, int idx) {
        JTextField detailInput = new JTextField("");
        detailInput.addActionListener(new ActionListener() {
            final int jComboBoxIdx = idx;
            @Override
            public void actionPerformed(ActionEvent e) {
                characterSheet.setCharakterDetail(characterDetails[jComboBoxIdx].getName(),
                        characterDetailsInput[jComboBoxIdx].getText());
            }
        });
        detailInput.setColumns(10);
        detailInput.setName(detail +"Input");
        return detailInput;
    }

    protected void erstelleCharacterAttribute() {
        this.attribute = new JLabel[this.characterSheet.getAllAttributeNames().size()];
        this.attributeValues = new JComboBox[this.characterSheet.getAllAttributeNames().size()];
        Integer[] arr = new Integer[20];
        for (int i = 0; i < 20; i++) {
            arr[i] = i + 1;
        }
        int idx = 0;
        for (String attributeName : this.systemAttribute.split(",")) {
            this.attribute[idx] = new JLabel(attributeName);
            this.attribute[idx].setMaximumSize(new Dimension(Integer.MAX_VALUE, this.attribute[idx].getMinimumSize().height));
            this.attributeValues[idx] = new JComboBox<>(arr);
            this.attributeValues[idx].setSelectedIndex(this.characterSheet.getValueOfAttribute(attributeName) - 1);
            this.attributeValues[idx].setMaximumSize(this.attributeValues[idx].getPreferredSize());
            this.attributeValues[idx].setName(attributeName);
            int currentIdx = idx;
            this.attributeValues[idx].addActionListener(new ActionListener() {
                final int jComboBoxIdx = currentIdx;
                @Override
                public void actionPerformed(ActionEvent e) {
                    characterSheet.setValueOfAttribute(attributeValues[jComboBoxIdx].getName(),
                            Integer.parseInt(String.valueOf(attributeValues[jComboBoxIdx].getSelectedItem())));
                }
            });
            idx++;
        }
    }

    //Platziert die allgemeinen items im Frame
    protected void erstelleMainFrameMitAllgemeinenInhalten() {
        setMenuBarInFrame(getMenuBar());
        setCharacterDetailsInPanel();
        setCharacterAttributeInPanel();
        addSpacer();
    }
    @Override
    protected JMenuBar getMenuBar() {
        JMenuBar mb = new JMenuBar();
        mb.add(new JMenu(""));
        //Sucht alle gespeicherten Charaktere des entsprechenden Systems und erstellt eine Liste mit den Namen der Files
        JMenu load = new JMenu("Load saved character");
        File folder = new File(System.getProperty("user.dir"));
        ArrayList<String> fileNames = new ArrayList<>();
        for (File file : Objects.requireNonNull(folder.listFiles())) {
            if (!file.isDirectory() && file.getName().contains(this.characterSheet.getSystemName() + ".txt")) {
                fileNames.add(file.getName());
            }
        }
        // Erstellt ein Menü für die Menüleiste mit den Namen der ladbaren Files. Durch Klicken der Namen wird der ent-
        // sprechende Charakter ins CharacterSheet geladen
        for (String fileName : fileNames) {
            JMenuItem jMI = new JMenuItem(new AbstractAction(fileName) {
                @Override
                public void actionPerformed(ActionEvent e) {
                    characterSheet.loadCharakter(e.getActionCommand());
                    updateJFrameAfterLoadCharacter();
                }
            });
            load.add(jMI);
        }
        load.setOpaque(true);
        load.setForeground(Color.DARK_GRAY);
        load.setBorder(new BasicBorders.ButtonBorder(Color.GRAY,Color.GRAY,Color.GRAY,Color.GRAY));
        mb.add(load);
        mb.add(new JMenu(""));
        //Erstellt einen Button in der Menüleiste, welcher beim Klicken den angezeigten Charakter speichert
        JButton save = new JButton(new AbstractAction("Save current character") {
            @Override
            public void actionPerformed(ActionEvent e) {
                characterSheet.saveCharakter();
            }
        });
        save.setForeground(Color.DARK_GRAY);
        save.setBorder(new BasicBorders.ButtonBorder(Color.GRAY,Color.GRAY,Color.GRAY,Color.GRAY));
        mb.add(save);
        mb.add(new JMenu(""));
        //Erstellt einen Button in der Menüleiste, welcher beim Klicken das Charakterbogenfenster schließt
        mb.add(getCloseButton());
        mb.add(new JMenu(""));
        return mb;
    }

    private JButton getCloseButton() {
        JButton close = new JButton(new AbstractAction("Close") {
            @Override
            public void actionPerformed(ActionEvent e) {
                new QuestionAtCloseToSaveFenster("Aktuellen Charakter speichern?", characterSheet);
                mainFrame.setVisible(false);
            }
        });
        close.setForeground(Color.DARK_GRAY);
        close.setBorder(new BasicBorders.ButtonBorder(Color.GRAY,Color.GRAY,Color.GRAY,Color.GRAY));
        return close;
    }

    protected void setCharacterDetailsInPanel() {
        Container bg = this.mainFrame.getContentPane();

        for (int i = 0; i < Data.characterDetailsDefault.split(",").length; i++) {
            bg.add(this.characterDetails[i], createBGC(2*i,0));
            bg.add(this.characterDetailsInput[i], createBGC((2*i)+1, 0));


        }
        bg.add(this.checkButton, createBGC(2*(Data.characterDetailsDefault.split(",").length),0));
    }

    @SuppressWarnings("rawtypes")
    protected void setCharacterAttributeInPanel() {
        Container bg = this.mainFrame.getContentPane();

        int y = 1;
        for (JLabel att : this.attribute) {
            bg.add(att, createBGC(0, y));
            y++;
        }
        y = 1;
        for (JComboBox attVal : this.attributeValues) {
            bg.add(attVal, createBGC(1, y));
            y++;
        }
    }

    //Ändert die veränderbaren items im Frame, nachdem ein Charakter geladen wurde
    protected void updateJFrameAfterLoadCharacter() {
        int idx = 0;
        for (String att: this.systemAttribute.split(",")) {
            this.attributeValues[idx].setSelectedIndex(this.characterSheet.getValueOfAttribute(att)-1);
            idx++;
        }
        for (int i = 0; i < Data.characterDetailsDefault.split(",").length; i++) {
            this.characterDetailsInput[i].setText(this.characterSheet.getCharakterDetail(characterDetails[i].getName()));
        }
        updateJFrameAfterLoadCharacterSystemSpecific();
    }

    /**
     * Die Methode wird automatisch aufgerufen, nachdem alle allgemeinen Eigenschaften des Charakters geladen wurden.
     * Sie soll die systemspezifischen Eigenschaften updaten.
     */
    abstract protected void updateJFrameAfterLoadCharacterSystemSpecific();

    protected void addSpacer() {

        Container bg = this.mainFrame.getContentPane();

        for (int i = 5; i < 50; i++) {
            bg.add(new JLabel(""), createBGC(i,0));
        }
        for (int i = 9; i < 35; i++) {
            bg.add(new JLabel(""), createBGC(0,i));
        }
    }

    protected GridBagConstraints createBGC(int x, int y) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;

        gbc.anchor = (x%2 == 0) ? GridBagConstraints.EAST : GridBagConstraints.WEST;
        //gbc.fill = (x == 0) ? GridBagConstraints.BOTH : GridBagConstraints.HORIZONTAL;

        gbc.insets = (x == 0) ? new Insets(0,0,0,5) : new Insets(0,5,0,0);
        gbc.weightx = (x == 0) ? 0.1 : 1.0;
        gbc.weighty = 1;

        return gbc;
    }
}