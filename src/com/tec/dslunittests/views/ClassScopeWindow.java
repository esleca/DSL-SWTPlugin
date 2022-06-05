package com.tec.dslunittests.views;

import java.io.IOException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import com.google.gson.Gson;
import com.tec.dslunittests.models.UnitTestData;

public class ClassScopeWindow {

	private Button editBtn, deleteBtn, newBtn;
	private Composite layer;
	private CTabFolder folder;
	private String path;

	private String jsonString = """
			[

				{
					"packageName": "package_1",
					"className": "class1",
					"functionName": "getEdades",
					"testName": "getEdades_test1",
					"parameters" : ['param1','param2','param3'],
					"expected": ["1","2","3"]
				},
				{
					"packageName": "package_1",
					"className": "class1",
					"functionName": "getEdades",
					"testName": "getEdades_test2",
					"parameters" : ['param1','param2','param3'],
					"expected": ["1","2","3"]
				},
				{
					"packageName": "package_1",
					"className": "class2",
					"functionName": "getTable",
					"testName": "getTable_test1",
					"parameters" : ['param1','param2','param3'],
					"expected": ["1","2","3"]
				},
				{
					"packageName": "package_1",
					"className": "class2",
					"functionName": "getTable",
					"testName": "getTable_test2",
					"parameters" : ['param1','param2','param3'],
					"expected": ["1","2","3"]
				}
			]""";
	
	public ClassScopeWindow() {
		
	}
	
	public ClassScopeWindow(String path) {
		this.path = path;
	}

	/**
	 * Defines widgets and layout for the package unit test window
	 *
	 * @param parent main composite in which the view must be rendered
	 * @return the composite ready to be displayed
	 * @throws IOException file cannot be read
	 */
	public Composite render(Composite parent) {
		// Creates new composite layer to add widgets
		layer = new Composite(parent, SWT.NONE);
		layer.setLayout(new FillLayout(SWT.HORIZONTAL));

		Gson gson = new Gson();

		// Transforms json data to java object
		UnitTestData[] UTdata = gson.fromJson(jsonString, UnitTestData[].class);

		Table table = new Table(layer, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
		data.heightHint = 200;
		table.setLayoutData(data);

		String[] columns = { "#", "Function", "Unit test", "Parameters", "Expected result", " ", " " };

		for (int i = 0; i < columns.length; i++) {
			TableColumn column = new TableColumn(table, SWT.NONE);
			column.setText(columns[i]);
			table.getColumn(i).pack();
		}

		for (int i = 0; i <= UTdata.length - 1; i++) {
			TableItem item = new TableItem(table, SWT.NONE);
			item.setText(0, "" + (i + 1));
			item.setText(1, UTdata[i].getFunctionName());
			item.setText(2, UTdata[i].getTestName());

			String parameters = "";
			for (int k = 0; k <= UTdata[i].getParameters().size() - 1; k++) {
				parameters += UTdata[i].getParameters().get(k) + " ";
			}
			item.setText(3, parameters);

			String expected = UTdata[i].getExpected().getValue();			
			item.setText(4, expected);

			// Data of the existent unit tests
			final UnitTestData editable = UTdata[i];

			// Set attributes for the button to load edition window
			editBtn = new Button(table, SWT.PUSH);
			editBtn.setText("Edit");
			editBtn.computeSize(SWT.DEFAULT, table.getItemHeight());
			editBtn.addListener(SWT.Selection, event -> loadEditionWindow(parent, editable));

			TableEditor editor = new TableEditor(table);
			editor.setEditor(editBtn, item, 5);

			// Set attributes of the editor
			editor.grabHorizontal = true;
			editor.minimumHeight = editBtn.getSize().y;
			editor.minimumWidth = editBtn.getSize().x;

			// Set attributes for the delete button
			deleteBtn = new Button(table, SWT.PUSH);
			deleteBtn.setText("Delete");
			deleteBtn.computeSize(SWT.DEFAULT, table.getItemHeight());
			deleteBtn.addListener(SWT.Selection, event -> deleteUnitTest(parent.getShell(), editable));

			editor = new TableEditor(table);
			editor.setEditor(deleteBtn, item, 6);

			// Set attributes of the editor
			editor.grabHorizontal = true;
			editor.minimumHeight = deleteBtn.getSize().y;
			editor.minimumWidth = deleteBtn.getSize().x;
		}

		for (int i = 0; i < columns.length; i++) {
			table.getColumn(i).pack();
		}

		return layer;
	}

	/**
	 * Loads the view for editing the unit test
	 *
	 * @param parent main composite in which the view must be rendered and data 
	 * of the unit test
	 * @return 
	 */
	private void loadEditionWindow(Composite parent, UnitTestData test) {

		folder = (CTabFolder) parent;

		int idx = folder.getSelectionIndex();

		// Creates new tab item for content of the window
		CTabItem item = new CTabItem(folder, SWT.CLOSE, idx + 1);
		item.setText("Edit unit test");
		folder.setSelection(idx + 1);

		// Creates window to create individual unit tests
		EditionWindow editionWindow = new EditionWindow();

		// Renders content in new tab
		item.setControl(editionWindow.render(parent, test));

		// Refreshes view
		parent.requestLayout();
	}

	/**
	 * Shows confirmation message and if accepted, deletes unit test
	 *
	 * @param shell main composite in which the view must be rendered and data 
	 * of the unit test
	 * @return 
	 */
	private void deleteUnitTest(Shell shell, UnitTestData test) {
		MessageBox dialog = new MessageBox(shell, SWT.ICON_QUESTION | SWT.OK | SWT.CANCEL);
		dialog.setText("Delete confirmation");
		dialog.setMessage("Are you sure you want to delete \"" + test.getTestName() + "\" unit test");

		// open dialog and await user selection
		int returnCode = dialog.open();

		if (returnCode == 32) {
			// Here goes the code to delete unit test
			System.out.println(returnCode);
		}
	}

}
