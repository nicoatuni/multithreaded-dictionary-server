package client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ClientGUI extends JFrame {
	private JTextField searchTextField;
	private JTextField newWordInput;
	private JTextField newWordDefinitionInput;
	private JTextField updateWordInput;
	private JTextField updateWordDefinitionInput;
	private JTextField removeWordInput;

	public ClientGUI() {
		setTitle("Dictionary Client");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(640, 600);
		getContentPane().setLayout(null);

		searchTextField = new JTextField();
		searchTextField.setBounds(6, 6, 496, 34);
		searchTextField.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));
		getContentPane().add(searchTextField);
		searchTextField.setColumns(10);

		JButton searchButton = new JButton("Search");
		searchButton.setFont(new Font("Helvetica Neue", Font.PLAIN, 14));
		searchButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO: query word definition
			}
		});
		searchButton.setBounds(514, 6, 117, 34);
		getContentPane().add(searchButton);

		JTextArea searchResults = new JTextArea();
		searchResults.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));
		searchResults.setEditable(false);
		searchResults.setBounds(10, 53, 615, 60);
		getContentPane().add(searchResults);

		JSeparator separator = new JSeparator();
		separator.setBounds(0, 125, 640, 12);
		getContentPane().add(separator);

		JLabel addNewWordLabel = new JLabel("Add New Word:");
		addNewWordLabel.setFont(new Font("Helvetica Neue", Font.PLAIN, 14));
		addNewWordLabel.setBounds(12, 149, 101, 16);
		getContentPane().add(addNewWordLabel);

		newWordInput = new JTextField();
		newWordInput.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));
		newWordInput.setBounds(120, 140, 384, 34);
		getContentPane().add(newWordInput);
		newWordInput.setColumns(10);

		JLabel newWordDefinitionLabel = new JLabel("Definition:");
		newWordDefinitionLabel.setFont(new Font("Helvetica Neue", Font.PLAIN, 14));
		newWordDefinitionLabel.setBounds(12, 189, 71, 16);
		getContentPane().add(newWordDefinitionLabel);

		newWordDefinitionInput = new JTextField();
		newWordDefinitionInput.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));
		newWordDefinitionInput.setColumns(10);
		newWordDefinitionInput.setBounds(120, 180, 384, 34);
		getContentPane().add(newWordDefinitionInput);

		JButton addNewWordButton = new JButton("Add");
		addNewWordButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO: add new word definition
			}
		});
		addNewWordButton.setFont(new Font("Helvetica Neue", Font.PLAIN, 14));
		addNewWordButton.setBounds(514, 180, 117, 34);
		getContentPane().add(addNewWordButton);

		JTextArea addNewWordResults = new JTextArea();
		addNewWordResults.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));
		addNewWordResults.setEditable(false);
		addNewWordResults.setBounds(12, 227, 615, 34);
		getContentPane().add(addNewWordResults);

		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(0, 273, 640, 12);
		getContentPane().add(separator_1);

		JLabel updateWordLabel = new JLabel("Update Word:");
		updateWordLabel.setFont(new Font("Helvetica Neue", Font.PLAIN, 14));
		updateWordLabel.setBounds(12, 303, 101, 16);
		getContentPane().add(updateWordLabel);

		updateWordInput = new JTextField();
		updateWordInput.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));
		updateWordInput.setColumns(10);
		updateWordInput.setBounds(120, 294, 384, 34);
		getContentPane().add(updateWordInput);

		updateWordDefinitionInput = new JTextField();
		updateWordDefinitionInput.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));
		updateWordDefinitionInput.setColumns(10);
		updateWordDefinitionInput.setBounds(120, 334, 384, 34);
		getContentPane().add(updateWordDefinitionInput);

		JButton updateWordButton = new JButton("Update");
		updateWordButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO: update word definition
			}
		});
		updateWordButton.setFont(new Font("Helvetica Neue", Font.PLAIN, 14));
		updateWordButton.setBounds(514, 334, 117, 34);
		getContentPane().add(updateWordButton);

		JTextArea updateWordResults = new JTextArea();
		updateWordResults.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));
		updateWordResults.setEditable(false);
		updateWordResults.setBounds(12, 381, 615, 34);
		getContentPane().add(updateWordResults);

		JSeparator separator_2 = new JSeparator();
		separator_2.setBounds(0, 427, 640, 12);
		getContentPane().add(separator_2);

		JLabel updateWordDefinitionLabel = new JLabel("New Definition:");
		updateWordDefinitionLabel.setFont(new Font("Helvetica Neue", Font.PLAIN, 14));
		updateWordDefinitionLabel.setBounds(12, 343, 101, 16);
		getContentPane().add(updateWordDefinitionLabel);

		JLabel removeWordLabel = new JLabel("Remove Word:");
		removeWordLabel.setFont(new Font("Helvetica Neue", Font.PLAIN, 14));
		removeWordLabel.setBounds(12, 460, 101, 16);
		getContentPane().add(removeWordLabel);

		removeWordInput = new JTextField();
		removeWordInput.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));
		removeWordInput.setColumns(10);
		removeWordInput.setBounds(120, 451, 384, 34);
		getContentPane().add(removeWordInput);

		JButton removeWordButton = new JButton("Remove");
		removeWordButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO: remove word
			}
		});
		removeWordButton.setFont(new Font("Helvetica Neue", Font.PLAIN, 14));
		removeWordButton.setBounds(514, 451, 117, 34);
		getContentPane().add(removeWordButton);

		JTextArea updateWordResults_1 = new JTextArea();
		updateWordResults_1.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));
		updateWordResults_1.setEditable(false);
		updateWordResults_1.setBounds(12, 498, 615, 34);
		getContentPane().add(updateWordResults_1);

		setVisible(true);
	}
}
