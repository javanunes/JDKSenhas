/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.javanunes.jdksenhas;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author ricardo
 */
public class ControllerBancoDados extends ControllerArquivos{
    
    private String rotaConexaoArquivoBanco = "jdbc:sqlite:"+ControllerArquivos.getCaminhoArquivoUnixBancoDados(); 
    
    ControllerBancoDados(){
        criarTabelaSenhasPelaPrimeiraVez();
    }
    
    public Connection getConexaoBanco(){
        try{
           Connection conexao = DriverManager.getConnection(rotaConexaoArquivoBanco);
           return conexao;
        }
        catch(Exception e){
           System.out.println("ERRO DB: "+e); 
           return null; 
        }
    }
    
    public Statement getManipuladorBanco(Connection conexao){
        try{
            Statement declaracao = conexao.createStatement();
            return declaracao;
        }
        catch(Exception e){
           System.out.println("ERRO DB: "+e); 
           return null; 
        }
    }

    public PreparedStatement getManipuladorBancoVariosCampos(Connection conexao, String query){
        try{
            PreparedStatement preparaDeclaracao = conexao.prepareStatement(query);
            return preparaDeclaracao;
        }
        catch(Exception e){
           System.out.println("ERRO DB: "+e); 
           return null; 
        }
    }
    
    public void salvaTabelaSenhas(String site, String usuario, String senha1, String senha2, String data){
        if(!site.isEmpty() && !usuario.isEmpty()){
            if(!site.isBlank() && !usuario.isBlank()){
                try{
                     String query = "INSERT INTO senhas(site, usuario, senha1, senha2, data) VALUES(?, ?, ?, ?, ?)";
                     Connection conexao = getConexaoBanco();
                     PreparedStatement preparaDeclaracao = getManipuladorBancoVariosCampos(conexao, query);
                     preparaDeclaracao.setString(1, site);
                     preparaDeclaracao.setString(2, usuario);
                     preparaDeclaracao.setString(3, senha1);
                     preparaDeclaracao.setString(4, senha2);
                     preparaDeclaracao.setString(5, data);
                     preparaDeclaracao.executeUpdate();
                     preparaDeclaracao.close();
                     conexao.close();
                }
                catch(Exception e){
                    System.out.println("ERRO DB: "+e);
                }
            }    
        }
    }    
    
    public int atualizaTabelaSenhasPorId(int id, String site, String usuario, String senha1, String senha2){
        if(id > 0){
            if(!site.isEmpty() && !usuario.isEmpty()){
                if(!site.isBlank() && !usuario.isBlank()){
                   try{
                        String query = "UPDATE senhas SET site=?, usuario=?, senha1=?, senha2=?  WHERE id=?";
                        Connection conexao = getConexaoBanco();
                        PreparedStatement preparaDeclaracao = getManipuladorBancoVariosCampos(conexao, query);
                        preparaDeclaracao.setString(1, site);
                        preparaDeclaracao.setString(2, usuario);
                        preparaDeclaracao.setString(3, senha1);
                        preparaDeclaracao.setString(4, senha2);
                        preparaDeclaracao.setInt(5, id);
                        preparaDeclaracao.executeUpdate();
                        preparaDeclaracao.close();
                        conexao.close();
                        return id;
                   }
                   catch(Exception e){
                       System.out.println("ERRO DB: "+e); 
                       return -1;
                   }
                }
            }
        }
        return 0;
    }
    
    public void criarTabelaSenhasPelaPrimeiraVez(){    
        try{
           Connection conexao = getConexaoBanco();
           Statement declaracao = getManipuladorBanco(conexao);
           String tabelaSenhas = "CREATE TABLE IF NOT EXISTS senhas (\n"
                    + "   id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                    + "   site TEXT,\n"
                    + "   usuario VARCHAR(400) NOT NULL,\n"
                    + "   senha1 TEXT,\n"
                    + "   senha2 TEXT,\n"
                    + "   data TEXT NOT NULL\n"
                    + ");";
            declaracao.execute(tabelaSenhas);
            declaracao.close();
            conexao.close();
        }
        catch(Exception e){
            System.out.println("ERRO DB: "+e);
        }
    }
    
    public void listarSitesContendo(String texto){
        try{   
            Connection conexao = getConexaoBanco();
            
            int id;
            String site;
            String usuario;
            String senha1;
            String senha2;
            String data;
            
            String query = "SELECT * FROM senhas WHERE site LIKE '%"+texto+"%'";
            PreparedStatement preparaDeclaracao = getManipuladorBancoVariosCampos(conexao, query);
            //preparaDeclaracao.setString(1, texto);
            ResultSet resultadosEncontrados = preparaDeclaracao.executeQuery();
            System.out.println("Registros com '" + texto + "' no campo 'site':");
            System.out.println("--------------------------------------------------");
            System.out.printf("%-5s %-26s %-20s  %-15s%n", "ID", "SITE", "USUARIO" , "DATA");
            System.out.println("--------------------------------------------------");

            while (resultadosEncontrados.next()) {
                id = resultadosEncontrados.getInt("id");
                site = resultadosEncontrados.getString("site");
                usuario = resultadosEncontrados.getString("usuario");
                data = resultadosEncontrados.getString("data");
                System.out.printf("%-5d %-26s %-20s %-15s%n", id, site, usuario ,data);
            }
            conexao.close();
            preparaDeclaracao.close();
            resultadosEncontrados.close();
            System.out.println("--------------------------------------------------");

        }
        catch(Exception e ){
            System.out.println("ERRO DB: "+e);
        }
    }
    
    public void exibirSenhaSitesContendoId(int idSite, String senha){
        try{
            Connection conexao = getConexaoBanco();
            ControllerCriptografia cc = new ControllerCriptografia();
            
            int id;
            String site;
            String usuario;
            String senha1;
            String senha2;
            String data;
            
            String query = "SELECT * FROM senhas WHERE id=?";
            PreparedStatement preparaDeclaracao = getManipuladorBancoVariosCampos(conexao, query);
            preparaDeclaracao.setInt(1, idSite);
            ResultSet resultadosEncontrados = preparaDeclaracao.executeQuery();
            System.out.println("Registro com ID'" + idSite + " cuidado com as senhas expostas:");
            System.out.println("--------------------------------------------------");
            System.out.printf("%-5s %-26s %-20s %-30s %-30s %-15s%n", "ID", "SITE", "USUARIO" ,"SENHA", "SENHA2", "DATA");
            System.out.println("--------------------------------------------------");

            while (resultadosEncontrados.next()) {
                id = resultadosEncontrados.getInt("id");
                site = resultadosEncontrados.getString("site");
                usuario = resultadosEncontrados.getString("usuario");
                senha1 = cc.descriptografa2x(resultadosEncontrados.getString("senha1"), senha);
                senha2 = cc.descriptografa2x(resultadosEncontrados.getString("senha2"), senha);
                data = resultadosEncontrados.getString("data");
                System.out.printf("%-5d %-26s %-20s %-30s %-30s %-15s%n", id, site, usuario ,senha1, senha2, data);
            }
            conexao.close();
            preparaDeclaracao.close();
            resultadosEncontrados.close();
            System.out.println("--------------------------------------------------");

        }
        catch(Exception e ){
            System.out.println("ERRO DB: "+e);
        }
    } 
    
    public void deletaSitePorId(int idSite){
        if(idSite > 0){
            try{
               Connection conexao = getConexaoBanco();
               String query="DELETE FROM senhas WHERE id=?";
               PreparedStatement preparaDeclaracao = getManipuladorBancoVariosCampos(conexao, query);
               preparaDeclaracao.setInt(1, idSite);
               preparaDeclaracao.executeUpdate();
               preparaDeclaracao.close();
               conexao.close();
            }
            catch(Exception e){
               System.out.println("ERRO DB: "+e); 
            }
            
        }
    }
    
}
    

