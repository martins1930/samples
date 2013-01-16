/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uy.edu.fing.pgrad.pge.saml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509CRL;
import java.util.ArrayList;
import java.util.Collection;
import javax.xml.namespace.QName;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.opensaml.DefaultBootstrap;
import org.opensaml.common.SAMLObjectBuilder;
import org.opensaml.common.xml.SAMLConstants;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.saml2.core.Attribute;
import org.opensaml.saml2.core.AttributeStatement;
import org.opensaml.saml2.core.AttributeValue;
import org.opensaml.saml2.core.Audience;
import org.opensaml.saml2.core.AudienceRestriction;
import org.opensaml.saml2.core.Conditions;
import org.opensaml.saml2.core.Issuer;
import org.opensaml.saml2.core.NameID;
import org.opensaml.saml2.core.Subject;
import org.opensaml.saml2.core.SubjectConfirmation;
import org.opensaml.saml2.core.SubjectConfirmationData;
import org.opensaml.saml2.core.impl.AttributeBuilder;
import org.opensaml.saml2.core.impl.AttributeStatementBuilder;
import org.opensaml.saml2.core.impl.AudienceBuilder;
import org.opensaml.saml2.core.impl.AudienceRestrictionBuilder;
import org.opensaml.saml2.core.impl.ConditionsBuilder;
import org.opensaml.saml2.core.impl.IssuerBuilder;
import org.opensaml.saml2.core.impl.NameIDBuilder;
import org.opensaml.saml2.core.impl.SubjectBuilder;
import org.opensaml.saml2.core.impl.SubjectConfirmationBuilder;
import org.opensaml.saml2.core.impl.SubjectConfirmationDataBuilder;
import org.opensaml.samlext.saml2delrestrict.Delegate;
import org.opensaml.samlext.saml2delrestrict.DelegationRestrictionType;
import org.opensaml.samlext.saml2delrestrict.impl.DelegateBuilder;
import org.opensaml.samlext.saml2delrestrict.impl.DelegationRestrictionTypeBuilder;
import org.opensaml.xml.Configuration;
import org.opensaml.xml.ConfigurationException;
import org.opensaml.xml.XMLObjectBuilder;
import org.opensaml.xml.XMLObjectBuilderFactory;
import org.opensaml.xml.io.Marshaller;
import org.opensaml.xml.io.MarshallerFactory;
import org.opensaml.xml.io.MarshallingException;
import org.opensaml.xml.schema.XSAny;
import org.opensaml.xml.security.credential.Credential;
import org.opensaml.xml.security.keyinfo.KeyInfoHelper;
import org.opensaml.xml.security.x509.BasicX509Credential;
import org.opensaml.xml.signature.KeyInfo;
import org.opensaml.xml.signature.Signature;
import org.opensaml.xml.signature.SignatureConstants;
import org.opensaml.xml.signature.SignatureException;
import org.opensaml.xml.signature.Signer;
import org.opensaml.xml.signature.impl.KeyInfoBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

public class MakeSAML {

    private static final Logger log = LoggerFactory.getLogger(MakeSAML.class);

    public static void main(String[] args) throws ConfigurationException, MarshallingException, TransformerConfigurationException, TransformerException, KeyStoreException, NoSuchAlgorithmException, NoSuchAlgorithmException, CertificateException, IOException, IOException, UnrecoverableKeyException, SignatureException {
        log.info("se ejecuta makeSAML");

        DefaultBootstrap.bootstrap();

        // Get the builder factory
        XMLObjectBuilderFactory builderFactory = Configuration.getBuilderFactory();

        // Get the assertion builder based on the assertion element name
        SAMLObjectBuilder<Assertion> builder = (SAMLObjectBuilder<Assertion>) builderFactory.getBuilder(Assertion.DEFAULT_ELEMENT_NAME);


        // Create the assertion
        if (builder != null) {
            Assertion assertion = builder.buildObject();
            log.info("se obtuvo la assertion");
            
            // Issuer ----------------------------------------------------------
            IssuerBuilder issuerBuild = (IssuerBuilder) builderFactory.getBuilder(Issuer.DEFAULT_ELEMENT_NAME);
            Issuer iss = issuerBuild.buildObject();
            iss.setValue("urn:agesic:sts");
            
            assertion.setIssuer(iss);
            
            // Subject ---------------------------------------------------------
            SubjectBuilder subjectBuild = (SubjectBuilder) builderFactory.getBuilder(Subject.DEFAULT_ELEMENT_NAME);
            Subject subj = subjectBuild.buildObject();
            //NameID --------
            NameIDBuilder nameBuild = (NameIDBuilder) builderFactory.getBuilder(NameID.DEFAULT_ELEMENT_NAME);
            NameID nameIdSubj = nameBuild.buildObject();
            nameIdSubj.setValue("CN=rolDoctor,O=BPS");
            subj.setNameID(nameIdSubj);
            // SubjectConfirmation -----
            SubjectConfirmationBuilder subjconfBuild = (SubjectConfirmationBuilder) builderFactory.getBuilder(SubjectConfirmation.DEFAULT_ELEMENT_NAME);
            SubjectConfirmation sconf = subjconfBuild.buildObject();
            sconf.setMethod("urn:oasis:names:tc:SAML:2.0:cm:bearer");
            NameID nameIdConfInterm = nameBuild.buildObject();
            nameIdConfInterm.setValue("urn:agesic:intermediarioProcesos");
            nameIdConfInterm.setFormat("urn:oasis:names:tc:SAML:2.0:nameid-format:entity");
            sconf.setNameID(nameIdConfInterm);
            //SubjectConfirmation-->SubjectConfirmationData
            SubjectConfirmationDataBuilder scdBuild = (SubjectConfirmationDataBuilder) builderFactory.getBuilder(SubjectConfirmationData.DEFAULT_ELEMENT_NAME);
            SubjectConfirmationData scdInst = scdBuild.buildObject();
            scdInst.setAddress("192.168.10.10");
            sconf.setSubjectConfirmationData(scdInst);
            subj.getSubjectConfirmations().add(sconf);
            
            assertion.setSubject(subj);
            
            
            // Conditions ------------------------------------------------------
            ConditionsBuilder condsBuild = (ConditionsBuilder) builderFactory.getBuilder(Conditions.DEFAULT_ELEMENT_NAME);
            Conditions conditions = condsBuild.buildObject();
            //AudienceRestriction ---------------------------------------------
            AudienceRestrictionBuilder audRestBuilder = (AudienceRestrictionBuilder) builderFactory.getBuilder(AudienceRestriction.DEFAULT_ELEMENT_NAME);
            AudienceRestriction ar = audRestBuilder.buildObject();
            //Audience --------------------------------------------------------
            AudienceBuilder audBuild = (AudienceBuilder) builderFactory.getBuilder(Audience.DEFAULT_ELEMENT_NAME);
            Audience aud1 = audBuild.buildObject();
            Audience aud2 = audBuild.buildObject();
            Audience aud3 = audBuild.buildObject();
            aud1.setAudienceURI("http://pge/ServicioA");
            aud2.setAudienceURI("http://pge/ServicioB");
            aud3.setAudienceURI("http://pge/ServicioC");
            ar.getAudiences().add(aud1);
            ar.getAudiences().add(aud2);
            ar.getAudiences().add(aud3);
            conditions.getAudienceRestrictions().add(ar);
            //Condition Delegation -------------------------------------------
            DelegationRestrictionTypeBuilder delegRestrBuild = new DelegationRestrictionTypeBuilder();
            DelegationRestrictionType delegRestriction = delegRestrBuild.buildObject();
            DelegateBuilder delegBuild = (DelegateBuilder) builderFactory.getBuilder(Delegate.DEFAULT_ELEMENT_NAME);
            Delegate deleg1 = delegBuild.buildObject();
            NameID nameDeleg = nameBuild.buildObject();
            nameDeleg.setValue("urn:agesic:intermediarioProcesos");
            nameDeleg.setFormat("urn:oasis:names:tc:SAML:2.0:nameid-format:entity");
            deleg1.setNameID(nameDeleg);
            delegRestriction.getDelegates().add(deleg1);
            conditions.getConditions().add(delegRestriction);
            
            assertion.setConditions(conditions);
            
            
            //AttributeStatement------------------------------------------------
            AttributeStatementBuilder attrStatBuild = (AttributeStatementBuilder) builderFactory.getBuilder(AttributeStatement.DEFAULT_ELEMENT_NAME);
            AttributeStatement attrStatement = attrStatBuild.buildObject();
            //Attribute
            AttributeBuilder attrBuild = (AttributeBuilder) builderFactory.getBuilder(Attribute.DEFAULT_ELEMENT_NAME);
            Attribute attr1 = attrBuild.buildObject();
            attr1.setName("User");
            
//            org.opensaml.saml1.core.Attribute sd1 = null ;
//            sd1.set
            //AttributeValue
            XMLObjectBuilder<XSAny> xsAnyBuilder = builderFactory.getBuilder(XSAny.TYPE_NAME);
            XSAny attrValue = xsAnyBuilder.buildObject(SAMLConstants.SAML20_NS, AttributeValue.DEFAULT_ELEMENT_LOCAL_NAME, SAMLConstants.SAML20_PREFIX);
            attrValue.setTextContent("JuanPedro");
            attrValue.getUnknownAttributes().put(new QName("http://www.w3.org/2001/XMLSchema-instance", "type", "xsi"), "xs:string");
            attr1.getAttributeValues().add(attrValue);
            attrStatement.getAttributes().add(attr1);
            
            //Attribute con policy name
            Attribute attrPolicy = attrBuild.buildObject();
            attrPolicy.setName("PolicyName");
            XSAny attrValuePolicy = xsAnyBuilder.buildObject(SAMLConstants.SAML20_NS, AttributeValue.DEFAULT_ELEMENT_LOCAL_NAME, SAMLConstants.SAML20_PREFIX);
            attrValuePolicy.setTextContent("urn:agesic:intermediarioProcesos");
            attrValuePolicy.getUnknownAttributes().put(new QName("http://www.w3.org/2001/XMLSchema-instance", "type", "xsi"), "xs:string");
            attrPolicy.getAttributeValues().add(attrValuePolicy);
            attrStatement.getAttributes().add(attrPolicy);

            
            assertion.getAttributeStatements().add(attrStatement);
            //Signature -------------------------------------------------------
            Signature signature = (Signature) builderFactory.getBuilder(Signature.DEFAULT_ELEMENT_NAME)
                                            .buildObject(Signature.DEFAULT_ELEMENT_NAME);
            
            Credential credential = getCredential();
            signature.setSigningCredential(credential);
            signature.setSignatureAlgorithm(SignatureConstants.ALGO_ID_SIGNATURE_RSA_SHA1);
            signature.setCanonicalizationAlgorithm(SignatureConstants.ALGO_ID_C14N_EXCL_OMIT_COMMENTS);
            
            KeyInfoBuilder keyinfoBuilder = (KeyInfoBuilder) builderFactory.getBuilder(KeyInfo.DEFAULT_ELEMENT_NAME);
            KeyInfo keyInfo = (KeyInfo) keyinfoBuilder.buildObject(KeyInfo.DEFAULT_ELEMENT_NAME);
            KeyInfoHelper.addCertificate(keyInfo, ((BasicX509Credential)credential).getEntityCertificate() );
            
            signature.setKeyInfo(keyInfo);
            
            assertion.setSignature(signature);
            
            Element assertXml = assertionToElement(assertion);
            
            Signer.signObject(signature);
            
            log.info("assertion: {}", transformElement(assertXml));


        }

    }

    private static Element assertionToElement(Assertion assertion) throws MarshallingException {
        MarshallerFactory mf = Configuration.getMarshallerFactory();
        Marshaller marshaller = mf.getMarshaller(assertion);
        Element assertXml = marshaller.marshall(assertion);
        return assertXml;
    }

    private static String transformElement(Element e) throws TransformerConfigurationException, TransformerException {
        TransformerFactory transFactory = TransformerFactory.newInstance();
        Transformer transformer = transFactory.newTransformer();
        StringWriter buffer = new StringWriter();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        transformer.transform(new DOMSource(e), new StreamResult(buffer));
        String str = buffer.toString();
        return str;
    }
    
    
//    public static Credential getCredential(String keyStorePwd, String keyStoreFilePath, String alias) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException,UnrecoverableKeyException {
    public static Credential getCredential() throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException,UnrecoverableKeyException {

        String keyStorePwd = "changeit";
        String keyStoreFilePath = "/etc/mystsconfig/stskeystore1";
        String alias = "stskey1"; 
//        if (staticCredential!=null && x509Certificate!=null) return staticCredential; //e
       
        File keyStoreFile = new File(keyStoreFilePath);
        FileInputStream keyStoreFis;

        keyStoreFis = new FileInputStream(keyStoreFile);
        KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(keyStoreFis, keyStorePwd.toCharArray());
        java.security.cert.X509Certificate x509Certificate = (java.security.cert.X509Certificate) keyStore.getCertificate(alias);
        java.security.Key key = keyStore.getKey(alias, keyStorePwd.toCharArray());

        BasicX509Credential credential = new BasicX509Credential();
        credential.setEntityCertificate(x509Certificate);

        // TODO Ver que pasa con los certificados revocados!
        Collection<java.security.cert.X509CRL> crls = new ArrayList<X509CRL>();
        credential.setCRLs(crls);
        credential.setPrivateKey((PrivateKey) key);
        credential.setPublicKey(x509Certificate.getPublicKey());
        credential.getKeyNames().add(alias);

        return credential;

    }    
    
    
}
