package Fenster;

import CharacterSheets.SR5CharacterSheet;
import Data.Data;

class SR5Fenster extends SpielweltFenster{

    public SR5Fenster() {
        //Erstellt das Fenster und den Charakterbogen
        super("SR5 Charakterbogen", new SR5CharacterSheet(), Data.sR5AttributeNames);

        showFrame();
    }

    @Override
    protected void updateJFrameAfterLoadCharacterSystemSpecific() {

    }
}
