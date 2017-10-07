/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.papenhagen.dnssock;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
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
public class JsonHandler {

    //start logging
    private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(JsonHandler.class);

    private static JsonHandler instance;

    private final Gson gson;

    private final String filepath;

    public JsonHandler() {
        this.filepath = "C:\\Go\\domains.json";
        instance = this;
        this.gson = new Gson();
    }

    public static JsonHandler getInstance() {
        return instance;
    }

    /**
     * read the JSON
     *
     * @return the JSON as List of Domain //TODO doamins.json from resoruce
     * haveto copyed to a good place
     */
    public List<Domain> readJSON() {
        String fileinput = "";
        try {
            fileinput = new String(Files.readAllBytes(Paths.get(filepath)));
        } catch (IOException ex) {
            LOG.error(ex.getMessage());
        }
        List<Domain> list = new ArrayList<>();
        list = parseLocalDomainJsonFromFile(fileinput);

        return list;
    }

    /**
     * Save the given domain into the private file
     *
     * @param dom
     */
    public void saveJSON(Domain dom) {
        //get all Domain
        List<Domain> list = readJSON();
        //add given one
        list.stream().filter((domain) -> (domain.getId().equals(dom.getId()))).forEachOrdered((domain) -> {
            LocalDateTime lastChange = LocalDateTime.now();

            //change IP and lastChange timestamp
            domain.setIp(dom.getIp());
            domain.setLastChange(lastChange.toString());
        });

        String output = gson.toJson(list);

        File file = new File(filepath);
        try {
            //delete old file
            file.createNewFile();
            
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(output.getBytes());
            
            //move all to file befor close it
            fos.flush();
            fos.close();
        } catch (IOException ex) {
            LOG.error(ex.getMessage());
        }

    }

    /**
     * export the JSON
     *
     * @param list
     * @return
     */
    public String exportToJSON(List<ExportDomain> list) {
        String output = gson.toJson(list);

        return output;
    }

    /**
     * parsing the JSON file into a List of Domain
     *
     * @param fileinput
     * @return
     */
    private List<Domain> parseLocalDomainJsonFromFile(String fileinput) {
        Type founderListType = new TypeToken<ArrayList<Domain>>() {
        }.getType();
        List<Domain> founderList = gson.fromJson(fileinput, founderListType);

        return founderList;
    }

}
