package com.sendmail;

import com.opencsv.CSVReader;
import junit.framework.TestCase;
import org.json.simple.parser.ParseException;
import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.sendmail.Main.generateEmail;

public class MainTest extends TestCase {
    private final static String TEMPLATE_FILE = "src/test/input/email_template.json";
    private final static String CUSTOMER_FILE = "src/test/input/customers.csv";
    private final static String OUTPUT_FOLDER = "src/test/output";
    private final static String ERROR_FILE = "src/test/errors/errors.csv";

    private final static String TEMPLATE_FILE_WRONG = "src/test/input/email_template_wrong.json";

    @Test
    public void testGenerateEmailSuccess() throws IOException, ParseException {
        File templateFile = new File(TEMPLATE_FILE);
        File customerFile = new File(CUSTOMER_FILE);
        File outputFolder = new File(OUTPUT_FOLDER);
        File errorFile = new File(ERROR_FILE);
        generateEmail(outputFolder, errorFile, customerFile, templateFile);
        assertEquals(outputFolder.list().length, 4);
    }

    @Test
    public void testCheckReadCustomerFileDataSuccess() throws IOException, ParseException {
        File customerFile = new File(CUSTOMER_FILE);
        List<Customer> customers = new ArrayList<>();
        try {
            Reader reader = new FileReader(customerFile);
            for (var customer : readAll(reader)) {
                if (customer.length > 0) {
                    if (customer.length == 4)
                        customers.add(new Customer(customer[0], customer[1], customer[2], customer[3], true));
                    else
                        customers.add(new Customer(customer[0], customer[1], customer[2], "", false));
                }
            }
            // remove header
            customers.remove(0);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(customers.size(), 9);
        assertEquals(customers.stream().filter(customer -> customer.getHasMail()).collect(Collectors.toList()).size(), 5);
        assertEquals(customers.stream().filter(customer -> !customer.getHasMail()).collect(Collectors.toList()).size(), 4);

    }

    private static List<String[]> readAll(Reader reader) throws Exception {
        CSVReader csvReader = new CSVReader(reader);
        List<String[]> list = new ArrayList<>();
        list = csvReader.readAll();
        reader.close();
        csvReader.close();
        return list;
    }

    @Test(expected = FileNotFoundException.class)
    public void testGenerateEmailWrongInputFile() throws IOException, ParseException {
        try {
            File templateFile = new File(TEMPLATE_FILE_WRONG);
            File customerFile = new File(CUSTOMER_FILE);
            File outputFolder = new File(OUTPUT_FOLDER);
            File errorFile = new File(ERROR_FILE);
            generateEmail(outputFolder, errorFile, customerFile, templateFile);
        } catch (FileNotFoundException e) {
            assertTrue(e.getMessage().contains(TEMPLATE_FILE_WRONG) || e.getMessage().contains("not found"));
        }
    }


}
