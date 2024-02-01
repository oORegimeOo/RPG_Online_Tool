package Fenster;

import CharacterSheets.DSA5CharacterSheet;
import Data.Data;

class DSA5Fenster extends SpielweltFenster{

    DSA5CharacterSheet chara;
    public DSA5Fenster() {
        //Erstellt das Fenster und den Charakterbogen
        super("DSA5 Charakterbogen", new DSA5CharacterSheet(), Data.dSA5AttributeNames);

        showFrame();
    }

    @Override
    protected void updateJFrameAfterLoadCharacterSystemSpecific() {
        this.chara = (DSA5CharacterSheet) this.characterSheet;
        System.out.println(chara.getLebenspunkte());
    }
}