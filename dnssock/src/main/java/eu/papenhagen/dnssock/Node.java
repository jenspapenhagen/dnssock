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
    private String password;
    @Setter
    @Getter
    private String ip;
    @Setter
    @Getter
    private String lastChange;
   
}
