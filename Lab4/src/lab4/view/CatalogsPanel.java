package lab4.view;

import javax.swing.*;
import javax.swing.event.TreeSelectionListener;
import javax.swing.plaf.metal.MetalIconFactory;
import javax.swing.tree.*;

@SuppressWarnings("serial")
public class CatalogsPanel extends JPanel {
	
	private JTree tree;
	private DefaultMutableTreeNode root;
    private DefaultTreeModel treeModel;
	
	public CatalogsPanel() {
        root = new DefaultMutableTreeNode(null, true);
        treeModel = new DefaultTreeModel(root);
        tree = new JTree(treeModel);
        ((DefaultTreeCellRenderer) tree.getCellRenderer()).setLeafIcon(MetalIconFactory.getTreeFolderIcon());
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.setShowsRootHandles(true);
        add(tree);
	}
	
	public void updateRootNode(DefaultMutableTreeNode root) {
		treeModel.setRoot(root);
    }
	
	public void updateChildNode(DefaultMutableTreeNode node) {
		TreePath treePath = tree.getSelectionPath();
		treeModel.reload(node);
		tree.setSelectionPath(treePath);
    }
	
	public void setTreeSelectionListener(TreeSelectionListener treeSelectionListener) {
		tree.addTreeSelectionListener(treeSelectionListener);
	}
}
