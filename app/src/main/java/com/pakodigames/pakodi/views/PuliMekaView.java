package com.pakodigames.pakodi.views;

import android.animation.Animator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.pakodigames.pakodi.R;
import com.pakodigames.pakodi.model.Player;
import com.pakodigames.pakodi.model.PuliMekaRoom;

import java.util.ArrayList;
import java.util.List;

public class PuliMekaView extends View {

    private static String ANIM_X = "X_ANIM";
    private static String ANIM_Y= "Y_ANIM";
    private List<PointF> pointsList;
    //private List<Integer> boardState;
    private PuliMekaRoom puliMekaRoom;
    private boolean isAnimation;
    private float animPosX;
    private float animPosY;
    private int animChar;
    private Paint paint;
    private IPositionClickListener listener;
    private int selectedPosition = -1;
    private int killPosition = -1;
    private Bitmap lamb;
    private Bitmap lion;

    public PuliMekaView(Context context) {
        super(context);
    }

    public PuliMekaView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setColor(Color.BLUE);
//        boardState = new ArrayList();
//        for(int i=0;i<23;i++){
//            boardState.add(0);
//        }
//        boardState.set(0,1);
//        boardState.set(3,1);
//        boardState.set(4,1);
//
//        boardState.set(7,2);
//        boardState.set(8,2);
//        boardState.set(9,2);
//        boardState.set(10,2);
//        boardState.set(12,2);
//        boardState.set(13,2);
//        boardState.set(14,2);
//        boardState.set(15,2);
//        boardState.set(17,2);
//        boardState.set(18,2);
//        boardState.set(19,2);
//        boardState.set(22,2);
//        boardState.set(20,2);
//        boardState.set(21,2);
//        boardState.set(1,2);

        lamb = BitmapFactory.decodeResource(getResources(), R.drawable.lamb2);
        lion = BitmapFactory.decodeResource(getResources(), R.drawable.lion);
        lamb = Bitmap.createScaledBitmap(lamb,50,50,true);
        lion = Bitmap.createScaledBitmap(lion,50,50,true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        init(View.MeasureSpec.getSize(widthMeasureSpec)/100.0f - 2);
    }

    private void init(float h) {
        pointsList = new ArrayList();
        for(int i=0;i<23;i++){
           pointsList.add(getPoint(i,h));
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        for(int i=0; i<23; i++){
//            PointF p = getPoint(i,View.MeasureSpec.getSize(getWidth())-100);
//            canvas.drawCircle(p.x/100 + 50,p.y/100 +50,10,paint);
//        }
        if(puliMekaRoom == null){
            return;
        }
        canvas.translate(100,100);


        if(pointsList!=null) {
            canvas.drawLine(pointsList.get(0).x, pointsList.get(0).y, pointsList.get(19).x, pointsList.get(19).y, paint);
            canvas.drawLine(pointsList.get(0).x, pointsList.get(0).y, pointsList.get(20).x, pointsList.get(20).y, paint);
            canvas.drawLine(pointsList.get(0).x, pointsList.get(0).y, pointsList.get(21).x, pointsList.get(21).y, paint);
            canvas.drawLine(pointsList.get(0).x, pointsList.get(0).y, pointsList.get(22).x, pointsList.get(22).y, paint);
            canvas.drawLine(pointsList.get(19).x, pointsList.get(19).y, pointsList.get(22).x, pointsList.get(22).y, paint);

            canvas.drawLine(pointsList.get(19).x, pointsList.get(19).y/3, pointsList.get(22).x, pointsList.get(19).y/3, paint);
            canvas.drawLine(pointsList.get(19).x, pointsList.get(19).y/2, pointsList.get(22).x, pointsList.get(19).y/2, paint);
            canvas.drawLine(pointsList.get(19).x, pointsList.get(19).y*0.66f, pointsList.get(22).x, pointsList.get(22).y*0.66f, paint);

            canvas.drawLine(pointsList.get(19).x, pointsList.get(19).y/3, pointsList.get(19).x, pointsList.get(19).y*0.66f, paint);
            canvas.drawLine(pointsList.get(22).x, pointsList.get(19).y/3, pointsList.get(22).x, pointsList.get(22).y*0.66f, paint);
        }
        for(int i=0;i<23;i++){
            if(selectedPosition == i && puliMekaRoom.getCellAt(i) != 0){
                canvas.drawCircle(pointsList.get(i).x, pointsList.get(i).y,50,paint);
            }
            if(puliMekaRoom.getCellAt(i) == PuliMekaRoom.TIGER){
                canvas.drawBitmap(lion,pointsList.get(i).x-25, pointsList.get(i).y-25,paint);
            }else if(puliMekaRoom.getCellAt(i) == PuliMekaRoom.LAMB){
                canvas.drawBitmap(lamb,pointsList.get(i).x-25, pointsList.get(i).y-25,paint);
            }else{
                //canvas.drawCircle(pointsList.get(i).x, pointsList.get(i).y,30,paint);
            }
        }
        if(isAnimation && selectedPosition != -1){
            if(animChar == PuliMekaRoom.TIGER){
                canvas.drawBitmap(lion,animPosX-50,animPosY-50,paint);
            }else if(animChar == PuliMekaRoom.LAMB){
                canvas.drawBitmap(lamb,animPosX-50,animPosY-50,paint);
            }
        }
        canvas.translate(-100,-100);
    }

    public PointF getPoint(int p, float h){
        switch (p){
            case 0:
                return new PointF(50.0f*h,0.0f*h);
            case 1:
                return new PointF(0.0f*h,33.33f*h);
            case 2:
                return new PointF(33.33f*h,33.33f*h);
            case 3:
                return new PointF(44.44f*h,33.33f*h);
            case 4:
                return new PointF(55.55f*h,33.33f*h);
            case 5:
                return new PointF(66.67f*h,33.33f*h);
            case 6:
                return new PointF(100.0f*h,33.33f*h);
            case 7:
                return new PointF(0.0f*h,50.0f*h);
            case 8:
                return new PointF(25.0f*h,50.0f*h);
            case 9:
                return new PointF(41.67f*h,50.0f*h);
            case 10:
                return new PointF(58.33f*h,50.0f*h);
            case 11:
                return new PointF(75.0f*h,50.0f*h);
            case 12:
                return new PointF(100.0f*h,50.0f*h);
            case 13:
                return new PointF(0.0f*h,66.66f*h);
            case 14:
                return new PointF(16.67f*h,66.66f*h);
            case 15:
                return new PointF(38.89f*h,66.66f*h);
            case 16:
                return new PointF(61.11f*h,66.66f*h);
            case 17:
                return new PointF(83.33f*h,66.66f*h);
            case 18:
                return new PointF(100.0f*h,66.66f*h);
            case 19:
                return new PointF(0.0f*h,100.0f*h);
            case 20:
                return new PointF(33.33f*h,100.0f*h);
            case 21:
                return new PointF(66.66f*h,100.0f*h);
            case 22:
                return new PointF(99.99f*h,100.0f*h);
        }
        return new PointF(50f,0f);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch(event.getActionMasked()){
            case MotionEvent.ACTION_UP:
                float x = event.getX();
                float y = event.getY();
                int point = getNearPoint(x,y);
                if(listener != null){
                    onClicked(point);
                }
                Log.i("onTouchEvent ", "Point ->"+ point );
        }
        return true;
    }

    public void setPuliMekaRoom(PuliMekaRoom room){
        this.puliMekaRoom = room;
        invalidate();
    }

    public PuliMekaRoom getPuliMekaRoom(){
        return puliMekaRoom;
    }
    private int getNearPoint(float x, float y) {
        for(int i=0;i<pointsList.size();i++){
            if(Math.abs(pointsList.get(i).x+100-x) <= 50 && Math.abs(pointsList.get(i).y+100-y) <= 50){
                return i;
            }
        }
        return -1;
    }

    public void setOnPositionClickListener(IPositionClickListener listener){
        this.listener = listener;
    }

    public void animatePosition(final int from, final int to){
        animChar = puliMekaRoom.getCellAt(from);
        if(animChar == 0){
            return;
        }
        PointF start = getPoint(from,getHeight()/100 -1);
        PointF end = getPoint(to,getHeight()/100 -1);
        PropertyValuesHolder propertyXPos = PropertyValuesHolder.ofFloat(ANIM_X, start.x,end.x);
        PropertyValuesHolder propertyYPos = PropertyValuesHolder.ofFloat(ANIM_Y, start.y,end.y);

        ValueAnimator animator = new ValueAnimator();
        animator.setValues(propertyXPos, propertyYPos);
        animator.setDuration(500);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                animPosX = (float) animation.getAnimatedValue(ANIM_X);
                animPosY = (float) animation.getAnimatedValue(ANIM_Y);
                Log.i("ANIMATION " , "X --> "+animPosX + "\n Y --> "+animPosY);
                invalidate();
            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                puliMekaRoom.setCellAt(selectedPosition,0);
                isAnimation = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isAnimation = false;
                selectedPosition = -1;
                puliMekaRoom.setCellAt(to,animChar);
                puliMekaRoom.killLamb(killPosition);
                invalidate();
                killPosition = -1;
                listener.onPositionClickListener(-1);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.start();
    }

    public interface IPositionClickListener{
        public void onPositionClickListener(int position);
    }

    private void onClicked(int position){
        if(isAnimation || position == -1) return;
        if(!puliMekaRoom.getPlayerTurn().equals(Player.currentPlayer.getPlayerId())){
            return;
        }
        if(puliMekaRoom.currentTurn() == PuliMekaRoom.LAMB && puliMekaRoom.getLambsOutside() > 0){
            if(puliMekaRoom.addLambAt(position)) {
                listener.onPositionClickListener(-1);
            }else{
                return;
            }
        }else if(selectedPosition != -1 && position != -1 && puliMekaRoom.getCellAt(position) == 0){
            if(validate(selectedPosition,position)){
                animatePosition(selectedPosition,position);
            }

            //selectedPosition = -1;
        }else{
            if(position != -1 && puliMekaRoom.getCellAt(position) == puliMekaRoom.currentTurn()) {
                selectedPosition = position;
            }
        }
        invalidate();
    }

    private boolean validate(int start, int end) {
        if(puliMekaRoom.getAdjacentList(start).contains(end)){
            return true;
        }else if(puliMekaRoom.getCellAt(start) == PuliMekaRoom.TIGER && puliMekaRoom.getAdjacentJumpList(start).contains(end)){
            killPosition = puliMekaRoom.getKillPosition(start,end);
            return true;
        }
        return false;
    }
}
