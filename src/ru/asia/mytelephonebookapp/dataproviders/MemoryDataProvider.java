package ru.asia.mytelephonebookapp.dataproviders;

import java.util.ArrayList;

import ru.asia.mytelephonebookapp.models.Contact;

public class MemoryDataProvider implements DataProvider {
	
	ArrayList<Contact> dataInMemory;

	@Override
	public void getData() {
		dataInMemory = new ArrayList<Contact>();
		
	}

	@Override
	public void deleteData() {
		
	}

	@Override
	public void addItem() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void query() {
		// TODO Auto-generated method stub
		
	}

}
