package me.eddiep.android.pins.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.Log;

import java.util.HashMap;
import java.util.Random;

import me.eddiep.android.pins.Level;
import me.eddiep.android.pins.R;

public class GameLevel {

    protected int difficulty;
    protected Canvas frameOne;
    protected Canvas frameTwo;
    protected Bitmap frameOneBmp;
    protected Bitmap frameTwoBmp;

    public static Bitmap PIN_BITMAP;

    public static final Random RANDOM = new Random();
    public static final HashMap<Integer, Bitmap> CACHE = new HashMap<Integer, Bitmap>();

    public GameLevel(int difficulty) {
        this.difficulty = difficulty;
    }

    public static void setup(Context context) {
        PIN_BITMAP = BitmapFactory.decodeResource(context.getResources(), R.drawable.pins);
    }

    public int getDifficulty() {
        return difficulty;
    }

    public Drawable getFrameOne(Context context) {
        return new BitmapDrawable(context.getResources(), frameOneBmp);
    }

    public void generate(int width, int height) {
        frameOneBmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        frameTwoBmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        int pin_num = RANDOM.nextInt((difficulty + 4) - 3) + 3;

        Loc[] locations = new Loc[pin_num];
        Loc[] frameTwoLocations = new Loc[pin_num];

        int rand_pin = RANDOM.nextInt(locations.length);

        for (int i = 0; i < pin_num; i++) {
            Loc loc = new Loc();
            loc.x = RANDOM.nextInt(width);
            loc.y = RANDOM.nextInt(height);
            loc.rot = RANDOM.nextInt(360);
            loc.color =  Color.argb(1, RANDOM.nextInt(255), RANDOM.nextInt(255), RANDOM.nextInt(255));

            locations[i] = loc;
            if (rand_pin == i) {
                Loc clone = loc.clone();
                int type = RANDOM.nextInt(4);
                switch (type) {
                    case 0:
                        clone.rot += 0;
                        break;
                    case 1:
                        clone.rot += 90;
                        break;
                    case 2:
                        clone.rot += 180;
                        break;
                    case 3:
                        clone.rot += 270;
                        break;
                }

                frameTwoLocations[i] = clone;
            } else frameTwoLocations[i] = loc;
        }

        this.frameOne = new Canvas(frameOneBmp);

        for (Loc loc : locations) {
            Bitmap rotated = rotate(loc.rot);

            Paint paint = new Paint(loc.color);
            ColorFilter filter = new LightingColorFilter(loc.color, 1);
            paint.setColorFilter(filter);

            this.frameOne.drawBitmap(rotated, loc.x, loc.y, paint);
        }

        this.frameTwo = new Canvas(frameTwoBmp);

        for (Loc loc : frameTwoLocations) {
            Bitmap rotated = rotate(loc.rot);

            Paint paint = new Paint(loc.color);
            ColorFilter filter = new LightingColorFilter(loc.color, 1);
            paint.setColorFilter(filter);

            this.frameTwo.drawBitmap(rotated, loc.x, loc.y, paint);
        }
    }

    private Bitmap rotate(int angle) {
        if (!CACHE.containsKey(angle)) {
            Matrix matrix = new Matrix();
            matrix.postRotate(angle);

            Bitmap toSave = Bitmap.createBitmap(PIN_BITMAP, 0, 0, PIN_BITMAP.getWidth(), PIN_BITMAP.getHeight(), matrix, true);

            CACHE.put(angle, toSave);

            return toSave;
        }

        return CACHE.get(angle);
    }

    public Drawable getFrameTwo(Context context) {
        return new BitmapDrawable(context.getResources(), frameTwoBmp);
    }

    private class Loc {
        public int x, y, rot, color;

        public Loc clone() {
            Loc loc = new Loc();
            loc.x = x;
            loc.y = y;
            loc.rot = rot;
            loc.color = color;

            return loc;
        }
    }
}
