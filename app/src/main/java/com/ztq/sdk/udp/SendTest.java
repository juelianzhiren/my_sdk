package com.ztq.sdk.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class SendTest {
	public static void main(String[] args) {
		DatagramSocket ds = null;
		byte[] arr = "hello".getBytes();
		try {
			ds = new DatagramSocket();
			DatagramPacket dp = new DatagramPacket(arr, arr.length, InetAddress.getLocalHost(), ReceiveTest.PORT);
			ds.send(dp);
			System.out.println("发送数据成功！");
			
			byte[] arr2 = new byte[20];
			DatagramPacket dp2 = new DatagramPacket(arr2, arr2.length);
			ds.receive(dp2);
			System.out.println("接收到的回发信息是：" + new String(arr2, 0, dp2.getLength()));
			System.out.println("【发送方】发送的数据报内置端口为：" + dp2.getPort() + "; 接收数据报的端口为：" + ds.getLocalPort());  // 不要用ds.getPort()
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (ds != null) {
				ds.close();
			}
		}
	}
}