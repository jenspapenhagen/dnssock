/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.papenhagen.dnssock;

import lombok.*;

/**
 *
 * @author jens.papenhagen
 */
public class Node {

    @Getter
    private String id;
    @Getter
    private String token;
    @Setter
    @Getter
    private String ipaddress;
    @Setter
    @Getter
    private String lastChange;

    public Node() {
    }

    public Node(String id, String tk) {
        this.id = id;
        this.token = tk;
    }

}
