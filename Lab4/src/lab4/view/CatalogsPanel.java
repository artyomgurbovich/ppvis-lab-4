package lab4.view;

import java.io.File;
import java.util.Enumeration;

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
	
	public void openHomeDirectory() {
		tree.setSelectionPath(new TreePath(root.getPath()));
	}
	
	public void updateRootNode(File fileRoot) {
		root = new DefaultMutableTreeNode(fileRoot, true);
		treeModel.setRoot(root);
		ChildNodeManager childNodeManager = new ChildNodeManager(root, fileRoot);
        new Thread(childNodeManager).start();
    }
	
	private void reloadNode(DefaultMutableTreeNode node) {
		TreePath treePath = tree.getSelectionPath();
		treeModel.reload(node);
		tree.setSelectionPath(treePath);
    }
	
	public void updateChildNode(File file) {
		DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
		if (childNode != null && childNode.isLeaf()) {
			ChildNodeManager childNodeManager = new ChildNodeManager(childNode, file);
			new Thread(childNodeManager).start();
		}
	}
	
	public void openFolder(String folderName) {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
		Enumeration<?> children = node.children();
		while (children.hasMoreElements()) {
		    TreeNode childNode = (TreeNode) children.nextElement();
		    if (childNode.toString().equals(folderName)) {
		    	tree.setSelectionPath(tree.getSelectionPath().pathByAddingChild(childNode));
		    	break;
		    }
		}
	}
	
	public void setTreeSelectionListener(TreeSelectionListener treeSelectionListener) {
		tree.addTreeSelectionListener(treeSelectionListener);
	}
	
	class ChildNodeManager implements Runnable {
	    private DefaultMutableTreeNode node;
	    private File fileRoot;

	    public ChildNodeManager(DefaultMutableTreeNode node, File fileRoot) {
	        this.fileRoot = fileRoot;
	        this.node = node;
	    }

	    @Override
	    public void run() {
	    	getCatalog(node, fileRoot);
	    }

		private void getCatalog(DefaultMutableTreeNode node, File file) {
			file = new File(file.getPath() + "\\");
			File filesList[] = file.listFiles();
			if (filesList != null) {
	            for(File child: filesList)
	            {
	                DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(child.getName());
	                if(child.isDirectory())
	                {
	                	node.add(childNode);
	                	if (node != null) {
	                		reloadNode(node);
	                	}
	                }
	            }
			}
	    }
	}
}
