package com.example.fish;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.PopupWindow;

public class PlayerMovementView extends View {
    private Bitmap backgroundImage;
    private Bitmap player[] = new Bitmap[2];
    private Bitmap enemy[] = new Bitmap[2];
    private Bitmap Inventory[] = new Bitmap[2];
    private Bitmap gold[] = new Bitmap[2];
    private Bitmap brick[] = new Bitmap[2];
    private Bitmap stick[] = new Bitmap[2];
    private boolean touchplayer = false;
    private boolean touchinventory = false;
    private int playerX = 100;
    private int playerY = 0;
    private int InventoryX = 0;
    private int InventoryY = 0;
    private int playerSpeed;
    private int canvasWidth = 1, canvasHeight = 1;
    private int scoreBrick;
    private int scoreStick;
    private int scoreGold;
    private int yellowX, yellowY, yellowSpeed = 16;
    private int greenX, greenY, greenSpeed = 20;
    private int redX, redY, redSpeed = 25;
    private int enemyX, enemyY, enemySpeed = 10;
    private Paint scorePaintStick = new Paint();
    private Paint scorePaintBrick = new Paint();
    private Paint scorePaintGold = new Paint();

    public PlayerMovementView(Context context)
    {
        super(context);

        player[0] = BitmapFactory.decodeResource(getResources(), R.drawable.player1);
        player[1] = BitmapFactory.decodeResource(getResources(), R.drawable.player2);
        enemy[0] = BitmapFactory.decodeResource(getResources(), R.drawable.knife);
        gold[0] = BitmapFactory.decodeResource(getResources(), R.drawable.gold);
        stick[0] = BitmapFactory.decodeResource(getResources(), R.drawable.stick);
        brick[0] = BitmapFactory.decodeResource(getResources(), R.drawable.brick);
        Inventory[0] = BitmapFactory.decodeResource(getResources(), R.drawable.backpack);
        backgroundImage = BitmapFactory.decodeResource(getResources(), R.drawable.background);

        scorePaintStick.setColor(Color.BLACK);
        scorePaintStick.setTextSize(70);
        scorePaintStick.setTypeface(Typeface.DEFAULT_BOLD);
        scorePaintStick.setAntiAlias(true);

        //Brick
        scorePaintBrick.setColor(Color.BLACK);
        scorePaintBrick.setTextSize(70);
        scorePaintBrick.setTypeface(Typeface.DEFAULT_BOLD);
        scorePaintBrick.setAntiAlias(true);

        //Gold
        scorePaintGold.setColor(Color.BLACK);
        scorePaintGold.setTextSize(70);
        scorePaintGold.setTypeface(Typeface.DEFAULT_BOLD);
        scorePaintGold.setAntiAlias(true);

        //Player directional starting spot
        playerX = 0;
        playerY = 1100;
        InventoryX = 800;
        InventoryY = 50;
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        int minPlayerX = player[0].getWidth();
        int maxPlayerX = canvasWidth - player[0].getWidth() * 3;

        super.onDraw(canvas);
        canvasWidth = canvas.getWidth();
        canvasHeight = canvas.getHeight();
        canvas.drawBitmap(backgroundImage, 0 , 0,  null);


        // player movement in the x direction
        playerX = playerX + playerSpeed;
        if (playerX < minPlayerX)
        {
            playerX = minPlayerX;
        }
        if (playerX > maxPlayerX)
        {
            playerX = maxPlayerX;
        }
        playerSpeed = playerSpeed + 2;


        // on click player movement
        if (touchplayer)
        {
            canvas.drawBitmap(player[1], playerX, playerY, null);
            touchplayer = false;
        }
        else
        {
            canvas.drawBitmap(player[0], playerX, playerY,null);
        }

        if(touchinventory)
        {
            canvas.drawBitmap(Inventory[1], InventoryX, InventoryY, null);

            touchinventory = false;


        }
        else
        {
            canvas.drawBitmap(Inventory[0], InventoryX, InventoryY, null);
        }

        // Items movement across the screen in the x direction
        //Movement for the the gold item
        yellowY = yellowY - yellowSpeed;
        if (hitBallChecker(yellowX, yellowY))
        {
            scoreGold = scoreGold + 1;
            yellowY =- 100;
        }

        if (yellowY < 0)
        {
            yellowY = canvasHeight + 21;
            yellowX = (int) Math.floor(Math.random() * (maxPlayerX - minPlayerX)) + minPlayerX;
        }
        canvas.drawBitmap(gold[0], yellowX, yellowY,null);

        // Movement for the Brick item
        greenY = greenY - greenSpeed;
        if (hitBallChecker(greenX, greenY))
        {
            scoreBrick = scoreBrick + 1;

            greenY =- 100;
        }

        if (greenY < 0)
        {
            greenY = canvasHeight + 21;
            greenX = (int) Math.floor(Math.random() * (maxPlayerX - minPlayerX)) + minPlayerX;
        }
        canvas.drawBitmap(brick[0], greenX, greenY, null);

        //Movement for the Stick item
        redY = redY - redSpeed;
        if (hitBallChecker(redX, redY))
        {
            scoreStick = scoreStick + 1;
            redX =- 100;
        }

        if (redY < 0)
        {
            redY = canvasHeight + 21;
            redX = (int) Math.floor(Math.random() * (maxPlayerX - minPlayerX)) + minPlayerX;
        }
        canvas.drawBitmap(stick[0], redX, redY,null);

        enemyY = enemyY - enemySpeed;
        if(hitBallChecker(enemyX, enemyY))
        {
            scoreStick = scoreStick - 5;
            scoreGold = scoreGold - 5;
            scoreBrick = scoreBrick - 5;
            enemyY =- 100;
        }
        if (enemyY < 0)
        {
            enemyY = canvasHeight + 21;
            enemyX = (int) Math.floor(Math.random() * (maxPlayerX - minPlayerX)) + minPlayerX;
        }
        canvas.drawBitmap(enemy[0], enemyX, enemyY, null);

        //Scores for the number of items collected while moving on the screen
        canvas.drawText("Stick - " + scoreStick,20,60, scorePaintStick);
        canvas.drawText("Bricks - " + scoreBrick,20,120, scorePaintBrick);
        canvas.drawText("Gold - " + scoreGold,20,180, scorePaintGold);

    }

//    public void showPopup(View v){
//        PopupMenu popup = new PopupMenu(this, v);
//        MenuInflater inflater = popup.getMenuInflater();
//        inflater.inflate(R.menu.example_menu, popup.getMenu());
//        popup.show();
//
//    }

    //function check to see if the player has touched one of the items on the screen
    public boolean hitBallChecker(int x, int y)
    {
        if (playerX < x && x < (playerX + player[0].getWidth()))
        {
            return true;
        }
        return false;
    }

    // current player movement function, listens for a click and move the player a set distance
    // for each click.
    //TODO: make the movement go back and forth instead of just in one direction.
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if (event.getAction() == MotionEvent.ACTION_DOWN)
        {
            touchplayer = true;
            playerSpeed = -22;
        }
        if(event.getAction() == MotionEvent.ACTION_HOVER_ENTER)
        {
            touchinventory = true;
            if(event.getAction() == MotionEvent.ACTION_HOVER_EXIT)
            {
                touchinventory = false;
            }
        }

        return true;
    }

}
