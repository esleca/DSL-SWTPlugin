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
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import com.google.gson.Gson;
import com.tec.dslunittests.models.UnitTestData;

public class PackageUnitTestWindow {

	private Button editBtn;
	private Composite layer;
	private CTabFolder folder;

	private String jsonString = """
			[

				{
					"packageName": "package_1",
					"className": "class1",
					"functionName": "getEdades",
					"testName": "getEdades_test1"
				},
				{
					"packageName": "package_1",
					"className": "class1",
					"functionName": "getEdades",
					"testName": "getEdades_test2"
				},
				{
					"packageName": "package_1",
					"className": "class2",
					"functionName": "getTable",
					"testName": "getTable_test1"
				},
				{
					"packageName": "package_1",
					"className": "class2",
					"functionName": "getTable",
					"testName": "getTable_test2"
				}
			]""";

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

		//Transforms json data to java object
		UnitTestData[] UTdata = gson.fromJson(jsonString, UnitTestData[].class);

		Table table = new Table(layer, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
		data.heightHint = 200;
		table.setLayoutData(data);

		String[] columns = { "#", "Class", "Function", "Unit test", "Actions" };

		for (int i = 0; i < columns.length; i++) {
			TableColumn column = new TableColumn(table, SWT.NONE);
			column.setText(columns[i]);
			table.getColumn(i).pack();
		}

		for (int i = 0; i <= UTdata.length - 1; i++) {
			TableItem item = new TableItem(table, SWT.NONE);
			item.setText(0, "" + (i + 1));
			item.setText(1, UTdata[i].getClassName());
			item.setText(2, UTdata[i].getFunctionName());
			item.setText(3, UTdata[i].getTestName());
			
			//Data of the existent unit tests
			final UnitTestData editable = UTdata[i];

			// Set attributes for the button to load edition window
			editBtn = new Button(table, SWT.PUSH);
			editBtn.setText("Edit");
			editBtn.computeSize(SWT.DEFAULT, table.getItemHeight());
			editBtn.addListener(SWT.Selection, event -> loadEditionWindow(parent, editable));

			TableEditor editor = new TableEditor(table);
			editor.setEditor(editBtn, item, 4);

			// Set attributes of the editor
			editor.grabHorizontal = true;
			editor.minimumHeight = editBtn.getSize().y;
			editor.minimumWidth = editBtn.getSize().x;
		}

		for (int i = 0; i < columns.length; i++) {
			table.getColumn(i).pack();
		}

		return layer;
	}

	/**
	 * Defines widgets and layout for the package unit test window
	 *
	 * @param parent main composite in which the view must be rendered and data 
	 * of the unit test
	 * @return the composite ready to be displayed
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

}
