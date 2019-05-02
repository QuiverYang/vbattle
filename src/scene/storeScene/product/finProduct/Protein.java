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
public class Fund extends FinProduct{

    public static final int PRODUCT_FUND_PRICE = 500;
    
    public static final String PRODUCT_FUND_PATH = "/resources/balance.jpg";

    public static final String PRODUCT_FUND_INFO  = "基金:購買一張股票  風險0.20/利潤0.04";
    
    
    public Fund() {
        super(PRODUCT_FUND_PATH);
        this.name = "股票";
        this.price = PRODUCT_FUND_PRICE;
        this.risk = 0.2d;
        this.profit = 0.04d;
        this.info = PRODUCT_FUND_INFO;
        this.value = price;
    }
}
