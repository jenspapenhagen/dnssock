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
public class Ip {
    @Getter
    @Setter
    private int ip0;
    @Getter
    @Setter
    private int ip1;
    @Getter
    @Setter
    private int ip2;
    @Getter
    @Setter
    private int ip3;
}
