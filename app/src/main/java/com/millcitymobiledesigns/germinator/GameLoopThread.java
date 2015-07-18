package com.millcitymobiledesigns.germinator;

import android.graphics.Canvas;

/**
 * Created by averywold on 7/18/15.
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
            Canvas c = null;
            startTime = System.currentTimeMillis();
            try {
                c = view.getHolder().lockCanvas();
                synchronized (view.getHolder()) {
                    if(running)
                        view.myDraw(c);
                }
            } finally {
                if (c != null) {
                    view.getHolder().unlockCanvasAndPost(c);
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
