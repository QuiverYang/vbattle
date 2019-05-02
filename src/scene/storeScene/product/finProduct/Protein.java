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
public class Protein extends FinProduct{

    public final int PRODUCT_PORTEIN_PRICE = 500;

    public final String PRODUCT_PORTEIN_INFO  = "健康食品:購買一個健康食品  成長風險0.20/利潤0.04";
    
    
    public Protein() {
        super("/resources/protein.jpg");
        this.name = "健康食品";
        this.price = PRODUCT_PORTEIN_PRICE;
        this.risk = 0.2d;
        this.profit = 0.04d;
        this.info = PRODUCT_PORTEIN_INFO;
        this.value = price;
    }
}
