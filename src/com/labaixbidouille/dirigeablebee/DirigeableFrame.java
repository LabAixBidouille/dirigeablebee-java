package com.labaixbidouille.dirigeablebee;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.rapplogic.xbee.XBeePin;
import com.rapplogic.xbee.api.RemoteAtRequest;
import com.rapplogic.xbee.api.RemoteAtResponse;
import com.rapplogic.xbee.api.XBee;
import com.rapplogic.xbee.api.XBeeAddress64;
import com.rapplogic.xbee.api.XBeeException;

public class DirigeableFrame extends JFrame {
	private static final long serialVersionUID = -8910079295209151596L;
	
	
	private final static Logger log = Logger.getLogger(DirigeableFrame.class);
	private XBee xbee = new XBee();

	public DirigeableFrame() {
		super("Dirigeable");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(100, 100);
		setVisible(true);

		addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent keyEvent) {
			}

			@Override
			public void keyReleased(KeyEvent keyEvent) {
			}

			@Override
			public void keyPressed(KeyEvent keyEvent) {
				try {
					int keyCode = keyEvent.getKeyCode();
					switch (keyCode) {
					case KeyEvent.VK_UP:
						avancer();
						break;
					case KeyEvent.VK_DOWN:
						stopper();
						break;
					case KeyEvent.VK_LEFT:
						gauche();
						break;
					case KeyEvent.VK_RIGHT:
						droite();
						break;
					case KeyEvent.VK_SPACE:
						monter();
						break;
					}
				} catch (XBeeException e) {
					log.error(e.getCause());
				}
			}
		});
	}

	private void eteindreMoteur(Moteur moteur) throws XBeeException {
		actionMoteur(moteur, XBeePin.Capability.DIGITAL_OUTPUT_LOW);
	}

	private void allumerMoteur(Moteur moteur) throws XBeeException {
		actionMoteur(moteur, XBeePin.Capability.DIGITAL_OUTPUT_HIGH);
	}

	private void actionMoteur(Moteur m, XBeePin.Capability etatIO)
			throws XBeeException {
		if (!xbee.isConnected())
			xbee.open("COM15", 9600);

		XBeeAddress64 addr64 = XBeeAddress64.BROADCAST;

		RemoteAtRequest request = new RemoteAtRequest(addr64, m.getSortie(),
				new int[] { etatIO.getValue() });

		xbee.sendAsynchronous(request);

		RemoteAtResponse response = (RemoteAtResponse) xbee.getResponse();

		if (response.isOk()) {
			System.out.println("Successfully turned  " + etatIO.name() + " "
					+ m.getSortie());
		} else {
			System.out.println("Attempt to turn " + etatIO.name() + " "
					+ m.getSortie() + " failed.  Status: "
					+ response.getStatus());
		}
	}

	private void monter() throws XBeeException {
		allumerMoteur(Moteur.HAUT_AVANT);
		allumerMoteur(Moteur.HAUT_ARRIERE);
	}

	private void avancer() throws XBeeException {
		allumerMoteur(Moteur.GAUCHE);
		allumerMoteur(Moteur.DROITE);
	}

	private void stopper() throws XBeeException {
		eteindreMoteur(Moteur.HAUT_AVANT);
		eteindreMoteur(Moteur.HAUT_ARRIERE);
		eteindreMoteur(Moteur.GAUCHE);
		eteindreMoteur(Moteur.DROITE);
	}

	private void gauche() throws XBeeException {
		allumerMoteur(Moteur.GAUCHE);
		eteindreMoteur(Moteur.DROITE);
	}

	private void droite() throws XBeeException {
		eteindreMoteur(Moteur.GAUCHE);
		allumerMoteur(Moteur.DROITE);

	}

	public static void main(String[] args) {
		PropertyConfigurator.configure("log4j.properties");
		new DirigeableFrame();
	}

}
