package com.example.lirecsvfile;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;



import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LireCsvFileApplication {

    public static void main(String[] args) {
        String csvFile = "clients.csv";
        String xmlFile = "clients.xml";
        String jsonFile = "clients.json";
        //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        String line = "";
        String cvsSplitBy = ",";
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            FileWriter xmlWriter = new FileWriter(xmlFile);
            XmlMapper xmlMapper = new XmlMapper();
            xmlWriter.write("<clients>\n");
            while ((line = br.readLine()) != null) {
                String[] data = line.split(cvsSplitBy);
                String nom = data[0];
                String prenom = data[1];
                String dateNaissance = data[2];
                String profession = data[3];
                String clientXml = "<client>\n" +
                        "   <nom>" + nom + "</nom>\n" +
                        "   <prenom>" + prenom + "</prenom>\n" +
                        "   <dateNaissance>" + dateNaissance + "</dateNaissance>\n" +
                        "   <profession>" + profession + "</profession>\n" +
                        "</client>\n";
                xmlWriter.write(clientXml);
                client client = new client(nom, prenom, dateNaissance, profession);
                ObjectMapper objectMapper = new ObjectMapper();
                FileWriter jsonWriter = new FileWriter(jsonFile, true);
                objectMapper.writeValue(jsonWriter, client);
                jsonWriter.close();
            }
            xmlWriter.write("</clients>\n");
            xmlWriter.close();
            System.out.println("Conversion terminée avec succès.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




}
