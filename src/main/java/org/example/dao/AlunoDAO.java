package org.example.dao;

import jakarta.persistence.EntityManager;
import org.example.model.Aluno;

public class AlunoDAO {
    private EntityManager em;

    public AlunoDAO(EntityManager em){
        this.em = em;
    }

    public void cadastrar(Aluno aluno){
        this.em.persist(aluno);
    }

    public Aluno buscarPorNome(String nome){
        String jpql = "SELECT a FROM Aluno a WHERE a.nome = :n";
        return em.createQuery(jpql, Aluno.class).setParameter("n",nome).getSingleResult();
    }

    public void deletar(String nome){
        this.em.remove(buscarPorNome(nome));
    }
}
