package com.elfath.scraper;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

public class CSVDataWriter {

    private String csvPath;

    /**
     * 
     */
    public CSVDataWriter(String csvPath) {
        this.csvPath = csvPath;
    }
    

    public void writeCsv(List<Product> products) throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        System.out.println("Writing csv into file.");
        Writer fileWriter = new FileWriter(csvPath);
        StatefulBeanToCsv<Product> csvBuilder = new StatefulBeanToCsvBuilder<Product>(fileWriter).build();
        csvBuilder.write(products);
        fileWriter.close();
        System.out.println("Writing csv into file finish.");
     
    }




    
    
}
