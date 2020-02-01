package com.discrod.receipt;

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
    public enum CATEGORIES{
        GROCERY_STORE, 
        FAST_FOOD,
        RESTAURANT,
        MISC
    }

    public static void main( String[] args ) throws Exception
    {   
        builder = new JDABuilder(AccountType.BOT);
        Authenticator authen = new Authenticator();
        builder.setToken(authen.getToken());
        builder.addEventListeners(new Dv8tion_Controller());
        builder.build();
    }

    @Override
    public void onPrivateMessageReceived(PrivateMessageReceivedEvent event) {
    	
    }
}
