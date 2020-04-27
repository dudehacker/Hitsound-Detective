package detective;

import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import detective.mistake.TimedMistake;

import javax.swing.JTabbedPane;
import javax.swing.SwingWorker;

@SuppressWarnings("serial")
public class ResultsWindow extends JFrame {

	private JPanel contentPane;
	private JTabbedPane tabbedPane;
	private static final String VERSION = "v20200427";
	private static final String A_HREF = "<a href=\"";
	private static final String HREF_CLOSED = "\">";
	private static final String HREF_END = "</a>";
	private static final String HTML = "<html>";
	private static final String HTML_END = "</html>";
	private String songPath;

	/**
	 * Create the frame.
	 * @param osuPath 
	 */
	public ResultsWindow(String path) {
		songPath = path;
		setTitle("Hitsound Detective by DH - " + VERSION);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 463, 333);
		setExtendedState(java.awt.Frame.MAXIMIZED_BOTH);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(0, 0, 445, 288);
		contentPane.add(tabbedPane);
		contentPane.addComponentListener(new ComponentListener() {

			@Override
			public void componentResized(ComponentEvent e) {
				tabbedPane.setSize(e.getComponent().getWidth() - 10, e.getComponent().getHeight() - 10);

			}

			@Override
			public void componentMoved(ComponentEvent e) {

			}

			@Override
			public void componentShown(ComponentEvent e) {

			}

			@Override
			public void componentHidden(ComponentEvent e) {

			}

		});
		tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

	}
	

	public void addTabForAllDifficulties(String tabName, Collection<String> HS) {
		if (HS.size() > 0) {
			JPanel panel = new JPanel(false);
			panel.setLayout(new GridLayout(0, 1, 0, 10));
			panel.setAutoscrolls(true);
			for (String s : HS) {
				makeTextPanel(panel, s);
			}

			JScrollPane scrollPane = new JScrollPane(panel);
			scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
			tabbedPane.addTab(tabName, scrollPane);
			if (tabName.equals("Unused hitsound")) {
				deleteUnused(HS);
			}
		} else {
			JOptionPane.showMessageDialog(null, "No " + tabName, "Good News", JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
	private void deleteUnused(Collection<String> HS) {
		int input = JOptionPane.showConfirmDialog(null,"Do you want to DELETE all un-used hitsound samples?");
		if (input==JOptionPane.YES_OPTION) {
			Iterator<String> ite = HS.iterator();
			while (ite.hasNext()) {
				String s = ite.next();
				File f = new File(songPath+"\\"+s);
//				System.out.println(f);
				try {
					Files.deleteIfExists(f.toPath());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void addTabForSpecificDifficulty(String tabName, Collection<TimedMistake> mistakes) {
		if (mistakes.size() == 0) {
			JOptionPane.showMessageDialog(null, "No mistake in " + tabName, "Good News",
					JOptionPane.INFORMATION_MESSAGE);
		} else {
			JPanel panel = new JPanel(false);
			panel.setLayout(new GridLayout(0, 1, 0, 10));
			panel.setAutoscrolls(true);

			for (TimedMistake mistake : mistakes) {
				makeTextPanel(panel, mistake.getURL(), mistake.getDescription());
			}

			JScrollPane scrollPane = new JScrollPane(panel);
			scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
			// scrollPane.setBounds(50, 30, 300, 50);
			tabbedPane.addTab(tabName, scrollPane);
		}

	}

	



	private static void makeLinkable(JLabel c, String url, String description, MouseListener ml) {
		assert ml != null;
		c.setText(htmlIfy(linkIfy(url,description)));
		c.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
		c.addMouseListener(ml);
	}



	private JLabel initLabel(JPanel panel,String text) {
		JLabel label = new JLabel(text);
		label.setHorizontalAlignment(JLabel.LEFT);
		Font font = label.getFont();
		label.setFont(new Font(font.getFontName(), font.getStyle(), font.getSize() * 2));
		label.setPreferredSize(new Dimension(panel.getWidth(), 20));
		panel.add(label);
		return label;
	}
	
	protected void makeTextPanel(JPanel panel, String text) {
		initLabel(panel,text);
	}
	
	protected void makeTextPanel(JPanel panel, String url, String description) {
		JLabel label = initLabel(panel, null);
		makeLinkable(label, url, description, new LinkMouseListener());
	}

	private static String getPlainLink(String s) {
		return s.substring(s.indexOf(A_HREF) + A_HREF.length(), s.indexOf(HREF_CLOSED));
	}
	
	// WARNING
	// This method requires that s is a plain string that requires
	// no further escaping
	private static String htmlIfy(String s) {
		return HTML.concat(s).concat(HTML_END);
	}
	
	// WARNING
	// This method requires that s is a plain string that requires
	// no further escaping
	private static String linkIfy(String url, String text) {
		return A_HREF.concat(url).concat(HREF_CLOSED).concat(url + " - " + text).concat(HREF_END);
	}

	private static class LinkMouseListener extends MouseAdapter {

		@Override
		public void mouseClicked(java.awt.event.MouseEvent evt) {
			JLabel l = (JLabel) evt.getSource();
			try {
				URI uri = new java.net.URI(getPlainLink(l.getText()));
				(new LinkRunner(uri)).execute();
			} catch (URISyntaxException use) {
				throw new AssertionError(use + ": " + l.getText());
			}
		}
	}

	private static class LinkRunner extends SwingWorker<Void, Void> {

		private final URI uri;

		private LinkRunner(URI u) {
			if (u == null) {
				throw new NullPointerException();
			}
			uri = u;
		}

		@Override
		protected Void doInBackground() throws Exception {
			Desktop desktop = java.awt.Desktop.getDesktop();
			desktop.browse(uri);
			return null;
		}

		@Override
		protected void done() {
			try {
				get();
			} catch (ExecutionException ee) {
				handleException(uri, ee);
			} catch (InterruptedException ie) {
				handleException(uri, ie);
			}
		}

		private static void handleException(URI u, Exception e) {
			JOptionPane.showMessageDialog(null,
					"Sorry, a problem occurred while trying to open this link in your system's standard browser.",
					"A problem occured", JOptionPane.ERROR_MESSAGE);
		}
	}

}
