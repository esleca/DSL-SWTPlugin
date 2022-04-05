package com.tec.dslunittests;

import org.eclipse.swt.widgets.Composite;

//import gestors.GestorDSL;
//import gestors.IGestorDSL;
//import processor.gastgateway.CompilationUnitHandler;
//import utils.ConsolePrinter;

import org.eclipse.ui.part.ViewPart;

import com.tec.dslunittests.Views.CreationWindow;

public class UnitTests extends ViewPart {
	
	private Composite window;

	//private IGestorDSL dsl;

	public UnitTests() {
		//dsl = new GestorDSL( null );
	}

	@Override
	public void createPartControl(Composite parent) {
		//Creates window to create individual unit tests
		CreationWindow creationWindow = new CreationWindow();
		creationWindow.render(parent);
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}




	
}
