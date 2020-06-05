package com.pakodigames.pakodi.views;

import android.content.Context;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.pakodigames.pakodi.R;
import com.pakodigames.pakodi.model.Room;
import com.pakodigames.pakodi.model.TicTacToeRoom;

public class TicTacToeView extends View {

    private Context context;
    private TicTacToeRoom room;
    private Bitmap backgroundGrid;
    private Bitmap bitampCross;
    private Bitmap bitmapCircle;
    private IOnGridClickListener listener;
    private int height;
    private int width;

    public TicTacToeView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public TicTacToeView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.context = context;
        init();
    }

    public void init(){
        bitampCross = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.cross);
        bitmapCircle = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.circle);
        backgroundGrid = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.grid);
        setEnabled(true);
        setClickable(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        this.height = heightMeasureSpec;
        this.width = widthMeasureSpec;
    }

    public void onAddGridClickListener(IOnGridClickListener listener) {
        this.listener = listener;
    }

    protected void onDraw(Canvas canvas) {
        Paint background = new Paint();
        background.setColor(Color.WHITE);
        canvas.drawRect(0, 0, getWidth(), getHeight(), background);
//        canvas.drawBitmap(backgroundGrid, 10, 0, null);
        canvas.drawBitmap(backgroundGrid, null, new RectF(0, 0, getWidth(), getHeight()), null);
        if(room == null)
            return;
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++) {
                int pos = room.getGameState().get(i*3+j);
                if (pos == 1) {
                    canvas.drawBitmap(bitampCross, null, new RectF(j*(getWidth()/3),i*(getHeight()/3),(j+1)*(getWidth()/3),(i+1)*(getHeight()/3)), null);
//                    canvas.drawBitmap(bitampCross, j
//                            * (bitampCross.getWidth() + 20), i
//                            * (bitampCross.getHeight() + 15), null);
                }
                if (pos == 2) {
                    canvas.drawBitmap(bitmapCircle, null, new RectF(j*(getWidth()/3),i*(getHeight()/3),(j+1)*(getWidth()/3),(i+1)*(getHeight()/3)), null);
//                    canvas.drawBitmap(bitmapCircle, j
//                            * (bitmapCircle.getWidth() + 20), i
//                            * (bitmapCircle.getHeight() + 15), null);
                }
            }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        int winner = -1;
        boolean isValidMove = false;

        if (action == MotionEvent.ACTION_DOWN) {
            return true;

        } else if (action == MotionEvent.ACTION_UP) {
            int x = (int) event.getX();
            int y = (int) event.getY();

            // if game over reset grid, and begin new game
            /*if (game.checkGameFinished() != 0) {
                game.setGrid();
                invalidate();
                return false;
            }*/

            int bgWidth = getWidth()/ 3;
            int bgHeight = getHeight() / 3;

            x = x / bgWidth;
            y = y / bgHeight;
            Log.v("test x:y", String.valueOf(x) + " : " + String.valueOf(y));
            if(listener != null){
                listener.onGridClicked(y*3 +x);
            }
            /*if (x < 3 && x >= 0 && y < 3 && y >= 0) {
                isValidMove = game.setGridPosition(x, y, game.getPlayer());
            }
            if (isValidMove == true) {
                winner = game.checkGameFinished();

                if (winner == 0) {
                    game.setCurMove(game.getOponent());
                    game.computerMove();
                }
                winner = game.checkGameFinished();

                if (winner == 1) {
                    if (game.getPlayer() == 1) {
                        userWins++;
                    } else {
                        aiWins++;
                    }
                }
                if (winner == 2) {
                    if (game.getPlayer() == 2) {
                        userWins++;
                    } else {
                        aiWins++;
                    }
                } else if (winner == 3) {
                    userWins++;
                    aiWins++;
                }
            }*/
        }
        //invalidate();
        return false;
    }

    public void updateUI(TicTacToeRoom room) {
        this.room = room;
        invalidate();
    }

    public interface IOnGridClickListener {
        void onGridClicked(int position);
    }
}