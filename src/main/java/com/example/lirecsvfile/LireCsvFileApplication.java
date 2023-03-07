package com.example.lirecsvfile;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.example.lirecsvfile.Client;
import org.springframework.util.ConcurrencyThrottleSupport;

//@SpringBootApplication
public class LireCsvFileApplication {

    public static void main(String[] args) throws IOException {

        String pathInput = System.getProperty("pathInput");
        System.out.println("input"  + pathInput);
        String pathOutput = System.getProperty("pathOutput");
        System.out.println("output"  + pathOutput);

        if(pathInput == null || pathOutput == null) {
            throw new IllegalArgumentException("PathInput ou PathOutput is not set");
        }

        LireCsvFileApplication myApp = new LireCsvFileApplication();

        String csvFile = "clients.csv";
        String jsonFile = "clients.json";
        String xmlFile = "clients.xml";

        Pair<List<Client>, List<String>> clientsParsing = myApp.readCsv(pathInput + "/" + csvFile);
        myApp.writeRejects(clientsParsing.getRight());
        List<Client> uniqueClients = myApp.removeDuplicates(clientsParsing.getLeft());


        try {
            myApp.writeJson(pathOutput + "/" + jsonFile, uniqueClients);
        } catch (Exception e) {
            System.err.println("Exeption while writing Json ... ");
            e.printStackTrace();
        }

        try {
        myApp.writeXml(pathOutput + "/" + xmlFile, uniqueClients);
        } catch (Exception e) {
            System.err.println("Exeption while writing Json ... ");
            e.printStackTrace();
        }
    }

    private void writeRejects(List<String> rejects) {
        System.out.println("Rejects printing ... ");
        rejects.forEach(System.out::println);
    }

    private Pair<List<Client>, List<String>> readCsv(String fileName) throws IOException {
        List<Client> clients = new ArrayList<>();
        String line = "";
        String delimiter = ",";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        List<String> rejects = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {

            while ((line = br.readLine()) != null) {
                try {
                    Client client = parseClient(line, delimiter, formatter);
                    clients.add(client);
                } catch (Exception e) {
                    rejects.add(line);
                }
            }
        }

        return Pair.of(clients, rejects);
    }

    private Client parseClient(String line, String delimiter, DateTimeFormatter formatter) {
        String[] data = line.split(delimiter);
        String nom = data[0];
        String prenom = data[1];
        LocalDate dateNaissance = LocalDate.parse(data[2], formatter);
        String profession = data[3];

        return new Client(nom, prenom, dateNaissance, profession);
    }

    private List<Client> removeDuplicates(List<Client> clients) {
        Set<Client> uniqueClients = new HashSet<>(clients);
        return new ArrayList<>(uniqueClients);
    }

    private void writeJson(String fileName, List<Client> clients) throws IOException {
        System.out.println("Writing Json file ... ");
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.enable(SerializationFeature.INDENT_OUTPUT);


        try (FileWriter writer = new FileWriter(fileName)) {
            mapper.writeValue(writer, clients);
        }
        System.out.println("Json file written on " + fileName);
    }

    private void writeXml(String fileName, List<Client> clients) throws IOException {
        System.out.println("Writing XML file ... ");
        XmlMapper mapper = new XmlMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(ToXmlGenerator.Feature.WRITE_XML_DECLARATION, true);

        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            mapper.writeValue(writer, clients);
        }

        System.out.println("XML file written on " + fileName);
    }

    class RejectsConsumer<String> implements Consumer<String> {

        @Override
        public void accept(String reject) {
            System.out.println("reject=" + reject);
        }
    }
}
