
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author bts_snir
 */
public class C_EtherIO {
    String Server;
    String PortCom;
    String Port;
    int Valeur;
    String Message;

    //------------------Constructeur------------------------------
    C_EtherIO(String Server, String PortCom){
        this.Server = Server;
        this.PortCom = PortCom;
    }    
    
    //-------------------contole module-----------------------------
    public void EcrirePort(String Port, int Valeur){
        //on prend le string et on le converti + test pour enssuite l'envoier via EnvoyerSocket
        
        //conversion de int en char 
        char valconverti=(char)Valeur; 
        
        //PORT+VALEUR
        //ex: A+m met le sortie du port A sur 01101101
        String Output=Port+valconverti;
        EnvoyerSocket(Output);
    }
    
    //------------------------------------------------
    public int LirePort(String Port){
        //on prend le string et on le converti + test pour enssuite l'envoier via EnvoyerSocket
        
        //conversion de int en char 
        char valconverti=(char)Valeur; 
        
        //PORT+VALEUR
        //ex: A+m met le sortie du port A sur 01101101
        String Output=Port;
         byte[] whatigetis = EnvoyerSocketEtReceptionnerReponse(Output);
         //byte 1 = identifier
         //byte 2 = value
         //donc
         int identifier = whatigetis[0];
         int val = whatigetis[1];
         //on concaténation
         int output = (val&0xFF);
         
         
        return output;
    }
    
    //------------------------------------------------
    public void ConfiguerPortSortie(String Port){
        //on prend le string et on le converti + test pour enssuite l'envoier via EnvoyerSocket
        
        //PORT+VALEUR
        //ex: A+m met le sortie du port A sur full 1 car sortie
        //null = ASCII(0) ???
        
        
        
        /*
        String Commande="";
        int valeur = 0;
        String ASCII = new Character((char) valeur).toString();
        Commande= "!"+Port.toUpperCase()+ASCII;
        this.EnvoyerSocket(Commande);
        */
        String Output="!"+Port+null;
        EnvoyerSocket(Output);
    
    }
    
    //------------------------------------------------
    public void ConfigurerPortEntree(String Port){
    //on prend le string et on le converti + test pour enssuite l'envoier via EnvoyerSocket
        
        //PORT+VALEUR
        //ex: A+m met le sortie du port A sur full 1 car sortie
        
        
        /*
        String Commande="";
        int valeur = 255;
        String ASCII = new Character((char) valeur).toString();
        Commande= "!"+Port.toUpperCase()+ASCII;
        this.EnvoyerSocket(Commande);
        
        */
        String Output="!"+Port+"ÿ";
        EnvoyerSocket(Output);
    }
    
    //-----------------Communication-------------------------------
    protected void EnvoyerSocket(String Message){
        try {
            DatagramSocket clientSocket = new DatagramSocket();
            InetAddress IP_Serveur=InetAddress.getByName(this.Server);
            byte[] sendData = new byte[1024];
            sendData = Message.getBytes("ISO-8859-1");
            DatagramPacket sendPacket = new DatagramPacket( sendData,
                                                            sendData.length,
                                                            IP_Serveur,
                                                            Integer.parseInt(this.PortCom)
            );
            clientSocket.send(sendPacket);
            clientSocket.close();
            
        } catch (SocketException ex) {
            Logger.getLogger(C_EtherIO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnknownHostException ex) {
            Logger.getLogger(C_EtherIO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(C_EtherIO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(C_EtherIO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    //------------------------------------------------
    protected byte[] EnvoyerSocketEtReceptionnerReponse(String Message){
        byte[] Reponse = null;
        try{
            DatagramSocket clientSocket = new DatagramSocket();
            InetAddress IP_Serveur=InetAddress.getByName(this.Server);
            byte[] sendData = new byte[1024];
            byte[] recevieData = new byte[1024];
            sendData = Message.getBytes("ISO-8859-1");
            DatagramPacket sendPacket = new DatagramPacket( sendData,
                                                            sendData.length,
                                                            IP_Serveur,
                                                            Integer.parseInt(this.PortCom)
            );
            clientSocket.send(sendPacket);
            //reponce
            DatagramPacket receviePacket = new DatagramPacket( recevieData,
                                                               recevieData.length
            );
            System.out.print("att reponce");
            clientSocket.setSoTimeout(3000);
            try{
                clientSocket.receive(receviePacket);
                Reponse = receviePacket.getData();
            } catch (SocketTimeoutException ex) {
                System.out.println("TIMEOUT...");
            } catch (IOException ex) {
                Logger.getLogger(C_EtherIO.class.getName()).log(Level.SEVERE, null, ex);
            }
            //end
            clientSocket.close();
        
        } catch (SocketException ex) {
            Logger.getLogger(C_EtherIO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnknownHostException ex) {
            Logger.getLogger(C_EtherIO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(C_EtherIO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(C_EtherIO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Reponse;
    }
    //END
}


