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

    public void toOutputFile(JSONObject country, Element docElem) throws IOException {
        boolean isPolygonMulti = ((JSONObject)country.get("geometry")).get("type").toString().equals("MultiPolygon");

        Element placemark = new Element("Placemark");
        String countryName = ((JSONObject)country.get("properties")).get("ADMIN").toString();
        docElem.addContent(placemark);
        placemark.addContent(new Element("name").setText(countryName));
        placemark.addContent(new Element("styleUrl").setText("#orange-5px"));

        String coordinatesStr ="";



        if(isPolygonMulti){
            JSONArray testArray = (JSONArray) ((JSONObject)country.get("geometry")).get("coordinates");
            Element geomStyle = new Element("MultiGeometry");


            for(int i =0; i < testArray.size(); i++){
                Element outer = new Element("outerBoundaryIs");
                extractCoordMP(i, country);
                Element poly = new Element("Polygon");
                Element linearRing = new Element("LinearRing");
                Element coordinates = new Element("coordinates");
                coordinatesStr = extractCoordMP(i, country);


                coordinates.setText(coordinatesStr);
                linearRing.addContent(coordinates);
                outer.addContent(linearRing);
                poly.addContent(outer);
                //poly.addContent(outer.clone().setName("innerBoundaryIs"));

                geomStyle.addContent(poly);
            }
            placemark.addContent(geomStyle);
        }else{
            Element geomStyle = new Element("Polygon");
            Element linearRing = new Element("LinearRing");
            Element coordinates = new Element("coordinates");
            Element outer = new Element("outerBoundaryIs");
            coordinatesStr = polygonToStr(country);
            geomStyle.addContent(outer);
            outer.addContent(linearRing);
            linearRing.addContent(coordinates);
            coordinates.setText(coordinatesStr);

            //geomStyle.addContent(outer.clone().setName("innerBoundaryIs"));
           // placemark.addContent(geomStyle);
            placemark.addContent(geomStyle);
        }

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

    Element headerOutput(){
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

        //couleur interne
        style.addContent(new Element("PolyStyle").addContent(new Element("color").addContent(POLY_INNER)));

        docElem.addContent(name);
        docElem.addContent(style.addContent(lineStyle));

        return docElem;
    }

}
