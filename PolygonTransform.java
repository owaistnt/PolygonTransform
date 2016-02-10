
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;

import com.squareup.picasso.Transformation;

/*
 * Copyright 2016 Owais Idris, Abhijit Mitkar
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class PolygonTransform implements Transformation {

    int targetWidth, sides;
    boolean rotate;

    private Point[] calculate(int width , int sides, boolean rotate) {
        int radius = width / 2;
        double angle = 0.0;
        double step = 2 * Math.PI / sides;
        Point points[] = new Point[sides];
        for (int i = 0; i < sides; i++) {
            Point point = new Point();
            if(rotate){
                point.x = (int) (radius * Math.sin(angle))+radius;//Added a radius value to shift polygon in 1st Quadrant.
                point.y = (int) (radius * Math.cos(angle))+radius;
            }else{
                point.x = (int) (radius * Math.cos(angle))+radius;//Added a radius value to shift polygon in 1st Quadrant.
                point.y = (int) (radius * Math.sin(angle))+radius;
            }
            points[i] = point;
            angle += step;
        }

      /*  Logger.i("HEXAGON", "Point start");
        for (int i = 0; i < points.length; i++) {
            Logger.i("HEXAGON", "Point:" + points[i].x + ", " + points[i].y);
        }
        Logger.i("HEXAGON", "Point end");*/
        return points;
    }


    //Reference formula.

   /* angle = 0.0;
    step = 2 * pi / 8;

    for ( n = 0; n < 8; n++ ) {
        x = radius * cos(angle);
        y = radius * sin(angle);
        angle += step;
    }*/


    /***
     * To draw polygons of any number of sides
     * @param targetWidth desired width
     * @param sides as for triangel/tetragon/pentagon/hexagon
     * @param rotate to rotate the shape.
     */
    public PolygonTransform(int targetWidth, int sides, boolean rotate) {

        this.targetWidth = targetWidth;
        this.sides=sides;
        this.rotate = rotate;
    }

    @Override
    public Bitmap transform(Bitmap source) {

       int targetWidth = this.targetWidth;
        double aspectRatio = 0.0D;
        if(source.getHeight()>source.getWidth() || source.getHeight()==source.getWidth()){
            aspectRatio=(double) source.getHeight() / (double) source.getWidth();
        }else{
            aspectRatio=(double) source.getWidth() / (double) source.getHeight();
        }
        Log.i("POLYGON TRANSFORM", "aspect ratio=" + aspectRatio);
        int targetHeight = (int) (targetWidth * aspectRatio);
        Log.i("POLYGON TRANSFORM", "width=" + targetWidth+" Height="+targetHeight);
        Bitmap sourceB = Bitmap.createScaledBitmap(source, targetWidth, targetHeight, false);
        if (sourceB != source) {
            // Same bitmap is returned if sizes are the same
            source.recycle();
        }
        int size = Math.min(sourceB.getWidth(), sourceB.getHeight());

        int x = (sourceB.getWidth() - size) / 2;
        int y = (sourceB.getHeight() - size) / 2;
        Bitmap squaredBitmap = Bitmap.createBitmap(sourceB, x, y, size, size);
        if (squaredBitmap != sourceB) {
            sourceB.recycle();
        }
        Bitmap bitmap = Bitmap.createBitmap(size, size, sourceB.getConfig());
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        BitmapShader shader = new BitmapShader(squaredBitmap, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
        paint.setShader(shader);
        paint.setAntiAlias(true);


        Point[] points = calculate(targetWidth, sides, rotate);
        Path path = new Path();
        path.reset();
        for (int i = 0; i < points.length; i++) {
            if (i == 0) {
                path.moveTo(points[i].x, points[i].y);
            } else {
                path.lineTo(points[i].x, points[i].y);
            }
        }
        path.lineTo(points[0].x, points[0].y);
        canvas.drawPath(path, paint);
        sourceB.recycle();
        return bitmap;
    }

    @Override
    public String key() {
        return "Polygon";
    }
}
