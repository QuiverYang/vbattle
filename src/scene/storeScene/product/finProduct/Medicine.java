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
public class Medicine extends FinProduct{
    
    public static final int PRODUCT_MEDICINE_PRICE = 600;

    public final String PRODUCT_MEDICINE_INFO = "抗生藥品:每一回合  有7成機會最大體力增加30  3成機會減少30";
    

    public Medicine() {
        super("/resources/medicine.jpg");
        this.name = "抗生藥品";
        this.price = PRODUCT_MEDICINE_PRICE;
        this.risk = 0.3d;
        this.profit = 30;
        this.info = PRODUCT_MEDICINE_INFO;
    }
   
    
}
