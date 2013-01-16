/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uy.edu.fing.pgrad.pge.opensaml;

import org.opensaml.common.impl.AbstractSAMLObjectBuilder;
import org.opensaml.common.xml.SAMLConstants;
import org.opensaml.saml2.core.Condition;


public class ConditionBuilder extends AbstractSAMLObjectBuilder<Condition>  {

    public ConditionBuilder() {
    }

    
    @Override
    public Condition buildObject() {
        return buildObject(SAMLConstants.SAML20_NS, Condition.DEFAULT_ELEMENT_LOCAL_NAME, SAMLConstants.SAML20_PREFIX);
    }

    @Override
    public Condition buildObject(String namespaceURI, String localName, String namespacePrefix) {
        throw new UnsupportedOperationException("Not supported yet.");
//        return new ConditionImpl(namespaceURI, localName, namespacePrefix);        
    }
    
}
