package com.discrod.receipt;

import java.io.File;
import java.util.Date;
import lombok.Data;



@Data
public class Receipt{

    private String store_name;
    private Double total, user_1_total, user_2_total;   //assumes user1 is always the person storing everything (user of this app/)
    private Date date;
    private File img;

    /**
     * If the reciept wasn't split between two people
     * @param store_name - name of the store
     * @param total - total of the trans
     * @param date - the date that is occurred
     */
    public Receipt(String store_name, Double total, Date date, File file){
        this.store_name = store_name;
        this.total = total;
        this.date = date;
        if(file.exists())
            this.img = file;
    }

    /**
     * If the reciept was split between two people
     * @param store_name - name of the store
     * @param total - total of the receipt
     * @param date - date that the transaction occurred
     * @param user1 - the first user's total (the person storing the amount)
     * @param user2 - the second user's total
     */
    public Receipt(String store_name, Double total, Date date, Double user1, Double user2, File file){
        this.store_name = store_name;
        this.total = total;
        this.date = date;
        this.user_1_total = user1;
        this.user_2_total = user2;
        if(file.exists())
            this.img = file;
    }


}