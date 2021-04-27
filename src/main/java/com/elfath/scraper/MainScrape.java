package com.elfath.scraper;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.List;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

/**
 * Hello world!
 *
 */
public class MainScrape 
{

    private static final String TOKPED_HANDSET_CATEGORY_URL = "https://www.tokopedia.com/p/handphone-tablet/handphone";
   
    public static void main( String[] args )
    {
        //check argument
        if (args == null || args.length == 0) {
            System.out.println("Please add csv file location argument e.g: java -jar D:\\data\\tokped-top100-handset.csv");
            return;
        }

        //String csvPath = "E:\\test-scrape.csv";
        String csvPath = args[0];

        System.out.println( "Start scraping top products of category Mobile phones/Handphone from Tokopedia and save to "+args[0]);

        try {
            List<HtmlAnchor> productList = scrapeByAcnhor();

            ExtractData extractData = new ExtractData();
            List<Product> products = extractData.extract(productList);


            System.out.println("Write to csv.");
            CSVDataWriter csvDataWriter = new CSVDataWriter(csvPath);
            csvDataWriter.writeCsv(products);

        } catch (FailingHttpStatusCodeException e) {
            // TODO Auto-generated catch block            
            e.printStackTrace();
            System.out.println("Getting FailingHttpStatusCodeException error "+e.getMessage());
            return;
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block            
            e.printStackTrace();
            System.out.println("Getting MalformedURLException error "+e.getMessage());
            return;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.println("Getting IOException error "+e.getMessage());
            return;
        } catch (CsvDataTypeMismatchException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (CsvRequiredFieldEmptyException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        
    }


    private static void scrapeBydiv() throws FailingHttpStatusCodeException, MalformedURLException, IOException {
        URLFetcher urlFetcher = new URLFetcher();

        //Get prodcut list page 1 (60 row)
       List<HtmlDivision> productList1 = urlFetcher.scrapeProductListByDiv(TOKPED_HANDSET_CATEGORY_URL);
       System.out.println("Page 1 Products size: "+productList1.size());

        //Get prodcut list page 1 (61 - 120 row), get 40 row
       List<HtmlDivision> productList2 = urlFetcher.scrapeProductListByDiv(TOKPED_HANDSET_CATEGORY_URL);
       System.out.println("Page 2 Products size: "+productList2.size());

       if (productList1 != null && productList2 != null)
       productList1.addAll(productList2.subList(0, 10));  //productList1.addAll(productList2.subList(0, 39));

       //loop every div to get the detail product
       for (HtmlDivision htmlDivision:productList1) {
           String linkProductUrl =  ( (HtmlElement) htmlDivision.getByXPath("//a").get(0) ).getAttribute("id");
           HtmlDivision productDetailDiv = urlFetcher.getProductDetail(linkProductUrl);
       }
    }

    private static List<HtmlAnchor> scrapeByAcnhor() throws FailingHttpStatusCodeException, MalformedURLException, IOException{
        URLFetcher urlFetcher = new URLFetcher();

        //Get prodcut list page 1 (60 row)
       List<HtmlAnchor> productList1 = urlFetcher.scrapeProductListByAnchor(TOKPED_HANDSET_CATEGORY_URL);
       System.out.println("Page 1 Products size: "+productList1.size());

        //Get prodcut list page 1 (61 - 120 row), get 40 row
       List<HtmlAnchor> productList2 = urlFetcher.scrapeProductListByAnchor(TOKPED_HANDSET_CATEGORY_URL);
       System.out.println("Page 2 Products size: "+productList2.size());

       if (productList1 != null && productList2 != null)
       productList1.addAll(productList2);  //productList1.addAll(productList2.subList(0, 39));

       System.out.println("productList1 ="+productList1.size());

       return productList1;
    }
    

   

}
