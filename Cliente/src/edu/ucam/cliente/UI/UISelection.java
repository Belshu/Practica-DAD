package edu.ucam.cliente.UI;

import java.awt.Component;
import java.util.List;
import java.util.function.Function;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

public class UISelection {
	public static <T> List<T> seleccionarVarios(Component parent, String titulo, List<T> disponibles, Function<T, String> labeler) {
		
		DefaultListModel <T> model = new DefaultListModel<>();
		for(T d : disponibles) model.addElement(d);
		
		JList<T> list = new JList<T>(model);
		list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		
		list.setCellRenderer((lst, value, index, isSelected, cellHasFocus) -> {
			JLabel lbl = new JLabel(labeler.apply(value));
			
			if(isSelected) {
				lbl.setOpaque(true);
				lbl.setBackground(lst.getSelectionBackground());
				lbl.setForeground(lst.getSelectionForeground());
			}
			
			return lbl;
		});
		
		JScrollPane scroll = new JScrollPane(list);
		int opt = JOptionPane.showConfirmDialog(parent, scroll, titulo, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if(opt != JOptionPane.OK_OPTION) return null;
		
		return list.getSelectedValuesList();
	}
}
