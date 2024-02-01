package Fenster;

import CharacterSheets.CharacterSheet;

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
    protected JLabel characterGivenName;
    protected JLabel characterSurname;
    protected JTextField characterGivenNameInput;
    protected JTextField characterSurnameInput;
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
        this.characterGivenName = new JLabel("Vorname:");
        this.characterGivenName.setOpaque(false);
        this.characterGivenNameInput = new JTextField("");
        this.characterGivenNameInput.addActionListener(e -> this.characterSheet.setCharakterVorname(this.characterGivenNameInput.getText()));
        this.characterGivenNameInput.setColumns(10);
        this.characterSurname = new JLabel("Nachname:");
        this.characterSurname.setOpaque(false);
        this.characterSurnameInput = new JTextField("");
        this.characterSurnameInput.addActionListener(e -> this.characterSheet.setCharakterNachname(this.characterSurnameInput.getText()));
        this.characterSurnameInput.setColumns(10);
        this.checkButton = new JButton("Übernehmen");
        this.checkButton.setBorder(new BasicBorders.ButtonBorder(Color.BLACK,Color.BLACK,Color.BLACK,Color.BLACK));
        this.checkButton.addActionListener(e -> {
            this.characterSheet.setCharakterVorname(this.characterGivenNameInput.getText());
            this.characterSheet.setCharakterNachname(this.characterSurnameInput.getText());
        });
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
        this.mainFrame.add(setCharacterDetailsInPanel(), BorderLayout.NORTH);
        this.mainFrame.add(setCharacterAttributeInPanel(), BorderLayout.WEST);
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

    protected JPanel setCharacterDetailsInPanel() {
        JPanel characterDetails = new JPanel(new FlowLayout(FlowLayout.LEFT));
        characterDetails.add(characterGivenName);
        characterDetails.add(characterGivenNameInput);
        characterDetails.add(characterSurname);
        characterDetails.add(characterSurnameInput);
        characterDetails.add(checkButton);

        return characterDetails;
    }
    protected JPanel setCharacterAttributeInPanel() {
        JPanel charakterAttribute = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        int maxWidthA = 0;
        int maxWidthAV = 0;
        int maxHeight = 0;
        for (int idx = 0; idx < this.systemAttribute.split(",").length; idx++ ) {
            JPanel attNameValue = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            int widthA = this.attribute[idx].getPreferredSize().width;
            int widthAV = this.attributeValues[idx].getPreferredSize().width;
            maxHeight = this.attribute[idx].getPreferredSize().height;
            if (widthA > maxWidthA) {
                maxWidthA = widthA;
            }
            if (widthAV > maxWidthAV) {
                maxWidthAV = widthAV;
            }
            attNameValue.add(this.attribute[idx]);
            attNameValue.add(this.attributeValues[idx]);
            charakterAttribute.add(attNameValue);
        }

        charakterAttribute.setPreferredSize(new Dimension(maxWidthA + maxWidthAV + 25, maxHeight * this.systemAttribute.split(",").length));
        return charakterAttribute;
    }

    //Ändert die veränderbaren items im Frame, nachdem ein Charakter geladen wurde
    protected void updateJFrameAfterLoadCharacter() {
        int idx = 0;
        for (String att: this.systemAttribute.split(",")) {
            this.attributeValues[idx].setSelectedIndex(this.characterSheet.getValueOfAttribute(att)-1);
            idx++;
        }
        this.characterGivenNameInput.setText(this.characterSheet.getCharakterVorname());
        this.characterSurnameInput.setText(this.characterSheet.getCharakterNachname());
        updateJFrameAfterLoadCharacterSystemSpecific();
    }

    /**
     * Die Methode wird automatisch aufgerufen, nachdem alle allgemeinen Eigenschaften des Charakters geladen wurden.
     * Sie soll die systemspezifischen Eigenschaften updaten.
     */
    abstract protected void updateJFrameAfterLoadCharacterSystemSpecific();
}