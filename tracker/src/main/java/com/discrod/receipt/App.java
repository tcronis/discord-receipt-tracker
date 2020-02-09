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
    private static boolean ask_for_images = false, start_inputs = false;
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
                else if (category == null){
                    category = categoryParsing(message);
                    sendMessage(event, "If you would like to attach an image, send it now, otherwise reply with No");
                }else{
                    if(!message.equalsIgnoreCase("no") || !event.getMessage().getAttachments().isEmpty()){
                        try{
                            event.getMessage().getAttachments().get(0).downloadToFile();
                            img_object = new File(event.getMessage().getAttachments().get(0).getFileName());
                            while(!img_object.exists()){
                                Thread.sleep(2000);
                            }
                        }catch(Exception e){e.printStackTrace();}
                    }
                    start_inputs = false;
                    runData();
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
