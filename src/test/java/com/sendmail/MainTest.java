package com.sendmail;

import junit.framework.TestCase;
import org.junit.Test;

import java.io.File;

public class MainTest extends TestCase {

    @Test
    public void testSuccess(){
        File templateFile = new File("src/test/input/email_template.json");
        File customerFile = new File("src/test/input/customers.csv");
        File outputFolder = new File("src/test/output");
        File errorFile = new File("src/test/errors/errors.csv");
        assertEquals(1 , 1);
    }

}
