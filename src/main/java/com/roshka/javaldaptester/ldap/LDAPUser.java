package com.roshka.javaldaptester.ldap;

import java.util.ArrayList;
import java.util.List;

public class LDAPUser {

    private String principal;
    private String name;
    private String nameInNamespace;
    private List<String> groups;

    public LDAPUser()
    {
        groups = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameInNamespace() {
        return nameInNamespace;
    }

    public void setNameInNamespace(String nameInNamespace) {
        this.nameInNamespace = nameInNamespace;
    }

    public List<String> getGroups() {
        return groups;
    }

    public void setGroups(List<String> groups) {
        this.groups = groups;
    }

    public void addMemberOf(String group) {
        this.groups.add(group);
    }

    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }
}
