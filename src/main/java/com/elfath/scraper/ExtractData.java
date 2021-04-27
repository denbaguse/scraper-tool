package com.elfath.scraper;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlElement;

public class ExtractData {

    private URLFetcher urlFetcher;

    
   /**
     * 
     */
    public ExtractData() {
        urlFetcher = new URLFetcher();
    }


    public List<Product> extract(List<HtmlAnchor> productList) throws FailingHttpStatusCodeException, MalformedURLException, IOException {
        List<Product> products = new ArrayList<>();

        //loop every div to get the detail product
        int counter = 1;
        for (HtmlAnchor htmlAnchor:productList) {
            // System.out.println(" html = "+htmlAnchor.asXml());
            String linkProductUrl =  ( (HtmlElement) htmlAnchor).getAttribute("href");

            if (linkProductUrl.contains("topads")) {
                linkProductUrl = linkProductUrl.substring(linkProductUrl.indexOf("?") + 1, linkProductUrl.length() );
                //split the url and get the original url
                String[] arrUrl  = linkProductUrl.split("&");
        
                for (String u : arrUrl) {
                        if (u.startsWith("r=http")) {
                        linkProductUrl = u.split("=")[1];
                        linkProductUrl = URLDecoder.decode(linkProductUrl, Charset.forName("UTF-8"));
                        break;
                    }
                }

            }

            
            System.out.println("Product "+(counter++)+" detail url = "+ linkProductUrl);

            HtmlDivision productDetailDiv = urlFetcher.getProductDetail(linkProductUrl);
            products.add(buildProduct(productDetailDiv));
        }


        return products;
            
    }


    private Product buildProduct(HtmlDivision productHtmlDivision) {
        Product product = new Product();
        product.setImageLink(getImageLink(productHtmlDivision));
        product.setDescription(getDescription(productHtmlDivision));
        product.setMerchant(getMerchantName(productHtmlDivision));
        product.setName(getProductName(productHtmlDivision));
        product.setPrice(getPrice(productHtmlDivision));
        product.setRating(getRating(productHtmlDivision));

        return product;
    }



     /**
     * Gte Image Link
     */
    private String getImageLink(HtmlDivision mainProductDiv) {
        String imageLink = ( (HtmlElement)  mainProductDiv.getByXPath("//*[@id='pdp_comp-product_media']/div/div[1]/div/div/img").get(0) ).getAttribute("src");
       // System.out.println(String.format("Image link= %s", imageLink));
        return imageLink;
   }

   /**
    * Get description
    * @param mainProductDiv
    * @return
    */
    private String getDescription(HtmlDivision mainProductDiv) {
       String description = ( (HtmlElement)  mainProductDiv.getByXPath("//div[@id='pdp_comp-product_detail']/div[2]/div[2]/div/span/span/div").get(0) ).getTextContent();
      // System.out.println(String.format("Description= %s", description));
       return description;
  }


   /**
    * Get product name
    * @param mainProductDiv
    * @return
    */
    private String getProductName(HtmlDivision mainProductDiv) {
       List<HtmlElement> elements = mainProductDiv.getByXPath("//div[@id='pdp_comp-product_content']/div/h1");

       String productName = "";
       for (HtmlElement  el : elements) {
           productName = el.getTextContent();
       }

       return productName;
   }

   /**
    * Get Rating
    * @param mainProductDiv
    * @return
    */
    private String getRating(HtmlDivision mainProductDiv) {
       List<HtmlElement> element =  mainProductDiv.getByXPath("//*[@id='pdp_comp-product_content']/div/div[1]/div/div[2]/span[1]/span[2]");

       String rating = "0";
       if (element.size() > 0 )  {
           rating = ((HtmlElement)  element.get(0) ).getTextContent();
          // System.out.println(String.format("Rating= %s", rating));

           return rating;
       } else {
           rating = getRating2(mainProductDiv);
       }

       return rating;
   }


   private String getRating2(HtmlDivision mainProductDiv) {
       //String rating = ( (HtmlElement)  mainProductDiv.getByXPath("//*[@id='pdp_comp-review']/div[1]/div[1]/div/h5").get(0) ).getTextContent();
       
       String rating = "0";
       List<HtmlElement> element =  mainProductDiv.getByXPath("//*[@id='pdp_comp-review']/div[1]/div[1]/div/h5");
       if (element.size() > 0 )  {
           rating = ((HtmlElement)  element.get(0) ).getTextContent();
       }
      // System.out.println(String.format("Rating 2= %s", rating));
       return rating;
   }


   /**
    * Get Price
    * @param mainProductDiv
    * @return
    */
    private String getPrice(HtmlDivision mainProductDiv) {
       String price = ( (HtmlElement)  mainProductDiv.getByXPath("//div[@id='pdp_comp-product_content']/div/div[2]/div").get(0)  ).getTextContent();
       //System.out.println(String.format("Price= %s", price));
       return price == null || "".equals(price) ? "0" : price.replaceFirst("Rp", "");
   }


   /**
    * Get Merchant name
    * @param mainProductDiv
    * @return
    */
    private String getMerchantName(HtmlDivision mainProductDiv) {
       List<HtmlElement> element =  mainProductDiv.getByXPath("//*[@id='pdp_comp-shop_credibility']/div[2]/div[1]/div/a/h2");

       if (element.size() > 0 )  {
           String merchantName = ((HtmlElement)  element.get(0) ).getTextContent();
        //   System.out.println(String.format("Merchant Name>= %s", merchantName));

           return merchantName;
       }
       
       return "";
   }
    
}
