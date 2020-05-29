/**
 * SER : LABO3
 * MATTEI SIMON, DILHOMME CLAIRE
 * 31.05.2020
 */

import java.io.*;
import java.util.HashMap;

import org.jdom2.*;
import org.jdom2.output.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class JDOM2Manager {

    private String XMLPath;
    private Document doc;

    public JDOM2Manager(String output){
        XMLPath = output;
    }

    public void writeKMLPolygon(JSONObject polygon){
        System.out.println(((JSONObject)polygon.get("geometry")).get("type"));
        System.out.println(((JSONObject)polygon.get("properties")).get("ADMIN"));
       // JSONArray coordinatesArray = (JSONArray) ((JSONObject)polygon.get("geometry")).get("coordinates");
       // JSONObject coordinatesTest = (JSONObject) ((JSONObject)polygon.get("geometry")).get("coordinates");

      //  System.out.println(coordinatesArray);
    }

    public void toOutputFile(JSONObject country) throws IOException {
        Element docElem = new Element("Document");
        doc = new Document(docElem);

        Element name = new Element("name");
        name.setText("countries.kml");
        Element style = new Element("Style");
        style.setAttribute(new Attribute("id", "orange-5px"));
        Element lineStyle = new Element("LineStyle");
        lineStyle.addContent(new Element("color").setText("ff00aaff"));
        lineStyle.addContent(new Element("width").setText("5"));

        docElem.addContent(name);
        docElem.addContent(style.addContent(lineStyle));


        Element placemark = new Element("Placemark");
        String countryName = ((JSONObject)country.get("properties")).get("ADMIN").toString();
        docElem.addContent(placemark);
        placemark.addContent(name.clone().setText(countryName));
        placemark.addContent(new Element("styleUrl").setText("#orange-5px"));

        String coordinatesStr ="";
//        String coordinatesStr = ((JSONObject)country.get("geometry")).get("type").toString().equals("MultiPolygon") ?
//                multiPolygonToStr(country) : polygonToStr(country);
        JSONArray testArray = (JSONArray) ((JSONObject)country.get("geometry")).get("coordinates");
        Element multiG = new Element("MultiGeometry");
        for(int i =0; i < testArray.size(); i++){
            extractCoordMP(i, country);
            Element lineString = new Element("LineString");
            Element coordinates = new Element("coordinates");
            coordinatesStr = extractCoordMP(i, country);
            multiG.addContent(lineString);
            lineString.addContent(coordinates);
            coordinates.setText(coordinatesStr);
        }

        placemark.addContent(multiG);










        //Output

        XMLOutputter xmlOutputter = new XMLOutputter();
        xmlOutputter.setFormat(Format.getPrettyFormat());
        xmlOutputter.output(doc, new FileWriter(XMLPath));
    }

    String polygonToStr(JSONObject polygon){
        JSONArray coordinatesArrayRoot = (JSONArray) ((JSONObject)polygon.get("geometry")).get("coordinates");
         JSONArray coordinatesArraySuperRoot = (JSONArray) coordinatesArrayRoot.get(0);
        //JSONArray coordinatesArray = true ? (JSONArray) coordinatesArraySuperRoot.get(0) : coordinatesArraySuperRoot;
        JSONArray coordinatesArray = coordinatesArraySuperRoot;

        String coordinatesStr = "\n";

        for(Object coord : coordinatesArray){
            JSONArray coordArray = (JSONArray)coord;
            coordinatesStr += coordArray.get(0)+ "," + coordArray.get(1) + ",0\n";
        }
        return coordinatesStr;
    }

    String multiPolygonToStr(JSONObject multiPolygon){
        JSONArray coordinatesArrayRoot = (JSONArray) ((JSONObject)multiPolygon.get("geometry")).get("coordinates");
        JSONArray coordinatesArraySuperRoot = (JSONArray) coordinatesArrayRoot.get(0);
        //JSONArray coordinatesArray = coordinatesArraySuperRoot.get(0);

        String coordinatesStr = "\n";
    for(Object coordinatesArray : coordinatesArrayRoot){
        JSONArray polyArray = (JSONArray) ((JSONArray) (coordinatesArray)).get(0);

        for(Object coord : (JSONArray) polyArray){
            JSONArray coordArray = (JSONArray)coord;
            coordinatesStr += coordArray.get(0)+ "," + coordArray.get(1) + ",0\n";
        }
    }

        return coordinatesStr;
    }

    String extractCoordMP(int y, JSONObject multiPolygon){
        JSONArray coordinatesArrayRoot = (JSONArray) ((JSONObject)multiPolygon.get("geometry")).get("coordinates");
        JSONArray coordinatesArray = (JSONArray) coordinatesArrayRoot.get(y);
        JSONArray polyArray = (JSONArray) ((JSONArray) (coordinatesArray)).get(0);

        String coordinatesStr = "\n";
            for(Object coord : (JSONArray) polyArray){
                JSONArray coordArray = (JSONArray)coord;
                coordinatesStr += coordArray.get(0)+ "," + coordArray.get(1) + ",0\n";
            }

        return coordinatesStr;
    }

    String extractCoordPoly(int i){
        return "";
    }
}
