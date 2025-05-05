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

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import javax.crypto.BadPaddingException;

import javax.crypto.spec.IvParameterSpec;


public class ControllerCriptografia {
    //Algoritmo de criptografia
    private final String ALGORITMO_CRIPTOGRAFICO = "AES";
    private final String SEQUENCIA_ALGORITIMOS_ACEITOS= "/CBC/PKCS5Padding";
    private final String PROVEDOR_DE_ALGORITIMOS_ACEITOS= "SunJCE";
    //Deve ser usada uma string de 16 caracteres para embaraçar a senha ainda mais
    private final String blocoAletaorioParaDificultarSenha = "javanunes16anos!";
    
    
    public String criptografa(String informacao, String senha) {
        try{
            Cipher cipher = Cipher.getInstance(ALGORITMO_CRIPTOGRAFICO+SEQUENCIA_ALGORITIMOS_ACEITOS, PROVEDOR_DE_ALGORITIMOS_ACEITOS);
            SecretKeySpec key = new SecretKeySpec(senha.getBytes("UTF-8"), ALGORITMO_CRIPTOGRAFICO);
            cipher.init(Cipher.ENCRYPT_MODE,  key, new IvParameterSpec(blocoAletaorioParaDificultarSenha.getBytes("UTF-8")));
            byte[] cipherText = cipher.doFinal(informacao.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(cipherText);
        }
        catch(Exception e){
            System.out.println("Deu erro na criptografia do dado pois "+e);
            return "";
        }
    }

    public  String descriptografa(String informacao, String senha){
        try{
            Cipher cipher = Cipher.getInstance(ALGORITMO_CRIPTOGRAFICO+SEQUENCIA_ALGORITIMOS_ACEITOS, PROVEDOR_DE_ALGORITIMOS_ACEITOS);
            SecretKeySpec key = new SecretKeySpec(senha.getBytes("UTF-8"), ALGORITMO_CRIPTOGRAFICO);    
            cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(blocoAletaorioParaDificultarSenha.getBytes("UTF-8")));
            byte[] plainText = cipher.doFinal(Base64.getDecoder().decode(informacao));
            return new String(plainText, StandardCharsets.UTF_8);
        }
        catch(BadPaddingException e){
            System.out.println("A chave mestra usada agora, não é mesma que foi usada para guardar os dados! Perdeu!");
            return "?";
        }
        catch(Exception e){
             return "?";
        }
    }
    
    // criptografa em dobro
    public String criptografa2x(String informacao, String senha) {
         return criptografa( criptografa(informacao, senha) , senha);
     }
    
    // descriptografa em dobro
    public String descriptografa2x(String informacao, String senha){
         return descriptografa( descriptografa(informacao, senha) , senha);
     }

}
