# PolygonTransform
Polygon Transform for Picasso


This Transform sub class for Picasso Library for rendering Image. Use is like any other transform.
Constructor takes 3 argument.<br/><br/>
<b>public PolygonTransform(int targetWidth, int sides, boolean rotate);</b>
<br/><br/>1. Targetwidth: and maintains aspect ratio.
2. No. of sides 3 for triangle, 5 for pentagon, 6 for hexagon, .. 8 for octagon and so on.
4. To rotate 90 degree.

e.g
Picasso.from(context).load("imagUrl").transform(new PolygonTransform(100, 6, true)).into(imageView);


Contribution: 
<b>Thanks to</b>
Abhijit Mitkar &
Julian Shen for his CircleTransform reference.
