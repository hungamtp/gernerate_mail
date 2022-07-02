package com.sendmail;


import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    enum INPUT_ARGS {
        TEMPLATE_FILE, CUSTOMER_FILE, OUTPUT_FILE, ERROR_FILE
    }

    public static void main(String[] args) throws Exception {
        try {
            validateArgs(args);
            File templateFile = validateFileName(args[INPUT_ARGS.TEMPLATE_FILE.ordinal()] , "json");
            File customerFile = validateFileName(args[INPUT_ARGS.CUSTOMER_FILE.ordinal()] , "csv");
            File outputFolder = validateFolderOuput(args[INPUT_ARGS.OUTPUT_FILE.ordinal()]);
            File errorFile = validateFileName(args[INPUT_ARGS.ERROR_FILE.ordinal()] , "csv");
            generateEmail(outputFolder, errorFile, customerFile, templateFile);
            System.out.println("GENERATED SUCCESS");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void validateArgs(String[] args) throws Exception {
        if (args.length != 4) {
            throw new Exception("Must input 4 arguments <template_file_path> <customer_file_path> <outout_folder> <error_file_path>");
        }
    }

    private static File validateFileName(String fileName , String extension) throws Exception {
        File file = new File(fileName);
        if (!file.exists()) {
            throw new FileNotFoundException(String.format("%s not found.", fileName));
        }
        if(!file.getName().contains(extension)){
            throw new Exception(String.format("%s wrong format file , it must be %s", fileName , extension));
        }
        return file;
    }

    private static File validateFolderOuput(String filePath) throws Exception {
        File file = new File(filePath);
        if (!file.isDirectory()) {
            throw new Exception(String.format("%s is wrong directory", filePath));
        }
        return file;
    }

    private static Template readTemplate(File file) throws IOException, ParseException {
        JSONParser jsonParser = new JSONParser();

        try (FileReader reader = new FileReader(file)) {
            Object obj = jsonParser.parse(reader);
            JSONObject templateJson = (JSONObject) obj;
            return convertJsonToTemplate(templateJson);
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException(e.getMessage());
        } catch (IOException e) {
            throw new IOException(e.getMessage());
        } catch (ParseException e) {
            throw new ParseException(1);
        }

    }

    private static Template convertJsonToTemplate(JSONObject templateJson) {
        return new Template(templateJson.get(TemplateColumn.FROM).toString(), templateJson.get(TemplateColumn.SUBJECT).toString()
            , templateJson.get(TemplateColumn.MINE_TYPE).toString(), templateJson.get(TemplateColumn.BODY).toString());
    }

    private static List<Customer> getAllCustomers(File file) {
        List<Customer> customers = new ArrayList<>();
        try {
            Reader reader = new FileReader(file);
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
            return customers;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return customers;
    }

    private static List<String[]> readAll(Reader reader) throws Exception {
        CSVReader csvReader = new CSVReader(reader);
        List<String[]> list = new ArrayList<>();
        list = csvReader.readAll();
        reader.close();
        csvReader.close();
        return list;
    }

    public static void generateEmail(File folder, File errorFile, File customerFile, File templateFile) throws IOException, ParseException {
        Template template = readTemplate(templateFile);
        List<Customer> customers = getAllCustomers(customerFile);
        List<Customer> validCustomer = customers.stream().filter(customer -> customer.getHasMail()).collect(Collectors.toList());
        List<Customer> invalidCustomer = customers.stream().filter(customer -> !customer.getHasMail()).collect(Collectors.toList());

        for (var customer : validCustomer) {
            File file = new File(folder.getAbsolutePath() + "/" + customer.getEmail() + ".json");
            file.getParentFile().mkdirs();
            file.createNewFile();
            Format f = new SimpleDateFormat("dd MMM yyyy");
            String strDate = f.format(new Date());

            // use LinkedHashMap to keep the order
            Map<String , String> obj = new LinkedHashMap<>();

            obj.put(TemplateColumn.FROM, template.getFrom());
            obj.put(TemplateColumn.SUBJECT, template.getSubject());
            obj.put(TemplateColumn.MINE_TYPE, template.getMineType());
            obj.put(TemplateColumn.BODY, template.getBody()
                .replace(TemplateColumn.TITLE, customer.getTitle())
                .replace(TemplateColumn.FIRST_NAME, customer.getFirstname())
                .replace(TemplateColumn.LAST_NAME, customer.getLastname())
                .replace(TemplateColumn.TODAY, strDate)
            );

            try (FileWriter fileWriter = new FileWriter(file)) {
                fileWriter.write(JSONObject.toJSONString(obj));
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        if (invalidCustomer.size() > 0) {
            FileWriter errorFileWriter = new FileWriter(errorFile , true);
            CSVWriter writer = new CSVWriter(errorFileWriter);
            for (var customer : invalidCustomer) {
                writer.writeNext(new String[]{customer.getTitle(), customer.getFirstname(), customer.getLastname()});
            }
            writer.close();
        }

    }

}
