package com.example.srini.nutrivisai;
import java.lang.String;
import java.util.*;
import java.net.*;
import javax.net.ssl.HttpsURLConnection;
import java.io.*;
class Nutritionix{
   /* public static void main(String[] args){
         System.out.println(getNutritionInfo("taco"));

    }*/

    public static String getNutritionInfo(String food){
        try{
            String requestUrl = "https://trackapi.nutritionix.com/v2/natural/nutrients";
            URL url = new URL(requestUrl);
            HttpsURLConnection request = (HttpsURLConnection)url.openConnection();
            request.setRequestMethod("POST");
            //set the headers
            request.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            request.setRequestProperty("x-app-key", "ca79da43af470d366a9037cf2265f4a6");
            request.setRequestProperty("x-app-id", "e3b1fdc1");
            //set query param
            String param = "query=" + food;
            //required for POST, allows to write to the connection
            request.setDoOutput(true);
            //write out the post body
            OutputStreamWriter out = new OutputStreamWriter(request.getOutputStream());
            out.write(param);
            out.close();
            //get the response in str
            BufferedReader responseStream = new BufferedReader(new InputStreamReader(request.getInputStream()));
            String str= "", temp;
            while(( temp = responseStream.readLine()) != null){
                str += temp;
            }
            responseStream.close();

            return str;

        }catch(MalformedURLException e){
            System.err.println("malformed url excpetion occured " + e.getMessage());
        }catch(ProtocolException e){
            System.err.println("protocol excpetion occured " + e.getMessage());
        }catch(IOException e){
            System.err.println("io exception occured " + e.getMessage());
        }
        return "";

    }
}
