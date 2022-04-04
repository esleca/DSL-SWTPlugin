package com.tec.dslunittests;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Named;

//import gestors.GestorDSL;
//import gestors.IGestorDSL;
//import processor.gastgateway.CompilationUnitHandler;
//import utils.ConsolePrinter;

import org.eclipse.ui.part.ViewPart;


import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.swt.*;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.*;

public class UnitTests extends ViewPart {
	
	private SashForm sashForm;
	private Text txtCode, txtVar;
	private Group codeGroup, diagramGroup, dataGroup;
	private TextSelection txt;
	private Button btncode;
	private Canvas cnv;
	private ScrolledComposite scroller;
	private Image img;
	private Composite comp;
	private Point point = new Point(10, 10);

	//private IGestorDSL dsl;
	private final String ERROR_FILE_EXT = "Formato de archivo inv�lido, utilice archivos .java, .cs o .py";

	public UnitTests() {
		//dsl = new GestorDSL( null );
	}

	@Override
	public void createPartControl(Composite parent) {
		FillLayout fillLayout = new FillLayout(SWT.VERTICAL);
		parent.setLayout(new GridLayout(1, true));

		// Create the SashForm with HORIZONTAL
		// Un sash form permite arrastrar su borde y redimencionarlo
		sashForm = new SashForm(parent, SWT.HORIZONTAL);
		// Se agrega el atributo de layout para permitir que el tama�o se maneje
		// din�micamente
		sashForm.setLayout(new GridLayout(1, true));
		// Un layout que ocupa todo el espacio disponible
		sashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		codeGroup = new Group(sashForm, SWT.CENTER);
		codeGroup.setText("C�digo");
		codeGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		codeGroup.setLayout(new GridLayout(1, true));

		diagramGroup = new Group(sashForm, SWT.CENTER);
		diagramGroup.setText("Diagrama de flujo");
		diagramGroup.setLayout(fillLayout);

		dataGroup = new Group(sashForm, SWT.CENTER);
		dataGroup.setText("Variables");
		dataGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		dataGroup.setLayout(new GridLayout(1, true));

		txtCode = new Text(codeGroup, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL);
		txtCode.setText("Seleccione el c�digo que desea ejecutar");
		txtCode.setLocation(10, 25);
		txtCode.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		txtCode.pack();

		// DiagramContainer diagram = new DiagramContainer();
		// scroller = diagram.createContainer(diagramGroup);
		// scroller.pack();
		// comp = (Composite)scroller.getChildren()[0];
		// diagramGroup.pack();

		// FillLayout fillLayout = new FillLayout(SWT.VERTICAL);
		scroller = new ScrolledComposite(diagramGroup, SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER);
		scroller.setLayout(fillLayout);

		comp = new Composite(scroller, SWT.BORDER);
		// comp.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		// comp.setLayout(new GridLayout(1, true));
		comp.setLayout(fillLayout);
		comp.pack();

		// scroller.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		scroller.setExpandHorizontal(true);
		scroller.setExpandVertical(true);
		scroller.setContent(comp);
		diagramGroup.pack();

		txtVar = new Text(dataGroup, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL);
		txtVar.setLocation(10, 25);
		txtVar.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		txtVar.pack();

		btncode = new Button(codeGroup, SWT.PUSH);
		btncode.setText("Debug");
		btncode.addListener(SWT.Selection, event -> click());
		codeGroup.pack();
		
		
		// Provisionamiento
		//parent.setLayout(new FillLayout(SWT.VERTICAL));
		
		
		/*
		// Seleccione fuente: 
		Text text = new Text(parent, SWT.NONE);
		
		Button btn = new Button(parent, SWT.NONE);
		btn.setText("Seleccione archivo fuentehhhhhhh...");
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
		*/
		
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
	

	/**
	 * This method manages the selection of your current object. In this example
	 * we listen to a single Object (even the ISelection already captured in E3
	 * mode). <br/>
	 * You should change the parameter type of your received Object to manage
	 * your specific selection
	 * 
	 * @param o:
	 *            the current object received
	 */
	@Inject
	@Optional
	public void setSelection(@Named(IServiceConstants.ACTIVE_SELECTION) Object o) {
		// Test if label exists (inject methods are called before PostConstruct)
		if (txtCode != null) {
			if (o instanceof TextSelection) {
				txt = (TextSelection) o;
				txtCode.setText(txt.getText());
			}
		}

	}
	
	private void click() {	

	}


	
}
