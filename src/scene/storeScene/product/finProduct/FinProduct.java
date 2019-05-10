
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
    
    public static final String PRODUCT_FUND_PATH = "/resources/protein.jpg";
    public static final String PRODUCT_STOCK_PATH = "/resources/medicine.jpg";
    public static final String PRODUCT_FUTURES_PATH = "/resources/robot.jpg";

    public static final String PRODUCT_STOCK_INFO = "抗生藥品:購買一個抗生藥品  成長風險0.30/利潤10";
    public static final String PRODUCT_FUTURES_INFO  = "生化元素:購買一個生化藥劑  成長風險0.60/利潤50";
    public static final String PRODUCT_FUND_INFO  = "健康食品:購買一個健康食品  成長風險0.20/利潤30";
    
    protected double risk;
    protected int profit;
    protected int plusHp,plusMp;
    
    public FinProduct(){
    }
    
    public FinProduct(String fileName){
        super(fileName);
    }
    
    public FinProduct(String fileName, String name, int price, double risk, int profit, String info,int plusHp, int plusMp){
        super(fileName, name, price, info);
        this.risk = risk;
        this.profit = profit;
        this.plusHp = plusHp;
        this.plusMp = plusMp;
    }
    
    public FinProduct(String fileName, int x, int y, int width, int height){
        super(fileName, x, y, width, height);
    }
    
    
    public void changeValue(){
        if(Math.random()>risk){
            plusHp += profit;
            plusMp += profit;
        }else{
            plusHp -= profit;
            plusMp -= profit;
        }
    }

    public int getPlusHp() {
        return plusHp;
    }

    public void setPlusHp(int plusHp) {
        this.plusHp = plusHp;
    }

    public int getPlusMp() {
        return plusMp;
    }

    public void setPlusMp(int plusMp) {
        this.plusMp = plusMp;
    }
    

    public double getRisk() {
        return risk;
    }

    public void setRisk(double risk) {
        this.risk = risk;
    }


    public int getProfit() {
        return profit;
    }

    
    
}
