package com.discrod.receipt;

import java.io.File;
import java.io.FileWriter;
import java.util.Date;

import com.discrod.receipt.App.CATEGORIES;
import com.opencsv.CSVWriter;

import lombok.Data;



@Data
public class Receipt{

    private String store_name, total, date;
    private File img;
    private CATEGORIES cat;

    /**
     * If the reciept wasn't split between two people
     * @param store_name - name of the store
     * @param total - total of the trans
     * @param date - the date that is occurred
     * @param file - the file object that will point at the picture sent (if it exists)
     */
    public Receipt(String store_name, String total, String date, CATEGORIES cat, File file){
        this.store_name = store_name;
        this.total = total;
        this.date = date;
        this.cat = cat;
        if(file != null && file.exists())
            this.img = file;
    }
    

    public void save_data() throws Exception{
        String month = date.substring(0,2);
        //check if there exists a directory with the date
        File month_directory = new File("data/"+month);
        File month_csv = new File("data/"+month+"/tracker.csv");
        Boolean csv_existed = month_csv.exists();
        File img_directory = new File("data/"+month+"/imgs/");
        if(!month_directory.exists())
            month_directory.mkdirs();
        if(!img_directory.exists())
            img_directory.mkdirs();
        if(!month_csv.exists())
            month_csv.createNewFile();
        //open csv and add data (name of store, total amount spent, date)
        CSVWriter writer = new CSVWriter(new FileWriter(month_csv, true));
        if(!csv_existed){
            String [] headers = {"Store Name", "Total amount spent", "Date of the Transaction", "Category"};
            writer.writeNext(headers);
            String [] values = {store_name, total.toString(), date, cat.toString()};
            writer.writeNext(values);
        }else{
            String [] values = {store_name, total.toString(), date, cat.toString()};
            writer.writeNext(values);
        }
        writer.close();

        if(img != null){
            String path = img_directory.toString() + "/" + store_name + "_" + date + "_" + total + ".jpg";
            img.renameTo(new File(path));
            // img.delete();
        }
        
        //move around the existing file to the new directory and save it there with the new name
            //img - storename_date_total

    }




}