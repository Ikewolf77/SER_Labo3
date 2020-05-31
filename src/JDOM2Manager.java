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

    /**
     * Converts current country to output file
     * @param country country
     * @param docElem Document
     */
    public void toOutputFile(JSONObject country, Element docElem) throws IOException {

        //add placemark
        Element placemark = new Element("Placemark");
        docElem.addContent(placemark);

        JSONObject properties = (JSONObject)country.get("properties");
        String countryName = properties.get("ADMIN").toString();
        String countryCode = properties.get("ISO_A3").toString();

        System.out.println("(" + countryCode + ") " + countryName);

        placemark.addContent(new Element("name").setText(countryName));
        placemark.addContent(new Element("styleUrl").setText("#orange-5px"));

        JSONObject geometry = (JSONObject)country.get("geometry");
        boolean isPolygonMulti = geometry.get("type").toString().equals("MultiPolygon");

        JSONArray coordinates1 = (JSONArray) geometry.get("coordinates");

        if(isPolygonMulti){

            Element geomStyle = new Element("MultiGeometry");

            for(int i =0; i < coordinates1.size(); i++){
                JSONArray root = (JSONArray)coordinates1.get(i);
                geomStyle.addContent(parseCoordinates(root));
            }
            placemark.addContent(geomStyle);

        } else {

            placemark.addContent(parseCoordinates(coordinates1));

        }

        //Output
        XMLOutputter xmlOutputter = new XMLOutputter();
        xmlOutputter.setFormat(Format.getPrettyFormat());
        xmlOutputter.output(doc, new FileWriter(XMLPath));
    }

    private Element parseCoordinates(JSONArray coordinatesArray){
        Element linearRing = new Element("LinearRing");
        Element coordinates = new Element("coordinates");
        Element outer = new Element("outerBoundaryIs");
        Element poly = new Element("Polygon");

        String coordinatesStr = polygonToStr(coordinatesArray);
        coordinates.setText(coordinatesStr);
        linearRing.addContent(coordinates);
        outer.addContent(linearRing);
        poly.addContent(outer);

        return poly;
    }

    /**
     * gets coordinates from a simple polygon
     * @param coordinatesArrayRoot JSONObject polygon
     * @return coordinates
     */
    private String polygonToStr(JSONArray coordinatesArrayRoot){
        JSONArray coordinatesArray = (JSONArray) coordinatesArrayRoot.get(0);

        StringBuilder coordinatesStr = new StringBuilder("\n");
        for(Object coord : coordinatesArray){
            JSONArray coordArray = (JSONArray)coord;
            coordinatesStr.append(coordArray.get(0)).append(",").append(coordArray.get(1)).append(",0\n");
        }

        return coordinatesStr.toString();
    }

    /**
     * Creates header before each country
     * @return Header Element
     */
    public Element headerOutput(){
        String POLY_INNER = "f0000f";
        Element docElem = new Element("Document");
        doc = new Document(docElem);

        Element name = new Element("name");
        name.setText("countries.kml");
        Element style = new Element("Style");
        style.setAttribute(new Attribute("id", "orange-5px"));
        Element lineStyle = new Element("LineStyle");
        lineStyle.addContent(new Element("color").setText("ff00aaff"));
        lineStyle.addContent(new Element("width").setText("5"));

        //intern color
        style.addContent(new Element("PolyStyle").addContent(new Element("color").addContent(POLY_INNER)));

        docElem.addContent(name);
        docElem.addContent(style.addContent(lineStyle));

        return docElem;
    }

}
