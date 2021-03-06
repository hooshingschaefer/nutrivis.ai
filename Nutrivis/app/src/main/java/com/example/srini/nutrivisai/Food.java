package com.example.srini.nutrivisai;

import android.annotation.SuppressLint;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Food {

    private String name;
    private double fatContent;
    private double calorieCount;
    private String dateScanned;

    @SuppressLint("NewApi")
    public Food(String foodName, double fatContent, double calorieCount){
        name = foodName;
        this.fatContent = fatContent;
        this.calorieCount = calorieCount;

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        this.dateScanned = dtf.format(now);
    }

    public String toString(){
        return name + ": Fat: "+ fatContent + "g Calories: " + calorieCount + " Scanned on: " + dateScanned;
    }

    public String getName(){
        return name;
    }

    public double getFatContent(){
        return fatContent;
    }

    public double getCalorieCount(){
        return calorieCount;
    }

    public String getDateScanned(){
        return dateScanned;
    }
}

