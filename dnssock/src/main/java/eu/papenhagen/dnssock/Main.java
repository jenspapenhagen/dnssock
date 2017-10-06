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
import java.time.LocalDateTime;
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
            get("/:" + d.getName() + "/:psw", (request, response) -> {
                String psw = request.params(":psw");
                String user = request.params(":"+ d.getName());
                String renewIp = request.ip();
                Ip opIp = new Ip();
                
                int field0 = Integer.parseInt(renewIp.split("\\.")[0]);
                int field1 = Integer.parseInt(renewIp.split("\\.")[0]);
                int field2 = Integer.parseInt(renewIp.split("\\.")[0]);
                int field3 = Integer.parseInt(renewIp.split("\\.")[0]);
                
                if( checkPsw(user, psw)){
                    //fill the new ip into domain
                    opIp.setIp0(field0);
                    opIp.setIp1(field1);
                    opIp.setIp2(field2);
                    opIp.setIp3(field3);
                    
                    //set ip to domain
                    d.setIp(opIp);
                    
                    
                    
                    
                    //return the new ip
                    return ip;
                }
                
                
                
                response.status(400);
                return "No user with psw found";


            });
            //e404
            get("/", (req, res) -> "0.0.0.0");

        });

        System.out.println("http://localhost:4567/hello");
//        get("/hello", (req, res) -> "Hello World");
    }

    private static Boolean checkPsw(String user, String psw) {
        List<Domain> list = readJSON();
        //check if the user/psw are in the json file
        return list.stream().filter((domain) -> (domain.getName().equals(user))).anyMatch((domain) -> (domain.getPassword().equals(psw)));
    }

    private static void saveJSON(Domain d){
        //get all Domain
        List<Domain> list = readJSON();
        //add given one
        for (Domain domain : list) {
            if(domain.getName().equals(d.getName())){
                domain.setIp(d.getIp());
                LocalDateTime lastChange = LocalDateTime.now();
                domain.setLastChange(lastChange.toString());
                //TODO postion in list for replace
                
                
            }
        }

        

        
    }
    
    

    /**
     * read the JSON
     *
     * @return the JSON as List of Domain //TODO doamins.json from resoruce
     * haveto copyed to a good place
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
