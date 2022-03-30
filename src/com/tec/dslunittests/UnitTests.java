package com.tec.dslunittests;

import java.util.ArrayList;

//import gestors.GestorDSL;
//import gestors.IGestorDSL;
import processor.gastgateway.CompilationUnitHandler;
//import utils.ConsolePrinter;

import org.eclipse.ui.part.ViewPart;
import org.eclipse.swt.*;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.*;



public class UnitTests extends ViewPart {

	//private IGestorDSL dsl;
	private final String ERROR_FILE_EXT = "Formato de archivo inválido, utilice archivos .java, .cs o .py";

	public UnitTests() {
		//dsl = new GestorDSL( null );
	}

	@Override
	public void createPartControl(Composite parent) {
		// Provisionamiento
		parent.setLayout(new FillLayout(SWT.VERTICAL));
		
		// Seleccione fuente: 
		Text text = new Text(parent, SWT.NONE);
		
		Button btn = new Button(parent, SWT.NONE);
		btn.setText("Seleccione archivo fuente...");
		btn.addSelectionListener(new SelectionAdapter()
		{
			public void widgetSelected(SelectionEvent event) {
				FileDialog dlg = new FileDialog(parent.getShell());
				dlg.setFilterPath("C:/");
				dlg.setText("File Dialog");
				String dir = dlg.open();
				
				boolean valid = isValidFileExtension(dir);
				if (valid) {
					text.setText(dir);
				} else {
					text.setText(ERROR_FILE_EXT);
				}
			}
		});
		
		// Archivo seleccionado:
		Label label = new Label(parent, SWT.NONE);
		label.setText(text.getText());
		
		//
		
		// Escenarios de pruebas unitarias
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	
	private boolean isValidFileExtension(String filePath) {
		ArrayList<String> extensions = new ArrayList<String>() {
            {
                add("java");
                add("cs");
                add("py");
            }
        };
		
		String ext = filePath.substring(filePath.lastIndexOf(".") + 1);
		if (extensions.contains(ext)) {
			return true;
		} else {
			return false;
		}
	}

	
}
