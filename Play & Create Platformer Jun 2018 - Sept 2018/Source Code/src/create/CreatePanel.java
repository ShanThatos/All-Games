package create;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicArrowButton;

public class CreatePanel extends JPanel {
	private final int FPS = 60;
	
	private Manager m;
	
	// Settings
	private int currentPage = 0;
	private final int totalPages = 2;
	
	// Change Page Area
	private JButton saveMap, openMap;
	private JButton right, left;
	// Page Container
	private JPanel pageContainer;
	// Display Components
	private JPanel display;
	private JLabel currentTilingInfo;
	// Tile Edit Components
	private JPanel options, tiledisplay;
	private JLabel lblTile, lblMeshBox;
	private JComboBox<String> tileSheets;
	private JToggleButton tglbtnMesh, tglbtnBackground = new JToggleButton("Background Tiling"), tglbtnDelete;
	private JSeparator separator, separator_1;
	private JComboBox<ImageIcon> tileChooser, meshBoxes;
	// Player Edit Components
	private JPanel player;
	private JLabel lblPlayerSettings;
	private JToggleButton tglbtnSetPosition;
	private JButton btnFocusPosition;
	private JSeparator separator_2;
	
	public CreatePanel(Manager m) {
		this.m = m;
		setBackground(Color.LIGHT_GRAY);
		setPreferredSize(new Dimension(1100, 700));
		setLayout(null);
		createComponents();
	}
	private void createComponents() {
		createDisplay();
		createPageContainer();
	}
	private void createDisplay() {
		display = new JPanel() {
			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				m.display(g);
			}
		};
		display.setLayout(null);
		display.setFocusable(true);
		display.setBorder(new LineBorder(new Color(0, 0, 0)));
		display.setBounds(50, 50, 800, 600);
		display.addMouseListener(m);
		display.addMouseMotionListener(m);
		display.addMouseWheelListener(m);
		display.addKeyListener(m);
		
		currentTilingInfo = new JLabel("<html>Current Tiling Info:<br/>" + m.getTilingInfo() + "</html>");
		currentTilingInfo.setFont(new Font("Yu Gothic", Font.BOLD, 12));
		currentTilingInfo.setHorizontalAlignment(SwingConstants.LEFT);
		currentTilingInfo.setForeground(Color.red);
		currentTilingInfo.setBounds(630, 10, 160, 200);
		currentTilingInfo.setVerticalAlignment(JLabel.TOP);
		display.add(currentTilingInfo);
		
		add(display);
	}
	private void createPageContainer() {
		pageContainer = new JPanel();
		pageContainer.setLayout(null);
		pageContainer.setBounds(850, 50, 200, 600);
		pageContainer.setBackground(new Color(153, 153, 255));
		pageContainer.setBorder(new LineBorder(new Color(0, 0, 0)));
		pageContainer.add(createTileEditPage());
		
		createPlayerEditPage();
		
		saveMap = new JButton("Save Map");
		saveMap.setBounds(32, 470, 136, 30);
		saveMap.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try { m.save(); } catch (Exception exp) {}
			}
		});
		pageContainer.add(saveMap);
		
		openMap = new JButton("Open Map");
		openMap.setBounds(32, 505, 136, 30);
		openMap.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try { m.read(); } catch (Exception exp) {}
			}
		});
		pageContainer.add(openMap);
		
		right = new BasicArrowButton(BasicArrowButton.EAST);
		right.setBounds(115, 550, 70, 35);
		right.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int nextPage = Math.min(totalPages, currentPage + 1);
				if (nextPage == currentPage)
					return;
				currentPage = nextPage;
				resetPage();
			}
		});
		pageContainer.add(right);
		left = new BasicArrowButton(BasicArrowButton.WEST);
		left.setBounds(15, 550, 70, 35);
		left.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int nextPage = Math.min(totalPages, currentPage - 1);
				if (nextPage == currentPage)
					return;
				currentPage = nextPage;
				resetPage();
			}
		});
		pageContainer.add(left);
		
		add(pageContainer);
	}
	private void resetPage() {
		pageContainer.remove(options);
		pageContainer.remove(player);
		switch (currentPage) {
		case 0:
			pageContainer.add(options);
			break;
		case 1:
			pageContainer.add(player);
		}
		m.setCurrentPage(currentPage);
	}
	private JPanel createTileEditPage() {
		options = new JPanel();
		options.setBackground(new Color(221, 160, 221));
		options.setBorder(new LineBorder(new Color(0, 0, 0)));
		options.setBounds(0, 0, 200, 450);
		options.setLayout(null);
		
		tileSheets = new JComboBox<>();
		for (String s : Tile.getTileSheets())
			tileSheets.addItem(s);
		tileSheets.setBounds(6, 234, 88, 31);
		tileSheets.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				m.setTileSheet(tileSheets.getSelectedIndex());
				tileChooser.removeAllItems();
				for (ImageIcon tile : Tile.getImagesForTileSheet(tileSheets.getSelectedIndex())) 
					tileChooser.addItem(tile);
				updateCurrentTilingInfo(currentTilingInfo);
			}
		});
		options.add(tileSheets);
		
		tileChooser = new JComboBox<>();
		for (ImageIcon tile : Tile.getImagesForTileSheet(tileSheets.getSelectedIndex())) 
			tileChooser.addItem(tile);
		tileChooser.setBounds(6, 270, 88, 70);
		tileChooser.setMaximumRowCount(3);
		tileChooser.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				m.setTileNumber(tileChooser.getSelectedIndex() + Tile.getTileImageIndices()[tileSheets.getSelectedIndex()]);
			}
		});
		options.add(tileChooser);
		
		meshBoxes = new JComboBox<>();
		for (ImageIcon meshBox : Tile.getAllMeshBoxIcons())
			meshBoxes.addItem(meshBox);
		meshBoxes.setSelectedIndex(1);
		m.setMeshNumber(1);
		updateCurrentTilingInfo(currentTilingInfo);
		meshBoxes.setBounds(106, 270, 88, 70);
		meshBoxes.setMaximumRowCount(3);
		meshBoxes.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (!tglbtnBackground.isSelected()) {
					m.setMeshNumber(meshBoxes.getSelectedIndex());
					updateCurrentTilingInfo(currentTilingInfo);
				}
			}
		});
		options.add(meshBoxes);
		
		tglbtnMesh = new JToggleButton("Show Mesh");
		tglbtnMesh.setMargin(new Insets(2, 4, 2, 4));
		tglbtnMesh.setBackground(new Color(220, 20, 60));
		tglbtnMesh.setFont(new Font("Yu Gothic", Font.PLAIN, 10));
		tglbtnMesh.setBounds(12, 138, 80, 25);
		tglbtnMesh.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				m.setMeshDisplay(tglbtnMesh.isSelected());
			}
		});
		tglbtnMesh.setSelected(true);
		m.setMeshDisplay(true);
		
		tiledisplay = new JPanel() {
			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				m.displaySingleTile(g);
			}
		};
		tiledisplay.setBackground(new Color(173, 216, 230));
		tiledisplay.setBorder(new LineBorder(new Color(0, 0, 0)));
		tiledisplay.setBounds(12, 13, 176, 176);
		options.add(tiledisplay);
		tiledisplay.setLayout(null);
		tiledisplay.add(tglbtnMesh);
		
		separator = new JSeparator();
		separator.setBounds(12, 198, 176, 2);
		options.add(separator);
		
		lblTile = new JLabel("Tile");
		lblTile.setFont(new Font("Yu Gothic Medium", Font.BOLD, 18));
		lblTile.setHorizontalAlignment(SwingConstants.CENTER);
		lblTile.setBounds(12, 200, 76, 31);
		options.add(lblTile);
		
		lblMeshBox = new JLabel("<html><div style=\"text-align:center;\">Mesh<br/>Box</div></html>");
		lblMeshBox.setFont(new Font("Yu Gothic", Font.BOLD, 18));
		lblMeshBox.setHorizontalAlignment(SwingConstants.CENTER);
		lblMeshBox.setBounds(112, 194, 76, 62);
		options.add(lblMeshBox);
		
		separator_1 = new JSeparator();
		separator_1.setBounds(12, 343, 176, 2);
		options.add(separator_1);
		
		tglbtnBackground.setBackground(new Color(222, 184, 135));
		tglbtnBackground.setBounds(32, 358, 140, 30);
		tglbtnBackground.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				m.setTileInBackground(tglbtnBackground.isSelected());
				if (tglbtnBackground.isSelected())
					meshBoxes.setSelectedIndex(0);
				meshBoxes.setEnabled(!tglbtnBackground.isSelected());
				m.setMeshNumber(meshBoxes.getSelectedIndex());
				updateCurrentTilingInfo(currentTilingInfo);
			}
		});
		options.add(tglbtnBackground);
		
		tglbtnDelete = new JToggleButton("Delete Mode");
		tglbtnDelete.setBackground(new Color(255, 99, 71));
		tglbtnDelete.setBounds(32, 401, 140, 30);
		tglbtnDelete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean isSelected = tglbtnDelete.isSelected();
				m.setDeleteTiles(isSelected);
				meshBoxes.setEnabled(!isSelected);
				tglbtnBackground.setEnabled(!isSelected);
				tileSheets.setEnabled(!isSelected);
				tileChooser.setEnabled(!isSelected);
			}
		});
		options.add(tglbtnDelete);
		
		return options;
	}
	
	private JPanel createPlayerEditPage() {
		player = new JPanel();
		player.setBackground(new Color(102, 255, 178));
		player.setBorder(new LineBorder(new Color(0, 0, 0)));
		player.setBounds(0, 0, 200, 450);
		player.setLayout(null);
		
		lblPlayerSettings = new JLabel("Player Settings");
		lblPlayerSettings.setHorizontalAlignment(SwingConstants.CENTER);
		lblPlayerSettings.setFont(new Font("Yu Gothic UI Semibold", Font.BOLD, 20));
		lblPlayerSettings.setBounds(29, 13, 141, 33);
		player.add(lblPlayerSettings);
		
		separator_2 = new JSeparator();
		separator_2.setBounds(21, 48, 158, 2);
		player.add(separator_2);
		
		tglbtnSetPosition = new JToggleButton("Set Position");
		tglbtnSetPosition.setBackground(new Color(135, 206, 250));
		tglbtnSetPosition.setBounds(29, 59, 141, 25);
		player.add(tglbtnSetPosition);
		
		btnFocusPosition = new JButton("Focus Position");
		btnFocusPosition.setBackground(new Color(205, 92, 92));
		btnFocusPosition.setBounds(29, 97, 141, 25);
		btnFocusPosition.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				m.setCameraCenterToPlayer();
			}
		});
		player.add(btnFocusPosition);
		
		return player;
	}
	
	private void updateCurrentTilingInfo(JLabel currentTilingInfo) {
		currentTilingInfo.setText("<html>Current Tiling Info:<br/>" + m.getTilingInfo() + "</html>");
	}
	
	public void update() {
		m.update();
	}
}
