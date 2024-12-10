import javax.swing.*;
import java.awt.*;

public class GrafoPanel extends JPanel {
    private final Integer[][] matrizAdjacencia;
    private final int numeroDeVertices;

    public GrafoPanel(Integer[][] matrizAdjacencia) {
        this.matrizAdjacencia = matrizAdjacencia;
        this.numeroDeVertices = matrizAdjacencia.length;
        setBackground(Color.BLACK);
    }

    private Color gerarCorAleatoria() {
        int r = (int) (Math.random() * 206) + 50;
        int g = (int) (Math.random() * 206) + 50;
        int b = (int) (Math.random() * 206) + 50;
        return new Color(r, g, b);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int raio = 20;
        int largura = getWidth();
        int altura = getHeight();

        double angulo = 2 * Math.PI / numeroDeVertices;

        int[] x = new int[numeroDeVertices];
        int[] y = new int[numeroDeVertices];

        for (int i = 0; i < numeroDeVertices; i++) {
            x[i] = (int) ((double) largura / 2 + ((double) largura / 3) * Math.cos(i * angulo));
            y[i] = (int) ((double) altura / 2 + ((double) altura / 3) * Math.sin(i * angulo));
        }

        java.util.List<int[]> textosDesenhados = new java.util.ArrayList<>();

        for (int i = 0; i < numeroDeVertices; i++) {
            for (int j = i + 1; j < numeroDeVertices; j++) {
                if (matrizAdjacencia[i][j] != null && matrizAdjacencia[i][j] != 0) {
                    Color corAresta = gerarCorAleatoria();

                    g.setColor(corAresta);
                    g.drawLine(x[i], y[i], x[j], y[j]);

                    int midX = (x[i] + x[j]) / 2;
                    int midY = (y[i] + y[j]) / 2;

                    int margem = 15;
                    boolean sobreposto;
                    do {
                        sobreposto = false;
                        for (int[] pos : textosDesenhados) {
                            int dx = pos[0] - midX;
                            int dy = pos[1] - midY;
                            double distancia = Math.sqrt(dx * dx + dy * dy);
                            if (distancia < 30) {
                                midX += margem;
                                midY += margem;
                                sobreposto = true;
                                break;
                            }
                        }
                    } while (sobreposto);

                    g.setColor(corAresta);
                    g.drawString(String.valueOf(matrizAdjacencia[i][j]), midX, midY);
                    g.setColor(Color.white);

                    textosDesenhados.add(new int[] {midX, midY});
                }
            }
        }

        g.setColor(Color.RED);
        for (int i = 0; i < numeroDeVertices; i++) {
            g.fillOval(x[i] - raio, y[i] - raio, 2 * raio, 2 * raio);
            g.setColor(Color.BLACK);
            g.drawString(String.valueOf(i), x[i] - raio / 2, y[i] + raio / 2);
            g.setColor(Color.RED);
        }
    }
}