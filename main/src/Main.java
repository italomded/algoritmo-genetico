import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class Main {
    private static final Random RANDOM = new Random();
   
    private static final int VERTICES_GRAFO = 6;
    private static final int TAMANHO_POPULACAO_INICIAL = 20;
    private static final int VERTICE_DE_PARTIDA = 0;
    private static final Set<Integer> VERTICES_OBRIGATORIOS = Set.of(2, 3, 5);
   
    private static final double TAXA_MUTACAO = 0.05;
    private static final int PESO_CAMINHO_INVALIDO = 50000;
    
    private static List<Integer> MELHOR_INDIVIDUO = null;
    private static int APTIDAO_MELHOR_INDIVIDUO = 0;
    private static boolean COMPLETOU_ROTA = false;
    private static int GERACOES = 0;

    private static Double[] ROLETA_VICIADA;
    private static Integer[][] MATRIZ_ADJACENCIA;

    public static void main(String[] args) {
        final int ii = PESO_CAMINHO_INVALIDO;
        MATRIZ_ADJACENCIA = new Integer[VERTICES_GRAFO][VERTICES_GRAFO];
                                //   0   1   2   3   4   5
        MATRIZ_ADJACENCIA[0] = new Integer[]{ii, 30, 50, 20, ii, ii}; // 20, 15, 15, 20 = 70
        MATRIZ_ADJACENCIA[1] = new Integer[]{30, ii, 40, ii, ii, ii};
        MATRIZ_ADJACENCIA[2] = new Integer[]{50, 40, ii, 15, 30, 20};
        MATRIZ_ADJACENCIA[3] = new Integer[]{20, ii, 15, ii, ii, ii};
        MATRIZ_ADJACENCIA[4] = new Integer[]{ii, ii, 30, ii, ii, ii};
        MATRIZ_ADJACENCIA[5] = new Integer[]{ii, ii, 20, ii, ii, ii};


        List<List<Integer>> populacao = new ArrayList<>();
        while (populacao.size() != TAMANHO_POPULACAO_INICIAL) {
            List<Integer> inviduo = criarIndividuo();
//            System.out.println(inviduo);
            populacao.add(criarIndividuo());
        }

        while (true) {
            GERACOES++;

            final Map<Integer, Double> pupulacaoAptidao = calcularAptidaoPopulacao(populacao);
            montarRoletaViciada(pupulacaoAptidao);

            List<List<Integer>> novaPopulacao = new ArrayList<>();
            while (populacao.size() > novaPopulacao.size()) {
                final List<Integer> paiA = populacao.get(sorteio());
                final List<Integer> paiB = populacao.get(sorteio());
                novaPopulacao.addAll(cruzar(paiA, paiB));
            }

            populacao = novaPopulacao;
//            System.out.println("FILHOS DA GERAÇÃO");
//            for (List<Integer> individuo : populacao) {
//                System.out.println(individuo);
//            }
//            System.out.println("----");
        }
    }

    private static void montarRoletaViciada(Map<Integer, Double> posicaoAptidaoPorcentagem) {
        ROLETA_VICIADA = new Double[posicaoAptidaoPorcentagem.size()];
        double ultimoValor = 0;
        for (final int posicao : posicaoAptidaoPorcentagem.keySet()) {
            ultimoValor = ultimoValor + posicaoAptidaoPorcentagem.get(posicao);
            ROLETA_VICIADA[posicao] = ultimoValor;
        }
    }

    private static int sorteio() {
        final double valorSorteado = RANDOM.nextDouble(0, 100);
        for (int individuo = 0; individuo < ROLETA_VICIADA.length; individuo++) {
            final double valorMaximoPosicao = ROLETA_VICIADA[individuo];
            final boolean individuoSorteado = valorMaximoPosicao >= valorSorteado;
            if (individuoSorteado) {
                return individuo;
            }
        }
        throw new RuntimeException("PIPIPIPIPI!");
    }

    private static Map<Integer, Double> calcularAptidaoPopulacao(final List<List<Integer>> populacao) {
        final Map<Integer, Integer> individuoAptidao = new HashMap<>();

        for (int individuo = 0; individuo < populacao.size(); individuo++) {
            individuoAptidao.put(individuo, calcularAptidaoIndividual(populacao.get(individuo)));
        }

        final int aptidaoTotalBase = individuoAptidao.values().stream().mapToInt(Integer::intValue).sum();
        int aptidaoTotalFormatada = 0;

        for (int individuo = 0; individuo < populacao.size(); individuo++) {
            final int aptidaoFormatada = aptidaoTotalBase - individuoAptidao.get(individuo);
            aptidaoTotalFormatada += aptidaoFormatada;
            individuoAptidao.put(individuo, aptidaoFormatada);
        }

        final Map<Integer, Double> posicaoAptidaoPorcentagem = new HashMap<>();
        for (int individuo = 0; individuo < populacao.size(); individuo++) {
            final double porcentagem = (double) individuoAptidao.get(individuo) * 100 / aptidaoTotalFormatada;
            posicaoAptidaoPorcentagem.put(individuo, porcentagem);
        }

        return posicaoAptidaoPorcentagem;
    }

    private static int calcularAptidaoIndividual(final List<Integer> individuo) {
        int aptidao = 0;

        aptidao += MATRIZ_ADJACENCIA[VERTICE_DE_PARTIDA][individuo.getFirst()];
        for (int gene = 0; gene < individuo.size() - 1; gene++) {
            aptidao += MATRIZ_ADJACENCIA[individuo.get(gene)][individuo.get(gene + 1)];
        }
        aptidao += MATRIZ_ADJACENCIA[individuo.getLast()][VERTICE_DE_PARTIDA];

        final boolean completouRota = new HashSet<>(individuo).containsAll(VERTICES_OBRIGATORIOS);
        if (!completouRota) {
            aptidao += PESO_CAMINHO_INVALIDO;
        }

        if (MELHOR_INDIVIDUO == null) {
            MELHOR_INDIVIDUO = individuo;
            APTIDAO_MELHOR_INDIVIDUO = aptidao;
            COMPLETOU_ROTA = completouRota;
        } else {
            if (APTIDAO_MELHOR_INDIVIDUO > aptidao) {
                MELHOR_INDIVIDUO = individuo;
                APTIDAO_MELHOR_INDIVIDUO = aptidao;
                COMPLETOU_ROTA = completouRota;
                System.out.println(MELHOR_INDIVIDUO);
                System.out.println("APTIDAO: " + APTIDAO_MELHOR_INDIVIDUO);
                System.out.println("COMPLETOU ROTA: " + COMPLETOU_ROTA);
                System.out.println("GERACAO ATUAL: " + GERACOES);
                System.out.println("---");
            }
        }

        return aptidao;
    }

    public static List<List<Integer>> cruzar(List<Integer> paiA, List<Integer> paiB) {
        final int menorCadeiaGenetica = Math.min(paiA.size(), paiB.size());
        final int pontoDeCruzamento = RANDOM.nextInt(menorCadeiaGenetica);

        List<Integer> filhoA = new ArrayList<>();
        List<Integer> filhoB = new ArrayList<>();

        filhoA.addAll(paiA.subList(0, pontoDeCruzamento));
        filhoA.addAll(paiB.subList(pontoDeCruzamento, paiB.size()));

        filhoB.addAll(paiB.subList(0, pontoDeCruzamento));
        filhoB.addAll(paiA.subList(pontoDeCruzamento, paiA.size()));

        mutar(filhoA, TAXA_MUTACAO);
        mutar(filhoB, TAXA_MUTACAO);

        return List.of(filhoA, filhoB);
    }

    public static void mutar(final List<Integer> individuo, final double taxaMutacao) {
        for (int gene = 0; gene < individuo.size(); gene++) {
            final boolean mutar = taxaMutacao >= RANDOM.nextDouble(0, 100);
            if (mutar) {
                individuo.set(gene, RANDOM.nextInt(VERTICES_GRAFO));
            }
        }
        final boolean mutar = taxaMutacao >= RANDOM.nextDouble(0, 100);
        if (mutar) {
            final int bound = individuo.size() == 1 ? 1 : 2;
            switch (RANDOM.nextInt(bound)) {
                case 0 -> individuo.add(RANDOM.nextInt(individuo.size()), RANDOM.nextInt(VERTICES_GRAFO));
                case 1 -> individuo.remove(RANDOM.nextInt(individuo.size()));
            }
        }
    }

    private static List<Integer> criarIndividuo() {
        final List<Integer> individuo = new ArrayList<>();
        final int qtdGenesIndividuo = RANDOM.nextInt(VERTICES_OBRIGATORIOS.size(), VERTICES_GRAFO);
        while (individuo.size() != qtdGenesIndividuo) {
            individuo.add(RANDOM.nextInt(VERTICES_GRAFO));
        }
        return individuo;
    }
}