package com.millcitymobiledesigns.germinator;

import android.graphics.Bitmap;

import java.util.Random;

/**
 * Created by Avery Wold on 7/18/15.
 *
 * This class creates the germs and moves them
 */
public class Germ extends AnimatedBitmap {

    public static final int D_GERM_ID = 1;
    private int id;
    private Bitmap germs;
    private Bitmap germ = null;
    private int IMG_PIXELS;
    private int disp_height, disp_width;

    private float 	x, y;
    private double 	angle_step;
    private float 	angle;
    private int 	speed_y;
    private int 	angle_max;

    private  float germ_scale;

    public Germ(Bitmap bmp, int display_height, int display_width) {
        germs = bmp;

        IMG_PIXELS = germs.getWidth() / 4;
        Random random = new Random();

        // Initializing Fields/
        disp_height = display_height;
        disp_width = display_width;

        // Get random Germ from Germ bitmap matrix
        id = random.nextInt(16);

        // Initializing x, y position
        x = random.nextInt(disp_width - IMG_PIXELS);
        y = display_height;

        // Initializing scale
        germ_scale = ((float)random.nextInt(6)) / 6f + 1f;

        // Initializing speed
        speed_y = (int)(germ_scale * 15);

        // Initializing rotation
        angle_step = ((double)random.nextInt(628)) / 100;
        angle_max = random.nextInt(60);

        // Initializing the Bitmap
        int bx = (id % 4)*IMG_PIXELS;
        int by = (id / 4)*IMG_PIXELS;

        germ = Bitmap.createBitmap(germs, bx, by, IMG_PIXELS, IMG_PIXELS, null, false);

        setBitmap(D_GERM_ID, germ, true);
    }

    public void addWind(float w){

        x += w * germ_scale;
        if (x < 0)
            x = 0;
        if (x > disp_width - IMG_PIXELS)
            x = disp_width - IMG_PIXELS;
    }

    @Override
    public void update() {

        //Updating position
        y = y - speed_y;
        if (y < 0 - germ.getHeight())
        {	stop();
            return;
        }

        setPosition(x,y);

        angle = (float)Math.sin(angle_step)*angle_max;
        angle_step += 0.05;
        setRotateCenter(angle);

        setScale(germ_scale, germ_scale);

        super.update();
    }
}
