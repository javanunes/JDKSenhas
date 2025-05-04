/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.javanunes.jdksenhas;

/**
 *
 * @author ricardo
 */



public class ControllerArquivos {
    
    private static String getSistemaOperacional(){
        String sistemaOperacional = System.getProperty("os.name");
        if(sistemaOperacional.contains("Linux")){
            return "Linux";
        }
        if(sistemaOperacional.contains("Unix")){
            return "Unix";
        }
        if(sistemaOperacional.contains("Windows")){
            return "Windows";
        }
        return "desconhecido";
    }
    
   
    public static String getCaminhoArquivoUnixBancoDados(){
          final String ARQUIVO_BANCO_DAD0S_SENHAS = "jdkSenhas.sqlt"; 
          String sistemaOperacional =  getSistemaOperacional();
          String caminhoArquivoBancoDados = "";
          if(sistemaOperacional.equals("Linux") || sistemaOperacional.equals("Unix")){
              caminhoArquivoBancoDados = System.getenv("HOME").concat("/.config/").concat(ARQUIVO_BANCO_DAD0S_SENHAS);
          }
          if(sistemaOperacional.equals("Windows")){
              caminhoArquivoBancoDados = System.getenv("HOMEPATH").concat("\\Documents\\").concat(ARQUIVO_BANCO_DAD0S_SENHAS);
          }
          return caminhoArquivoBancoDados;
    }
    
}
