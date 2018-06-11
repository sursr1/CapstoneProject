package com.studentdetails.form.ui;

import com.studentdetails.form.repo.StudentRepository;
import com.studentdetails.form.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.annotation.SpringViewDisplay;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@SpringUI
@SpringViewDisplay
public class VaadinUI extends UI {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private final UserRepository repo;

	private final StudentRepository studentRepository;

	private Navigator navigator;

	@Autowired
	public VaadinUI(UserRepository repo, StudentRepository studentRepository) {
		this.repo = repo;
		this.studentRepository = studentRepository;

	}

	@Override
	protected void init(VaadinRequest request) {
		// build layout
       

		VerticalLayout mainLayout = new VerticalLayout();

		LoginView loginView = new LoginView(repo);

		mainLayout.addComponent(loginView);
		mainLayout.setSizeFull();
		mainLayout.setComponentAlignment(loginView, Alignment.TOP_CENTER);

		setContent(mainLayout);

		StudentEditor studentEditor = new StudentEditor(studentRepository);

		MainView mainView = new MainView(studentRepository, studentEditor);
		GridLayout gl = new GridLayout(2, 1);
		
		Label lbReference = new Label("Référence");
		lbReference.setWidth("200px");
		gl.addComponent(lbReference);
		Label lbReferenceValue = new Label("REF-001");    	
		lbReferenceValue.addStyleName("dark-label");    	
		gl.addComponent(lbReferenceValue);
		//mainLayout.addComponent(new Label("<br/>", Label.CONTENT_XHTML));
		
		//mainView.setSizeFull();

		// build navigator
		navigator = new Navigator(this, mainLayout);

		navigator.addView(loginView.VIEW_NAME, loginView);
		navigator.addView(mainView.VIEW_NAME, mainView);
		navigator.navigateTo(loginView.VIEW_NAME);

	}
}
