package com.discrod.receipt;

import java.io.File;
import java.util.ArrayList;
import java.util.regex.Pattern;

import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

/**
 * Hello world!
 *
 */
public class App extends ListenerAdapter
{
    private static JDABuilder builder;
    private static Pattern msg = Pattern.compile("[^a-zA-Z]");
    public enum CATEGORIES{
        GROCERY_STORE, 
        FAST_FOOD,
        RESTAURANT,
        ALCOHOL,
        GAS, 
        MISC
    }

    public static void main( String[] args ) throws Exception
    {   
        builder = new JDABuilder(AccountType.BOT);
        Authenticator authen = new Authenticator();
        builder.setToken(authen.getToken());
        builder.addEventListeners(new App());
        builder.build();
    }
    /*expected input
        name of store, category, Date (mm-dd-yyyy or now for current date of now), total
    */
    @Override
    public void onPrivateMessageReceived(PrivateMessageReceivedEvent event) {
        
        if(event.getMessage().getContentDisplay().equals("q"))
            System.exit(0);
        else if(event.getMessage().getContentDisplay().contains("stats")){

        }else{
            Boolean img = (event.getMessage().getAttachments().size() > 0) ? true : false;
            ArrayList<String> value = get_values(event.getMessage().getContentDisplay().replaceAll(" ", ""));
            String name = null, date = null;
            Double total = 0.0;
            CATEGORIES cat = CATEGORIES.MISC;
            try{
                name = value.get(0);
                date = value.get(2);
                total = Double.parseDouble(value.get(3));
                String temp = value.get(1);
                if(temp.equalsIgnoreCase("GROCERY_STORE") || temp.equalsIgnoreCase("groceries") || temp.equalsIgnoreCase("grocery"))
                    cat = CATEGORIES.GROCERY_STORE;
                else if (temp.equalsIgnoreCase("FAST_FOOD") || temp.equalsIgnoreCase("fast food") || temp.equalsIgnoreCase("ff"))
                    cat = CATEGORIES.FAST_FOOD;
                else if(temp.equalsIgnoreCase("RESTAURANT") || temp.equalsIgnoreCase("r"))
                    cat = CATEGORIES.RESTAURANT;
                else if(temp.equalsIgnoreCase("ALCOHOL") || temp.equalsIgnoreCase("Beer") || temp.equalsIgnoreCase("wine") || temp.equalsIgnoreCase("a"))
                    cat = CATEGORIES.ALCOHOL;
                else if(temp.equalsIgnoreCase("Gas"))
                    cat = CATEGORIES.GAS;
                else 
                    cat = CATEGORIES.MISC;

                
            }catch(Exception e){e.printStackTrace();}
            File f = null;
            if(img){
                try{
                    event.getMessage().getAttachments().get(0).downloadToFile();
                    f = new File(event.getMessage().getAttachments().get(0).getFileName());
                    while(!f.exists()){
                        Thread.sleep(2000);
                    }
                    
                }catch(Exception e){e.printStackTrace();}
            }
            try{
                Receipt r = new Receipt(name, total, date, cat, f);
                r.save_data();
            }catch(Exception e){
                e.printStackTrace();
            }
            
        }

        

        

    }


    //name of store, total amount, Date (mm/dd/yyyy or now for current date of now), user1 amount
    private static ArrayList<String> get_values(String value){
        ArrayList<String> v = new ArrayList<>();
        int index = value.indexOf(",");
        while(value != null){
            System.out.println(value.substring(0, index));
            v.add(value.substring(0, index));
            if(value.length() == index)
                value = null;
            else{
                value = value.substring(index + 1);
                if(value.contains(","))
                    index = value.indexOf(",");
                else
                    index = value.length();
            }
        }
        return v;
    }
}
