package com.studentdetails.form.ui;

import com.vaadin.icons.VaadinIcons;
import org.springframework.beans.factory.annotation.Autowired;
import com.studentdetails.form.model.Student;
import com.studentdetails.form.repo.StudentRepository;
import com.vaadin.data.Binder;
import com.vaadin.event.ShortcutAction;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;


@SpringComponent
@UIScope
public class StudentEditor extends VerticalLayout implements View{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private final StudentRepository studentRepository;

	public Student student;

	/* Fields to edit properties in Student entity */
	public TextField firstName = new TextField("First name");
	public TextField lastName = new TextField("Last name");
	public TextField phone = new TextField("Phone");
	public TextField eMail = new TextField("E mail");
	
	//public TextField birthDate = new TextField("DOB");
	//public DateField birthDate = new DateField("Birth date");


	/* Action buttons */
	public Button save = new Button("Save", FontAwesome.SAVE);
	public Button cancel = new Button("Cancel", VaadinIcons.CLOSE);
	public Button delete = new Button("Delete", VaadinIcons.TRASH);
	CssLayout actions = new CssLayout(save, cancel, delete);

	Binder<Student> binder = new Binder<>(Student.class);

	@Autowired
	public StudentEditor(StudentRepository repository) {
		this.studentRepository = repository;
		//binder.forField(birthDate).asRequired("Please provide a birth date").bind(Student::getBirthDate, Student::setBirthDate);
		addComponents(firstName, lastName, phone, eMail, actions);

		// bind using naming convention
		binder.bindInstanceFields(this);

		// Configure and style components
		setSpacing(true);
		actions.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
	//	setComponentAlignment(actions, Alignment.MIDDLE_CENTER);
		
		save.setStyleName(ValoTheme.BUTTON_PRIMARY);
		save.setClickShortcut(ShortcutAction.KeyCode.ENTER);

		setVisible(false);
	}

	public interface ChangeHandler {

		void onChange();
	}

	public final void editStudent(Student s) {
		if (s == null) {
			setVisible(false);
			return;
		}
		final boolean persisted = s.getId() != null;
		if (persisted) {
			// Find fresh entity for editing
			student = studentRepository.findOne(s.getId());
		}
		else {
			student = s;
		}
		cancel.setVisible(persisted);

		// Bind student properties to similarly named fields
		// Could also use annotation or "manual binding" or programmatically
		// moving values from fields to entities before saving
		binder.setBean(student);

		setVisible(true);

		// A hack to ensure the whole form is visible
		save.focus();
		// Select all text in firstName field automatically
		firstName.selectAll();
	}

	public void setChangeHandler(ChangeHandler h) {
		// ChangeHandler is notified when either save or delete
		// is clicked
		save.addClickListener(e -> h.onChange());
		delete.addClickListener(e -> h.onChange());
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub

	}

}

