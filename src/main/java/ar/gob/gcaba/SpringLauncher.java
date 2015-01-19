package ar.gob.gcaba;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringLauncher {
    public void launch() {
        new ClassPathXmlApplicationContext("applicationContext.xml");
        
    }
}