package com.discrod.receipt;

import com.discrod.receipt.*;
import com.discrod.receipt.App.CATEGORIES;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;

import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Stats{
    private String month;
    private File file;

    Stats(String month){
        this.month = month;
    }

    public void printStats(PrivateMessageReceivedEvent event) throws Exception{
        //find directory
        file = new File("data/" + month + "/tracker.csv");
        if(file.exists()){
            CSVReader reader = new CSVReaderBuilder(new FileReader(file))
                .withSkipLines(1)
                .build();
            List<String []> data = reader.readAll();
            HashMap<String, Double> totals = new HashMap<>(); //name of store and total amount spent
            HashMap<CATEGORIES, Double> categories_spent = new HashMap<>(); //name of category and the total amount spent
            for(String [] row : data){
                //"schnucks","7.57","02-20-2020","GROCERY_STORE"
                String name = row[0];
                Double amt = Double.parseDouble(row[1]);
                CATEGORIES cat = App.categoryParsing(row[3]);

                if(totals.containsKey(name))
                    totals.put(name, (totals.get(name) + amt));
                else
                    totals.put(name, amt);
                

                if(categories_spent.containsKey(cat))
                    categories_spent.put(cat, (categories_spent.get(cat) + amt));
                else
                    categories_spent.put(cat, amt);
            }
            StringBuilder output = new StringBuilder();
            Double total_amount_spent = 0.0;
            for(String value : totals.keySet()){
                total_amount_spent += totals.get(value);
                output.append(value + " : " + totals.get(value) + "\n");
            }
            output.append("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - \n");
            for(CATEGORIES c : categories_spent.keySet()){
                output.append(c.toString() + " : " + categories_spent.get(c) + "\n");
            }

            output.append("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - \n");
            output.append("Total amount spent for the month : " + total_amount_spent);
            App.sendMessage(event, output.toString());
        }
        else
            App.sendMessage(event, "File not found in directory for the month: " + month);

        //check if file exists

        //open file and read in data recrusively 

        //structure the data to be held in a map

        //store the totals

        //display how much was in totals was spent for the month





    }
    





}