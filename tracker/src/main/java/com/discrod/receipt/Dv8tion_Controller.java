package com.discrod.receipt;

import java.io.File;

import com.discrod.receipt.Authenticator;

import lombok.Getter;
import net.dv8tion.jda.*;
import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;

public class Dv8tion_Controller{
    private static JDABuilder builder;


    public Dv8tion_Controller() throws Exception{
        builder = new JDABuilder(AccountType.BOT);
        Authenticator authen = new Authenticator();
        builder.setToken(authen.getToken());
        builder.addEventListeners(new Dv8tion_Controller());
        builder.build();
    }

	public static JDABuilder getBuilder() {
		return builder;
	}

    @Override
    public void onPrivateMessageReceived(PrivateMessageReceivedEvent event) {
    	
    }


}