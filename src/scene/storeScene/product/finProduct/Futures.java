/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scene.storeScene.product.finProduct;

import scene.storeScene.product.finProduct.FinProduct;


/**
 *
 * @author menglinyang
 */
public class Futures extends FinProduct{
    
    public static final int PRODUCT_FUTURES_PRICE = 500;
    
    public static final String PRODUCT_FUTURES_PATH = "/resources/profit.jpg";

    public static final String PRODUCT_FUTURES_INFO  = "期貨:購買一張股票  風險0.60/利潤0.16";
    
    public Futures() {
        super(PRODUCT_FUTURES_PATH);
        this.name = "股票";
        this.price = PRODUCT_FUTURES_PRICE;
        this.risk = 0.6d;
        this.profit = 0.16d;
        this.info = PRODUCT_FUTURES_INFO;
    }
}
