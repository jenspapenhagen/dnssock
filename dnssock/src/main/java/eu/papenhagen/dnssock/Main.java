/**
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.papenhagen.dnssock;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;
import spark.Spark;

/**
 *
 * @author jens.papenhagen
 */
public class Main {

    //start logging
    private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(Main.class);

    /**
     * simble build of a dnyDNS service as mircoservice
     *
     * @param args
     */
    public static void main(String[] args) {
        //list get only read once
        List<Node> listOfNodes = NodeSerivce.getInstance().getAllNodes();
        List<String> listOfNodesIDs = new ArrayList<>(listOfNodes.size());

        //fill listOfNodesIDs
        listOfNodes.forEach((node) -> {
            listOfNodesIDs.add(node.getId());
        });
        
        //get all Node and IPs
        Spark.get("/all", (Request request, Response response) -> {
            response.type("application/json");
            return JsonHandler.getInstance().exportToJSON(NodeSerivce.getInstance().convertNodeToExportNode());
        });

        //get singel IP for one node
        Spark.get("/:id", (Request request, Response response) -> {
            String id = request.params(":id");
            if (listOfNodesIDs.contains(id)) {
                Node tempNode = NodeSerivce.getInstance().getNode(id);
                return tempNode.getIpaddress();
            } else {
                LOG.debug("Node not Found");
                return "Node not Found";
            }

        });


        //change the ip
        Spark.get("/:id" + "/:token", (Request request, Response response) -> {
            String id = request.params(":id");
            String token = request.params(":token");
            String renewIp = request.ip();
            boolean nodeexist = false;

            LOG.debug("id " + id);
            LOG.debug("token " + token);
            LOG.debug("renewIp " + renewIp);
            
            //check node exist
            if (listOfNodesIDs.contains(id)) {
                nodeexist = true;
            } else {
                LOG.debug("Node not Found");
                return "Node not Found";
            }


            if (nodeexist && NodeSerivce.getInstance().checkToken(id, token)) {
                //fill the new ip into domain
                Node tempNode = NodeSerivce.getInstance().getNode(id);
                tempNode.setIpaddress(renewIp);

                //update the node in the list
                NodeSerivce.getInstance().setNode(tempNode);

                //return the new ip
                return renewIp;
            }

            response.status(400);
            LOG.debug("wrong Token");
            
            return "wrong Token";

        });
        //e404
        Spark.get("/", (req, res) -> "no node set");

        //set HTTPS
//        Spark.secure(keystoreFilePath, keystorePassword, truststoreFilePath, truststorePassword);
        //set none standard Port 4567
//        Spark.port(8080);
        System.out.println("http://localhost:4567/test");

    }

}
