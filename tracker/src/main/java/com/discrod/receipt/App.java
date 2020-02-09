package com.discrod.receipt;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.startsWith;

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

    private static String store_name = null, total_amount = null, date = null;
    private static File img_object = null;
    private static CATEGORIES category = null;
    private static boolean check_stats = false, delete_progress_check = false, start_inputs = false;
    /*expected input
        name of store, category, Date (mm-dd-yyyy or now for current date of now), total
    */

    //Attempts to parse a string value into a hard coded category, will eventually switch over to a custom category option
    private static CATEGORIES categoryParsing(String temp){
        if(temp.equalsIgnoreCase("GROCERY_STORE") || temp.equalsIgnoreCase("groceries") || temp.equalsIgnoreCase("grocery"))
            return CATEGORIES.GROCERY_STORE;
        else if (temp.equalsIgnoreCase("FAST_FOOD") || temp.equalsIgnoreCase("fast food") || temp.equalsIgnoreCase("ff"))
            return CATEGORIES.FAST_FOOD;
        else if(temp.equalsIgnoreCase("RESTAURANT") || temp.equalsIgnoreCase("r"))
            return CATEGORIES.RESTAURANT;
        else if(temp.equalsIgnoreCase("ALCOHOL") || temp.equalsIgnoreCase("Beer") || temp.equalsIgnoreCase("wine") || temp.equalsIgnoreCase("a"))
            return CATEGORIES.ALCOHOL;
        else if(temp.equalsIgnoreCase("Gas"))
            return CATEGORIES.GAS;
        else 
            return CATEGORIES.MISC;
    }

    private static void sendMessage(PrivateMessageReceivedEvent event, String message){
        event.getAuthor().openPrivateChannel().queue((channel) -> {channel.sendMessage(message).queue();});
    }
    
    private static boolean competedInput(){
        if(store_name != null && category != null)
            return true;
        else   
            return false;
    }

    private static void runData(){
        System.out.println(store_name + ", " + total_amount + ", " + date + ", " + category.toString());
    }



    @Override
    public void onPrivateMessageReceived(PrivateMessageReceivedEvent event) {
        if(!event.getAuthor().isBot()){
            String message = event.getMessage().getContentDisplay().toString();
            if(message.equalsIgnoreCase("!exit") || message.equalsIgnoreCase("!q") || message.equalsIgnoreCase("!quit"))
                System.exit(1);
            if(start_inputs && (!message.equalsIgnoreCase("!stop") && !message.equalsIgnoreCase("!restart") && !message.equalsIgnoreCase("!start") && !message.equalsIgnoreCase("!create reciept"))){
                //input store name
                if(store_name == null){
                    store_name = message;
                    sendMessage(event, "Enter the amount spent please");
                }
                else if(total_amount == null){
                    total_amount = message;
                    sendMessage(event, "Enter the date of the transaction (MM-DD-YYYY) please");
                }
                else if(date == null){
                    date = message;
                    sendMessage(event, "Enter the category type please");
                }
                else{
                    category = categoryParsing(message);
                    start_inputs = false;
                }
                    
            }
                //no receipt object in progress
            else if(!start_inputs && (message.equalsIgnoreCase("!start") || message.equalsIgnoreCase("!create reciept"))){
                start_inputs = true;
                sendMessage(event, "Enter the store name please");
            }
            //run the statics tracker for a given month 
            else if(message.equalsIgnoreCase("!stats")){

            }

            
            
        // else if(event.getMessage().getContentDisplay().contains(",")){
        //         Boolean img = (event.getMessage().getAttachments().size() > 0) ? true : false;
        //         ArrayList<String> value = get_values(event.getMessage().getContentDisplay().replaceAll(" ", ""));
        //         String name = null, date = null;
        //         Double total = 0.0;
        //         CATEGORIES cat = CATEGORIES.MISC;
        //         try{
        //             name = value.get(0);
        //             date = value.get(2);
        //             total = Double.parseDouble(value.get(3));
        //             String temp = value.get(1);
        //             if(temp.equalsIgnoreCase("GROCERY_STORE") || temp.equalsIgnoreCase("groceries") || temp.equalsIgnoreCase("grocery"))
        //                 cat = CATEGORIES.GROCERY_STORE;
        //             else if (temp.equalsIgnoreCase("FAST_FOOD") || temp.equalsIgnoreCase("fast food") || temp.equalsIgnoreCase("ff"))
        //                 cat = CATEGORIES.FAST_FOOD;
        //             else if(temp.equalsIgnoreCase("RESTAURANT") || temp.equalsIgnoreCase("r"))
        //                 cat = CATEGORIES.RESTAURANT;
        //             else if(temp.equalsIgnoreCase("ALCOHOL") || temp.equalsIgnoreCase("Beer") || temp.equalsIgnoreCase("wine") || temp.equalsIgnoreCase("a"))
        //                 cat = CATEGORIES.ALCOHOL;
        //             else if(temp.equalsIgnoreCase("Gas"))
        //                 cat = CATEGORIES.GAS;
        //             else 
        //                 cat = CATEGORIES.MISC;

                    
        //         }catch(Exception e){e.printStackTrace();}
        //         File f = null;
        //         if(img){
        //             try{
        //                 event.getMessage().getAttachments().get(0).downloadToFile();
        //                 f = new File(event.getMessage().getAttachments().get(0).getFileName());
        //                 while(!f.exists()){
        //                     Thread.sleep(2000);
        //                 }
                        
        //             }catch(Exception e){e.printStackTrace();}
        //         }
        //         try{
        //             Receipt r = new Receipt(name, total, date, cat, f);
        //             r.save_data();
        //             event.getAuthor().openPrivateChannel().queue((channel) -> {
        //                 channel.sendMessage("added the data").queue();
        //             });
        //         }catch(Exception e){
        //             event.getAuthor().openPrivateChannel().queue((channel) -> {
        //                 channel.sendMessage("error in adding data").queue();
        //             });
        //             e.printStackTrace();
        //         }
                
        //     }

        }

        

    }


    //name of store, total amount, Date (mm/dd/yyyy or now for current date of now), user1 amount
    private static ArrayList<String> get_values(String value){
        ArrayList<String> v = new ArrayList<>();
        int index = value.indexOf(",");
        while(value != null){
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
