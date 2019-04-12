package lab;

public class Main {
	public static void main(String[] args) {
		MyFileChooser myFileChooser = new MyFileChooser();
		myFileChooser.setFilters(new String[] {"All files", "project"} );
		int ret = myFileChooser.showLoadDialog();
		System.out.println(ret);
	}
}
