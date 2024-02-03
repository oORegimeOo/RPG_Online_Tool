package CharacterSheets;

import Data.Data;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

/*
Superclass von allen Charakterbögen aus allen Systemen. Beinhaltet als Variablen alles, was Charakteren aus allen
Systemen gemein ist. Die Charakterbögen aus den einzelnen Systemen können weitere Variablen besitzen, welche individuelle
Systemwerte wiederspiegeln.
 */
public class CharacterSheet {
    //Jeder Charakter stammt aus einem System
    protected String systemName;

    //Jeder Charakter besitzt Attribute, die seine körperlichen und geistigen Fähigkeiten wiederspiegeln
    protected HashMap<String, Integer> attribute;

    //Jeder Charakter besitzt Eigenschaften, die ihn beschreiben
    protected HashMap<String, String> characterDetails;

    public CharacterSheet(String attributeNames, int attributeValue, String systemName){
        this.systemName = systemName;
        this.characterDetails = new HashMap<>();
        for (String det : Data.characterDetailsDefault.split(",")) {
            try {
                characterDetails.put(det, "");
            } catch (NullPointerException e) {
                System.out.println("Something went terrible wrong!");
            }
        }

        this.attribute = new HashMap<>();
        for (String att : attributeNames.split(",")) {
            try {
                this.attribute.put(att, attributeValue);
            } catch (NullPointerException e) {
                System.out.println("Something went terrible wrong!");
            }
        }
    }

    public int getValueOfAttribute(String attributeName) {
        try {
            return this.attribute.get(attributeName);
        } catch (NullPointerException w) {
            System.out.println(attributeName + " is not a attribut in your System!");
        }
        return -1;
    }

    public boolean setValueOfAttribute(String attributeName, int attributeValue) {
        return attribute.replace(attributeName, getValueOfAttribute(attributeName), attributeValue);
    }

    public String getSystemName() {
        return this.systemName;
    }

    public String getCharakterDetail(String detail) {
        return this.characterDetails.get(detail);
    }

    public boolean setCharakterDetail(String characterDetail, String characterDetailInput) {
        return this.characterDetails.replace(characterDetail, getCharakterDetail(characterDetail), characterDetailInput);
    }


    @SuppressWarnings("unchecked")
    public void saveCharakter(){
        try {
            JSONObject charakterSheet = new JSONObject();
            charakterSheet.put("System", this.systemName);
            charakterSheet.putAll(this.characterDetails);
            charakterSheet.putAll(this.attribute);
            FileWriter saver = new FileWriter(getCharakterDetail("Vorname") + getCharakterDetail("Nachname") + "_" + getSystemName() + ".txt");
            saver.write(charakterSheet.toJSONString());
            saver.flush();
            saver.close();
        } catch (IOException e) {System.out.println(e.getMessage());}
    }

    public void loadCharakter(String Datei){
        try {
            JSONParser parser = new JSONParser();
            JSONObject charakterSheet =  (JSONObject) parser.parse(new FileReader(Datei));
            this.systemName = (String) charakterSheet.get("System");
            for (String characterDetail : this.characterDetails.keySet()) {
                boolean isDone = setCharakterDetail(characterDetail, charakterSheet.get(characterDetail).toString());
                if (!isDone) {
                    System.out.println("Something went terrible wrong");
                }
            }
            for (String attributeName: this.attribute.keySet()) {
                boolean isDone = setValueOfAttribute(attributeName, Math.toIntExact((long) charakterSheet.get(attributeName)));
                if (!isDone) {
                    System.out.println("Something went terrible wrong");
                }
            }
        } catch (IOException|ParseException e) {System.out.println(e.getMessage());}
    }

    public Set<String> getAllAttributeNames() {
        return this.attribute.keySet();
    }

    public Set<String> getAllCharacterDetailNames() {
        return this.characterDetails.keySet();
    }
}
