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
public class Robot extends FinProduct{
    
    public static final int PRODUCT_ROBOT_PRICE = 700;

    public final String PRODUCT_ROBOT_INFO  = "生化元素:購買一個生化藥劑  成長風險0.60/利潤0.16";

    
    public Robot() {
        super("/resources/robot.jpg");
        this.name = "生化元素";
        this.price = PRODUCT_ROBOT_PRICE;
        this.risk = 0.6d;
        this.profit = 0.16d;
        this.info = PRODUCT_ROBOT_INFO;
        this.value = price;
    }
}
