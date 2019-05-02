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
public class Stock extends FinProduct{
    
    public static final int PRODUCT_STOCK_PRICE = 500;

    
    public static final String PRODUCT_STOCK_PATH = "/resources/stock.jpg";


    public static final String PRODUCT_STOCK_INFO = "股票:購買一張股票  風險0.30/利潤0.08";
    

    public Stock() {
        super(PRODUCT_STOCK_PATH);
        this.name = "股票";
        this.price = PRODUCT_STOCK_PRICE;
        this.risk = 0.3d;
        this.profit = 0.08d;
        this.info = PRODUCT_STOCK_INFO;
        this.value = price;
    }
   
    
}
