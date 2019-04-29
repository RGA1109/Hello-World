package com.example.fish;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class PlayerMovementView extends View {
    private Bitmap backgroundImage;
    private Bitmap brick[] = new Bitmap[2];
    private Bitmap enemy[] = new Bitmap[2];
    private Bitmap gold[] = new Bitmap[2];
    private Bitmap life[] = new Bitmap[3];
    private Bitmap player[] = new Bitmap[5];
    private Bitmap stick[] = new Bitmap[2];
    private Paint scorePaintStick = new Paint();
    private Paint scorePaintBrick = new Paint();
    private Paint scorePaintGold = new Paint();
    private int canvasWidth = 1, canvasHeight = 1;
    private int enemyX, enemyY, enemySpeed = 5;
    private int greenX, greenY, greenSpeed = 15;
    private int lifeCounterOfFish;
    private int invulnerability;
    private int doubleItemSpell = 10;
    private int lifeRegain;
    private int playerSpeed;
    private int playerX, playerY;
    private int redX, redY, redSpeed = 15;
    private int scoreBrick;
    private int scoreGold;
    private int scoreStick;
    private int yellowX, yellowY, yellowSpeed = 15;
    private boolean touchplayer = false;

    public PlayerMovementView(Context context)
    {
        super(context);

        //Bitmap map images
        backgroundImage = BitmapFactory.decodeResource(getResources(), R.drawable.background);
        brick[0] = BitmapFactory.decodeResource(getResources(), R.drawable.brick);
        brick[1] = BitmapFactory.decodeResource(getResources(), R.drawable.brickx2);
        enemy[0] = BitmapFactory.decodeResource(getResources(), R.drawable.knife);
        gold[0] = BitmapFactory.decodeResource(getResources(), R.drawable.gold);
        gold[1] = BitmapFactory.decodeResource(getResources(), R.drawable.goldx2);
        life[0] = BitmapFactory.decodeResource(getResources(), R.drawable.heart);
        life[1] = BitmapFactory.decodeResource(getResources(), R.drawable.broken);
        player[0] = BitmapFactory.decodeResource(getResources(), R.drawable.player1);
        player[1] = BitmapFactory.decodeResource(getResources(), R.drawable.player2);
        player[2]= BitmapFactory.decodeResource(getResources(), R.drawable.invencibility);
        player[3] = BitmapFactory.decodeResource(getResources(), R.drawable.powerupfront);
        stick[0] = BitmapFactory.decodeResource(getResources(), R.drawable.stick);
        stick[1] = BitmapFactory.decodeResource(getResources(), R.drawable.stickx2);

        //Stick icon info
        scorePaintStick.setColor(Color.BLACK);
        scorePaintStick.setTextSize(70);
        scorePaintStick.setTypeface(Typeface.DEFAULT_BOLD);
        scorePaintStick.setAntiAlias(true);

        //Brick icon info
        scorePaintBrick.setColor(Color.BLACK);
        scorePaintBrick.setTextSize(70);
        scorePaintBrick.setTypeface(Typeface.DEFAULT_BOLD);
        scorePaintBrick.setAntiAlias(true);

        //Gold icon info
        scorePaintGold.setColor(Color.BLACK);
        scorePaintGold.setTextSize(70);
        scorePaintGold.setTypeface(Typeface.DEFAULT_BOLD);
        scorePaintGold.setAntiAlias(true);

        //Player directional starting spot
        playerX = 0;
        playerY = 1100;
        lifeCounterOfFish = 3;
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        int minPlayerX = player[0].getWidth();
        int maxPlayerX = canvasWidth - player[0].getWidth();
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


        //Movement for the the gold item
        yellowY = yellowY - yellowSpeed;
        if (hitBallChecker(yellowX, yellowY))
        {
            if(scoreGold > 8) {
                scoreGold = scoreGold + 3;
            }
            else{
                scoreGold = scoreGold + 1;
            }
            yellowY =- 100;
        }

        if (yellowY < 0)
        {
            yellowY = canvasHeight + 21;
            yellowX = (int) Math.floor(Math.random() * (maxPlayerX - minPlayerX)) + minPlayerX;
        }
        if (scoreGold == 0 || scoreGold == 1 || scoreGold == 2 || scoreGold == 3 || scoreGold == 4 ||
                scoreGold == 5 || scoreGold == 6 || scoreGold == 7) {
            canvas.drawBitmap(gold[0], yellowX, yellowY, null);
        }
        else
            canvas.drawBitmap(gold[1], yellowX, yellowY,null);

        // Movement for the Brick item
        greenY = greenY - greenSpeed;
        if (hitBallChecker(greenX, greenY))
        {
            if(scoreBrick > 8) {
                scoreBrick = scoreBrick + 3;
            }
            else {
                scoreBrick = scoreBrick + 1;
            }
                greenY =- 100;
        }

        if (greenY < 0)
        {
            greenY = canvasHeight + 21;
            greenX = (int) Math.floor(Math.random() * (maxPlayerX - minPlayerX)) + minPlayerX;
        }
        if (scoreBrick == 0 || scoreBrick == 1 || scoreBrick == 2 || scoreBrick == 3 ||
                scoreBrick == 4 || scoreBrick == 5 || scoreBrick == 6 || scoreBrick == 7) {
            canvas.drawBitmap(brick[0], greenX, greenY, null);
        }
        else
            canvas.drawBitmap(brick[1], greenX, greenY, null);

        //Movement for the Stick item
        redY = redY - redSpeed;
        if (hitBallChecker(redX, redY))
        {   if(scoreStick > 8)
            {
                scoreStick = scoreStick + 3;
            }
            else {
                scoreStick = scoreStick + 1;
            }
            redX =- 100;
        }

        if (redY < 0)
        {
            redY = canvasHeight + 21;
            redX = (int) Math.floor(Math.random() * (maxPlayerX - minPlayerX)) + minPlayerX;
        }

        if (scoreStick == 0 || scoreStick == 1 || scoreStick == 2 || scoreStick == 3 || scoreStick == 4 ||
                scoreStick == 5 || scoreStick == 6 || scoreStick == 7) {
            canvas.drawBitmap(stick[0], redX, redY, null);
        }
        else
            canvas.drawBitmap(stick[1], redX, redY,null);

        // enemy speed counter and checker for if an enemy icon is touched
        enemyY = enemyY - enemySpeed;
        if(hitBallChecker(enemyX, enemyY))
        {
            scoreStick = scoreStick - 1;
            scoreGold = scoreGold - 1;
            scoreBrick = scoreBrick - 1;
            enemyY =- 100;
            lifeCounterOfFish--;

            if (lifeCounterOfFish == 0)
            {
                Toast.makeText(getContext(), "Game Over", Toast.LENGTH_SHORT).show();

                Intent gameOverIntent = new Intent(getContext(), GameOver.class);
                gameOverIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                getContext().startActivity(gameOverIntent);

            }
        }

        if (enemyY < 0)
        {
            enemyY = canvasHeight + 21;
            enemyX = (int) Math.floor(Math.random() * (maxPlayerX - minPlayerX)) + minPlayerX;
        }
        canvas.drawBitmap(enemy[0], enemyX, enemyY, null);


        //Life hit box counter  keeps track of hits and switching the life icons when damaged
        for (int i=0; i<3; i++)
        {
            int x = (int) (580 + life[0].getWidth() * 1.5 * i);
            int y = 30;
            if (scoreGold == 5 && i > 0){
                canvas.drawBitmap(life[1], x, y, null);
                i--;
                scoreGold -=5;
                lifeCounterOfFish++;
            }

            if (i < lifeCounterOfFish)
            {

                canvas.drawBitmap(life[0], x, y, null);
            }
            else
            {
                canvas.drawBitmap(life[1], x, y, null);
            }
        }


        //Scores for the number of items collected while moving on the screen
        canvas.drawText("Stick - " + scoreStick,20,60, scorePaintStick);
        canvas.drawText("Bricks - " + scoreBrick,20,120, scorePaintBrick);
        canvas.drawText("Gold - " + scoreGold,20,180, scorePaintGold);
    }


    //function check to see if the player has touched one of the items on the screen
    public boolean hitBallChecker(int x, int y)
    {
        if (playerX < x && x < (playerX + player[0].getWidth()) && playerY < y && y < (playerY + player[0].getHeight()))
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
        return true;
    }

}
