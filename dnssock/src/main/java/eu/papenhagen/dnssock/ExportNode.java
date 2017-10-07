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
public class ExportNode {
    @Getter
    @Setter
    private String id;
    
    @Getter
    @Setter
    private String ip;

    @Getter
    @Setter
    private String lastChange;
}
