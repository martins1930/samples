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

public class ServerPasswordCallback implements CallbackHandler {

    private Map<String, String> passwords = new HashMap<String, String>();

    public ServerPasswordCallback() {
        passwords.put("Alice", "ecilA");
        passwords.put("pepe", "admin");
        passwords.put("abcd", "dcba");
        passwords.put("clientx509v1", "storepassword");
        passwords.put("serverx509v1", "storepassword");
    }

    @Override
    public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {

        for (int i = 0; i < callbacks.length; i++) {
            WSPasswordCallback pc = (WSPasswordCallback) callbacks[i];

            String pass = passwords.get(pc.getIdentifier());
            if (pass != null) {
                pc.setPassword(pass);
                return;
            }
            else {
                pc.setPassword("noexiste");
            }
        }
        
    }
}
