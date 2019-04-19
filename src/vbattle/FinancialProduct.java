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
public class FinancialProduct {
    private String name;
    private double risk;//增加的機會
    private int buyPrice;
    private double profit;
    int value;

    public FinancialProduct(String name, double risk, int price, double profit) {
        this.name = name;
        this.risk = risk;
        this.buyPrice = price;
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

    public double getProfit() {
        return profit;
    }

    public void setProfit(double profit) {
        this.profit = profit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getRisk() {
        return risk;
    }

    public void setRisk(double risk) {
        this.risk = risk;
    }

    public int getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(int price) {
        this.buyPrice = price;
    }
    
    

    
}
