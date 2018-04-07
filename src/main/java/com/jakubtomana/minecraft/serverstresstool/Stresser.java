package com.jakubtomana.minecraft.serverstresstool;



import com.github.steveice10.mc.protocol.MinecraftConstants;
import com.github.steveice10.mc.protocol.MinecraftProtocol;
import com.github.steveice10.mc.protocol.data.SubProtocol;
import com.github.steveice10.mc.protocol.data.message.Message;
import com.github.steveice10.mc.protocol.data.message.TranslationMessage;
import com.github.steveice10.mc.protocol.data.status.ServerStatusInfo;
import com.github.steveice10.mc.protocol.data.status.handler.ServerInfoHandler;
import com.github.steveice10.mc.protocol.data.status.handler.ServerPingTimeHandler;
import com.github.steveice10.mc.protocol.packet.ingame.client.ClientChatPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerChatPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerJoinGamePacket;
import com.github.steveice10.packetlib.Client;
import com.github.steveice10.packetlib.Session;
import com.github.steveice10.packetlib.event.session.DisconnectedEvent;
import com.github.steveice10.packetlib.event.session.PacketReceivedEvent;
import com.github.steveice10.packetlib.event.session.SessionAdapter;
import com.github.steveice10.packetlib.tcp.TcpSessionFactory;

import java.net.Proxy;
import java.util.Arrays;
import java.util.Random;

public class Stresser {
    //Final variables
    private final int threadsNum;
    private final String serverAdress;
    private final int port;
    private final String nick;
    private final boolean needRegister;
    private final String registerCommand;
    private final String loginCommand;
    private final int delay;
    //variables
    private int totalSlots;
    private int freeSlots;
    private Random rn = new Random();
    /** Creates new stresser object (with register and login)*/
    public Stresser(String serverAdress,int port,int threadsNum,String nick,String loginCommand,String registerCommand,int delay)
    {
        this.serverAdress=serverAdress;
        this.port=port;
        this.nick=nick;
        this.threadsNum=threadsNum;
        this.loginCommand=loginCommand;
        this.registerCommand=registerCommand;
        this.delay=delay;
        needRegister=true;
    }
    /** Creates new stresser object (without register and login)*/
    public Stresser(String serverAdress,int port,int threadsNum,String nick,int delay)
    {
        this.serverAdress=serverAdress;
        this.port=port;
        this.nick=nick;
        this.threadsNum=threadsNum;
        this.loginCommand="";
        this.registerCommand="";
        this.delay=delay;
        needRegister=false;
    }
    /** Prints server info to messanger class*/
    public void getServerInfo()
    {
        MinecraftProtocol protocol = new MinecraftProtocol(SubProtocol.STATUS);
        Client client = new Client(serverAdress, port, protocol, new TcpSessionFactory(Proxy.NO_PROXY));
        client.getSession().setFlag(MinecraftConstants.AUTH_PROXY_KEY,Proxy.NO_PROXY);
        client.getSession().setFlag(MinecraftConstants.SERVER_INFO_HANDLER_KEY, new ServerInfoHandler() {
            @Override
            public void handle(Session session, ServerStatusInfo info) {
                Messanger.println("Version: " + info.getVersionInfo().getVersionName(),Color.RED);
                Messanger.println("Player Count: " + info.getPlayerInfo().getOnlinePlayers() + " / " + info.getPlayerInfo().getMaxPlayers(),Color.WHITE);
                freeSlots=info.getPlayerInfo().getMaxPlayers()-info.getPlayerInfo().getOnlinePlayers();
                totalSlots=info.getPlayerInfo().getMaxPlayers();
                Messanger.println("Players: " + Arrays.toString(info.getPlayerInfo().getPlayers()));
                Messanger.println("Description: " + info.getDescription().getFullText());
                Messanger.println("Icon: " + info.getIcon());
            }
        });
        client.getSession().connect();
    }
    /** Start stressing server */
    public void startStressTest()
    {
        for(int x=0;x<threadsNum;x++)
        {
            Thread t = new Thread(this::joinserver);
            t.start();
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void joinserver()
    {
        MinecraftProtocol protocol = new MinecraftProtocol(nick+rn.nextInt(30));
        Client client = new Client(serverAdress,port, protocol, new TcpSessionFactory(Proxy.NO_PROXY));
        client.getSession().setFlag(MinecraftConstants.AUTH_PROXY_KEY,Proxy.NO_PROXY);
        client.getSession().addListener(new SessionAdapter() {
            @Override
            public void packetReceived(PacketReceivedEvent event) {
                if(event.getPacket() instanceof ServerJoinGamePacket) {
                    if(needRegister)
                    {
                        int password = rn.nextInt();
                        String registerCmd = "/" + registerCommand + " "+ password + " " + password;
                        String loginCmd= "/" + loginCommand + " " + password;
                        event.getSession().send(new ClientChatPacket(registerCmd));
                        event.getSession().send(new ClientChatPacket(loginCmd));
                        event.getSession().send(new ClientChatPacket("Testing server performance by Minecraft Server Stress Tool (github.com)"));
                    }
                    event.getSession().send(new ClientChatPacket("Testing server performance by Minecraft Server Stress Tool by Jakub Tomana"));
                    Messanger.println("Connected and sended messanges!",Color.GREEN);
                    Messanger.setColor(Color.WHITE);
                } else if(event.getPacket() instanceof ServerChatPacket) {
                    Message message = event.<ServerChatPacket>getPacket().getMessage();
                    Messanger.println("Received Message: " + message.getText());
                    if(message instanceof TranslationMessage) {
                       // System.out.println("Received Translation Components: " + Arrays.toString(((TranslationMessage) message).getTranslationParams()));
                    }
                    //event.getSession().disconnect("Finished");
                }
            }

            @Override
            public void disconnected(DisconnectedEvent event) {
                System.out.println("Disconnected: " + Message.fromString(event.getReason()).getFullText());
                if(event.getCause() != null) {
                    event.getCause().printStackTrace();
                }
            }
        });

        client.getSession().connect();
    }

}
