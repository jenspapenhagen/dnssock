/**
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.papenhagen.dnssock;

import static spark.Spark.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;

/**
 *
 * @author jens.papenhagen
 */
public class Main {

    //start logging
    private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(Main.class);

    /**
     * simble build of a dnyDNS service why NO Token?
     *
     * @param args
     */
    public static void main(String[] args) {
        List<Node> list = JsonHandler.getInstance().readJSON();
        list.forEach(new Consumer<Node>() {
            @Override
            public void accept(Node n) {
                String ipAsString = n.getIp().getIp0() + "." + n.getIp().getIp1() + "." + n.getIp().getIp2() + "." + n.getIp().getIp3();
                //get singel IP for one node
                get("/" + n.getId(), (Request request, Response response) -> "" + ipAsString);
                //get all Node and IPs
                get("/all", (Request request, Response response) -> {
                    return JsonHandler.getInstance().exportToJSON(convertDomainToExportNode());
                } );
                //change the ip
                get("/:" + n.getId() + "/:psw", (Request request, Response response) -> {
                    String psw = request.params(":psw");
                    String user = request.params(":" + n.getId());
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
                        n.setIp(opIp);
                        
                        //save List of Domain to file
                        JsonHandler.getInstance().saveJSON(n);
                        
                        //return the new ip
                        return ipAsString;
                    }
                    
                    response.status(400);
                    LOG.debug("No user with psw found");
                    return "No user with psw found";
                    
                });
                //e404
                get("/", (req, res) -> "0.0.0.0");
            }
        });

        System.out.println("http://localhost:4567/test");
//        get("/hello", (req, res) -> "Hello World");
    }

    private static Boolean checkPsw(String user, String psw) {
        List<Node> list = JsonHandler.getInstance().readJSON();
        //check if the user/psw are in the json file
        return list.stream().filter((domain) -> (domain.getId().equals(user))).anyMatch((domain) -> (domain.getPassword().equals(psw)));
    }

    private List<ExportNode> convertDomainToExportNode() {
        //get all Domain
        List<Node> list = JsonHandler.getInstance().readJSON();
        //build new Exportlist of the existen private domain.json
        List<ExportNode> exportlist = new ArrayList<>(list.size());

        list.stream().map((d) -> {
            ExportNode exportnode = new ExportNode();

            //set ID and IP and lastChange TimeStamp
            exportnode.setId(d.getId());
            exportnode.setIp(d.getIp());
            exportnode.setLastChange(d.getLastChange());

            return exportnode;
        }).forEachOrdered(exportlist::add //add to list
        );

        return exportlist;
    }

}
