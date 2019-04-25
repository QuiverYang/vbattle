/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scene.storeScene;

/**
 *
 * @author menglinyang
 */
public class FinProduct extends Product{
    

    public static final int PRODUCT_STOCK_PRICE = 500;
    public static final int PRODUCT_FUTURES_PRICE = 500;
    public static final int PRODUCT_FUND_PRICE = 500;
    
    public static final String PRODUCT_STOCK_PATH = "/resources/stock.jpg";
    public static final String PRODUCT_FUTURES_PATH = "/resources/stock.jpg";
    public static final String PRODUCT_FUND_PATH = "/resources/stock.jpg";

    public static final String PRODUCT_STOCK_INFO = "股票:購買一張股票  風險0.30/利潤0.08";
    public static final String PRODUCT_FUTURES_INFO  = "期貨:購買一張股票  風險0.60/利潤0.16";
    public static final String PRODUCT_FUND_INFO  = "基金:購買一張股票  風險0.20/利潤0.04";
    
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
