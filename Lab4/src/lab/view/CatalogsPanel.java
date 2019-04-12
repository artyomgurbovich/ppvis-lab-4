package lab.view;

import javax.swing.*;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.*;

@SuppressWarnings("serial")
public class CatalogsPanel extends JPanel {
	
	private JTree tree;
	private DefaultMutableTreeNode root;
	
	public CatalogsPanel() {
        root = new DefaultMutableTreeNode(null, true);
        tree = new JTree(root);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.setShowsRootHandles(true);
        add(tree);
	}
	
	public void updateData(DefaultMutableTreeNode root) {
		DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
		model.setRoot(root);
    }
	
	public void setTreeSelectionListener(TreeSelectionListener treeSelectionListener) {
		tree.addTreeSelectionListener(treeSelectionListener);
	}
}
