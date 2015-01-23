/* 
 * Copyright (C) 2015 Wen, Chifeng <https://sourceforge.net/u/daviesx/profile/>
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package GameCore;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 服务器对客户端的输入/输出.
 */
class ServerClientIO {

        protected String m_port;
        protected ServerConnection m_connection;
        protected ServerSocket m_server;
        protected Socket m_socket;
        protected DataInputStream m_input;
        protected DataOutputStream m_output;
        protected ConcurrentLinkedDeque<byte[]> m_cache;

        public ServerClientIO(ServerSocket server, Socket client, String port, ServerConnection connect) {
                try {
                        m_port = port;
                        m_connection = connect;
                        m_server = server;
                        m_socket = client;
                        m_input = new DataInputStream(client.getInputStream());
                        m_output = new DataOutputStream(client.getOutputStream());
                        m_cache = new ConcurrentLinkedDeque();
                } catch (IOException ex) {
                        Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                }
        }

        public void close() {
                try {
                        m_server.close();
                        m_socket.close();
                } catch (IOException ex) {
                        Logger.getLogger(ServerClientIO.class.getName()).log(Level.SEVERE, null, ex);
                }
        }

        public void receive() {
                try {
                        //System.out.println("接收长度："+m_input.available());
                        if (m_input.available() == 0) {
                                return;
                        }
                        byte[] header = new byte[NetworkDataPacket.c_Header_Length];
                        m_input.readFully(header);
                        int length = NetworkDataPacket.decode_packet_length(header);
                        Server.server_print("接收长度：" + length);
                        byte[] data = new byte[NetworkDataPacket.c_Header_Length + length];
                        System.arraycopy(header, 0, data, 0, header.length);
                        m_input.readFully(data, header.length, length);
                        m_cache.addLast(data);
                } catch (IOException ex) {
                        Logger.getLogger(ServerClientIO.class.getName()).log(Level.SEVERE, null, ex);
                }
        }

        public byte[] extract() {
                if (!m_cache.isEmpty()) {
                        return m_cache.removeFirst();
                } else {
                        return null;
                }
        }

        public void send(byte[] data) {
                try {
                        Server.server_print("发送长度：" + NetworkDataPacket.decode_packet_length(data));
                        m_output.write(data);
                } catch (IOException ex) {
                        Logger.getLogger(ServerClientIO.class.getName()).log(Level.SEVERE, null, ex);
                }
        }
}

/**
 * 与客户端端建立连接.
 */
class ServerConnection implements Runnable {

        private final ServerResponser m_responser;
        private ServerSocket m_serversocket;
        private final String m_port;
        private boolean m_isrunning = true;

        public ServerConnection(ServerResponser responser, String port) {
                m_responser = responser;
                m_port = port;
                try {
                        m_serversocket = new ServerSocket(Integer.parseInt(port));
                } catch (IOException ex) {
                        Logger.getLogger(ServerConnection.class.getName()).log(Level.SEVERE, null, ex);
                }
        }

        public void close() {
                m_isrunning = false;
        }

        /**
         * 建立链接，监听并提交数据.
         */
        @Override
        public void run() {
                try {
                        Server.server_print("正在监听端口：" + m_port);
                        Socket client = m_serversocket.accept();
                        ServerClientIO clientio
                            = m_responser.establish_permenant_connection(m_serversocket, client, m_port, this);
                        Server.server_print("已与客户端在端口：" + m_port + "建立连接");

                        while (m_isrunning) {
                                clientio.receive();
                                Thread.sleep(10);
                        }
                        m_responser.destroy_permenant_connection(m_port);
                } catch (IOException | InterruptedException ex) {
                        Logger.getLogger(ServerConnection.class.getName()).log(Level.SEVERE, null, ex);
                }
        }

}

/**
 * 响应客户端连接.
 */
class ServerResponser implements Runnable {

        public static final Integer c_TCP_Port = 1025;
        public static final String c_Server_IP = "127.0.0.1";

        private boolean m_isrunning = true;
        private Integer m_server_port = 1026;
        private final ObjectCache m_clients;

        private String get_connection_port() {
                return (m_server_port++).toString();
        }

        public ServerResponser() {
                m_clients = new ObjectCache();
        }

        public void close() {
                m_isrunning = false;
        }

        public static ServerClientIO establish_temporary_connection(ServerSocket server, Socket client) {
                return new ServerClientIO(server, client, ServerResponser.c_TCP_Port.toString(), null);
        }

        public ServerClientIO establish_permenant_connection(ServerSocket server, Socket client,
            String port, ServerConnection connection) {
                //@FIXME: Concerning thread safety
                ServerClientIO clientio = new ServerClientIO(server, client, port, connection);
                m_clients.declare(port, clientio);
                return clientio;
        }

        public void destroy_permenant_connection(String port) {
                //@FIXME: Concerning thread safety
                ServerClientIO io = m_clients.use(port);
                io.close();
                m_clients.undeclare(port);
        }

        public ObjectCache get_permenant_connections() {
                //@FIXME: Concerning thread safety
                return m_clients;
        }

        @Override
        public void run() {
                while (m_isrunning) {
                        try {
                                Server.server_print("正在等待客户端...");

                                ServerSocket server = null;
                                server = new ServerSocket(ServerResponser.c_TCP_Port);
                                Socket client = server.accept();

                                String alloc_port = get_connection_port();
                                ServerClientIO clientio = establish_temporary_connection(server, client);
                                // 发送分配出的端口
                                PacketGeneralData p = new PacketGeneralData(100);
                                p.write_string(alloc_port);
                                clientio.m_output.write(p.serialize());
                                Server.server_print("已响应客户端：" + alloc_port);
                                server.close();

                                ServerConnection connection_thread = new ServerConnection(this, alloc_port);
                                Thread thread = new Thread(connection_thread);
                                thread.start();
                        } catch (IOException ex) {
                                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                        }
                }
                // 关闭所有连接
                ServerClientIO io;
                for (Iterator i = m_clients.iterator();
                    (io = m_clients.get_next(i)) != null;) {
                        io.m_connection.close();
                }

        }
}

/**
 * 负责客户端间的数据交换.
 */
class Server implements Runnable {

        private final ServerResponser m_responser;
        private boolean m_isrunning = true;
        private final ObjectCache m_client_users;

        public static void server_print(String s) {
                System.out.println("服务器:" + s);
        }

        public Server() {
                m_client_users = new ObjectCache();

                // 构建响应线程
                m_responser = new ServerResponser();
                Thread responser_thread = new Thread(m_responser);
                responser_thread.start();
                // 数据分派线程
                Thread data_thread = new Thread(this);
                data_thread.start();
        }

        public void close() {
                if (m_responser.get_permenant_connections().is_empty()) {
                        m_isrunning = false;
                }
        }

        @Override
        public void run() {
                while (m_isrunning) {
                        ObjectCache connections = m_responser.get_permenant_connections();
                        ServerClientIO io1, io2;
                        for (Iterator i = connections.iterator();
                            (io1 = connections.get_next(i)) != null;) {
                                byte[] data;
                                while ((data = io1.extract()) != null) {
                                        if (NetworkDataPacket.decode_packet_signature(data) == GameNetwork.c_Internal_Signature) {
                                                // 处理特殊数据包
                                                int nature = NetworkDataPacket.decode_packet_nature(data);
                                                switch (nature) {
                                                        case NetworkDataPacketFactory.c_IdentityVerification_Packet: {
                                                                // 保存到服务器
                                                                PacketIdentityVerification userid = (PacketIdentityVerification) NetworkDataPacketFactory.create(nature);
                                                                userid.deserialize(data);
                                                                if (m_client_users.use(userid.id()) == null) {
                                                                        server_print("用户" + userid.id() + "登入，端口：" + io1.m_port);
                                                                        m_client_users.declare(userid.id(), userid);
                                                                } else {
                                                                        server_print("用户" + userid.id() + "已经登入登入，连接将取消");
                                                                        io1.close();
                                                                }
                                                                continue;
                                                        }
                                                        case NetworkDataPacketFactory.c_UserQuery_Packet: {
                                                                // 发送用户列表
                                                                PacketUserQuery query = new PacketUserQuery();

                                                                PacketIdentityVerification cuser;
                                                                for (Iterator k = m_client_users.iterator();
                                                                    (cuser = m_client_users.get_next(k)) != null;) {
                                                                        query.add_packet(cuser);
                                                                }
                                                                io1.send(query.serialize());
                                                                continue;
                                                        }
                                                }
                                        }
                                        // 其他数据包
                                        String port = NetworkDataPacket.decode_port_value(data);
                                        switch (port) {
                                                case NetworkDataPacket.c_Broadcast_Port: {
                                                        // 属于公共广播数据包
                                                        for (Iterator j = connections.iterator();
                                                            (io2 = connections.get_next(j)) != null;) {
                                                                io2.send(data);
                                                        }
                                                        break;
                                                }
                                                case NetworkDataPacket.c_Upload_port: {
                                                        //@TODO 或许需要在服务器保存数据
                                                        break;
                                                }
                                                default: {
                                                        // 发送到指定端口
                                                        io2 = connections.use(port);
                                                        if (io2 != null) {
                                                                io2.send(data);
                                                        } else {
                                                                server_print("严重错误——无法找到端口：" + port);
                                                        }
                                                        break;
                                                }
                                        }
                                }
                        }
                        try {
                                Thread.sleep(10);
                        } catch (InterruptedException ex) {
                                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                        }
                }
                m_responser.close();
        }

}

class ClientDataThread implements Runnable {

        private Socket m_socket = null;
        private boolean m_isrunning = true;

        protected DataInputStream m_input;
        protected DataOutputStream m_output;
        protected ConcurrentLinkedDeque<byte[]> m_cache;

        public ClientDataThread(PacketIdentityVerification identity, Integer port) {
                do {
                        try {
                                m_socket = new Socket(ServerResponser.c_Server_IP, port);
                                m_input = new DataInputStream(m_socket.getInputStream());
                                m_output = new DataOutputStream(m_socket.getOutputStream());
                                m_cache = new ConcurrentLinkedDeque();
                        } catch (IOException ex) {
                                Logger.getLogger(ClientDataThread.class.getName()).log(Level.SEVERE, null, ex);
                        }
                } while (m_socket == null);
                Client.client_print("已与端口" + port + "建立连接");
                Client.client_print("正在登录...用户名：" + identity.id());
                identity.update_this_port(port.toString());
                try {
                        byte[] login_info = identity.serialize();
                        NetworkDataPacket.encode_packet_signature(login_info, GameNetwork.c_Internal_Signature);
                        m_output.write(login_info);
                } catch (IOException ex) {
                        Logger.getLogger(ClientDataThread.class.getName()).log(Level.SEVERE, null, ex);
                }
        }

        public void close() {
                m_isrunning = false;
        }

        @Override
        public void run() {
                NetworkDataPacketFactory fact = new NetworkDataPacketFactory();
                try {
                        while (m_isrunning) {
                                byte[] header = new byte[NetworkDataPacket.c_Header_Length];
                                while (m_input.available() == 0) {
                                        Thread.sleep(10);
                                }
                                m_input.readFully(header);

                                //int nature = NetworkDataPacket.decode_packet_nature(header);
                                //NetworkDataPacket packet = NetworkDataPacketFactory.create(nature);
                                int length = NetworkDataPacket.decode_packet_length(header);
                                byte[] data = new byte[length];
                                m_input.readFully(data);

                                byte[] total = new byte[header.length + data.length];
                                System.arraycopy(header, 0, total, 0, header.length);
                                System.arraycopy(data, 0, total, header.length, data.length);
                                //packet.deserialize(total);

                                m_cache.addLast(total);

                                Thread.sleep(10);
                        }
                        m_socket.close();
                } catch (IOException | InterruptedException ex) {
                        Logger.getLogger(ClientDataThread.class.getName()).log(Level.SEVERE, null, ex);
                }
        }

}

/**
 * 客户端.
 */
class Client {

        private final PacketIdentityVerification m_identity;
        private ClientDataThread m_data_thread = null;

        public Client(PacketIdentityVerification identity) {
                m_identity = identity;
        }

        public static void client_print(String s) {
                System.out.println("客户端：" + s);
        }

        @SuppressWarnings("empty-statement")
        public boolean login() {
                client_print("正在等待服务器响应... ip:" + ServerResponser.c_Server_IP
                    + "，端口：" + ServerResponser.c_TCP_Port);
                Socket s = null;
                do {
                        try {
                                Thread.sleep(1000);
                                s = new Socket(ServerResponser.c_Server_IP, ServerResponser.c_TCP_Port);
                        } catch (IOException | InterruptedException ex) {
                                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                        }
                } while (s == null);
                try {
                        DataInputStream in = new DataInputStream(s.getInputStream());
                        while (in.available() == 0);
                        byte[] port_info = new byte[in.available()];
                        in.readFully(port_info);

                        PacketGeneralData p = new PacketGeneralData();
                        p.deserialize(port_info);
                        Integer alloc_port = Integer.parseInt(p.read_string());
                        client_print("已得到分配端口：" + alloc_port);
                        s.close();

                        client_print("正在与端口" + alloc_port + "建立连接");
                        m_data_thread = new ClientDataThread(m_identity, alloc_port);
                        client_print("正在开启数据监听线程...");
                        Thread thread = new Thread(m_data_thread);
                        thread.start();
                } catch (IOException ex) {
                        Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                        return false;
                }
                return true;
        }

        public void logout() {
                m_data_thread.close();
        }

        public void send(NetworkDataPacket packet, Integer signature) {
                try {
                        byte[] data = packet.serialize();
                        NetworkDataPacket.encode_packet_signature(data, signature);
                        client_print("发送长度：" + NetworkDataPacket.decode_packet_length(data)
                            + "签名：" + signature);
                        m_data_thread.m_output.write(data);
                } catch (IOException ex) {
                        Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }
        }

        public NetworkDataPacket[] receive(Integer signature) {
                ArrayList<NetworkDataPacket> packets = new ArrayList();
                byte[] data;
                for (Iterator<byte[]> i = m_data_thread.m_cache.iterator();
                    i.hasNext();) {
                        data = i.next();
                        if (NetworkDataPacket.decode_packet_signature(data) == signature) {
                                NetworkDataPacket packet
                                    = NetworkDataPacketFactory.create(NetworkDataPacket.decode_packet_nature(data));
                                packet.deserialize(data);
                                packets.add(packet);
                                i.remove();
                        }
                }
                return packets.<NetworkDataPacket>toArray(new NetworkDataPacket[packets.size()]);
        }

        public NetworkDataPacket[] preview() {
                return m_data_thread.m_cache.toArray(new NetworkDataPacket[m_data_thread.m_cache.size()]);
        }

        public PacketIdentityVerification[] query_online_users() {
                send(new PacketUserQuery(), GameNetwork.c_Internal_Signature);
                PacketUserQuery query = null;
                do {
                        byte[] data;
                        for (Iterator<byte[]> i = m_data_thread.m_cache.iterator();
                            i.hasNext();) {
                                data = i.next();
                                if (NetworkDataPacket.decode_packet_nature(data)
                                    == NetworkDataPacketFactory.c_UserQuery_Packet
                                    && NetworkDataPacket.decode_packet_signature(data)
                                    == GameNetwork.c_Internal_Signature) {
                                        NetworkDataPacket packet
                                            = NetworkDataPacketFactory.create(NetworkDataPacket.decode_packet_nature(data));
                                        packet.deserialize(data);
                                        query = (PacketUserQuery) packet;
                                        i.remove();
                                }
                        }
                        try {
                                Thread.sleep(10);
                        } catch (InterruptedException ex) {
                                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                        }
                } while (query == null);
                return query.get_packets();
        }
}

/**
 * 网络连接
 *
 * @author Wen, Chifeng <https://sourceforge.net/u/daviesx/profile/>
 */
public class GameNetwork {

        public static Integer c_Internal_Signature = 0;
        public static Integer c_General_Signature = 1;

        private final Server m_server;
        private final ObjectCache m_clients;

        private static boolean is_host_availiable() {
                try (Socket s = new Socket(ServerResponser.c_Server_IP, ServerResponser.c_TCP_Port)) {
                        System.out.println("已检测到主机，主机地址:" + ServerResponser.c_Server_IP
                            + "，主机响应端口：" + ServerResponser.c_TCP_Port);
                        return true;
                } catch (IOException ex) {
                        System.out.println("未检测到主机，将在本机构建服务器");
                }
                return false;
        }

        /**
         * 初始化网络.
         */
        public GameNetwork() {
                if (!is_host_availiable()) {
                        m_server = new Server();
                } else {
                        m_server = null;
                }
                m_clients = new ObjectCache();
        }

        /**
         * 关闭网络.
         */
        public void close() {
                if (m_server != null) {
                        m_server.close();
                }
        }

        /**
         * 创建客户端.
         *
         * @param identity 身份
         */
        public void client_create(PacketIdentityVerification identity) {
                m_clients.declare(identity.id(), new Client(identity));
        }

        /**
         * 客户端登录.
         *
         * @param identity 身份
         * @return
         */
        public boolean client_login(PacketIdentityVerification identity) {
                return m_clients.<Client>use(identity.id()).login();
        }

        /**
         * 客户端注销.
         *
         * @param identity 身份
         */
        public void client_logout(PacketIdentityVerification identity) {
                m_clients.<Client>use(identity.id()).logout();
                m_clients.undeclare(identity.id());
        }

        /**
         * 发送数据包.
         *
         * @param identity
         * @param signature
         * @param data
         */
        public void client_send_packet(
            PacketIdentityVerification identity,
            Integer signature,
            NetworkDataPacket data) {
                m_clients.<Client>use(identity.id()).send(data, signature);
        }

        /**
         * 接收数据包.
         *
         * @param identity
         * @param signature
         * @param is_2block
         * @return
         */
        public NetworkDataPacket[] client_receive_packet(
            PacketIdentityVerification identity,
            Integer signature,
            boolean is_2block) {
                NetworkDataPacket[] p;
                do {
                        Client c = m_clients.<Client>use(identity.id());
                        p = c.receive(signature);
                } while (is_2block && p == null);
                return p;
        }

        /**
         * 查询当前在线用户列表.
         *
         * @param identity
         * @return
         */
        public PacketIdentityVerification[] client_query_online_users(
            PacketIdentityVerification identity) {
                return m_clients.<Client>use(identity.id()).query_online_users();
        }
}
