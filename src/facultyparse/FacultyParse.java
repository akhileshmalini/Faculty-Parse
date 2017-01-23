/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facultyparse;
import java.applet.Applet;
import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 *
 * @author Akhilesh
 */
public class FacultyParse extends Applet {
    
   public void paint (Graphics g) {
      g.drawString ("Hello World", 25, 50);
   }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
//        Jsoup.connect("...").timeout(0).get(); 
        List<String> faclinks = new ArrayList<String>();
        List<Faculty> flist = new ArrayList<Faculty>();
        faclinks.clear();
        String url1 = "https://www.amrita.edu/faculty?field_faculty_department_tid=38&field_faculty_campus_tid=55&field_faculty_designation_tid=All&field_faculty_department_main_tid=363&field_center_name_tid=All";
//        String url2 = "https://www.amrita.edu/faculty?field_faculty_department_tid=38&field_faculty_campus_tid=55&field_faculty_designation_tid=All&field_faculty_department_main_tid=101&field_center_name_tid=All&page=0%2C1";
        System.out.println("Fetching %s..."+ url1);

        Document doc = Jsoup.connect(url1).get();
        Elements links = doc.select("a[href]");
        

       
        for (Element link : links) {
if(link.attr("abs:href").toString().contains("https://www.amrita.edu/faculty/")){
     System.out.println(link.attr("abs:href"));
        faclinks.add(link.attr("abs:href").toString());
       
}
        
        }
//        
//        doc = Jsoup.connect(url2).get();
//         links = doc.select("a[href]");
//                 for (Element link : links) {
//if(link.attr("abs:href").toString().contains("https://www.amrita.edu/faculty/")){
// System.out.println(link.attr("abs:href"));
//        faclinks.add(link.attr("abs:href").toString());
//        }
//       
//        }
                 int tot=faclinks.size();
         System.out.println("Found "+tot);
       Document d;    
       Elements designation,name,qual,res,email,image;
       String n="",de="",q="",e="",ims="";
       
       int num=0;
       for (String fac : faclinks) {
           String r=""; 
         d= Jsoup.connect(fac).get();
         name=d.getElementsByClass("title");
         designation=d.getElementsByClass("field-name-field-faculty-designation");
         qual=d.select(".field-name-field-faculty-qualification li");
         res=d.select(".field-name-field-faculty-research-interest li");
         email=d.getElementsByClass("field-name-field-faculty-email");
         image=d.select(".field-name-field-blog-image img");
         
         for (Element link : image) {
              System.out.println();
              ims=link.absUrl("src");
           }
         
           for (Element link : name) {
              n=link.text();
           }
           
            for (Element link : designation) {
            de=link.text();              
              
           }
             for (Element link : qual) {
              q=link.text();
                 
           }
              for (Element link : res) {
                  r=r+link.text()+", ";
                      
           }
           for (Element link : email) {
               String regex = "\\s+\\bis\\b\\s+";
                   
                   e=link.text().replaceAll("Faculty Email:", "").replaceAll(" ", "");
                   
           }
               
             if (r.endsWith(", ")) {
  r = r.substring(0, r.length() - 2);
}  
    
           flist.add(new Faculty(n,de,q,r,e,ims));
           
 
           
            System.out.println("Gathered "+(num+=1)+" of "+tot);
             
            }
       
       
       JSONArray jsonArray = new JSONArray();
for (int i=0; i < flist.size(); i++) {
        jsonArray.put(flist.get(i).getJSONObject());
}
       
try (Writer writer = new BufferedWriter(new OutputStreamWriter(
              new FileOutputStream("cultural.json"), "utf-8"))) {
   writer.write(jsonArray.toString());
}
        
    }

    
 
}

class Faculty {
   String Name,Designation,Research,Qualification,Email,Image;

    public Faculty() {
    }

    public Faculty(String Name, String Designation, String Research, String Qualification, String Email,String Image) {
        this.Name = Name;
        this.Designation = Designation;
        this.Research = Research;
        this.Qualification = Qualification;
        this.Email = Email;
        this.Image=Image;
    }

    public String getDesignation() {
        return Designation;
    }

    public String getEmail() {
        return Email;
    }

    public String getName() {
        return Name;
    }

    public String getQualification() {
        return Qualification;
    }

    public String getResearch() {
        return Research;
    }

    public void setDesignation(String Designation) {
        this.Designation = Designation;
    }

     public String getImage() {
        return Image;
    }

    public void setImage(String Image) {
        this.Image = Image;
    }

    
    public void setEmail(String Email) {
        this.Email = Email;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public void setQualification(String Qualification) {
        this.Qualification = Qualification;
    }

    public void setResearch(String Research) {
        this.Research = Research;
    }
    
//    
//      this.Designation = Designation;
//        this.Research = Research;
//        this.Qualification = Qualification;
//        this.Email = Email;
//        this.Image=Image;
   
   public JSONObject getJSONObject() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("Name", Name);
            obj.put("Designation", Designation);
            obj.put("Qualification", Qualification);    
              obj.put("Research", Research);
            obj.put("Email", Email);
            obj.put("Image ", Image);  
        } catch (JSONException e) {
            
        }
        return obj;
    }
    
}
