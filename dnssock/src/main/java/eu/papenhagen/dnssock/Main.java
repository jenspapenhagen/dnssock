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
import java.net.InetAddress;
import java.net.Inet6Address;

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
                String ipAsString = n.getIp().getHostAddress();

                //get singel IP for one node
                get("/" + n.getId(), (Request request, Response response) -> "" + ipAsString);

                //get all Node and IPs
                get("/all", (Request request, Response response) -> {
                    response.type("application/json");
                    return JsonHandler.getInstance().exportToJSON(convertDomainToExportNode());
                });

                //change the ip
                get("/:" + n.getId() + "/:psw", (Request request, Response response) -> {
                    String id = request.params(":" + n.getId());
                    String psw = request.params(":psw");
                    String renewIp = request.ip();

                    System.out.println("id " + id);
                    System.out.println("psw " + psw);
                    System.out.println("renewIp " + renewIp);

                    if (checkPsw(id, psw)) {
                        //fill the new ip into domain
                        if (renewIp.contains(":")) {
                            n.setIp(Inet6Address.getByName(renewIp));
                        } else {
                            n.setIp(InetAddress.getByName(renewIp));
                        }

                        //save List of Domain to file
                        JsonHandler.getInstance().saveJSON(n);

                        //return the new ip
                        return renewIp;
                    }

                    response.status(400);
                    LOG.debug("No user with psw found");
                    return "No user with psw found";

                });
                //e404
                get("/", (req, res) -> "0.0.0.0");

                //set HTTPS
                //secure(keystoreFilePath, keystorePassword, truststoreFilePath, truststorePassword);
            }
        });

        System.out.println("http://localhost:4567/test");
    }

    private static Boolean checkPsw(String id, String psw) {
        List<Node> list = JsonHandler.getInstance().readJSON();
        //check if the user/psw are in the json file
        return list.stream().filter((Node node) -> (node.getId().equals(id))).anyMatch((Node node) -> (node.getPassword().equals(psw)));
    }

    private static List<ExportNode> convertDomainToExportNode() {
        //get all Domain
        List<Node> list = JsonHandler.getInstance().readJSON();
        //build new Exportlist of the existen private domain.json
        List<ExportNode> exportlist = new ArrayList<>(list.size());

        list.stream().map((Node node) -> {
            ExportNode exportnode = new ExportNode();

            //set ID and IP and lastChange TimeStamp
            exportnode.setId(node.getId());
            exportnode.setIp(node.getIp().getHostAddress());
            exportnode.setLastChange(node.getLastChange());

            return exportnode;
        }).forEachOrdered(exportlist::add //add to list
        );

        return exportlist;
    }

}
