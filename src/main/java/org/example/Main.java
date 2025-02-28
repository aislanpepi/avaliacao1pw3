package org.example;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import org.example.dao.AlunoDAO;
import org.example.model.Aluno;
import org.example.util.JPAUtil;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        EntityManager em = JPAUtil.getEntityManager();
        AlunoDAO dao = new AlunoDAO(em);
        Scanner sc = new Scanner(System.in);
        int op = 0;

        while(op != 6) {
            System.out.println("** CADASTRO DE ALUNOS **\n");
            System.out.println("1 - Cadastrar aluno");
            System.out.println("2 - Excluir aluno");
            System.out.println("3 - Alterar aluno");
            System.out.println("4 - Buscar aluno pelo nome");
            System.out.println("5 - Listar alunos");
            System.out.println("6 - FIM");
            System.out.print("Digite a opcao desejada: ");
            op = Integer.parseInt(sc.nextLine());

            switch (op) {
                case 1:
                    Aluno aluno = new Aluno();
                    System.out.println("CADASTRO DE ALUNO");
                    System.out.print("Digite o nome: ");
                    aluno.setNome(sc.nextLine());
                    System.out.print("Digite o RA: ");
                    aluno.setRa(sc.nextLine());
                    System.out.print("Digite o email: ");
                    aluno.setEmail(sc.nextLine());
                    System.out.print("Digite a nota 1: ");
                    BigDecimal n = BigDecimal.valueOf(Double.parseDouble(sc.nextLine()));
                    while(n.doubleValue() < 0 || n.doubleValue() > 10){
                        System.out.print("Nota invalida. Digite novamente: ");
                        n = BigDecimal.valueOf(Double.parseDouble(sc.nextLine()));
                    }
                    aluno.setNota1(n);
                    System.out.print("Digite a nota 2: ");
                    n = BigDecimal.valueOf(Double.parseDouble(sc.nextLine()));
                    while(n.doubleValue() < 0 || n.doubleValue() > 10){
                        System.out.print("Nota invalida. Digite novamente: ");
                        n = BigDecimal.valueOf(Double.parseDouble(sc.nextLine()));
                    }
                    aluno.setNota2(n);
                    System.out.print("Digite a nota 3: ");
                    n = BigDecimal.valueOf(Double.parseDouble(sc.nextLine()));
                    while(n.doubleValue() < 0 || n.doubleValue() > 10){
                        System.out.print("Nota invalida. Digite novamente: ");
                        n = BigDecimal.valueOf(Double.parseDouble(sc.nextLine()));
                    }
                    aluno.setNota3(n);

                    em.getTransaction().begin();
                    dao.cadastrar(aluno);
                    em.getTransaction().commit();

                    break;

                case 2:
                    System.out.println("EXCLUIR ALUNO:");
                    System.out.print("Digite o nome: ");
                    String nome = sc.nextLine();

                    em = JPAUtil.getEntityManager();
                    dao = new AlunoDAO(em);
                    em.getTransaction().begin();
                    dao.deletar(nome);
                    try {
                        em.getTransaction().commit();
                        System.out.println("Aluno removido com sucesso!");
                    } catch (Exception e) {
                        System.err.println("Ocorreu algum erro!");
                    }
                    break;

                case 3:
                    System.out.println("ALTERAR ALUNO:");
                    System.out.print("Digite o nome: ");
                    nome = sc.nextLine();

                    dao = new AlunoDAO(em);
                    em.getTransaction().begin();
                    aluno = dao.buscarPorNome(nome);

                    em.persist(aluno);

                    System.out.println("Dados do Aluno(a)");
                    System.out.printf("Nome: %s\n",aluno.getNome());
                    System.out.printf("Email: %s\n",aluno.getEmail());
                    System.out.printf("RA: %s\n",aluno.getRa());
                    System.out.printf("Notas: %.2f - %.2f - %.2f\n",aluno.getNota1(),aluno.getNota2(),aluno.getNota3());

                    System.out.println("Novos dados:");
                    System.out.print("Digite o nome: ");
                    aluno.setNome(sc.nextLine());
                    System.out.print("Digite o RA: ");
                    aluno.setRa(sc.nextLine());
                    System.out.print("Digite o email: ");
                    aluno.setEmail(sc.nextLine());
                    System.out.print("Digite a nota 1: ");
                    aluno.setNota1(BigDecimal.valueOf(Long.parseLong(sc.nextLine())));
                    System.out.print("Digite a nota 2: ");
                    aluno.setNota2(BigDecimal.valueOf(Long.parseLong(sc.nextLine())));
                    System.out.print("Digite a nota 3: ");
                    aluno.setNota3(BigDecimal.valueOf(Long.parseLong(sc.nextLine())));

                    em.getTransaction().commit();

                    break;

                case 4:

                    System.out.println("CONSULTAR ALUNO");
                    System.out.print("Digite o nome: ");
                    nome = sc.nextLine();

                    dao = new AlunoDAO(em);
                    em.getTransaction().begin();
                    try {
                        aluno = dao.buscarPorNome(nome);

                        System.out.println("Dados do Aluno(a)");
                        System.out.printf("Nome: %s\n",aluno.getNome());
                        System.out.printf("Email: %s\n",aluno.getEmail());
                        System.out.printf("RA: %s\n",aluno.getRa());
                        System.out.printf("Notas: %.2f - %.2f - %.2f\n",aluno.getNota1(),aluno.getNota2(),aluno.getNota3());
                    } catch (NoResultException e){
                        System.err.println("Aluno nao encontrado!");
                    }

                    em.getTransaction().commit();

                    break;
                    
                case 5:
                    System.out.println("Exibindo todos os alunos:");
                    List<Aluno> alunos = dao.buscarTodos();

                    for(Aluno a : alunos){
                        double media = (a.getNota1().doubleValue() + a.getNota2().doubleValue() + a.getNota3().doubleValue()) / 3;
                        System.out.printf("\nNome: %s\n",a.getNome());
                        System.out.printf("Email: %s\n",a.getEmail());
                        System.out.printf("RA: %s\n",a.getRa());
                        System.out.printf("Notas: %.2f - %.2f - %.2f\n",a.getNota1(),a.getNota2(),a.getNota3());
                        System.out.printf("Media: %.2f\n",media);
                        if(media >= 6) System.out.print("Situação: Aprovado\n");
                        if(media < 4) System.out.print("Situação: Reprovado\n");
                        if(media >= 4 && media < 6) System.out.print("Situação: Recuperação\n");
                    }
            }
        }
    }
}