/**
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.papenhagen.dnssock;

import java.util.List;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;
import static spark.Spark.get;

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
        List<Node> list = NodeSerivce.getInstance().getAllNodes();
        list.forEach((Node n) -> {
            //get singel IP for one node
            get("/" + n.getId(), (Request request, Response response) -> "" + n.getIp());

            //get all Node and IPs
            get("/all", (Request request, Response response) -> {
                response.type("application/json");
                return JsonHandler.getInstance().exportToJSON(NodeSerivce.getInstance().convertNodeToExportNode());
            });

            //change the ip
            get("/:" + n.getId() + "/:psw", (Request request, Response response) -> {
                String id = request.params(":" + n.getId());
                String psw = request.params(":psw");
                String renewIp = request.ip();

                LOG.debug("id " + id);
                LOG.debug("psw " + psw);
                LOG.debug("renewIp " + renewIp);

                if (NodeSerivce.getInstance().checkPassword(id, psw)) {
                    //fill the new ip into domain
                    Node tempNode = NodeSerivce.getInstance().getNode(id);
                    tempNode.setIp(renewIp);

                    //update the node in the list
                    NodeSerivce.getInstance().setNode(tempNode);

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
            //set none standard Port 4567
            //port(8080);
        });

        System.out.println("http://localhost:4567/test");

    }

}
