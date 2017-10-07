/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.papenhagen.dnssock;

import java.time.LocalDateTime;
import lombok.*;

/**
 *
 * @author jens.papenhagen
 */
public class Domain {
    @Getter
    private String id;
    @Getter
    private String password;
    @Setter
    @Getter
    private Ip ip;
    @Setter
    @Getter
    private String lastChange;
    //private LocalDateTime lastChange;
    
   
}
