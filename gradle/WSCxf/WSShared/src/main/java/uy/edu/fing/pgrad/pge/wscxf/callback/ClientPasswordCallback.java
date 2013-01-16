/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uy.edu.fing.pgrad.pge.wscxf.callback;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import org.apache.ws.security.WSPasswordCallback;
import org.apache.ws.security.saml.ext.SAMLCallback;
import org.apache.ws.security.saml.ext.bean.SubjectBean;
import org.opensaml.common.SAMLVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientPasswordCallback implements CallbackHandler {

    private static final Logger log = LoggerFactory.getLogger(ClientPasswordCallback.class);
    
    private Map<String, String> passwords = new HashMap<String, String>();

    public ClientPasswordCallback() {
        passwords.put("Alice", "ecilA");
        passwords.put("pepe", "admin");
        passwords.put("abcd", "dcba");
        passwords.put("clientx509v1", "storepassword");
        passwords.put("serverx509v1", "storepassword");
    }

    @Override
    public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {

        for (int i = 0; i < callbacks.length; i++) {
            log.info("se inspecciona el tipo de callback");
            Callback citer = callbacks[i];
            if (citer instanceof WSPasswordCallback) {
                WSPasswordCallback pc = (WSPasswordCallback) citer;

                String pass = passwords.get(pc.getIdentifier());
                if (pass != null) {
                    pc.setPassword(pass);
                    return;
                }
                else {
                    pc.setPassword("noexiste");
                }
            }
            else if (citer instanceof SAMLCallback) {
                log.info("se ejecuta callback saml");
                SAMLCallback sc = (SAMLCallback) citer;
                sc.setSamlVersion(SAMLVersion.VERSION_20);
                sc.setIssuer("urn:agesic:sts");

                SubjectBean subjb = new SubjectBean();
                sc.setSubject(subjb);
                subjb.setSubjectName("pepe");
                subjb.setSubjectConfirmationMethod("urn:oasis:names:tc:SAML:2.0:cm:bearer");
            }
            
        }
        
    }
}
