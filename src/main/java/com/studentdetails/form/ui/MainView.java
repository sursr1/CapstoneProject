package com.studentdetails.form.ui;

import com.vaadin.shared.ui.ValueChangeMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.studentdetails.form.model.Student;
import com.studentdetails.form.repo.StudentRepository;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

@SpringView(name = MainView.VIEW_NAME)
public class MainView extends CustomComponent implements  View {


	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public static final String VIEW_NAME = "crud";

	private final StudentEditor editor;

	final Grid<Student> grid;

	final TextField filter;

	private final Button addNewBtn;

	private final StudentRepository studentRepository;

	@Autowired
	public MainView(StudentRepository studentRepository, StudentEditor editor) {
		this.studentRepository = studentRepository;
		this.editor = editor;
		this.grid = new Grid<>(Student.class);
		this.filter = new TextField();
		this.addNewBtn = new Button("New student", FontAwesome.PLUS);

		/***** To add page scroll*******/

		this.addStyleName("v-scrollable");
		this.setHeight("100%");
		VerticalLayout content = new VerticalLayout();
		// content has undefined height by default - just don't set one
		setCompositionRoot(content);

		/***** END - To add page scroll*******/

		// build layout
		HorizontalLayout actions = new HorizontalLayout(filter, addNewBtn);
		VerticalLayout mainLayout = new VerticalLayout(actions, grid, editor);

		grid.setHeight(300, Unit.PIXELS);
		grid.setColumns("id", "firstName", "lastName", "phone", "eMail", "birthDate");

		filter.setPlaceholder("Filter by last name");
		// Replace listing with filtered content when user changes filter
		filter.setValueChangeMode(ValueChangeMode.LAZY);
		filter.addValueChangeListener(e -> listStudents(e.getValue()));

		setCompositionRoot(mainLayout);

		// Connect selected Student to editor or hide if none is selected
		grid.asSingleSelect().addValueChangeListener(e -> {
			editor.editStudent(e.getValue());
		});

		// Instantiate and edit new Student the new button is clicked
		addNewBtn.addClickListener(e -> editor.editStudent(new Student("", "", "", "", null)));

		editor.save.addClickListener(e -> {
			studentRepository.save(editor.student);
			listStudents(null);
			editor.setVisible(false);
		});

		editor.delete.addClickListener(e -> {
			studentRepository.delete(editor.student);
			listStudents(null);
			editor.setVisible(false);
		});

		editor.cancel.addClickListener(e -> {
			editor.editStudent(editor.student);
			listStudents(null);
			editor.setVisible(false);
		});

		// Listen changes made by the filter textbox, refresh data from backend
		filter.addValueChangeListener(event -> {
			editor.setVisible(false);
			listStudents(event.getValue());
		});
		// Initialize listing
		listStudents(null);

	}

    void listStudents(String filterText) {
		if (StringUtils.isEmpty(filterText)) {
			grid.setItems(studentRepository.findAll());
		}
		else {
			grid.setItems(studentRepository.findByLastNameStartsWithIgnoreCase(filterText));
		}
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub

	}

}
