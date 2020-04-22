package apriori;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.SystemColor;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextPane;

public class Test extends JFrame {

	private JPanel contentPane;
	private JTable table;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Test frame = new Test();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Test() {
		JPanel contentPane2;
		JPanel panel2;
		// fenetre Table de similarite
		setAlwaysOnTop(true);
		setBackground(SystemColor.activeCaption);
		setBounds(100, 100, 667, 303);
		setTitle("Mesure Hamming et ensembles approximatifs");

		contentPane2 = new JPanel();
		contentPane2.setBackground(new Color(255, 239, 213));
		contentPane2.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane2);
		contentPane2.setLayout(null);

		panel2 = new JPanel();
		panel2.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "Similarity Table",
				TitledBorder.LEADING, TitledBorder.TOP, null, Color.BLUE));
		panel2.setBounds(10, 45, 351, 178);
		contentPane2.add(panel2);
		panel2.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 21, 331, 146);
		panel2.add(scrollPane);
		
		table = new JTable();
		scrollPane.setViewportView(table);
		
		JLabel NbItems = new JLabel("Nombre d'items");
		NbItems.setForeground(Color.BLUE);
		NbItems.setFont(new Font("Traditional Arabic", Font.BOLD, 15));
		NbItems.setBackground(Color.GRAY);
		NbItems.setBounds(371, 63, 115, 29);
		contentPane2.add(NbItems);
		
		JLabel NBTransactions = new JLabel("Nombre de transactions");
		NBTransactions.setForeground(Color.BLUE);
		NBTransactions.setFont(new Font("Traditional Arabic", Font.BOLD, 15));
		NBTransactions.setBackground(Color.GRAY);
		NBTransactions.setBounds(371, 133, 171, 29);
		contentPane2.add(NBTransactions);
		
		JTextPane NBitemText = new JTextPane();
		NBitemText.setEditable(false);
		NBitemText.setBounds(552, 63, 33, 29);
		contentPane2.add(NBitemText);
		
		JTextPane NbTransText = new JTextPane();
		NbTransText.setBounds(552, 133, 33, 29);
		contentPane2.add(NbTransText);
		
		JTextPane NbCandtext = new JTextPane();
		NbCandtext.setBounds(552, 194, 33, 29);
		contentPane2.add(NbCandtext);
		
		JLabel NbCandid = new JLabel("Nombre de candidats");
		NbCandid.setForeground(Color.BLUE);
		NbCandid.setFont(new Font("Traditional Arabic", Font.BOLD, 15));
		NbCandid.setBackground(Color.GRAY);
		NbCandid.setBounds(371, 194, 171, 29);
		contentPane2.add(NbCandid);


}
}