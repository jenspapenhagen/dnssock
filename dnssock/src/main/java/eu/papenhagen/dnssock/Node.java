/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.papenhagen.dnssock;

import java.net.InetAddress;

import lombok.*;

/**
 *
 * @author jens.papenhagen
 */
public class Node {
    @Getter
    private String id;
    @Getter
    private String password;
    @Setter
    @Getter
    private InetAddress ip;
    @Setter
    @Getter
    private String lastChange;
   
}
