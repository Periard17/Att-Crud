import java.io.*;
import java.util.*;

class Produto implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String nome;
    private double preco;

    public Produto(int id, String nome, double preco) {
        this.id = id;
        this.nome = nome;
        this.preco = preco;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public double getPreco() {
        return preco;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    @Override
    public String toString() {
        return "Produto{id=" + id + ", nome='" + nome + "', preco=" + preco + '}';
    }
}

class ProdutoDAO {
    private static final String FILE_NAME = "produtos.txt";
    private List<Produto> produtos;

    public ProdutoDAO() {
        produtos = carregarProdutos();
    }

@SuppressWarnings("unchecked")
  private List<Produto> carregarProdutos() {
      try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
          return (List<Produto>) ois.readObject();
      } catch (FileNotFoundException e) {
          return new ArrayList<>();
      } catch (IOException | ClassNotFoundException e) {
          e.printStackTrace();
          return new ArrayList<>();
      }
  }1

    private void salvarProdutos() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(produtos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void adicionarProduto(Produto produto) {
        produtos.add(produto);
        salvarProdutos();
    }

    public List<Produto> listarProdutos() {
        return produtos;
    }

    public Produto buscarProduto(int id) {
        return produtos.stream().filter(p -> p.getId() == id).findFirst().orElse(null);
    }

    public void atualizarProduto(Produto produto) {
        Produto existente = buscarProduto(produto.getId());
        if (existente != null) {
            existente.setNome(produto.getNome());
            existente.setPreco(produto.getPreco());
            salvarProdutos();
        }
    }

    public void removerProduto(int id) {
        produtos.removeIf(p -> p.getId() == id);
        salvarProdutos();
    }
}

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static ProdutoDAO produtoDAO = new ProdutoDAO();

    public static void main(String[] args) {
        int opcao;
        do {
            System.out.println("Menu:");
            System.out.println("1. Adicionar Produto");
            System.out.println("2. Listar Produtos");
            System.out.println("3. Atualizar Produto");
            System.out.println("4. Remover Produto");
            System.out.println("5. Sair");
            System.out.print("Escolha uma opção: ");
            opcao = scanner.nextInt();
            scanner.nextLine(); // consumir nova linha

            switch (opcao) {
                case 1:
                    adicionarProduto();
                    break;
                case 2:
                    listarProdutos();
                    break;
                case 3:
                    atualizarProduto();
                    break;
                case 4:
                    removerProduto();
                    break;
                case 5:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        } while (opcao != 5);
    }

    private static void adicionarProduto() {
        System.out.print("ID do Produto: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // consumir nova linha
        System.out.print("Nome do Produto: ");
        String nome = scanner.nextLine();
        System.out.print("Preço do Produto: ");
        double preco = scanner.nextDouble();
        scanner.nextLine(); // consumir nova linha

        Produto produto = new Produto(id, nome, preco);
        produtoDAO.adicionarProduto(produto);
        System.out.println("Produto adicionado com sucesso!");
    }

    private static void listarProdutos() {
        List<Produto> produtos = produtoDAO.listarProdutos();
        if (produtos.isEmpty()) {
            System.out.println("Nenhum produto encontrado.");
        } else {
            produtos.forEach(System.out::println);
        }
    }

    private static void atualizarProduto() {
        System.out.print("ID do Produto a ser atualizado: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // consumir nova linha
        Produto produto = produtoDAO.buscarProduto(id);
        if (produto != null) {
            System.out.print("Novo nome do Produto: ");
            String nome = scanner.nextLine();
            System.out.print("Novo preço do Produto: ");
            double preco = scanner.nextDouble();
            scanner.nextLine(); // consumir nova linha

            produto.setNome(nome);
            produto.setPreco(preco);
            produtoDAO.atualizarProduto(produto);
            System.out.println("Produto atualizado com sucesso!");
        } else {
            System.out.println("Produto não encontrado.");
        }
    }

    private static void removerProduto() {
        System.out.print("ID do Produto a ser removido: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // consumir nova linha
        Produto produto = produtoDAO.buscarProduto(id);
        if (produto != null) {
            produtoDAO.removerProduto(id);
            System.out.println("Produto removido com sucesso!");
        } else {
            System.out.println("Produto não encontrado.");
        }
    }
}
