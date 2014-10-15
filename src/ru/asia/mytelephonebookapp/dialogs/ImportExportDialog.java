package ru.asia.mytelephonebookapp.dialogs;

import ru.asia.mytelephonebookapp.DBContactsExportTask;
import ru.asia.mytelephonebookapp.DBContactsImportTask;
import ru.asia.mytelephonebookapp.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

public class ImportExportDialog extends DialogFragment {

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(R.string.title_import_export)
				.setItems(R.array.array_import_export, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case 0:
							new DBContactsImportTask(getActivity()).execute();
							break;
						case 1:
							new DBContactsExportTask(getActivity()).execute();
							break;
						}						
					}
				});
		
		return builder.create();
	}
	
}
