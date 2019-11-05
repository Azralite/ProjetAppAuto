package perceptron;

import java.util.Random;
import mnisttools.MnistReader;



public class ImageOnlinePerceptron {

    public static int TAILLE = 22-10;
    /* Les donnees */
    public static String path="";
    public static String labelDB=path+"emnist-byclass-train-labels-idx1-ubyte";
    public static String imageDB=path+"emnist-byclass-train-images-idx3-ubyte";

    /**
     *  Na exemples pour l'ensemble d'apprentissage
     */
    public static final int Na = 5000;

    /**
     * Nv exemples pour l'ensemble d'évaluation
     */
    public static final int Nv = 1000;

    /**
     * Nt exemples pour l'ensemble de test
     */
    public static final int Nt = 0;

    /**
     * Nombre d'epoque max
     */
    public final static int EPOCHMAX=40;

    /**
     * Classes qui nous interessent:
     */
    public static int classeMin = 10 ;
    public static int classeMax = 21;

    /**
     * Générateur de nombres aléatoires
     */
    public static int seed = 1234;
    public static Random GenRdm = new Random(seed);

    /**
     * BinariserImage :
     * @param image une image int à deux dimensions (extraite de EMNIST)
     * @param seuil parametre pour la binarisation
     * @return on binarise l'image à l'aide du seuil indiqué
     */
    public static int[][] BinariserImage(int[][] image, int seuil) {
        int [][] res = new int[image.length][image[0].length];
        for (int i = 0; i<image.length;i++) {
            for(int j = 0; j <image[i].length;j++) {
                if(image[i][j] > seuil)
                    res[i][j]=1;
                else
                    res[i][j]=0;
            }
        }
        return res;
    }


    /**
     * ConvertImage :
     * @param image : une image int binarisée à deux dimensions
     * @return 1. on convertit l'image en deux dimension dx X dy, en un tableau unidimensionnel de tail dx.dy
     *         2. on rajoute un élément en première position du tableau qui sera à 1
     *         La taille finale renvoyée sera dx.dy + 1
     */
    public static float[] ConvertImage(int[][] image) {
        float [] res = new float[(image.length*image[0].length)+1];
        res[0] = 1;
        for (int i = 1; i < (image.length*image[0].length)+1; i++){
            res[i] = image[(i-1)/image.length][(i-1)%image[0].length];
        }
        return res;
    }

}