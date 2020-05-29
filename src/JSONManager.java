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
            //reading file
            Object obj = parser.parse(reader);

            //parsing each country
            JSONArray features = (JSONArray) ((JSONObject)obj).get("features");

            //Writing
            JDOM2Manager writer = new JDOM2Manager(output);
            JSONObject test = (JSONObject) features.get(0);
//            for(Object country : features){
//                writer.writeKMLPolygon((JSONObject)country);
//            }
            writer.writeKMLPolygon((JSONObject)test);

            //flush on output file
            writer.toOutputFile(test);

        } catch (IOException | ParseException e ) {
            e.printStackTrace();
        }

    }

}
