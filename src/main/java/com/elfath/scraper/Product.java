package com.elfath.scraper;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;

import lombok.Data;

@Data
public class Product {

   // @CsvBindByPosition(position = 0)
    @CsvBindByName
    private String name;

   // @CsvBindByPosition(position = 1)
    @CsvBindByName
    private String description;

    //@CsvBindByPosition(position = 2)
    @CsvBindByName
    private String imageLink;

   // @CsvBindByPosition(position = 3)
    @CsvBindByName
    private String price;

   // @CsvBindByPosition(position = 4)
    @CsvBindByName
    private String rating;

    //@CsvBindByPosition(position = 5)
    @CsvBindByName
    private String merchant;

}
