package com.dija.mareu1.utils;

import com.dija.mareu1.R;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class getColouredImage {

    public static int getImage() {
        List<Integer> coloredImages = Arrays.asList(R.drawable.pastel_rose, R.drawable.pastel_bleu, R.drawable.pastel_vert);
        Random rand = new Random();
        int couleur = coloredImages.get(rand.nextInt(coloredImages.size()));
        return couleur;
    }


}
