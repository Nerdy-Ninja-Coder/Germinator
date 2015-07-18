package com.millcitymobiledesigns.germinator;

import android.graphics.Canvas;

/**
 * Created by Avery Wold on 7/18/15.
 *
 * This class contains the main thread for the game loop
 */
public class GameLoopThread extends Thread {

    static final long FPS = 20;

    private MainGamePanel view;
    private boolean running = false;

    public GameLoopThread(MainGamePanel view) {
        this.view = view;
    }

    public void setRunning(boolean run) {
        running = run;
    }

    @Override
    public void run() {
        long ticksPS = 1000 / FPS;
        long startTime;
        long sleepTime;

        while (running) {
            Canvas canvas = null;
            startTime = System.currentTimeMillis();
            try {
                canvas = view.getHolder().lockCanvas();
                synchronized (view.getHolder()) {
                    if(running)
                        view.myDraw(canvas);
                }
            } finally {
                if (canvas != null) {
                    view.getHolder().unlockCanvasAndPost(canvas);
                }
            }
            sleepTime = ticksPS-(System.currentTimeMillis() - startTime);
            try {
                if (sleepTime > 0)
                    sleep(sleepTime);
                else
                    sleep(10);
            } catch (Exception e) {}

        }
    }
}
