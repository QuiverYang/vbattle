/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vbattle;

/**
 *
 * @author menglinyang
 */
public class FinProduct extends Product{
    
    private double risk;
    private int value;
    private double profit;
    
    public FinProduct(String fileName, String name, int price, double risk, double profit, String info){
        super(fileName, name, price, info);
        this.risk = risk;
        this.profit = profit;
        this.value = price;
    }
    
    public void changeValue(){
        if(Math.random()>risk){
            value*=(1+risk);
        }else{
            value*=(1-risk);
        }
    }

    public double getRisk() {
        return risk;
    }

    public void setRisk(double risk) {
        this.risk = risk;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public double getProfit() {
        return profit;
    }

    public void setProfit(double profit) {
        this.profit = profit;
    }
    
    
}
