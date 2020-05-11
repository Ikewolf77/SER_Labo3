/**
 * SER : LABO3
 * MATTEI SIMON, DILHOMME CLAIRE
 * 31.05.2020
 */


import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class JSONManager {

    private JSONParser parser;
    private String file;

    public JSONManager(String file){
        parser = new JSONParser();
        this.file = file;
    }

    public void parseKML(String output) {

        try (FileReader reader = new FileReader(file)) {

            // lecture du fichier
            Object obj = parser.parse(reader);

            JSONObject file = (JSONObject) obj;
            System.out.println(file);


        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

    }

}
