package de.fiduciagad.anflibrary.anFReceiver.anFStorage.anFServiceHandling;

import android.content.Context;
import android.database.Cursor;

import de.fiduciagad.anflibrary.anFReceiver.anFStorage.AnFOpenHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Felix Schiefer on 09.01.2016.
 */
public class ServiceDB extends AnFOpenHandler {
    Context context;

    public ServiceDB(Context context) {
        super(context);
        this.context = context;
    }

    public List<String> getServiceList() {
        List<String> serviceList = new ArrayList<>();

        Cursor c = this.queryServices();
        int ciService = c.getColumnIndex(this.SERVICE);
        while (c.moveToNext()) {
            String service = c.getString(ciService);
            serviceList.add(service);
        }
        c.close();
        this.close();
        return serviceList;
    }

    public boolean insert(String service) {
        Cursor c = this.queryService(service);
        if (c.getCount() < 1) {
            long rowId = this.insertServices(service);

            if (rowId != -1) {
                CreateDefaultValues defaultValues = new CreateDefaultValues(context);
                defaultValues.setDefaultValues(service);
                return true;
            }
        }
        return false;
    }
}
