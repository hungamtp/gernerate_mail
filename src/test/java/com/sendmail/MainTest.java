package com.sendmail;

import com.opencsv.CSVReader;
import junit.framework.TestCase;
import org.json.simple.parser.ParseException;
import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static com.sendmail.Main.*;
import static org.junit.Assert.*;

public class MainTest {
    private final static String TEMPLATE_FILE = "src/test/input/email_template.json";
    private final static String CUSTOMER_FILE = "src/test/input/customers.csv";
    private final static String OUTPUT_FOLDER = "src/test/output";
    private final static String ERROR_FILE = "src/test/errors/errors.csv";

    private final static String TEMPLATE_FILE_WRONG = "src/test/input/email_template_wrong_path.json";
    private final static String TEMPLATE_FILE_WRONG_FORMAT = "src/test/input/email_template_wrong.json";

    @Test
    public void testGenerateEmailSuccess() throws IOException, ParseException {
        File templateFile = new File(TEMPLATE_FILE);
        File customerFile = new File(CUSTOMER_FILE);
        File outputFolder = new File(OUTPUT_FOLDER);
        File errorFile = new File(ERROR_FILE);
        generateEmail(outputFolder, errorFile, customerFile, templateFile);


        List<String> emails = new ArrayList<>();
        try {
            Reader reader = new FileReader(customerFile);
            for (var customer : readAll(reader)) {
                if (customer.length > 0) {
                    if (customer.length == 4)
                        emails.add(customer[3]);
                }
            }
            // remove header
            emails.remove(0);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(outputFolder.list().length, emails.size());

        for (var email : emails) {
            File file = new File(OUTPUT_FOLDER + "/" + email + ".json");
            assertTrue(file.exists());
        }
    }

    private static List<String[]> readAll(Reader reader) throws Exception {
        CSVReader csvReader = new CSVReader(reader);
        List<String[]> list = new ArrayList<>();
        list = csvReader.readAll();
        reader.close();
        csvReader.close();
        return list;
    }

    @Test
    public void testValidateFileSuccess() throws Exception {
        File templateFile = validateFileName(TEMPLATE_FILE, "json");
        assertTrue(templateFile.exists());
        File customerFile = validateFileName(CUSTOMER_FILE, "csv");
        assertTrue(customerFile.exists());
        File errorsFile = validateFileName(ERROR_FILE, "csv");
        assertTrue(errorsFile.exists());
    }

    @Test(expected = FileNotFoundException.class)
    public void testGenerateEmailWrongCustomerFile() throws Exception {
        validateFileName(TEMPLATE_FILE_WRONG, "json");
    }

    @Test
    public void testGetCustomerSuccess() throws Exception {
        File customerFile = validateFileName(CUSTOMER_FILE, "csv");
        assertTrue(customerFile.exists());
        List<Customer> customers = getAllCustomers(customerFile);
        long validCustomer = customers.stream().filter(customer -> customer.getHasMail()).count();
        long invalidCustomer = customers.stream().filter(customer -> !customer.getHasMail()).count();
        assertEquals(customers.size(), 9);
        assertEquals(validCustomer, 5);
        assertEquals(invalidCustomer, 4);
    }

    @Test
    public void testReadTemplateSuccess() throws Exception {
        File templateFile = validateFileName(TEMPLATE_FILE, "json");
        Template template = readTemplate(templateFile);
        assertNotNull(template);
        assertEquals(template.getFrom(), "The Marketing Team<marketing@exampLe..com");
        assertEquals(template.getSubject(), "A new product  being launch soon...");
        assertEquals(template.getMineType(), "text/plain");
        assertEquals(template.getBody(), "Hi {{TITLE}} {{FIRST_NAME}} {{LAST_NAME}},\nToday, {{T0DAY}}, we wouLd Like to teLL you that... Sincerety,\nThe Marketing Team");
    }

    @Test(expected = ParseException.class)
    public void testReadTemplateFail() throws Exception {
        File templateFile = validateFileName(TEMPLATE_FILE_WRONG_FORMAT, "json");
        readTemplate(templateFile);
    }


}
