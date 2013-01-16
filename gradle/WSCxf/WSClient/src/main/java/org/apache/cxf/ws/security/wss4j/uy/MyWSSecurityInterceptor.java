/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apache.cxf.ws.security.wss4j.uy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.soap.SOAPMessage;
import org.apache.cxf.binding.soap.SoapFault;
import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.binding.soap.SoapVersion;
import org.apache.cxf.binding.soap.saaj.SAAJOutInterceptor;
import org.apache.cxf.common.i18n.Message;
import org.apache.cxf.helpers.CastUtils;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.phase.Phase;
import org.apache.cxf.phase.PhaseInterceptor;
import org.apache.cxf.ws.security.wss4j.AbstractWSS4JInterceptor;
import org.apache.ws.security.WSConstants;
import org.apache.ws.security.WSSConfig;
import org.apache.ws.security.WSSecurityException;
import org.apache.ws.security.action.Action;
import org.apache.ws.security.handler.RequestData;
import org.apache.ws.security.handler.WSHandlerConstants;
import org.apache.ws.security.util.WSSecurityUtil;
import org.w3c.dom.Document;

public class MyWSSecurityInterceptor extends AbstractWSS4JInterceptor {

    private static final Logger LOG = Logger.getLogger(MyWSSecurityInterceptor.class.getName());
    private static final Logger TIME_LOG = Logger.getLogger(MyWSSecurityInterceptor.class.getName() + "-Time");
    
    public static final String WSS4J_ACTION_MAP = "wss4j.action.map";    
    private WSS4JOutInterceptorInternal2 ending;
    private SAAJOutInterceptor saajOut = new SAAJOutInterceptor();
    private boolean mtomEnabled;
    private String id_interceptor ;

    public MyWSSecurityInterceptor() {
        super();
        setPhase(Phase.PRE_PROTOCOL);
        getAfter().add(SAAJOutInterceptor.class.getName());
        id_interceptor = MyWSSecurityInterceptor.class.getName()+"ws"+System.currentTimeMillis();
        LOG.info("El valor del id es: "+id_interceptor);
        ending = createEndingInterceptor(id_interceptor);
    }

    public MyWSSecurityInterceptor(Map<String, Object> props) {
        this();
        setProperties(props);
    }

    public boolean isAllowMTOM() {
        return mtomEnabled;
    }

    /**
     * Enable or disable mtom with WS-Security. By default MTOM is disabled as
     * attachments would not get encrypted or be part of the signature.
     *
     * @param mtomEnabled
     */
    public void setAllowMTOM(boolean allowMTOM) {
        this.mtomEnabled = allowMTOM;
    }

    @Override
    public Object getProperty(Object msgContext, String key) {
        // use the superclass first
        Object result = super.getProperty(msgContext, key);

        // handle the special case of the RECV_RESULTS
        if (result == null
                && WSHandlerConstants.RECV_RESULTS.equals(key)
                && !this.isRequestor((SoapMessage) msgContext)) {
            result = ((SoapMessage) msgContext).getExchange().getInMessage().get(key);
        }
        return result;
    }

    @Override
    public String getId() {
        return id_interceptor ; 
    }

    @Override
    public void handleMessage(SoapMessage mc) throws Fault {
        //must turn off mtom when using WS-Sec so binary is inlined so it can
        //be properly signed/encrypted/etc...
        if (!mtomEnabled) {
            mc.put(org.apache.cxf.message.Message.MTOM_ENABLED, false);
        }

        if (mc.getContent(SOAPMessage.class) == null) {
            saajOut.handleMessage(mc);
        }

        mc.getInterceptorChain().add(ending);
    }

    @Override
    public void handleFault(SoapMessage message) {
        saajOut.handleFault(message);
    }

    public final WSS4JOutInterceptorInternal2 createEndingInterceptor(String id_interceptorint) {
        return new WSS4JOutInterceptorInternal2(id_interceptorint);
    }

    final class WSS4JOutInterceptorInternal2 implements PhaseInterceptor<SoapMessage> {

        private String id_interceptorint;
        
        public WSS4JOutInterceptorInternal2(String id_interceptor) {
            super();
            id_interceptorint = id_interceptor+System.currentTimeMillis();
            LOG.info("el id interno es: "+id_interceptorint);
        }

        @Override
        public void handleMessage(SoapMessage mc) throws Fault {

            boolean doDebug = LOG.isLoggable(Level.FINE);
            boolean doTimeDebug = TIME_LOG.isLoggable(Level.FINE);

            long t0 = 0;
            long t1 = 0;
            long t2 = 0;

            if (doTimeDebug) {
                t0 = System.currentTimeMillis();
            }

            if (doDebug) {
                LOG.fine("WSS4JOutInterceptorInternal2: enter handleMessage()");
            }
            /**
             * There is nothing to send...Usually happens when the provider
             * needs to send a HTTP 202 message (with no content)
             */
            if (mc == null) {
                return;
            }
            SoapVersion version = mc.getVersion();
            RequestData reqData = new RequestData();
            translateProperties(mc);

            reqData.setMsgContext(mc);

            /*
             * The overall try, just to have a finally at the end to perform some
             * housekeeping.
             */
            try {
                WSSConfig config = WSSConfig.getNewInstance();
                reqData.setWssConfig(config);

                /*
                 * Setup any custom actions first by processing the input properties
                 * and reconfiguring the WSSConfig with the user defined properties.
                 */
                this.configureActions(mc, doDebug, version, config);

                /*
                 * Get the action first.
                 */
                List<Integer> actions = new ArrayList<Integer>();
                String action = getString(WSHandlerConstants.ACTION, mc);
                if (action == null) {
                    throw new SoapFault(new Message("NO_ACTION", LOG), version
                            .getReceiver());
                }

                int doAction = WSSecurityUtil.decodeAction(action, actions, config);
                if (doAction == WSConstants.NO_SECURITY && actions.isEmpty()) {
                    return;
                }

                /*
                 * For every action we need a username, so get this now. The
                 * username defined in the deployment descriptor takes precedence.
                 */
                reqData.setUsername((String) getOption(WSHandlerConstants.USER));
                if (reqData.getUsername() == null
                        || reqData.getUsername().equals("")) {
                    String username = (String) getProperty(reqData.getMsgContext(),
                            WSHandlerConstants.USER);
                    if (username != null) {
                        reqData.setUsername(username);
                    }
                }

                /*
                 * Now we perform some set-up for UsernameToken and Signature
                 * functions. No need to do it for encryption only. Check if
                 * username is available and then get a passowrd.
                 */
                if ((doAction & (WSConstants.SIGN | WSConstants.UT | WSConstants.UT_SIGN)) != 0
                        && (reqData.getUsername() == null
                        || reqData.getUsername().equals(""))) {
                    /*
                     * We need a username - if none throw an SoapFault. For
                     * encryption there is a specific parameter to get a username.
                     */
                    throw new SoapFault(new Message("NO_USERNAME", LOG), version
                            .getReceiver());
                }
                if (doDebug) {
                    LOG.fine("Action: " + doAction);
                    LOG.fine("Actor: " + reqData.getActor());
                }
                /*
                 * Now get the SOAP part from the request message and convert it
                 * into a Document. This forces CXF to serialize the SOAP request
                 * into FORM_STRING. This string is converted into a document.
                 * During the FORM_STRING serialization CXF performs multi-ref of
                 * complex data types (if requested), generates and inserts
                 * references for attachements and so on. The resulting Document
                 * MUST be the complete and final SOAP request as CXF would send it
                 * over the wire. Therefore this must shall be the last (or only)
                 * handler in a chain. Now we can perform our security operations on
                 * this request.
                 */

                SOAPMessage saaj = mc.getContent(SOAPMessage.class);

                if (saaj == null) {
                    LOG.warning("SAAJOutHandler must be enabled for WS-Security!");
                    throw new SoapFault(new Message("NO_SAAJ_DOC", LOG), version
                            .getReceiver());
                }

                Document doc = saaj.getSOAPPart();

                if (doTimeDebug) {
                    t1 = System.currentTimeMillis();
                }

                doSenderAction(doAction, doc, reqData, actions, Boolean.TRUE
                        .equals(getProperty(mc, org.apache.cxf.message.Message.REQUESTOR_ROLE)));

                if (doTimeDebug) {
                    t2 = System.currentTimeMillis();
                    TIME_LOG.fine("Send request: total= " + (t2 - t0)
                            + " request preparation= " + (t1 - t0)
                            + " request processing= " + (t2 - t1)
                            + "\n");
                }

                if (doDebug) {
                    LOG.fine("WSS4JOutInterceptor: exit handleMessage()");
                }
            } catch (WSSecurityException e) {
                throw new SoapFault(new Message("SECURITY_FAILED", LOG), e, version
                        .getSender());
            } finally {
                reqData.clear();
                reqData = null;
            }
        }

        @Override
        public Set<String> getAfter() {
            return Collections.emptySet();
        }

        @Override
        public Set<String> getBefore() {
            return Collections.emptySet();
        }

        @Override
        public String getId() {
            return id_interceptorint;
        }

        @Override
        public String getPhase() {
            return Phase.POST_PROTOCOL;
        }

        @Override
        public void handleFault(SoapMessage message) {
            //nothing
        }

        private void configureActions(SoapMessage mc, boolean doDebug,
                SoapVersion version, WSSConfig config) {

            final Map<Integer, Object> actionMap = CastUtils.cast(
                    (Map<?, ?>) getProperty(mc, WSS4J_ACTION_MAP));
            if (actionMap != null) {
                for (Map.Entry<Integer, Object> entry : actionMap.entrySet()) {
                    Class<?> removedAction = null;

                    // Be defensive here since the cast above is slightly risky
                    // with the handler config options not being strongly typed.
                    try {
                        if (entry.getValue() instanceof Class<?>) {
                            removedAction = config.setAction(
                                    entry.getKey().intValue(),
                                    (Class<?>) entry.getValue());
                        } else if (entry.getValue() instanceof Action) {
                            removedAction = config.setAction(
                                    entry.getKey().intValue(),
                                    (Action) entry.getValue());
                        } else {
                            throw new SoapFault(new Message("BAD_ACTION", LOG), version
                                    .getReceiver());
                        }
                    } catch (ClassCastException e) {
                        throw new SoapFault(new Message("BAD_ACTION", LOG), version
                                .getReceiver());
                    }

                    if (doDebug) {
                        if (removedAction != null) {
                            LOG.fine("Replaced Action: " + removedAction.getName()
                                    + " with Action: " + entry.getValue()
                                    + " for ID: " + entry.getKey());
                        } else {
                            LOG.fine("Added Action: " + entry.getValue()
                                    + " with ID: " + entry.getKey());
                        }
                    }
                }
            }
        }

        public Collection<PhaseInterceptor<? extends org.apache.cxf.message.Message>> getAdditionalInterceptors() {
            return null;
        }
    }
}
