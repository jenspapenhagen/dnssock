/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.papenhagen.dnssock;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jens.papenhagen
 */
public class NodeSerivce {

    private static NodeSerivce instance;

    public NodeSerivce() {
        instance = this;
    }

    public static NodeSerivce getInstance() {
        return instance;
    }

    public static List<Node> getAllNodes() {
        return JsonHandler.getInstance().readJSON();
    }

    public static Node getNode(String id) {
        List<Node> list = getAllNodes();
        Node output = null;
        for (Node node : list) {
            if (node.getId().equals(id)) {
                return node;
            }
        }

        return output;
    }

    public static void setNode(Node n) {
        JsonHandler.getInstance().saveJSON(n);
    }

    /**
     * check if the user/psw are in the json file
     *
     * @param id
     * @param psw
     * @return
     */
    public static Boolean checkPassword(String id, String psw) {
        Node node = getNode(id);
        return node.getPassword().equals(psw);
    }

    /**
     * convert the Node to a exportable Node (without so many data)
     *
     * @return
     */
    public static List<ExportNode> convertNodeToExportNode() {
        //get all Domain
        List<Node> list = getAllNodes();
        //build new Exportlist of the existen private domain.json
        List<ExportNode> exportlist = new ArrayList<>(list.size());

        list.stream().map((Node node) -> {
            ExportNode exportnode = new ExportNode();

            //set ID and IP and lastChange TimeStamp
            exportnode.setId(node.getId());
            exportnode.setIp(node.getIp());
            exportnode.setLastChange(node.getLastChange());

            return exportnode;
        }).forEachOrdered(exportlist::add //add to list
        );

        return exportlist;
    }

    /**
     * parsing the JSON file into a List of Domain
     *
     * @param fileinput
     * @return
     */
    public static List<Node> parseLocalDomainJsonFromFile(String fileinput) {
        Gson gson = new Gson();
        Type founderListType = new TypeToken<ArrayList<Node>>() {
        }.getType();
        List<Node> founderList = gson.fromJson(fileinput, founderListType);

        return founderList;
    }

}
