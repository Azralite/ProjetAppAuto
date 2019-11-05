package perceptron;

import mnisttools.MnistReader;

import static perceptron.ImageOnlinePerceptron.*;
import static perceptron.PerceptronMulti.*;

public class Principal {


    public static void main(String[] args) {
        System.out.println("# Load the database !");
        /* Lecteur d'image */
        MnistReader db = new MnistReader(labelDB, imageDB);


        /* Tableau où stocker les données */
        float[][] trainData = new float[Na][(db.getImage(1).length * db.getImage(1)[0].length) + 1];
        int[] trainRefs = new int[Na];
        float[][] validData = new float[Nv][(db.getImage(1).length * db.getImage(1)[0].length) + 1];
        int[] validRefs = new int[Nv];
        int compteur = 1;
        int nbElem = 0;

        //On remplie notre tableau d'entrainement avec les Na premieres images qui correspondent a une lettre entre le A et le L
        while (nbElem < Na){
            if (db.getLabel(compteur) <= classeMax && db.getLabel(compteur) >= classeMin){
                int[][] image = db.getImage(compteur );
                int label = db.getLabel(compteur);
                int[][] imageBinarise = ImageOnlinePerceptron.BinariserImage(image, 128);
                trainData[nbElem] = ImageOnlinePerceptron.ConvertImage(imageBinarise);
                trainRefs[nbElem] = label-10;
                compteur++;
                nbElem++;
            }
            else {
                compteur++;
            }
        }

        //On remplie notre tableau de validation avec les Nv premieres images qui correspondent a une lettre entre le A et le L
        while (nbElem < Nv + Na){
            if (db.getLabel(compteur) <= classeMax && db.getLabel(compteur) >= classeMin){
                int[][] image = db.getImage(compteur);
                int label = db.getLabel(compteur);
                int[][] imageBinarise = ImageOnlinePerceptron.BinariserImage(image, 128);
                validData[nbElem-Na] = ImageOnlinePerceptron.ConvertImage(imageBinarise);
                validRefs[nbElem-Na] = label-10;
                compteur++;
                nbElem++;
            }
            else {
                compteur++;
            }
        }

        float[] erreurVal = new float[EPOCHMAX];
        float[] erreurTrain = new float[EPOCHMAX];
        float[] tabCoup = new float[EPOCHMAX];
        int[][] matConf = new int[TAILLE][TAILLE];

        int dim = (db.getImage(1).length * db.getImage(1)[0].length) + 1;
        float eta = 5 * (float) (Math.pow(10, -3));

        float[][] poids = InitialiseW2(dim, eta);

        for (int i = 0; i < EPOCHMAX; i++) {
            //tabCoup[i] = costFunctionTotal(poids, trainData, trainRefs);
            perceptronM(trainData, poids, trainRefs, eta);
            erreurVal[i] = (verifPoint(validData, poids, validRefs)/(float)Nv)*100;
            erreurTrain[i] = (verifPoint(trainData, poids, trainRefs)/(float)Na)*100;
        }
//        ecriErreurVal(erreurVal);
//        ecriErreurTrain(erreurTrain);
//        ecriCout(tabCoup, eta);
//        ecriScript(eta);

        //Creer la matrice de confusion
        for (int i=0 ; i < Nv; i++){
            matConfusion(matConf, validData[i], validRefs[i], poids);
        }
        ecritMat(matConf);
    }
}
