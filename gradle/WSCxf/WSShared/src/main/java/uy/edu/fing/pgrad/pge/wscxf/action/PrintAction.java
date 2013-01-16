/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uy.edu.fing.pgrad.pge.wscxf.action;

import org.apache.ws.security.WSSecurityException;
import org.apache.ws.security.action.Action;
import org.apache.ws.security.handler.RequestData;
import org.apache.ws.security.handler.WSHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

public class PrintAction implements Action{

    private static final Logger log = LoggerFactory.getLogger(PrintAction.class);
    
    @Override
    public void execute(WSHandler handler, int actionToDo, Document doc, RequestData reqData) throws WSSecurityException {
        log.info("********************se ejecuto la accion!!!!!!");
    }
    
}
