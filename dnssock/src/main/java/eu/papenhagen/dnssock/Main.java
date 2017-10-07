/**
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.papenhagen.dnssock;

import static spark.Spark.*;
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
        //list get only read once
        List<Node> list = NodeSerivce.getInstance().getAllNodes();

        //threadpool
        int maxThreads = 8;
        int minThreads = 2;
        int timeOutMillis = 30000;
        threadPool(maxThreads, minThreads, timeOutMillis);

        list.forEach(new Consumer<Node>() {
            @Override
            public void accept(Node n) {
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

                    System.out.println("id " + id);
                    System.out.println("psw " + psw);
                    System.out.println("renewIp " + renewIp);

                    if (NodeSerivce.getInstance().checkPassword(id, psw)) {
                        //fill the new ip into domain
                        n.setIp(renewIp);

                        //update the node in the list
                        NodeSerivce.getInstance().setNode(n);

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
                //set none standard Port
                //port(8080);
            }
        });

        System.out.println("http://localhost:4567/test");
    }

}
