package net.sytes.surfael.api.control.services;

import java.io.IOException;

import net.sytes.surfael.api.control.sync.ClientStream;

public class ConnectionCheckerThread implements Runnable {

	@Override
	public void run() {
		while(true) {
			if (ClientStream.getInstance().getSock() != null) {
				try {
					if (ClientStream.getInstance().getSock().getInputStream().read() != -1) {
						System.out.println("socket is alive");
					} else if (ClientStream.getInstance().getSock().getInputStream().read() == -1) {
						System.out.println("socket is alive and returning -1");
					}
				} catch (IOException e) {
					System.out.println("socket is dead");
//					e.printStackTrace();
				} 
			} else System.out.println("socket is null");
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
}
