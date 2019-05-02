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

    public final String PRODUCT_MEDICINE_INFO = "抗生藥品:購買一個抗生藥品  成長風險0.30/利潤0.08";
    

    public Medicine() {
        super("/resources/medicine.jpg");
        this.name = "抗生藥品";
        this.price = PRODUCT_MEDICINE_PRICE;
        this.risk = 0.3d;
        this.profit = 0.08d;
        this.info = PRODUCT_MEDICINE_INFO;
        this.value = price;
    }
   
    
}
