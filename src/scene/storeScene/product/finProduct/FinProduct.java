
package scene.storeScene.product.finProduct;

import scene.storeScene.product.Product;

/**
 *
 * @author menglinyang
 */
public class FinProduct extends Product{
    

    public static final int PRODUCT_FUND_PRICE = 500;
    public static final int PRODUCT_STOCK_PRICE = 600;
    public static final int PRODUCT_FUTURES_PRICE = 700;
    
    public static final String PRODUCT_STOCK_PATH = "/resources/needle.jpg";
    public static final String PRODUCT_FUTURES_PATH = "/resources/robot.jpg";
    public static final String PRODUCT_FUND_PATH = "/resources/medicine.jpg";

    public static final String PRODUCT_STOCK_INFO = "抗生藥品:購買一個抗生藥品  成長風險0.30/利潤0.08";
    public static final String PRODUCT_FUTURES_INFO  = "生化藥劑:購買一個生化藥劑  成長風險0.60/利潤0.16";
    public static final String PRODUCT_FUND_INFO  = "健康食品:購買一個健康食品  成長風險0.20/利潤0.04";
    
    protected double risk;
    protected int value;
    protected double profit;
    
    public FinProduct(){
    }
    
    public FinProduct(String fileName){
        super(fileName);
    }
    
    public FinProduct(String fileName, String name, int price, double risk, double profit, String info, int value){
        super(fileName, name, price, info);
        this.risk = risk;
        this.profit = profit;
        this.value = value;
    }
    
    public FinProduct(String fileName, int x, int y, int width, int height){
        super(fileName, x, y, width, height);
    }
    
    
    public void changeValue(){
        if(Math.random()>risk){
            value*=(1+risk);
        }else{
            value*=(1-risk);
            if(value < 0){
                value = 0;
            }
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
