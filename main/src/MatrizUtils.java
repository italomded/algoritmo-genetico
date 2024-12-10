import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MatrizUtils {
    public static Integer[][] generate(int numeroDeVertices, int distanciaMinima, int distanciaMaxima) {
        Integer[][] matriz = new Integer[numeroDeVertices][numeroDeVertices];
        Random random = new Random();

        for (int i = 0; i < numeroDeVertices; i++) {
            for (int j = 0; j < numeroDeVertices; j++) {
                matriz[i][j] = null;
            }
        }

        List<int[]> arestas = new ArrayList<>();
        for (int i = 1; i < numeroDeVertices; i++) {
            int origem = random.nextInt(i);
            arestas.add(new int[]{origem, i});
        }

        for (int[] aresta : arestas) {
            int origem = aresta[0];
            int destino = aresta[1];
            int distancia = random.nextInt(distanciaMaxima - distanciaMinima + 1) + distanciaMinima;
            matriz[origem][destino] = distancia;
            matriz[destino][origem] = distancia;
        }

        if (numeroDeVertices > 2) {
            int verticeA = random.nextInt(numeroDeVertices);
            int verticeB;
            do {
                verticeB = random.nextInt(numeroDeVertices);
            } while (verticeA == verticeB || matriz[verticeA][verticeB] != null);

            int distancia = random.nextInt(distanciaMaxima - distanciaMinima + 1) + distanciaMinima;
            matriz[verticeA][verticeB] = distancia;
            matriz[verticeB][verticeA] = distancia;
        }

        for (int i = 0; i < numeroDeVertices; i++) {
            matriz[i][i] = Integer.MAX_VALUE;
        }

        return matriz;
    }

    public static void copy(Integer[][] matrizAdjacencia, Integer[][] matrizView, int invalidWeight) {
        for (int i = 0; i < matrizAdjacencia.length; i++) {
            for (int j = 0; j < matrizAdjacencia[i].length; j++) {
                if (matrizView[i][j] == null) {
                    matrizAdjacencia[i][j] = invalidWeight;
                } else {
                    matrizAdjacencia[i][j] = matrizView[i][j];
                }
            }
        }
    }

    public static void show(Integer[][] matriz) {
        System.out.println("\n");
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length; j++) {
                System.out.print(matriz[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println("\n");
    }

    public static void showGeneratedGraph(Integer[][] matriz) {
        JFrame frame = new JFrame("Grafo de Adjacência");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 600);

        GrafoPanel painelGrafo = new GrafoPanel(matriz);
        frame.add(painelGrafo);

        frame.setVisible(true);
    }
}
