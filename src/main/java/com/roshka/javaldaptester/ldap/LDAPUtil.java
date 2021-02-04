package com.roshka.javaldaptester.ldap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.AuthenticationException;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import java.util.Properties;

public class LDAPUtil {
    
    private static Logger logger = LoggerFactory.getLogger(LDAPUtil.class);

    public static LDAPUser authenticate(LDAPConfig ldapConfig, String principalName, String password)
            throws LDAPTesterException
    {

        if (ldapConfig == null)
            throw new LDAPTesterException(Errors.ERROR_INVALID_CONFIG, "Config can't be null");


        String domainName = ldapConfig.getDomainName();

        if (domainName==null || "".equals(domainName)) {
            int delim = principalName.indexOf('@');
            domainName = principalName.substring(delim+1);
        }
        
        LDAPUser ldapUser = null;

        String url = ldapConfig.getUrl();

        String principal = String.format(ldapConfig.getPrincipalFormat(), principalName);

        Properties props = new Properties();
        props.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        props.put(Context.PROVIDER_URL, url);
        props.put(Context.SECURITY_PRINCIPAL, principal);
        props.put(Context.SECURITY_CREDENTIALS, password); // secretpwd
        if (url.toUpperCase().startsWith("LDAPS://")) {
            throw new LDAPTesterException(Errors.ERROR_UNSUPPORTED, "LDAPS not supported just yet.");
        }

        InitialDirContext context = null;
        try {
            context = new InitialDirContext(props);
            // if it passes, we're authenticated
            ldapUser = new LDAPUser();
            ldapUser.setPrincipal(principal);

            SearchControls ctrls = new SearchControls();
            ctrls.setSearchScope(SearchControls.SUBTREE_SCOPE);
            String filter = String.format(ldapConfig.getPrincipalFilterFormat(), principalName);
            NamingEnumeration<SearchResult> results = context.search(toDC(domainName), filter, ctrls);
            if(results.hasMore()) {
                SearchResult result = results.next();
                logger.debug("Got user -> " + result.getName());
                ldapUser.setName(result.getName());
                ldapUser.setNameInNamespace(result.getNameInNamespace());
                Attribute memberOf = result.getAttributes().get("memberOf");
                if (memberOf != null) {
                    for (int idx = 0; idx < memberOf.size(); idx++) {
                        ldapUser.addMemberOf(memberOf.get(idx).toString());
                    }
                }
            }
        } catch (AuthenticationException e) {
            logger.error("Invalid password for user: " + principalName + " (" + principal + ")");
            return null;
        } catch (NamingException e) {
            logger.error("Can't initialize LDAP Context", e);
            throw new LDAPTesterException(Errors.ERROR_LDAP, "LDAP is not properly configured. Exception: " + e.getMessage() + "/" + e.getExplanation(), e);
        } finally {
            try { context.close(); } catch(Exception ex) {
                logger.debug("Can't close context: " + ex.getMessage());
            }
        }

        return ldapUser;

    }

    /**
     * Create "DC=dev,DC=roshka,DC=com" string
     * @param domainName dev.roshka.com
     * @return
     */
    public static String toDC(String domainName) {
        StringBuilder buf = new StringBuilder();
        for (String token : domainName.split("\\.")) {
            if(token.length()==0) continue;
            if(buf.length()>0)  buf.append(",");
            buf.append("DC=").append(token);
        }
        return buf.toString();
    }

}
