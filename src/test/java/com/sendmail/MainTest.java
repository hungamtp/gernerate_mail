package com.sendmail;

import junit.framework.TestCase;
import org.junit.Test;

import java.io.File;

public class MainTest extends TestCase {

    @Test
    public void testSuccess(){
        File templateFile = new File("");
        File customerFile = new File("");
        File outputFolder = new File("");
        File errorFile = new File("");
        assertEquals(1 , 1);
    }

}
