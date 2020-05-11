/**
 * SER : LABO3
 * MATTEI SIMON, DILHOMME CLAIRE
 * 31.05.2020
 */

import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class JSONManager {

    private JSONParser parser;
    private String file;

    /**
     * Constructor
     * @param file path to parse
     */
    public JSONManager(String file){
        parser = new JSONParser();
        this.file = file;
    }

    /**
     * Parsing geoJson file to KML
     * @param output output file
     */
    public void parseKML(String output) {

        try (FileReader reader = new FileReader(file)) {

            JDOM2Manager writer = new JDOM2Manager(output);

            //reading file
            Object obj = parser.parse(reader);

            //parsing each country
            JSONArray features = (JSONArray) ((JSONObject)obj).get("features");
            for(Object country : features){
                writer.writeKMLPolygon((JSONObject)country);
            }

            //flush on output file
            writer.flush();

        } catch (IOException | ParseException e ) {
            e.printStackTrace();
        }

    }

}
