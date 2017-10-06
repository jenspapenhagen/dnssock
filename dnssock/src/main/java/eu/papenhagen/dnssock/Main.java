/**
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.papenhagen.dnssock;

import static spark.Spark.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.LoggerFactory;

/**
 *
 * @author jens.papenhagen
 */
public class Main {

    //start logging
    private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {

        List<Domain> list = readJSON();
        list.forEach((d) -> {
            String ip = d.getIp().getIp0() + "." + d.getIp().getIp1() + "." + d.getIp().getIp2() + "." + d.getIp().getIp3();
            get("/" + d.getName(), (req, res) -> "" + ip);
        });

        System.out.println("http://localhost:4567/hello");
//        get("/hello", (req, res) -> "Hello World");
    }
    
    

    /**
     * read the JSON
     *
     * @return the JSON as List of Domain
     * //TODO doamins.json from resoruce haveto copyed to a good place
     */
    private static List<Domain> readJSON() {
        String fileinput = "";
        try {
            fileinput = new String(Files.readAllBytes(Paths.get("C:\\EAI\\domains.json")));
        } catch (IOException ex) {
            LOG.error(ex.getMessage());
        }
        List<Domain> list = new ArrayList<>();
        list = parseLocalStatusJsonFromFile(fileinput);

        return list;
    }
        

    /**
     * parsing the JSON file into a List of Domain
     *
     * @param fileinput
     * @return
     */
    public static List<Domain> parseLocalStatusJsonFromFile(String fileinput) {
        Gson gson = new Gson();
        Type founderListType = new TypeToken<ArrayList<Domain>>() {
        }.getType();
        List<Domain> founderList = gson.fromJson(fileinput, founderListType);

        return founderList;
    }

}
