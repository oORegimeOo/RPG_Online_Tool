package CharacterSheets;

import Data.Data;

public class DSA5CharacterSheet extends CharacterSheet {
    private int lebenspunkte;
    private int astralpunkte;
    public DSA5CharacterSheet() {
        super(Data.dSA5AttributeNames,8,"DSA5");
        setLebenspunkte(2 * getValueOfAttribute("Konstitution") + 5);
        setAstralpunkte(0);
    }
    public int getLebenspunkte() {
        return lebenspunkte;
    }

    public void setLebenspunkte(int lebenspunkte) {
        this.lebenspunkte = lebenspunkte;
    }

    public int getAstralpunkte() {
        return astralpunkte;
    }

    public void setAstralpunkte(int astralpunkte) {
        this.astralpunkte = astralpunkte;
    }
}
