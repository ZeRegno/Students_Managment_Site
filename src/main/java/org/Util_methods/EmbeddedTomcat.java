package org.Util_methods;

import org.apache.catalina.startup.Tomcat;

public class EmbeddedTomcat {
    public static void main(String[] args) throws Exception {
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8080);
        tomcat.getHost().setAppBase("webapps");

        tomcat.addWebapp("/", "src/main/webapp");

        tomcat.start();

        tomcat.getServer().await();
    }
}

