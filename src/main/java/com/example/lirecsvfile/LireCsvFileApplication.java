package com.example.lirecsvfile;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.example.lirecsvfile.Client;
@SpringBootApplication
public class LireCsvFileApplication {
    public static void main(String[] args) {

        String csvFile = "clients.csv";
        String jsonFile = "clients.json";
        String xmlFile = "clients.xml";

        List<Client> clients = readCsv(csvFile);
        List<Client> uniqueClients = removeDuplicates(clients);
        writeJson(jsonFile, uniqueClients);
        writeXml(xmlFile, uniqueClients);
    }

    private static List<Client> readCsv(String fileName) {
        List<Client> clients = new ArrayList<>();
        String line = "";
        String delimiter = ",";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            while ((line = br.readLine()) != null) {
                String[] data = line.split(delimiter);
                String nom = data[0];
                String prenom = data[1];
                LocalDate dateNaissance = LocalDate.parse(data[2], formatter);
                String profession = data[3];

                Client client = new Client(nom, prenom, dateNaissance, profession);
                clients.add(client);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return clients;
    }

    private static List<Client> removeDuplicates(List<Client> clients) {
        Set<Client> uniqueClients = new HashSet<>(clients);
        return new ArrayList<>(uniqueClients);
    }

    private static void writeJson(String fileName, List<Client> clients) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.enable(SerializationFeature.INDENT_OUTPUT);


        try (FileWriter writer = new FileWriter(fileName)) {
            mapper.writeValue(writer, clients);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeXml(String fileName, List<Client> clients) {
        XmlMapper mapper = new XmlMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(ToXmlGenerator.Feature.WRITE_XML_DECLARATION, true);

        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            mapper.writeValue(writer, clients);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
