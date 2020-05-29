/**
 * SER : LABO3
 * MATTEI SIMON, DILHOMME CLAIRE
 * 31.05.2020
 */

import java.io.*;
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
        Element lineString = new Element("LineString");
        Element coordinates = new Element("coordinates");


        String countryName = ((JSONObject)country.get("properties")).get("ADMIN").toString();
        JSONArray coordinatesArray = (JSONArray) ((JSONObject)country.get("geometry")).get("coordinates");
        String coordinatesStr = "";

        for(Object coord : coordinatesArray){
            coordinatesStr += coord.toString() + "!!\r";
        }


        docElem.addContent(placemark);
        placemark.addContent(name.clone().setText(countryName));
        placemark.addContent(new Element("styleUrl").setText("#orange-5px"));
        placemark.addContent(lineString.addContent(new Element("tessellate").setText("1")));
        lineString.addContent(coordinates);
        coordinates.setText(coordinatesStr);






        XMLOutputter xmlOutputter = new XMLOutputter();
        xmlOutputter.setFormat(Format.getPrettyFormat());
        xmlOutputter.output(doc, new FileWriter(XMLPath));
    }

}
