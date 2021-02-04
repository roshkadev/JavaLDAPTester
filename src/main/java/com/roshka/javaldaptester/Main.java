package com.roshka.javaldaptester;

import com.roshka.javaldaptester.ldap.LDAPConfig;
import com.roshka.javaldaptester.ldap.LDAPTesterException;
import com.roshka.javaldaptester.ldap.LDAPUser;
import com.roshka.javaldaptester.ldap.LDAPUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class Main {

    private static Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {

        if (args.length < 3) {
            printUsage();
            System.exit(-1);
        }

        String propertiesFileName = args[0];
        String user = args[1];
        String password = args[2];

        logger.info("Welcome to JavaLDAPTester. Going to lead configuration.");

        logger.info("Loading properties file: " + propertiesFileName);

        try {
            LDAPConfig ldapConfig = LDAPConfig.loadFromFile(propertiesFileName);
            logger.info(String.format("Testing with user [%s] and password [%s]", user, password));
            final LDAPUser ldapUser = LDAPUtil.authenticate(ldapConfig, user, password);

            if (ldapUser != null) {
                logger.info(String.format("User [%s] authenticated correctly - Principal [%s] [%s]", ldapUser.getName(), ldapUser.getPrincipal(), ldapUser.getNameInNamespace()));
                logger.info("Groups:");
                if (ldapUser.getGroups() != null) {
                    for (String group : ldapUser.getGroups()) {
                        logger.info(group);
                    }
                }
            } else {
                logger.info("Can't authenticate user. Invalid Password?");
            }

            logger.info("=--=-==--=-==--==-=-");

        } catch (IOException e) {
            System.err.println("Can't read properties file: " + e.getMessage());
        } catch (LDAPTesterException e) {
            System.err.println("Can't test LDAP USER/PASSWORD: " + e.getMessage());

        }

    }

    private static void printUsage() {
        System.out.println("Usage: java -jar ldap-tester.java ldap.properties user password");
    }

}
