/**
 * SER : LABO3
 * MATTEI SIMON, DILHOMME CLAIRE
 * 31.05.2020
 */

import java.io.*;
import org.jdom2.*;
import org.jdom2.output.*;
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

    public void toOutputFile() throws IOException {
        Element company = new Element("company");
        doc = new Document(company);

        Element name = new Element("name");
        Element style = new Element("Style");
        style.setAttribute(new Attribute("id", "orange-5px"));
        Element lineStyle = new Element("LineStyle");
        lineStyle.addContent(new Element("color").setText("ff00aaff"));
        lineStyle.addContent(new Element("width").setText("5"));

        company.addContent(name);
        company.addContent(style.addContent(lineStyle));


//        name.addContent(new Element("firstname").setText("yong"));
//        name.addContent(new Element("lastname").setText("mook kim"));
//        name.addContent(new Element("nickname").setText("mkyong"));
//        name.addContent(new Element("salary").setText("199999"));
JSONObject polygon;

        //doc.setRootElement(company);

        XMLOutputter xmlOutputter = new XMLOutputter();
        xmlOutputter.setFormat(Format.getPrettyFormat());
        xmlOutputter.output(doc, new FileWriter(XMLPath));
    }

}
