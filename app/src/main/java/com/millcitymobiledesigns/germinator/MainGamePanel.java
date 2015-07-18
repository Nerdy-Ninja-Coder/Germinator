package com.millcitymobiledesigns.germinator;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.media.AudioManager;
import android.media.SoundPool;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by averywold on 7/18/15.
 */
public class MainGamePanel extends SurfaceView implements SurfaceHolder.Callback {

    private static int D_INITIAL_GERMS = 5;
    private SurfaceHolder mHolder;
    private GameLoopThread mGameLoopThread;

    private Bitmap mGerms;
    private Bitmap mBitmap_Star;
    private Group mGroup;

    private Shader mShader;
    private Paint  mPaintShader;

    private int mScore = 0;
    private Paint mPaintText;

    private float[] acc = new float[3];

    private SoundPool mSoundPool;
    private int	mSoundID;

    public MainGamePanel(Context context) {
        super(context);

        mHolder = getHolder();
        mHolder.addCallback(this);

        mBitmap_Star = BitmapFactory.decodeResource(context.getResources(), R.drawable.star);
        mGerms = BitmapFactory.decodeResource(context.getResources(), R.drawable.germs);

        mGroup = new Group();
        mPaintText = new Paint();
        mPaintText.setColor(Color.WHITE);
        mPaintText.setTextSize(32);

        mSoundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        //mSoundID = mSoundPool.load(context, R.raw.waterballoon, 1);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder mHolder) {
        boolean retry = true;
        mGameLoopThread.setRunning(false);
        while (retry) {
            try {
                mGameLoopThread.join();
                retry = false;
            } catch (InterruptedException e) {
            }
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder mHolder) {
        mGameLoopThread = new GameLoopThread(this);
        mGameLoopThread.setRunning(true);
        mGameLoopThread.start();

        mShader = new LinearGradient(0, 0, 0, getHeight(), Color.rgb(0, 0, 64), Color.RED, Shader.TileMode.CLAMP);
        mPaintShader = new Paint();
        mPaintShader.setShader(mShader);

        initGroup();

    }

    @Override
    public void surfaceChanged(SurfaceHolder mHolder, int format, int width, int height) {
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        synchronized (getHolder()) {

            if( event.getActionMasked() == MotionEvent.ACTION_DOWN ||
                    event.getActionMasked() == MotionEvent.ACTION_POINTER_DOWN){

                float x = event.getX(event.getActionIndex());
                float y = event.getY(event.getActionIndex());
                //Removing Germ if clicked
                int index = mGroup.getClickedIndex(x, y);
                if (index != -1){
                    mGroup.remove(index);
                    mScore++;

                    //Commit: adding play sound when Germ pops
                    mSoundPool.play(mSoundID, 1.0f, 1.0f, 0, 0, 1.0f);

                }else {
                    //Adding Germ if clicking outside
//                    MainActivity main = (MainActivity)getContext();
//                    if (main.isConnected() == false) {
//                        addGerm();
//                    } else {
//                        main.sendMessage(MainActivity.D_ADD_GERM);
//                    }

                    //Adding Star if clicking outside
//                    Star star = new Star(mBitmap_Star, x, y);
//                    mGroup.add(star);
                }
                //}

            }

            if (mGroup.size() == 0)
                initGroup();
        }
        return true;
    }

    private void initGroup() {
        //Initial mGerms
        Germ[] germ = new Germ[D_INITIAL_GERMS];
        for (int i = 0; i < D_INITIAL_GERMS; i++){
            germ[i] = new Germ(mGerms, getHeight(), getWidth());
            mGroup.add(germ[i]);
        }
    }

    public void addGerm(){
        Germ germ = new Germ(mGerms, getHeight(), getWidth());
        mGroup.add(germ);
    }

    public void myDraw(Canvas canvas) {

        canvas.drawRect(new RectF(0, 0, getWidth(), getHeight()), mPaintShader);

        canvas.drawText("Score: "+mScore, 30, 30, mPaintText);
        mGroup.drawAll(canvas);
    }

    public void setAcceleration(float[] values){
        acc[0] = values[0];
        acc[1] = values[1];
        acc[2] = values[2];

        for (int i = 0; i < mGroup.size(); i++){
            if (mGroup.get(i).getIdentification() == Germ.D_GERM_ID) {
                ((Germ)mGroup.get(i)).addWind(acc[0]);
            }
        }
    }
}
