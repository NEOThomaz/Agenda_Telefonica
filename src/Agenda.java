import java.io.*;
import java.util.*;

public class Agenda {
    private static final String ARQUIVO_CONTATOS = "contatos.txt";
    private static final int MAXIMO_CONTATOS = 100;
    private static Contato[] listaContatos = new Contato[MAXIMO_CONTATOS];
    private static int totalContatos = 0;
    private static long proximoIdContato = 1;

    public static void main(String[] args) {
        carregarContatos();

        Scanner scanner = new Scanner(System.in);
        int opcao = 0; // Inicializando com um valor padrão

        do {
            System.out.println("##################");
            System.out.println("##### AGENDA #####");
            System.out.println("##################");
            listarContatos();
            System.out.println(">>>> Menu <<<<");
            System.out.println("1 - Adicionar Contato");
            System.out.println("2 - Remover Contato");
            System.out.println("3 - Editar Contato");
            System.out.println("4 - Listar Contatos");
            System.out.println("5 - Sair");
            System.out.print("Escolha uma opção: ");
            if (scanner.hasNextInt()) { // Verifica se a entrada é um número inteiro
                opcao = scanner.nextInt();
                switch (opcao) {
                    case 1:
                        adicionarContato(scanner);
                        break;
                    case 2:
                        removerContato(scanner);
                        break;
                    case 3:
                        editarContato(scanner);
                        break;
                    case 4:
                        listarContatos();
                        break;
                    case 5:
                        System.out.println("Saindo...");
                        break;
                    default:
                        System.out.println("Opção inválida.");
                        break;
                }
            } else {
                System.out.println("Por favor, digite um número.");
                scanner.next(); // Descarta a entrada inválida
            }
        } while (opcao != 5);

        salvarContatos();
        scanner.close(); // Fechar o scanner ao finalizar o programa
    }

private static void carregarContatos() {
        try {
            BufferedReader leitor = new BufferedReader(new FileReader(ARQUIVO_CONTATOS));
            String linha;
            while ((linha = leitor.readLine()) != null) {
                String[] partes = linha.split(",");
                if (partes.length >= 4) { // Verifica se há pelo menos 4 partes na linha
                    Long id = Long.parseLong(partes[0]);
                    String nome = partes[1];
                    String sobreNome = partes[2];
                    Contato contato = new Contato(id, nome, sobreNome);
                    for (String telefone : partes[3].split(";")) {
                        String[] dadosTelefone = telefone.split(":");
                        if (dadosTelefone.length >= 2) { // Verifica se há pelo menos 2 partes no telefone
                            contato.adicionarTelefone(new Telefone(dadosTelefone[0], Long.parseLong(dadosTelefone[1])));
                        }
                    }
                    listaContatos[totalContatos++] = contato;
                    proximoIdContato = Math.max(proximoIdContato, id + 1);
                }
            }
            leitor.close(); // Fechar o leitor após a leitura
        } catch (IOException e) {
            System.err.println("Erro ao carregar contatos: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Erro ao converter número: " + e.getMessage());
        }
    }

    private static void salvarContatos() {
        try {
            BufferedWriter escritor = new BufferedWriter(new FileWriter(ARQUIVO_CONTATOS));
            for (int i = 0; i < totalContatos; i++) {
                Contato contato = listaContatos[i];
                escritor.write(contato.getId() + "," + contato.getNome() + "," + contato.getSobreNome() + ",");
                java.util.List<Telefone> telefones = contato.getTelefones(); // Aqui
                for (int j = 0; j < telefones.size(); j++) {
                    Telefone telefone = telefones.get(j);
                    escritor.write(telefone.getDdd() + ":" + telefone.getNumero());
                    if (j < telefones.size() - 1) {
                        escritor.write(";");
                    }
                }
                escritor.newLine();
            }
            escritor.close(); // Fechar o escritor após a escrita
        } catch (IOException e) {
            System.err.println("Erro ao salvar contatos: " + e.getMessage());
        }
    }

    private static void listarContatos() {
        if (totalContatos == 0) {
            System.out.println("A agenda está vazia.");
        } else {
            System.out.println(">>>> Contatos <<<<");
            System.out.println("Id | Nome | Sobrenome | Telefones");
            for (int i = 0; i < totalContatos; i++) {
                Contato contato = listaContatos[i];
                System.out.print(contato.getId() + " | " + contato.getNome() + " | " + contato.getSobreNome() + " | ");
                java.util.List<Telefone> telefones = contato.getTelefones(); // Aqui
                for (int j = 0; j < telefones.size(); j++) {
                    Telefone telefone = telefones.get(j);
                    System.out.print("(" + telefone.getDdd() + ")" + telefone.getNumero());
                    if (j < telefones.size() - 1) {
                        System.out.print(", ");
                    }
                }
                System.out.println();
            }
        }
    }

    private static void adicionarContato(Scanner scanner) {
        if (totalContatos < MAXIMO_CONTATOS) {
            scanner.nextLine(); // Limpar o buffer do scanner
            System.out.print("Digite o nome do contato: ");
            String nome = scanner.nextLine();
            System.out.print("Digite o sobrenome do contato: ");
            String sobreNome = scanner.nextLine();

            // Verificar se o número de telefone já existe
            System.out.print("Digite o DDD do telefone: ");
            String ddd;
            while (true) {
                ddd = scanner.nextLine();
                if (ddd.length() == 2 && ddd.matches("\\d+")) {
                    break;
                } else {
                    System.out.println("O DDD deve conter apenas dois dígitos.");
                    System.out.print("Digite o DDD do telefone: ");
                }
            }

            System.out.print("Digite o número de telefone (8 dígitos): ");
            Long numero;
            while (true) {
                try {
                    numero = Long.parseLong(scanner.nextLine());
                    if (String.valueOf(numero).length() == 8) {
                        break;
                    } else {
                        System.out.println("O não é necessário adicionar o numero 9 :D.");
                        System.out.print("Digite o número de telefone (8 dígitos): ");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Por favor, digite um número válido.");
                    System.out.print("Digite o número de telefone (8 dígitos): ");
                }
            }

            // Verificar se o número de telefone já existe
            if (existeTelefone(numero)) {
                System.out.println("Esse número de telefone já está cadastrado. Por favor, escolha outro número.");
                return;
            }

            Contato novoContato = new Contato(proximoIdContato++, nome, sobreNome);
            novoContato.adicionarTelefone(new Telefone(ddd, numero));
            listaContatos[totalContatos++] = novoContato;
            salvarContatos();
            System.out.println("Contato adicionado com sucesso.");
        } else {
            System.out.println("A agenda está cheia :( ");
        }
    }

    // Método para verificar se o número de telefone já existe na lista de contatos
    private static boolean existeTelefone(Long numero) {
        for (int i = 0; i < totalContatos; i++) {
            List<Telefone> telefones = listaContatos[i].getTelefones();
            for (Telefone telefone : telefones) {
                if (telefone.getNumero().equals(numero)) {
                    return true;
                }
            }
        }
        return false;
    }


    // Método para verificar se o número de telefone já existe na lista de contatos
    private static boolean existeNumero(Long numero) {
        for (int i = 0; i < totalContatos; i++) {
            List<Telefone> telefones = listaContatos[i].getTelefones();
            for (Telefone telefone : telefones) {
                if (telefone.getNumero().equals(numero)) {
                    return true;
                }
            }
        }
        return false;
    }


    private static void removerContato(Scanner scanner) {
        if (totalContatos > 0) {
            listarContatos();
            System.out.print("Digite o ID do contato a ser removido: ");
            long idRemover = scanner.nextLong();
            scanner.nextLine(); // Limpar o buffer do scanner
            int indiceRemover = -1;
            for (int i = 0; i < totalContatos; i++) {
                if (listaContatos[i].getId() == idRemover) {
                    indiceRemover = i;
                    break;
                }
            }
            if (indiceRemover != -1) {
                for (int i = indiceRemover; i < totalContatos - 1; i++) {
                    listaContatos[i] = listaContatos[i + 1];
                }
                totalContatos--;
                salvarContatos();
                System.out.println("Contato removido com sucesso.");
            } else {
                System.out.println("Contato não encontrado.");
            }
        } else {
            System.out.println("A agenda está vazia. Não há contatos para remover.");
        }
    }

    private static void editarContato(Scanner scanner) {
        if (totalContatos > 0) {
            listarContatos();
            System.out.print("Digite o ID do contato a ser editado: ");
            long idEditar = scanner.nextLong();
            scanner.nextLine(); // Limpar o buffer do scanner
            int indiceEditar = -1;
            for (int i = 0; i < totalContatos; i++) {
                if (listaContatos[i].getId() == idEditar) {
                    indiceEditar = i;
                    break;
                }
            }
            if (indiceEditar != -1) {
                Contato contatoEditar = listaContatos[indiceEditar];
                System.out.print("Digite o novo nome do contato: ");
                String novoNome = scanner.nextLine();
                System.out.print("Digite o novo sobrenome do contato: ");
                String novoSobrenome = scanner.nextLine();
                contatoEditar.setNome(novoNome);
                contatoEditar.setSobreNome(novoSobrenome);
                System.out.println("Contato editado com sucesso.");
                salvarContatos();
            } else {
                System.out.println("Contato não encontrado.");
            }
        } else {
            System.out.println("A agenda está vazia. Não há contatos para editar.");
        }
    }
}

class Contato {
    private Long id;
    private String nome;
    private String sobreNome;
    private List<Telefone> telefones;

    public Contato(Long id, String nome, String sobreNome) {
        this.id = id;
        this.nome = nome;
        this.sobreNome = sobreNome;
        this.telefones = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSobreNome() {
        return sobreNome;
    }

    public void setSobreNome(String sobreNome) {
        this.sobreNome = sobreNome;
    }

    public java.util.List<Telefone> getTelefones() { // Aqui
        return telefones;
    }

    public void adicionarTelefone(Telefone telefone) {
        telefones.add(telefone);
    }

    public void removerTelefone(Telefone telefone) {
        telefones.remove(telefone);
    }
}

class Telefone {
    private String ddd;
    private Long numero;

    public Telefone(String ddd, Long numero) {
        this.ddd = ddd;
        this.numero = numero;
    }

    public String getDdd() {
        return ddd;
    }

    public Long getNumero() {
        return numero;
    }
}
