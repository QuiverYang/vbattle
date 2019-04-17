package vbattle;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 *
 * @author menglinyang
 */
public class MouseListenerDragStuff extends MouseAdapter{
    
    boolean isClicked = false; //用來事先判斷點擊時有無點擊到圖片
    Stuff stuff;
    Button btn;
    
    public MouseListenerDragStuff(Stuff stuff){
        this.stuff = stuff;
    }
    public MouseListenerDragStuff(Button btn){
        this.btn = btn;
    }
    
    @Override
    public void mouseClicked(MouseEvent e){
        if(e.getButton() == MouseEvent.BUTTON1){
            if(isOnBtn(e)){
                btn.setClickState(true);
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

        if(e.getButton() == MouseEvent.BUTTON1){
            if(isOnPic(e)) isClicked = true;
        }
    }
    
    @Override
    public void mouseDragged(MouseEvent e){

        if(e.getButton() == MouseEvent.BUTTON1){
            if(isClicked){
                this.stuff.setX0(e.getX()-this.stuff.getImgWidth()/2);
                this.stuff.setY0(e.getY()-this.stuff.getImgHeight()/2);
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {

        isClicked = false;

    }
    //判斷滑鼠有沒有在（stuff）圖片上
    public boolean isOnPic(MouseEvent e){
        if((e.getX()>=this.stuff.getX0() && e.getX()<=this.stuff.getX0()+this.stuff.getImgWidth()
                &&e.getY()>=this.stuff.getY0() && e.getY()<=this.stuff.getY0()+this.stuff.getImgHeight())){
            return true;
        }else{
            return false;
        }
    }
    
    //判斷滑鼠有沒有在button上
    public boolean isOnBtn(MouseEvent e){
        if((e.getX()>=this.btn.getX() && e.getX()<=this.btn.getX()+this.btn.getImgWidth()
                &&e.getY()>=this.btn.getY() && e.getY()<=this.btn.getY()+this.btn.getImgHeight())){
            return true;
        }else{
            return false;
        }
    }
}
