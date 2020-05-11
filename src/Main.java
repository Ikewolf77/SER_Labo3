/**
 * SER : LABO3
 * MATTEI SIMON, DILHOMME CLAIRE
 * 31.05.2020
 */

public class Main {

    public static void main(String[] args){
        new JSONManager("ressources/countries.geojson").parseKML("ressources/countries.kml");
    }

}
