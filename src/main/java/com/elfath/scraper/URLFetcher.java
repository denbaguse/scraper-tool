package com.elfath.scraper;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import com.gargoylesoftware.htmlunit.AjaxController;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebWindowEvent;
import com.gargoylesoftware.htmlunit.WebWindowListener;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.Html;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.background.JavaScriptExecutor;

import org.openqa.selenium.firefox.FirefoxDriver;

/**
 * UrlFetcher
 */
public class URLFetcher {



    private WebClient webClient;

   // String tokpedHandsetCategoryUrl = "https://www.tokopedia.com/p/handphone-tablet/handphone";

    
    /**
     * 
     */
    public URLFetcher() {
        webClient = new WebClient(BrowserVersion.CHROME);
        setOptions();

    
    }

   
 
    private void setOptions() {
  
        webClient.getOptions().setCssEnabled(false);
       
        webClient.getOptions().setSSLInsecureProtocol("true");
        webClient.getOptions().setJavaScriptEnabled(false);

        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        webClient.getOptions().setThrowExceptionOnScriptError(false);

        
        webClient.getCurrentWindow().setInnerHeight(60000);
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());
        webClient.setAjaxController(new AjaxController(){
           
        @Override
        public boolean processSynchron(HtmlPage page, WebRequest request, boolean async)
        {
        return true;
        }
        });


        
    }



    public List<HtmlDivision> scrapeProductListByDiv(String url) throws FailingHttpStatusCodeException, MalformedURLException, IOException {
        webClient.waitForBackgroundJavaScript(20000);
             
        HtmlPage page = webClient.getPage(url);
        ScriptResult result = page.executeJavaScript("window.scrollTo(0, 600000);");
        result.getJavaScriptResult();
    
        List<HtmlDivision> productList = page.getByXPath("//a[@class='css-bk6tzz e1nlzfl3']");  //class="css-bk6tzz e1nlzfl3" 
        return productList;

    }

    public List<HtmlAnchor> scrapeProductListByAnchor(String url) throws FailingHttpStatusCodeException, MalformedURLException, IOException {
        HtmlPage page = webClient.getPage(url);
        List<HtmlAnchor> productList = page.getByXPath("//a[@data-testid='lnkProductContainer']");  //class="css-bk6tzz e1nlzfl3" 
        return productList;

    }



  
    public HtmlDivision getProductDetail(String url) throws FailingHttpStatusCodeException, MalformedURLException, IOException {
        // webClient.getOptions().setCssEnabled(true);
        // webClient.getOptions().setJavaScriptEnabled(true);

        HtmlPage page = webClient.getPage(url);

        //HtmlDivision mainProductDiv = (HtmlDivision) page.getByXPath("//div[@id='main-pdp-container']").get(0);
        HtmlDivision mainProductDiv = (HtmlDivision) page.getElementById("main-pdp-container");
       // System.out.println("main Product div ="+mainProductDiv.getAttribute(attributeName)));

        return mainProductDiv;
    }


   




    
}