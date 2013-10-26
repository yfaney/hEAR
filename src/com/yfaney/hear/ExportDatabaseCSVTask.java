package com.yfaney.hear;

import android.app.ProgressDialog;
import android.os.AsyncTask;

/*******************************************************************************

********************************************************************************

Export Database into CSV Class

********************************************************************************

*******************************************************************************/
class ExportDatabaseCSVTask {

}
/*
    class ExportDatabaseCSVTask extends AsyncTask<String, Void, Boolean> 

    {

        private final ProgressDialog dialog = new ProgressDialog(ctx);


        // can use UI thread here

        @Override

        protected void onPreExecute() 

        {

            this.dialog.setMessage("Exporting database...");

            this.dialog.show();

        }   


        // automatically done on worker thread (separate from UI thread)

        protected Boolean doInBackground(final String... args) 

        {

            File dbFile=getDatabasePath("excerDB.db");

            DbClass DBob = new DbClass(MainActivity.this);

            File exportDir = new File(Environment.getExternalStorageDirectory(), "");        

            if (!exportDir.exists()) 

            {

                exportDir.mkdirs();

            }

            File file = new File(exportDir, "excerDB.csv");

            try 

            {

                file.createNewFile();                

                CSVWriter csvWrite = new CSVWriter(new FileWriter(file));

                SQLiteDatabase db = DBob.getReadableDatabase();

                Cursor curCSV = db.rawQuery("SELECT * FROM ExcerTable",null);

                csvWrite.writeNext(curCSV.getColumnNames());

                while(curCSV.moveToNext())

                {

                    String arrStr[] ={curCSV.getString(0),curCSV.getString(1),

                        curCSV.getString(2),curCSV.getString(3),curCSV.getString(4)};

                    csvWrite.writeNext(arrStr);

                }

                csvWrite.close();

                curCSV.close();

                return true;

            }

            catch(SQLException sqlEx)

            {

                Log.e("MainActivity", sqlEx.getMessage(), sqlEx);

                return false;                

            }

            catch (IOException e) 

            {

                Log.e("MainActivity", e.getMessage(), e);

                return false;

            }

        }

        // can use UI thread here

        @Override

        protected void onPostExecute(final Boolean success) 

        {

            if (this.dialog.isShowing()) 

            {

                this.dialog.dismiss();

            }

            if (success) 

            {

                Toast.makeText(ctx, "Export successful!", Toast.LENGTH_SHORT).show();

            } 

            else 

            {

                Toast.makeText(ctx, "Export failed", Toast.LENGTH_SHORT).show();

            }

        }

    }*/