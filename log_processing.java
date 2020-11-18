/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lab4;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 *
 * @author pg
 */
public class log_processing {
    
public static void main(String args[]) throws Exception {
File file = new File("time_used.txt");
BufferedReader br = new BufferedReader(new FileReader(file));
String s = null;
int TJ_total = 0;
int TS_total = 0;
double TJ_avg = 0;
double TS_avg = 0;
int TJ_count = 0;
int TS_count = 0;
while ((s = br.readLine()) != null) {
    
  if(s.length() > 3)
 {
     char temp = s.charAt(1);
    if(temp == 'J')
    {
        String temp_TJ = s.substring(3,s.length());
        int TJ = Integer.parseInt(temp_TJ);
        TJ_total += TJ;
        TJ_count++;
        
    }
    else if(temp == 'S')
    {
        String temp_TS = s.substring(3,s.length());
        int TS = Integer.parseInt(temp_TS);
        TS_total += TS;
        TS_count++;
        
    }
 }
}
br.close();
  TJ_avg = (TJ_total / 1.0) / TJ_count;
  TS_avg = (TS_total / 1.0) / TS_count;
 
System.out.println("TJ is " + TJ_total);
System.out.println("TS is " + TS_total);
System.out.println("Average TJ is " + TJ_avg);
System.out.println("Average TS is " + TS_avg);
;
}
    
    
}
