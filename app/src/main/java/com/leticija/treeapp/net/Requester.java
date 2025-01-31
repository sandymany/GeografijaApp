package com.leticija.treeapp.net;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentManager;

import com.leticija.treeapp.Effects;
import com.leticija.treeapp.R;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

public class Requester {

    static String response;
    private static String baseUrl;

    public static void setUrl(Context context) {
        baseUrl = "https://posadistablo.000webhostapp.com";
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String request(String endpoint, final Map<String,String> headerToSend, final String bodyToSend, Context context, FragmentManager fragmentManager) {
        try {

            String url = baseUrl + endpoint;
            //open connection
            System.out.println("URL TO REQUEST: "+url);
            URL urlToOpen = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) urlToOpen.openConnection();

            conn.setDoOutput(true);
            conn.setRequestMethod(bodyToSend==null?"GET":"POST");

            // SLANJE HEADERA
            for (Map.Entry<String, String> e: headerToSend.entrySet()) {
                conn.setRequestProperty(e.getKey(),e.getValue());
            }

            // SLANJE RESPONSE BODYJA
            if (bodyToSend != null) {
                byte[] postData = bodyToSend.getBytes(StandardCharsets.UTF_8);
                DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
                wr.write(postData);
            }
            return getResponse(conn);
        } catch (ProtocolException e1) {
            e1.printStackTrace();
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        Effects.showServerErrorDialog(context,fragmentManager);
        System.out.println("SOME EXCEPTIOOn OCCCURED !!!!!");
        return null;
    }

    private static String getResponse (HttpURLConnection conn) {
        String response;
        try {
            Scanner in = new Scanner(conn.getInputStream());
            in.useDelimiter("\\A");
            if (in.hasNext()) {
                response = in.next();
                System.out.println("GOT RESPONSE IN request: " + response);
                return response;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
