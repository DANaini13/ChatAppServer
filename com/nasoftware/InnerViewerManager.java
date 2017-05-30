package com.nasoftware;

import java.util.Scanner;

/**
 * Created by zeyongshan on 5/29/17.
 */
public class InnerViewerManager {
    static void startInnerView()
    {
         InnerViewer innerViewer = new InnerViewer();
         innerViewer.start();
    }
}


class InnerViewer extends Thread
{
     private Scanner reader = new Scanner(System.in);
     public void run()
     {
          String content = reader.next();
          while (content!="close")
          {
              outputMesssage(content);
              content = reader.next();
          }
     }
     private void outputMesssage(String selection)
     {
         switch (selection)
         {
             case "accounts": System.out.println(AccountManager.getInfo());
                break;
             case "messages": System.out.println(MessageManager.getInfo());
                break;
             default:break;
         }
     }
}
