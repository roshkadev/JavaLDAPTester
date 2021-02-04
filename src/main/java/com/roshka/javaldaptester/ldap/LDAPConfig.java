package com.roshka.javaldaptester.ldap;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class LDAPConfig {

    private String url;
    private String domainName;
    private String principalFormat;
    private String principalFilterFormat;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public String getPrincipalFormat() {
        return principalFormat;
    }

    public void setPrincipalFormat(String principalFormat) {
        this.principalFormat = principalFormat;
    }

    public String getPrincipalFilterFormat() {
        return principalFilterFormat;
    }

    public void setPrincipalFilterFormat(String principalFilterFormat) {
        this.principalFilterFormat = principalFilterFormat;
    }

    public static LDAPConfig loadFromFile(String fileName)
        throws IOException
    {

        Properties props = new Properties();
        props.load(new FileInputStream(fileName));

        LDAPConfig ldapConfig = new LDAPConfig();
        ldapConfig.setUrl(props.getProperty("ldap_url", "ldap://localhost"));
        ldapConfig.setDomainName(props.getProperty("domain_name", "example.com"));
        ldapConfig.setPrincipalFormat(props.getProperty("principal_format"));
        ldapConfig.setPrincipalFilterFormat(props.getProperty("principal_filter_format"));
        return ldapConfig;
    }

    @Override
    public String toString() {
        return "LDAPConfig{" +
                "url='" + url + '\'' +
                ", domainName='" + domainName + '\'' +
                ", principalFormat='" + principalFormat + '\'' +
                ", principalFilterFormat='" + principalFilterFormat + '\'' +
                '}';
    }

}
