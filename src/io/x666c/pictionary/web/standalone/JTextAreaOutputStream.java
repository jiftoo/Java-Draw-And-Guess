package io.x666c.pictionary.web.standalone;

import java.awt.BorderLayout;
import java.awt.Container;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class JTextAreaOutputStream extends OutputStream {
	private final JTextArea destination;

	public JTextAreaOutputStream(JTextArea destination) {
		if (destination == null)
			throw new IllegalArgumentException("Destination is null");

		this.destination = destination;
	}

	@Override
	public void write(byte[] buffer, int offset, int length) throws IOException {
		final String text = new String(buffer, offset, length);
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				destination.append(text);
			}
		});
	}

	@Override
	public void write(int b) throws IOException {
		write(new byte[] { (byte) b }, 0, 1);
	}

	public static void main(String[] args) throws Exception {
		JTextArea textArea = new JTextArea(25, 80);

		textArea.setEditable(false);

		JFrame frame = new JFrame("stdout");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container contentPane = frame.getContentPane();
		contentPane.setLayout(new BorderLayout());
		contentPane.add(new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED), BorderLayout.CENTER);
		frame.pack();
		frame.setVisible(true);

		JTextAreaOutputStream out = new JTextAreaOutputStream(textArea);
		System.setOut(new PrintStream(out));

		while (true) {
			System.out.println("Current time: " + System.currentTimeMillis());
			Thread.sleep(1000L);
		}
	}
}