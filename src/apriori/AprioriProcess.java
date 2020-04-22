/* Date de creation 02/12/2016
 * Fatima Zohra Belabdi
 * Class MainApriori pour generer et appliquer l'algorithme Apriori
 * Derniere modification 05/12/2016
 */

package apriori;

// Les import utilisés
import java.awt.Color;
import java.awt.Font;
import java.awt.SystemColor;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

public class AprioriProcess {

	// Matrice de transaction pour le test Exp1
	// public static float TID[][] = { { 1, 2, 5 }, { 2, 4, 3 }, { 1, 2, 3 } };
	
	// Exemple 2 Data Mining Concepts and Techniques; Third Edition
	public static float TID[][] = { { 1, 2, 5, -1 }, { 2, 4, -1, -1 },
			{ 2, 3, -1, -1 }, { 1, 2, 4, -1 }, { 1, 3, -1, -1 },
			{ 2, 3, -1, -1 }, { 1, 3, -1, -1 }, { 1, 2, 3, 5 }, { 1, 2, 3, -1 } };
	
	// Les variables utilisées par le programme
	public static int NumItems = 4; // Le nombres de items par ligne ceux qu'on
	// veux les prendre
	public static int NumTransaction = 9; // Le nombre total de transaction dans
	// la TID
	public static int MinSup = 2; // Le Min support pas un % mais en valeurs int
	// 20% == 2
	public static Boolean stop = true;
	// ArrayList qui contient tous les condidats generer par l'algorithme Apriori

	public static ArrayList<ArrayList<ItemSet>> candedatGenerer = new ArrayList<ArrayList<ItemSet>>();

	/*
	 * ----------------------------------------------------------------------------------------------------
	 * ----------------------------------------------------------------------------------------------------
	   ----------------------------------------------------------------------------------------------------
	 * ------------------------------------Main Du Projet Algorithme Apriori-------------------------------
	 * ----------------------------------------------------------------------------------------------------
	 * ----------------------------------------------------------------------------------------------------
	 * ----------------------------------------------------------------------------------------------------
	 */

	public static void main(String[] args) throws IOException {

		// Lancer l'algorithme Apriori
		LancerApriori();
		AprioriFrame();

		for (int i = 0; i < candedatGenerer.size(); i++) {
			System.out.println("Condidat Numéro" + i);
			for (int j = 0; j < candedatGenerer.get(i).size(); j++) {
				System.out.println("ItemSet Numéro " + j + " ");
				for (int j2 = 0; j2 < candedatGenerer.get(i).get(j).ItemSet
						.size(); j2++) {

					System.out.print(candedatGenerer.get(i).get(j).ItemSet
							.get(j2));
				}
				System.out.print("  Support = "
						+ candedatGenerer.get(i).get(j).Support + "\n");
			}
		}
	} // Fin du main

	/*
	 * ------------------------------------------------------------------------------------------------------
	 * ------------------------------------------------------------------------------------------------------
	 * ------------------------------------------------------------------------------------------------------
	 * ------------------------------------------------------------------------------------------------------
	 * ----------------------------------- Les fonctions utilisées dans le projet----------------------------
	 * ------------------------------------------------------------------------------------------------------
	 * ------------------------------------------------------------------------------------------------------
	 * ------------------------------------------------------------------------------------------------------
	 */
	
	/*
	 * Fonction pour lancer l'algorithme Apriori
	 */
	
	public static void  LancerApriori(){
		
		// Numero du Condidat à generé
		int CandidatNum = 0;

		// Generer Les tables de Condidat tant que l'ensemble vide n'est pas
		// etteindre et le nombre de candidats < NumItems
		do {
			CandidatNum++;
			genererCandidat(CandidatNum);
			System.out.println("Candidat Numéro : " + CandidatNum);
			// System.out.println("Candidat  size: " +
			// candedatGenerer.get(CandidatNum - 1).size());

		} while (stop && candedatGenerer.get(CandidatNum - 1).size() > 1);
	} // Fin de la fonction LancerApriori
	
	/*
	 * Fonction genererCandidat qui genere les tables candidats Ci
	 */
	public static void genererCandidat(int NumC) {

		// Sauvgarder tous les itemset trouves dans ArrayList
		ArrayList<ItemSet> ArrayCandid = new ArrayList<ItemSet>();
		// Le premier candidat engendre tous les items existent dans la matrice
		if (NumC == 1) {
			// Parcourir la matrice de départ (BD)
			for (int i = 0; i < NumTransaction; i++) {
				for (int j = 0; j < NumItems; j++) {
					ItemSet itemset = new ItemSet();
					itemset.ItemSet = new Vector<String>();

					// Verifier si la case est vide ou non
					if (TID[i][j] != -1.0) {
						itemset.ItemSet.add(Float.toString(TID[i][j]));

						int exist = 0;
						// verifier s'il n'est pas deja dans la liste
						for (int k = 0; k < ArrayCandid.size(); k++) {

							if (ArrayCandid.get(k).ItemSet.contains(Float
									.toString(TID[i][j]))) {
								exist = 1;
								k = ArrayCandid.size();
							}

						}

						itemset.Support = CalculSupport(NumC, itemset.ItemSet);
						if (exist == 0 && itemset.Support > MinSup - 1) {
							ArrayCandid.add(itemset);

						}
					}

				}
			}// fin de parcours de la matrice TID
				// Rajouter la nouvelle table generer
			if (ArrayCandid.size() > 0)
				candedatGenerer.add(ArrayCandid);
		} // fin de traitement du premier cas

		// Generer le C2 à partir de C1 (TID)
		if (NumC == 2) {
			// parcourir le 1er Candidat genere
			for (int i = 0; i < candedatGenerer.get(NumC - 2).size(); i++) {

				for (int j = i + 1; j < candedatGenerer.get(NumC - 2).size(); j++) {

					ItemSet itemset = new ItemSet();
					itemset.ItemSet = new Vector<String>();
					// Generer les combinaisons possibles
					itemset.ItemSet = VectorTokenizer(
							candedatGenerer.get(NumC - 2).get(i).ItemSet,
							candedatGenerer.get(NumC - 2).get(j).ItemSet, NumC);
					int exist = ExistInArray(ArrayCandid, NumC, candedatGenerer
							.get(NumC - 2).get(i).ItemSet);

					// Ajouter l'element ssi il n'existe pas deje dans notre Set
					itemset.Support = CalculSupport(NumC, itemset.ItemSet);

					// System.out.println(" Calcul des Support pour i  "+ i +
					// "  j "+j +" Bien effectué ");
					if (exist == 0 && itemset.Support > MinSup - 1) {
						ArrayCandid.add(itemset);
						// System.out.println(itemset.ItemSet);

					}

				}
			}
			// Rajouter la nouvelle table generer
			if (ArrayCandid.size() > 0)
				candedatGenerer.add(ArrayCandid);
		}// fin de traitement du premier cas

		// Generer le reste des cas de la meme manière (Parcours Cn-1 et
		// recherche des combinaisons)
		if (NumC > 2) {
			//System.out.println("traitement " + NumC + " Size "+ candedatGenerer.get(NumC - 2).size());

			// if (candedatGenerer.get(NumC - 2).size() >= 2) {
			// parcourir le Candidat Cn-1 genere
			int size = candedatGenerer.get(NumC - 2).size();
			if (size != 1) {
				for (int i = 0; i < size; i++) {

					for (int j = i + 1; j < size; j++) {

						ItemSet itemset = new ItemSet();
						itemset.ItemSet = new Vector<String>();
						// Generer les combinaisons possibles
						itemset.ItemSet = VectorTokenizer(
								candedatGenerer.get(NumC - 2).get(i).ItemSet,
								candedatGenerer.get(NumC - 2).get(j).ItemSet,
								NumC);
						int exist = ExistInArray(ArrayCandid, NumC,
								candedatGenerer.get(NumC - 2).get(i).ItemSet);

						// Ajouter l'element ssi il n'existe pas deje dans notre
						// Set
						// et qu'il n'est pas vide
						if (exist == 0 && itemset.ItemSet.size() > 0) {
							itemset.Support = CalculSupport(NumC,
									itemset.ItemSet);
							if (itemset.Support > MinSup - 1) {
								ArrayCandid.add(itemset);
								//System.out.println(itemset.ItemSet);
							}

						}
					}
				} // fin de parcours de Cn-1

				if (ArrayCandid.size() > 0)
					candedatGenerer.add(ArrayCandid);
				else {
					stop = false;
				}

			}
		}
		System.out.println("Sortie " + NumC + stop);

	} // fin de la fonction genererCandidat

	/*
	 * Fonction VectorTokenizer qui permet de tokinizer un vecteur et de trouver
	 * les combinaisons possibles et non dupliquées entre ses éléments fusion de
	 * deux vecteurs + tokinization + Sortie : un vecteur
	 */
	public static Vector<String> VectorTokenizer(Vector<String> item,
			Vector<String> item2, int NumC) {
		// temporaire Item pour la sortie de la fonction
		Vector<String> tempItem = new Vector<String>();
		// Variable de comparaison après tokenization
		String str1, str2;
		// Variables de tokenization
		StringTokenizer st1, st2;
		// Vector pour fusionner les deux items (deux ligne dans un candidat)
		Vector<String> fusionVector = new Vector<String>();

		for (int i = 0; i < item.size(); i++) {
			// S'il n'existe pas alors l'ajouter pour eviter les duplications
			// des elements
			if (!fusionVector.contains(item.get(i))) {

				fusionVector.addElement(item.get(i));
			}
			if (!fusionVector.contains(item2.get(i))) {

				fusionVector.addElement(item2.get(i));
			}
		}

		// Dans le C2
		if (NumC == 2) {

			// Combiner un element uvec son seccesseur
			for (int i = 0; i < fusionVector.size(); i++) {
				// Tokenizer le vecteur i et son successeur après les combines
				st1 = new StringTokenizer(fusionVector.get(i));
				str1 = st1.nextToken();
				for (int j = i + 1; j < fusionVector.size(); j++) {
					st2 = new StringTokenizer(fusionVector.elementAt(j));
					str2 = st2.nextToken();
					tempItem.add(str1 + " " + str2);
				}
				// System.out .println(" Temp" + tempItem);
			}
			// Traiter les reste des cas Ci >2
		} else {
			// faire le meme traitement precedent
			for (int i = 0; i < fusionVector.size(); i++) {

				for (int j = i + 1; j < fusionVector.size(); j++) {
					// c
					str1 = new String();
					str2 = new String();
					// create the tokenizers
					st1 = new StringTokenizer(fusionVector.get(i));
					st2 = new StringTokenizer(fusionVector.get(j));

					// Extraire un string de longeur egale à NumC-2
					for (int s = 0; s < NumC - 2; s++) {
						str1 = str1 + " " + st1.nextToken();
						str2 = str2 + " " + st2.nextToken();
					}

					// Comparer les deux str pour eviter les duplications
					if (str2.compareToIgnoreCase(str1) == 0)
						tempItem.add((str1 + " " + st1.nextToken() + " " + st2
								.nextToken()).trim());
				}
			}
		}
		// Retourner le vecteur trouvé (Le nouveau Set generé)
		return tempItem;
	} // Fin de la fonction VectorTokenizer

	/*
	 * Fonction ExistInArray pour verifier si un element existe dans un ensemble
	 * dans ArrayCandid Sortie existe == 0 si n'existe pas sinon 1
	 */
	public static int ExistInArray(ArrayList<ItemSet> ArrayCandid, int NumC,
			Vector<String> itemSet) {

		int exist = 0;

		// verifier s'il n'est pas deja dans la liste

		for (int k = 0; k < ArrayCandid.size(); k++) {

			if (ArrayCandid.get(k).ItemSet.contains(itemSet)) {
				exist = 1;
				k = ArrayCandid.size();
			}

		}

		return exist;
	} // Fin de la fonction ExistInArray

	/*
	 * Fonction calcul de frequence ou support pour un itemSet
	 */
	public static int CalculSupport(int NumC, Vector<String> itemSet) {

		// Pour calculer la fréquence de l'item dans la table Ci-1
		int support = 0;

		// tokenize l'itemSet en plusieurs element pour calculer la
		// fréquence de chacun

		String str1 = itemSet.get(0);
		// System.out.println(" str1"+str1);
		// Parcourir Cn-1, la table précedente
		// 1er cas par rapport à la table TID (Matrice de départ)
		if (NumC == 1) {

			for (int i1 = 0; i1 < NumTransaction; i1++) {
				for (int j1 = 0; j1 < NumItems; j1++) {
					String str = Float.toString(TID[i1][j1]);
					// System.out.println(str);
					if (str.equals(str1)) {
						support++;
						// System.out.println(support);

					}
				}

			}
		}

		else {

			// Parcourir les transactions (lignes de matrice)
			for (int i = 0; i < NumTransaction; i++) {

				// Nombre d'element de ItemSet qui existe dans une ligne
				int NbExiste = 0;
				// Parcourir les elements de l'itemSet
				StringTokenizer st = new StringTokenizer(itemSet.toString());
				// Verifier les items de tous l'ensemble (Vector)
				while (st.hasMoreTokens()) {

					str1 = st.nextToken();
					str1 = str1.replace("[", "");
					str1 = str1.replace("]", "");

					// Parcourir les colonnes pour comparer avec les elements de
					// l'itemSet
					for (int j1 = 0; j1 < NumItems; j1++) {
						// Si vide alors passé à la prochaine case
						if (TID[i][j1] != -1.0) {

							String str2 = Float.toString(TID[i][j1]);
							// System.out.println(" i: " + i + "  j1: " + j1 +
							// str2);
							if (str1.equals(str2)) {
								// Incrémenter le nombre d'element qui existe
								NbExiste++;
								// System.out.println(i + " " + j1 +
								// " NbExiste = "+ NbExiste);
								// Sortir de cette boucle
								j1 = NumItems;
							}
						}
					}
				}
				// Incrémenter la frequence si tous les elements existe dans une
				// ligne
				if (NbExiste == NumC) {
					support++;
				}
			}

		}

		return support;
	} // Fin de la fonction CalculSupport
	
	
	
	
	
	//
	
	
	
	public static void AprioriFrame () throws IOException {
		
		JPanel contentPane2;
		JPanel panel2;
		// fenetre Table des Candidats générés
		JFrame frame =new JFrame();
		frame.setAlwaysOnTop(true);
		frame.setBackground(SystemColor.activeCaption);
		frame.setBounds(100, 100, 667, 303);
		frame.setTitle("Algorithme Apriori");

		contentPane2 = new JPanel();
		contentPane2.setBackground(new Color(255, 239, 213));
		contentPane2.setBorder(new EmptyBorder(5, 5, 5, 5));
		frame.setContentPane(contentPane2);
		contentPane2.setLayout(null);

		panel2 = new JPanel();
		panel2.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "Table des candidats",
				TitledBorder.LEADING, TitledBorder.TOP, null, Color.BLUE));
		panel2.setBounds(10, 45, 351, 178);
		contentPane2.add(panel2);
		panel2.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 21, 331, 146);
		panel2.add(scrollPane);
		
		JTable tableCandidat = new JTable();
		scrollPane.setViewportView(tableCandidat);
		
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
		
		NBitemText.setText(String.valueOf(NumItems));
		
		JTextPane NbTransText = new JTextPane();
		NbTransText.setBounds(552, 133, 33, 29);
		contentPane2.add(NbTransText);
		
		NbTransText.setText(String.valueOf(NumTransaction));
		
		JTextPane NbCandtext = new JTextPane();
		NbCandtext.setBounds(552, 194, 33, 29);
		contentPane2.add(NbCandtext);
		
		NbCandtext.setText(String.valueOf(candedatGenerer.size()+1));
		
		JLabel NbCandid = new JLabel("Nombre de candidats");
		NbCandid.setForeground(Color.BLUE);
		NbCandid.setFont(new Font("Traditional Arabic", Font.BOLD, 15));
		NbCandid.setBackground(Color.GRAY);
		NbCandid.setBounds(371, 194, 171, 29);
		contentPane2.add(NbCandid);


				File f = new File("Resultats");
				FileWriter fw = new FileWriter(f);

				// création d'un model de la Jtable
				DefaultTableModel model1 = (DefaultTableModel) tableCandidat.getModel();
				// remettre a 0 la table (Jtable)
				model1.setColumnCount(0);
				model1.setRowCount(0);

				fw.write("--------------------------- Algorithme Apriori --------------------------\n\n");

				fw.write("\n\n");
				model1.addColumn("Numéro Condidat");
				model1.addColumn("ItemSets");
				model1.addColumn("Support");

				// Parcourir la table de tous les candidats
				for (int i = 0; i < AprioriProcess.candedatGenerer.size(); i++) {
					
					fw.write(" \nCondidat numéro " + i + "\n\n");
					
					// Parcourir les candidats generes par l'algorithme Apriori
					for (int j = 0; j < AprioriProcess.candedatGenerer.get(i).size(); j++) {
						fw.write("\n");
						Vector ligne = new Vector();
						ligne.add(i+1);
						ligne.add(AprioriProcess.candedatGenerer.get(i).get(j).ItemSet);
						fw.write(AprioriProcess.candedatGenerer.get(i).get(j).ItemSet
								+ "\t");
						ligne.add(AprioriProcess.candedatGenerer.get(i).get(j).Support);
						fw.write(AprioriProcess.candedatGenerer.get(i).get(j).Support
								+ "\t");
						model1.addRow(ligne);

					}

				}
				AjusteTable(tableCandidat);
				fw.close();

				 frame.setVisible(true);

			}
	
	
	///
	
	
	
	public static void AjusteTable(JTable Table) {
		int col = 0, droiteMax = 0, larg = 0, largTotal = 0, row = 0, tableX = 0, width = 0;
		// Récuperer l'entete de Jtable à ajuster
		JTableHeader header = Table.getTableHeader();
		// Récuperer les colonnes en liste pour récuperer leurs largeurs
		Enumeration columns = Table.getColumnModel().getColumns();
		// Ne pas donner le choix à la table pour auto resize car on va le faire
		// manuellement
		Table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		// Parcourir les colonnes pour récupérer la longueur maximum en fonction
		// du contenu d'une colonne
		while (columns.hasMoreElements()) {
			TableColumn column = (TableColumn) columns.nextElement();
			col = header.getColumnModel()
					.getColumnIndex(column.getIdentifier());
			width = (int) Table
					.getTableHeader()
					.getDefaultRenderer()
					.getTableCellRendererComponent(Table,
							column.getIdentifier(), false, false, -1, col)
					.getPreferredSize().getWidth();
			for (row = 0; row < Table.getRowCount(); row++) {
				int preferedWidth = (int) Table
						.getCellRenderer(row, col)
						.getTableCellRendererComponent(Table,
								Table.getValueAt(row, col), false, false, row,
								col).getPreferredSize().getWidth();
				width = Math.max(width, preferedWidth);
			}
			// Redimmentioner les dimention de l'entete
			header.setResizingColumn(column);
			larg = width + Table.getIntercellSpacing().width;
			// ajouter une constance pour ne pas trop serrer les colonnes
			larg = larg + 20;
			largTotal += larg;
			column.setWidth(larg);
		}
	}// Fin de fonction Ajuste Jtable


}