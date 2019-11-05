package perceptron;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.*;

import static perceptron.ImageOnlinePerceptron.*;

public class PerceptronMulti {

    /**
     * InitialiseW2: Fonction qui initialise le vecteur poids de manière aléatoire
     * @param sizeW : la taille du vecteur de poids
     * @param alpha : facteur à rajouter devant le nombre aléatoire
     * @return le vecteur poids initialisé à l'aide d'un générateur de nombres aléatoires
     */
    public static float[][] InitialiseW2(int sizeW, float alpha) {
        float [][] res = new float[TAILLE][sizeW];
        for (int j = 0; j < res.length; j++) {
            for (int i = 0; i < sizeW; i++) {
                res[j][i] = alpha * (GenRdm.nextFloat()-0.5f);
            }
        }
        return res;
    }

    /**
     * OneHot : Fonction qui creer le vector OneHot d'une etiquette
     * @param etiquette l'etiquette d'une donnée
     *                  elle provient de validRefs ou trainRefs
     * @return int[] le vecteur de dimension taille contenant que des 0 et un 1 à l'index de l'etiquette
     */
    public static int[] OneHot(int etiquette){
        int[] res = new int[TAILLE];
        if (etiquette < 0 || etiquette >= TAILLE) {
            System.out.println("Mauvaise etiquette, erreur OneHot");
            return res;
        }
        for (int i =0; i < res.length; i++){
            res[i] = 1;
        }
        res[etiquette]=0;
        return res;
    }

    /**
     * InfPerceptron :
     * @param donnee une image binarisée à une dimension representant une lettre
     * @param w l'ensemble des poids du perceptron
     * @return un vecteur de float contenant la probabilité d'appartenir à une classe
     */
    public static float[] InfPerceptron(float[] donnee, float[][] w){
        float[] res = new float[w.length];
        float[] tabNum = new float[w.length];
        float den = 0;
        for (int i = 0; i < w.length; i++){
            for (int j = 0; j < w[i].length; j++){
                tabNum[i] += donnee[j]*w[i][j];
            }
            tabNum[i] = (float) Math.pow(Math.E,tabNum[i]);
            den += tabNum[i];
        }
        for (int i =0; i < w.length; i++){
            res[i] = tabNum[i]/den;
        }
        return res;
    }

    /**
     * verifPoint :
     * @param tabPoint un ensemble d'image
     * @param w l'ensemble des poids du perceptron
     * @param refs l'ensemble des etiquettes de l'ensemble d'image
     * @return le nombre d'image mal interprété par le perceptron
     */
    public  static int verifPoint(float [][] tabPoint, float[][] w, int[] refs){
        int res = 0;
        for (int i = 0; i < tabPoint.length; i++) {
            float[] y = InfPerceptron(tabPoint[i], w);
            int[] p = OneHot(refs[i]);
            float yMax = 0;
            int indiceMax = 0;
            for (int l = 0 ; l < y.length; l++) {
                if(y[l] >yMax){
                    yMax = y[l];
                    indiceMax = l;
                }
            }
            if (p[indiceMax] != 1){
                res++;
            }
        }
        return res;
    }


    /**
     * perceptronM pour perceptron multiclasse:
     * La fonction perceptron va mettre a jour les poids du perceptron pour chaque image du jeu de donnée tabPoint
     * @param tabPoint un ensemble d'image
     * @param w l'ensemble des poids du perceptron
     * @param refs l'ensemble des etiquettes de l'ensemble d'image
     * @param eta le taux d'aprentissage
     */
    public static void perceptronM(float [][] tabPoint, float[][] w, int[] refs,float eta){
        for (int i = 0; i < tabPoint.length; i++) {
            float[] y = InfPerceptron(tabPoint[i], w);
            int[] p = OneHot(refs[i]);
            for (int l = 0 ; l < w.length; l++) {
                for (int j = 0; j < tabPoint[i].length; j++) {
                    w[l][j] = w[l][j] - tabPoint[i][j] * eta * (y[l]-p[l]);
                }
            }
        }
    }


    /**
     * ecrire fonction qui creer un fichier et ecrit des choses dedant
     * @param nomFic le nom du fichier qu'on veut créer
     * @param texte le texte qu'on met dans notre fichier
     */
    public static void ecrire(String nomFic, String texte) { //on va chercher le chemin et le nom du fichier et on me tout ca dans un String
        String adressedufichier = System.getProperty("user.dir") + "/"+ nomFic; //on met try si jamais il y a une exception
        try {
            FileWriter fw = new FileWriter(adressedufichier); // le BufferedWriter output auquel on donne comme argument le FileWriter fw cree juste au dessus
            BufferedWriter output = new BufferedWriter(fw);//on marque dans le fichier ou plutot dans le BufferedWriter qui sert comme un tampon(stream)
            output.write(texte); //on peut utiliser plusieurs fois methode write
            output.flush(); //ensuite flush envoie dans le fichier, ne pas oublier cette methode pour le BufferedWriter
            output.close(); //et on le ferme
            System.out.println("fichier créé");
        }
        catch(IOException ioe){
            System.out.print("Erreur : ");
            ioe.printStackTrace();
        }
    }


    /**
     * Ecrit les poids dans un fichier texte
     * @param poids l'ensemble des poids du perceptron
     */
    public static void ecriPoids(float[][] poids){
        String wStr = "";
        for (int i = 0; i < poids.length; i++){
            for (int j = 0; j < poids[i].length ; j++){
                wStr = wStr +" " + poids[i][j];
            }
            wStr = wStr +"\n";
        }
        ecrire("poids.txt" , wStr);
    }

    /**
     * Fonction qui ecrit les erreurs du jeu d'entrainement par rapport au nombre d'epoque
     * @param erreurVal le tableau d'erreur en rapport des epoques
     */
    public static void ecriErreurVal(float[] erreurVal){
        String texteD = "";
        for (int i = 0; i < erreurVal.length; i++){
            texteD = texteD + "" + erreurVal[i] + "\n";
        }
        ecrire("erreurVal.d", texteD);
    }

    /**
     * Fonction qui ecrit les erreurs du jeu de validation par rapport au nombre d'epoque
     * @param erreurTrain le tableau d'erreur en rapport des epoques
     */
    public static void ecriErreurTrain(float[] erreurTrain){
        String texteB = "";
        for (int i = 0; i < erreurTrain.length; i++){
            texteB = texteB + "" + erreurTrain[i] + "\n";
        }
        ecrire("erreurTrain.d", texteB);
    }

    /**
     * Fonction qui ecrit un script gnuplot pour afficher les deux graphiques
     * @param eta pour changer le nom du fichier en fonction de eta
     */
    public static void ecriScript(float eta){
        String texte = "set terminal pngcairo  \n" +
                "set output 'eta"+eta+".png' \n" +
                "set grid \n" +
                "set yrange [5:35] \n" +
                "set style data linespoints \n"+
                "plot 'erreurVal.d' , 'erreurTrain.d'";
        ecrire("script2.gnu", texte);
    }

    /**
     * costFunctionTotal Fonction de cout
     * @param poids l'ensemble des poids du perceptron
     * @param data un ensemble d'image
     * @param refs l'ensemble des etiquettes de l'ensemble d'image
     * @return la valeur de la fonction de cout
     */
    public static float costFunctionTotal(float[][] poids, float[][] data, int[] refs){
        float tmp;
        float[] tabRes = new float[data.length];
        for (int u =0; u < data.length; u++){
            tmp = 1;
            float[] y = InfPerceptron(data[u], poids);
            int[] p = OneHot(refs[u]);
            for (int i = 0; i < poids.length; i++) {
                tmp = tmp * (float)Math.pow(y[i],p[i]);
            }
            tabRes[u] = tmp;
        }
        float finalRes =0;
        for (int j =0; j < tabRes.length; j++ ){
            finalRes += Math.log(tabRes[j]);
        }
        finalRes = finalRes/tabRes.length;
        return finalRes;
    }

    /**
     * Fonction qui ecrit le cout en fonction du temps
     * @param cout le tableau avec toute les valeurs de cout
     * @param eta pour changer le nom du fichier en fonction de eta
     */
    public static void ecriCout(float[] cout, float eta){
        String texteC = "";
        for (int i = 0; i < cout.length; i++){
            texteC = texteC + "" + cout[i] + "\n";
        }
        ecrire("cout.d", texteC);

        String texte = "set terminal pngcairo  \n" +
                "set output 'cout"+ eta +".png' \n" +
                "set grid \n" +
                "set style data linespoints \n"+
                "plot 'cout.d'";
        ecrire("script1.gnu", texte);
    }

    /**
     * matConfusion : calcule et creer la matrice de confusion
     * @param mat la matrice de confusion vierge passée en référence
     * @param data une image
     * @param refs l'etiquette de l'image
     * @param w l'ensemble des poids du perceptron
     */
    public static void matConfusion(int[][] mat, float[] data, int refs, float[][] w){
        float[] y = InfPerceptron(data, w);
        float max = 0;
        int indiceMax = 0;
        for (int i = 0; i < y.length; i++){
            if(y[i] > max){
                max = y[i];
                indiceMax = i;
            }
        }
        mat[refs][indiceMax]++;
    }

    /**
     Fonction qui ecrit la matrice de confusion dans un fichier csv(excel)
     * @param mat le tableau bi-dimensionel contenant la matrice de confusion
     */
    public static void ecritMat(int[][] mat){
        String text  = "Classe Reel\\Classe Estimee;A;B;C;D;E;F;G;H;I;J;K;L;\n";
        for (int x = 0; x < mat.length; x++){
            switch (x){
                case 0: text = text + "A;";
                    break;
                case 1: text = text + "B;";
                    break;
                case 2: text = text + "C;";
                    break;
                case 3: text = text + "D;";
                    break;
                case 4: text = text + "E;";
                    break;
                case 5: text = text + "F;";
                    break;
                case 6: text = text + "G;";
                    break;
                case 7: text = text + "H;";
                    break;
                case 8: text = text + "I;";
                    break;
                case 9: text = text + "J;";
                    break;
                case 10: text = text + "K;";
                    break;
                case 11: text = text + "L;";
                    break;
            }
            for (int y =0; y < mat[x].length; y++){
                text = text + mat[x][y] + ";";
            }
            text = text+" \n";
        }
        ecrire("matConfu.csv", text);
    }
}
