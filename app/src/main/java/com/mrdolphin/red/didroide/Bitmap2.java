package com.mrdolphin.red.didroide;

/**
 * Created by Mr on 22.12.2016.
 */
import android.graphics.Color;
import android.graphics.Bitmap;

public class Bitmap2 {
    public Bitmap setWinWidthAndCenterForBitmap(Bitmap bmp, double defaultLength, double defaultWidth, double currentLength, double currentWidth)
    {
        /*
        double difLength = currentLength - defaultLength;
        double difWidth = currentWidth - defaultWidth;
        int WinLength = 127 + (int)difLength;
        int WinWidth = 255 + (int)difWidth;
        double wMin = WinLength - 0.5 - (WinWidth - 1) / 2;
        double wMax = WinLength - 0.5 + (WinWidth - 1) / 2;
        //int bmp2;
        Color color;
        for (int i = 0; i < bmp.getWidth(); i++)
        {
            for (int j = 0; j < bmp.getHeight(); j++)
            {
                color = bmp.(i, j);

                int R = Color.red(bmp2);
                int B = Color.blue(bmp2);
                int G = Color.green(bmp2);
                //int alpha = Color.alpha(bmp2);
                color = Color.rgb(
                        calculateColor(Color.red(R), wMin, wMax, WinLength, WinWidth),
                        calculateColor(Color.green(G), wMin, wMax, WinLength, WinWidth),
                        calculateColor(Color.blue(B), wMin, wMax, WinLength, WinWidth));
                //color = Color.FromArgb(
                //        calculateColor(color.R, wMin, wMax, WinLength, WinWidth),
                //        calculateColor(color.G, wMin, wMax, WinLength, WinWidth),
                 //       calculateColor(color.B, wMin, wMax, WinLength, WinWidth));
                bmp.setPixel(i, j, color);
            }
        }
        */
        return bmp;

    }
    private byte calculateColor(byte c, double wMin, double wMax, double WinLength, double WinWidth)
    {
        if (c <= wMin)
            return 0;
        else if (c > wMax)
            return (byte) 255;
        else
            return (byte)(((c - (WinLength - 0.5)) / (WinWidth - 1) + 0.5) * 255);
    }
}
