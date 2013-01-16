/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uy.edu.fing.pgrad.pge.opensaml;

import org.opensaml.common.impl.AbstractSAMLObjectBuilder;
import org.opensaml.common.xml.SAMLConstants;
import org.opensaml.saml2.core.AttributeValue;

public class AttributeValueBuilder  extends AbstractSAMLObjectBuilder<AttributeValue> {

    public AttributeValueBuilder() {
    }
    

    @Override
    public AttributeValue buildObject() {
        return buildObject(SAMLConstants.SAML20_NS, AttributeValue.DEFAULT_ELEMENT_LOCAL_NAME, SAMLConstants.SAML20_PREFIX);        
    }

    @Override
    public AttributeValue buildObject(String namespaceURI, String localName, String namespacePrefix) {
        return buildObject(namespaceURI, localName, namespacePrefix);        
    }
    
}
