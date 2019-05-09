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

    public final String PRODUCT_PORTEIN_INFO  = "健康食品:$500  8成機會最大體力增加10  2成機會減少10(每回)";
    
    
    public Protein() {
        super("/resources/protein.jpg");
        this.name = "健康食品";
        this.price = PRODUCT_PORTEIN_PRICE;
        this.risk = 0.2d;
        this.profit = 10;
        this.info = PRODUCT_PORTEIN_INFO;
        
    }
}
