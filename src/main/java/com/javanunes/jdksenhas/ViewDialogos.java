/*
  O JDKSenhas foi criado para tentar salvar o monte de senhas que temos guardadas para os nossos sistemas e sites num local onde temos mais controle que é o nosso computador. Outros sistemas de senhas podem
  guardar as senhas que temos em nuvens que são facilmente acessadas por autoridades e agências de segurança como NSA, CIA e MI6, por isso criamos um programa de código aberto para termos menos medo
  de nossas senhas irem parar na mão de governantes.

 */
package com.javanunes.jdksenhas;

import java.io.Console;
import java.security.Provider;
import java.security.Security;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;


/**
 *
 * @author ricardo
 */
public class ViewDialogos {
    
    private static final int TAMANHO_MINIMO_SENHA_MESTRA_PROTETORA = 16;
    private static String senhaMestraParaInserirNovosRegistros = null;

    private static void bannerApresentacao(){
        System.out.println("\n=================================JDKSENHAS 1.2======================================");
        System.out.println("=                                                                                  =");
        System.out.println("=     ajudando a guardar senhas usando java no ambiente unix em arquivos           =");
        System.out.println("=     por enquanto só aceitamos senha-mestra de 16 caracteres, nem + nem -         =");
        System.out.println("=                                                                                  =");
        System.out.println("====================================================================================\n\n\n\n");
    }
    
    private static String getData(){
        Date data = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        return sdf.format(data);
    }
    
    private static String getRespostaUsuario(Object pergunta){
        String resposta = "";
        System.out.println(pergunta);
        Scanner teclado = new Scanner(System.in);
        resposta = teclado.nextLine();
        return resposta;
    }    
    
    private static String getRespostaFormaSecretaUsuario(String pergunta){
        String resposta = "";
        Console console = System.console();

        if (console == null) {
            System.err.println("Aqui não é um console real para leitura de senha.\nEntre num console Unix real apertando CTRL+ALT+F3 ou use o konsole\ne me chame novamente!");
            return "";
        }
        
        char[] senha = console.readPassword(pergunta);
        
        if (senha != null) {
            resposta = new String(senha);
            System.out.println("Senha digitada (oculta): **********"); // Apenas para indicar que algo foi lido
            java.util.Arrays.fill(senha, ' ');
        }
        
        return resposta;
    }    
    
    
    private static void setPromptPadrao(){
        System.out.print("$JDKSenhas -> ");
    }
    
    private static void exibeTiposSeguranca(){
        Provider[] providers = Security.getProviders();
        for (Provider provider : providers) {
            System.out.println("Provider: " + provider.getName());
            System.out.println("  Class: " + provider.getClass().getName());
            System.out.println("  Services:");
            for (Provider.Service service : provider.getServices()) {
                System.out.println("    " + service.getAlgorithm() + " (" + service.getType() + ")");
            }
            System.out.println();
        }
    }
    
    private static void limpaTela(){
        for(int linhas = 0; linhas < 100; linhas++){
            System.out.println("");
        }
        try {
            Runtime.getRuntime().exec("clear");
            try{
               Runtime.getRuntime().exec("cls"); 
            }
            catch(Exception e){
                
            }
        } 
        catch(Exception e) {
            
        }
    }
    
    private static int stringParaInteiro(String valor){
        try{
            return Integer.valueOf(valor);
        }
        catch(Exception e){
            return 0;
        }
    }
    
    private static void interpretadorComandos(String comando){
        if(!comando.isEmpty() && !comando.isBlank()){
            ControllerBancoDados cbd = new ControllerBancoDados();
            ControllerCriptografia cc = new ControllerCriptografia();
            
            int id;
            String senhaMestra;
            String site;
            String usuario;
            String senha1;
            String senha2;
            String data;
            String parametro;
            
            switch(comando){
                case "novo":
                    //Para inserir um novo registro de senha, se a pessoa já digitou a senha-mestra uma vez, não precisará digitar de novo
                    if(senhaMestraParaInserirNovosRegistros == null){
                       senhaMestra = getRespostaFormaSecretaUsuario("Digite A SENHA MESTRA[16] :");
                       System.out.println("\nSe a chave mestra for esquecida depois, os seus dados serão perdidos !!!\n");
                       setSenhaMestraParaInserirNovosRegistros(senhaMestra);
                    }
                    else{
                       senhaMestra = getSenhaMestraParaInserirNovosRegistros();
                    }
                    
                    if(senhaMestra.length() < TAMANHO_MINIMO_SENHA_MESTRA_PROTETORA){
                       System.out.println("Senha mestra DEVE SER MAIOR QUE "+TAMANHO_MINIMO_SENHA_MESTRA_PROTETORA);
                       break;
                    }
                    
                    site = getRespostaUsuario("Qual site?");
                    usuario  = getRespostaUsuario("Qual usuario?");
                    senha1 = getRespostaFormaSecretaUsuario("Senha 1 ?:");
                    senha2 = getRespostaFormaSecretaUsuario("Senha 2 ?:");
                    senha1 = cc.criptografa2x(senha1, senhaMestra);
                    senha2 = cc.criptografa2x(senha2, senhaMestra);
           
                    cbd.salvaTabelaSenhas(site, usuario, senha1, senha2, getData());
                    System.out.println("Pronto! Senha salva!");
                  break;
                case "corrige":

                    //Para inserir um novo registro de senha, se a pessoa já digitou a senha-mestra uma vez, não precisará digitar de novo
                    if(senhaMestraParaInserirNovosRegistros == null){
                       senhaMestra = getRespostaFormaSecretaUsuario("Digite A SENHA MESTRA[16] :");
                       setSenhaMestraParaInserirNovosRegistros(senhaMestra);
                    }
                    else{
                       senhaMestra = getSenhaMestraParaInserirNovosRegistros();
                    }
                    
                    if(senhaMestra.length() < TAMANHO_MINIMO_SENHA_MESTRA_PROTETORA){
                       System.out.println("Senha mestra DEVE SER MAIOR QUE "+TAMANHO_MINIMO_SENHA_MESTRA_PROTETORA);
                       break;
                    }
                                        
                    id = stringParaInteiro(getRespostaUsuario("ID do site a ser corrigido: "));
                    site = getRespostaUsuario("Site correto:");
                    usuario  = getRespostaUsuario("Usuario correto:");
                    senha1 = getRespostaFormaSecretaUsuario("Senha correta 1 ?:");
                    senha2 = getRespostaFormaSecretaUsuario("Senha correta 2 ?:");
                    senha1 = cc.criptografa2x(senha1, senhaMestra);
                    senha2 = cc.criptografa2x(senha2, senhaMestra);
                    
                    if( cbd.atualizaTabelaSenhasPorId(id, site, usuario, senha1, senha2) > 0 ){
                        System.out.printf("Tentativa de atualizar registro com ID %d feita!\n",id);
                    }
                    
                  break;  
                case "acha":
                    limpaTela();
                    site = getRespostaUsuario("Contendo site: ");
                    cbd.listarSitesContendo(site);
                  break;
                case "del":
                    id = Integer.valueOf(getRespostaUsuario("ID de site para deletar: "));
                    parametro = getRespostaUsuario("Deseja realmente apagar site iD "+id+" ?");
                    if(parametro.equals("sim")){
                        parametro="";
                        parametro = getRespostaUsuario("Você realmente deseja DELETAR PRA SEMPRE site iD "+id+" ???");
                        if(parametro.equals("sim")){
                            cbd.deletaSitePorId(id);
                            System.out.println("Senha de site ID "+id+" apagada!");
                        }
                    }
                  break;
                case "help":
                    limpaTela();
                    getHelp();
                    break;
                case "exit":
                  limpaTela();  
                  System.exit(0);
                default:
                    // se o comando não for nenhum desses acima e tiver parametros, tenta interpreta-los dependendo do comando
                    if(comando.indexOf(" ")>1){
                        try{
                            String[] comandos = comando.split(" ");
                            if(comandos[0].equals("ver")){
                               parametro =  comandos[1];
                               senhaMestra = getRespostaFormaSecretaUsuario("Digite A SENHA MESTRA[16] :");
                               if(senhaMestra.length() < TAMANHO_MINIMO_SENHA_MESTRA_PROTETORA){
                                   System.out.println("Senha mestra DEVE SER MAIOR QUE "+TAMANHO_MINIMO_SENHA_MESTRA_PROTETORA);
                                   break;
                               }
                               
                               // tenta visualizar a senhas daquele registro pelo id que é um int
                               cbd.exibirSenhaSitesContendoId(Integer.valueOf(parametro), senhaMestra);
                               break;
                            }
                        }
                        catch(Exception e){
                            System.out.println("Esse não deu!");
                        }
                    }
                    System.out.println("Oi?");
            }
        }
    }
    
    private static void getHelp(){
        System.out.println("\n\n\n-----------------------------------------------------------------------------------------");
        System.out.println("                                     AJUDA DO JDKSENHAS                                      \n");
        System.out.println("acha    : procura um registro de senha de um site que contenha uma string especificada");
        System.out.println("corrige : pede o ID de um registro para trocar as suas informações por outras corretas");
        System.out.println("novo    : cria um novo registro de senha para site ou sistema");
        System.out.println("del     : paga um site pelo id depois de concordar 2 vezes dando-se um 'sim'");
        System.out.println("ver n   : mostra a senha de um site com um id de número n onde n é um número digitado por você");
        System.out.println("exit    : termina o programa");
        System.out.println("help    : exibe esse menu");
        System.out.println("-----------------------------------------------------------------------------------------\n");
    }
    
    public static String getSenhaMestraParaInserirNovosRegistros() {
        return senhaMestraParaInserirNovosRegistros;
    }

    public static void setSenhaMestraParaInserirNovosRegistros(String senhaMestraParaInserirNovosRegistros) {
        ViewDialogos.senhaMestraParaInserirNovosRegistros = senhaMestraParaInserirNovosRegistros;
    }
   
    public static void main(String[] args) {
        
        ControllerCriptografia cc = new ControllerCriptografia();
        bannerApresentacao();
        String sistemaOperacional = System.getProperty("os.name");
        System.out.println(sistemaOperacional);
        while(true){
           setPromptPadrao();
           interpretadorComandos(getRespostaUsuario(""));
        } 
           
    }
    
}
