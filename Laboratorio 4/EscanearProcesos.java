/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package broadcast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author yensen
 */
public class EscanearProcesos {
     public static void main(String[] args) {
        // TODO code application logic here
        System.out.println("Corriendo");
        List<String> processes = listRunningProcesses();
        String result = "";
        // display the result 
        Iterator<String> it = processes.iterator();
        int i = 0;
        while (it.hasNext()) {
            result += it.next() +"\n,";
            i++;
            if (i==10) {
                result += "\n";
                i = 0;
            }
        }
        msgBox("Running processes : " + result);
        System.out.println(result);
    }
     
    public static List<String> listRunningProcesses() {
        List<String> processes = new ArrayList<String>();
        try {
            String line;
            Process p = Runtime.getRuntime().exec("tasklist.exe /fo csv /nh");
            BufferedReader input = new BufferedReader
                (new InputStreamReader(p.getInputStream()));
                while ((line = input.readLine()) != null) {
                    if (!line.trim().equals("")) {
                        // keep only the process name
                        line = line.substring(1);
                        //processes.add(line.substring(0, line.indexOf("")));
                        processes.add(line);
                        
                    }
                }
            input.close();
        }
        catch (Exception err) {
          err.printStackTrace();
        }
        return processes;
    }
    public static void msgBox(String msg) {
    javax.swing.JOptionPane.showConfirmDialog((java.awt.Component)
       null, msg, "WindowsUtils", javax.swing.JOptionPane.DEFAULT_OPTION);
    }  
}
