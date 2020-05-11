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
    private Document file;

    public JDOM2Manager(String output){
        XMLPath = output;
    }

    public void writeKMLPolygon(JSONObject polygon){
        System.out.println(((JSONObject)polygon.get("geometry")).get("type"));
    }

    public void flush() throws IOException {
        XMLOutputter xmlOutputter = new XMLOutputter();
        xmlOutputter.setFormat(Format.getPrettyFormat());
        xmlOutputter.output(file, new FileWriter(XMLPath));
    }

}
