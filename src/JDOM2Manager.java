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

        String coordinatesStr = "";

        JSONObject geometry = (JSONObject)country.get("geometry");
        boolean isPolygonMulti = geometry.get("type").toString().equals("MultiPolygon");

        if(isPolygonMulti){

            JSONArray testArray = (JSONArray) geometry.get("coordinates");
            Element geomStyle = new Element("MultiGeometry");

            for(int i =0; i < testArray.size(); i++){
                Element linearRing = new Element("LinearRing");
                Element coordinates = new Element("coordinates");
                Element outer = new Element("outerBoundaryIs");
                Element poly = new Element("Polygon");
                coordinatesStr = extractCoordMP(i, country);
                coordinates.setText(coordinatesStr);
                linearRing.addContent(coordinates);
                outer.addContent(linearRing);
                poly.addContent(outer);
                geomStyle.addContent(poly);
            }
            placemark.addContent(geomStyle);

        } else {

            Element linearRing = new Element("LinearRing");
            Element coordinates = new Element("coordinates");
            Element outer = new Element("outerBoundaryIs");
            Element geomStyle = new Element("Polygon");
            coordinatesStr = polygonToStr(country);
            geomStyle.addContent(outer);
            outer.addContent(linearRing);
            linearRing.addContent(coordinates);
            coordinates.setText(coordinatesStr);
            placemark.addContent(geomStyle);
        }

        //Output
        XMLOutputter xmlOutputter = new XMLOutputter();
        xmlOutputter.setFormat(Format.getPrettyFormat());
        xmlOutputter.output(doc, new FileWriter(XMLPath));
    }

    /**
     * gets coordinates from a simple polygon
     * @param polygon JSONObject polygon
     * @return coordinates
     */
    private String polygonToStr(JSONObject polygon){
        JSONArray coordinatesArrayRoot = (JSONArray) ((JSONObject)polygon.get("geometry")).get("coordinates");
        JSONArray coordinatesArray = (JSONArray) coordinatesArrayRoot.get(0);

        return getString(coordinatesArray);
    }

    /**
     * @param y coordinate position
     * @param multiPolygon multipolygon
     * @return coordinates for this position
     */
    private String extractCoordMP(int y, JSONObject multiPolygon){
        JSONArray coordinatesArrayRoot = (JSONArray) ((JSONObject)multiPolygon.get("geometry")).get("coordinates");
        JSONArray coordinatesArray = (JSONArray) coordinatesArrayRoot.get(y);
        JSONArray polyArray = (JSONArray) ((coordinatesArray)).get(0);

        return getString(polyArray);
    }

    /**
     * get string from a JSONArray
     * @param polyArray JSONArray
     * @return string convertion
     */
    private String getString(JSONArray polyArray) {
        StringBuilder coordinatesStr = new StringBuilder("\n");
        for(Object coord : polyArray){
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
