package br.elibrary.client;

import java.util.Properties;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class JNDIUtil {

    public static Context getInitialContext() throws NamingException {
        Properties p = new Properties();
        p.put(Context.INITIAL_CONTEXT_FACTORY, "org.wildfly.naming.client.WildFlyInitialContextFactory");
        p.put(Context.PROVIDER_URL, "http-remoting://localhost:8080");
        
        
        return new InitialContext(p);
    }

    public static Object lookup(Context context, String beanName, String interfaceName) throws NamingException {
        
        String nomeJNDI = "ejb:ELibraryEAR/ELibrary/" + beanName + "!" + interfaceName;
        return context.lookup(nomeJNDI);
    }
}