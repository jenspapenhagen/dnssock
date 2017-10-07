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
public class ExportDomain {
    @Getter
    @Setter
    private String id;
    
    @Setter
    @Getter
    private Ip ip;
    
    @Setter
    @Getter
    private String lastChange;
}
