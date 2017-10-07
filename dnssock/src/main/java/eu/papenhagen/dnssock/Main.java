/**
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.papenhagen.dnssock;

import static spark.Spark.*;
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
        List<Domain> list = JsonHandler.getInstance().readJSON();
        list.forEach((d) -> {
            String ip = d.getIp().getIp0() + "." + d.getIp().getIp1() + "." + d.getIp().getIp2() + "." + d.getIp().getIp3();
            get("/" + d.getId(), (req, res) -> "" + ip);
            get("/:" + d.getId() + "/:psw", (request, response) -> {
                String psw = request.params(":psw");
                String user = request.params(":" + d.getId());
                String renewIp = request.ip();
                Ip opIp = new Ip();

                int field0 = Integer.parseInt(renewIp.split("\\.")[0]);
                int field1 = Integer.parseInt(renewIp.split("\\.")[0]);
                int field2 = Integer.parseInt(renewIp.split("\\.")[0]);
                int field3 = Integer.parseInt(renewIp.split("\\.")[0]);

                if (checkPsw(user, psw)) {
                    //fill the new ip into domain
                    opIp.setIp0(field0);
                    opIp.setIp1(field1);
                    opIp.setIp2(field2);
                    opIp.setIp3(field3);

                    //set ip to domain
                    d.setIp(opIp);
                    
                    //save List of Domain to file
                    JsonHandler.getInstance().saveJSON(d);

                    //return the new ip
                    return ip;
                }

                response.status(400);
                return "No user with psw found";

            });
            //e404
            get("/", (req, res) -> "0.0.0.0");

        });

        System.out.println("http://localhost:4567/test");
//        get("/hello", (req, res) -> "Hello World");
    }

    private static Boolean checkPsw(String user, String psw) {
        List<Domain> list = JsonHandler.getInstance().readJSON();
        //check if the user/psw are in the json file
        return list.stream().filter((domain) -> (domain.getId().equals(user))).anyMatch((domain) -> (domain.getPassword().equals(psw)));
    }


    private List<ExportDomain> convertDomainToExportDomain() {
        //get all Domain
        List<Domain> list = JsonHandler.getInstance().readJSON();
       //build new Exportlist of the existen private domain.json
        List<ExportDomain> exportlist = new ArrayList<>(list.size());
        
        list.stream().map((d) -> {
            ExportDomain exportdomain = new ExportDomain();
            
            //set ID and IP and lastChange TimeStamp
            exportdomain.setId(d.getId());
            exportdomain.setIp(d.getIp());
            exportdomain.setLastChange(d.getLastChange());
            
            return exportdomain;
        }).forEachOrdered((exportdomain) -> {
            //add to list
            exportlist.add(exportdomain);
        });

        return exportlist;
    }
    

}
